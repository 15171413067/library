package cn.hnisi.frame.ajax;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.exception.AuthenticationException;
import cn.hnisi.frame.util.BeanUtil;
import cn.hnisi.frame.util.Util;

@Controller
@RequestMapping(value="/ajax")
public class AjaxDispatcherImpl implements AjaxDispatcher{
	
	private final Log log = LogFactory.getLog(this.getClass());
	
	@Override
	@RequestMapping(value="/ajax.do")
	@ResponseBody
	public AjaxOutParam ajax(@RequestBody AjaxInParam inParam){
		String serviceName = inParam.getService();
		String methodName = inParam.getMethod();
		Map<String,Object> param = inParam.getParam();
		
		//默认出参
		AjaxOutParam outParam = new AjaxOutParam();
		outParam.setRtnCode("1");
		outParam.setRtnMessage("success");
		outParam.setParam(inParam.getParam());
		
		try {
			ServiceConfig serviceConfig = prepareService(serviceName, methodName, param);
			Object service = serviceConfig.getService();
			Method method = serviceConfig.getMethod();
			Object[] paramArgs = serviceConfig.getParamArgs();
			
			Object result = method.invoke(service, paramArgs);
			
			Map<String,Object> rtnParam = responseService(result);
			outParam.setParam(rtnParam);
		} catch(InvocationTargetException e){
			//获取业务service异常，根据异常类型，进行后续处理
			outParam.setRtnCode(RtnCode.WARNNING.getKey());
			Throwable targetException = e.getTargetException();
			try{
				throw targetException;
			}catch(AuthenticationException t){
				outParam.setRtnCode(RtnCode.NOLOGIN.getKey());
			}catch(AppException t){
				outParam.setRtnMessage(t.getMessage());
			}catch(Throwable t){
				String message = MessageFormat.format("调用{0}.{1}出现异常", new Object[]{serviceName, methodName});
				log.error(message,e);
				throw new AppException(message,e);
			}
		} catch (Exception e) {
			String message = MessageFormat.format("调用{0}.{1}出现异常", new Object[]{serviceName, methodName});
			log.error(message,e);
			outParam.setRtnCode(RtnCode.ERROR.getKey());
			outParam.setRtnMessage(message);
		}
		
		return outParam;
	}
	
	@ExceptionHandler(AppException.class)
	@ResponseBody
	public String handlerException(AppException ex,HttpServletRequest request){
		return ex.getMessage();
	}
	
	class ServiceConfig{
		private Object service;
		private Method method;
		private Object[] paramArgs;
		public Object getService() {
			return service;
		}
		public void setService(Object service) {
			this.service = service;
		}
		public Method getMethod() {
			return method;
		}
		public void setMethod(Method method) {
			this.method = method;
		}
		public Object[] getParamArgs() {
			return paramArgs;
		}
		public void setParamArgs(Object[] paramArgs) {
			this.paramArgs = paramArgs;
		}
	}
	
	/**
	 * 调用service的准备工作
	 * @param serviceName
	 * @param methodName
	 * @param param
	 * @return
	 */
	private ServiceConfig prepareService(String serviceName,String methodName,Map<String,Object> param){
		//获取目标service
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		Object service;
		try{
			service = ctx.getBean(serviceName);
		}catch(Exception e){
			String msg = MessageFormat.format("调用{0}.{1}出现异常,{2}", new Object[]{serviceName, methodName,e.getMessage()});
			throw new AppException(msg);
		}
		
		
		
		//获取候选method
		Method[] methodArray = service.getClass().getMethods();
		List<Method> candidates = new ArrayList<Method>();
		for(Method method : methodArray){
			if(method.getName().equals(methodName)){
				if(method.getParameterTypes().length <= 1){
					candidates.add(method);
				}else{
					String msg = MessageFormat.format("调用{0}.{1},目标方法存在重载,暂不支持重载", new Object[]{serviceName, methodName});
					log.debug(msg);
				}
			}
		}
		
		if(candidates.size() == 0){
			String msg = MessageFormat.format("调用{0}.{1}出现异常,目标方法不存在", new Object[]{serviceName, methodName});
			throw new AppException(msg);
		}
		
		
		
		//获取目标method
		Method destMethod = null;
		Object[] parameterArgs = null;
		for(Method method: candidates){
			Class<?>[] parameterTypes = method.getParameterTypes();
			int cnt = parameterTypes.length;
			if(Util.isEmpty(param)){
				if(cnt == 0){
					//完全匹配
					destMethod = method;
					parameterArgs = new Object[]{};
					break;
				}else{
					//兼容匹配
					destMethod = method;
					parameterArgs = new Object[]{null};
				}
			}else{
				if(cnt == 0){
					//兼容匹配
					destMethod = method;
					parameterArgs = new Object[]{};
				}else{
					//完全匹配
					destMethod = method;
					parameterArgs = new Object[]{BeanUtil.convert(param, parameterTypes[0])};
					break;
				}
			}
		}
		
		if(destMethod == null){
			throw new AppException(MessageFormat.format("调用{0}.{1}出现异常,目标service不存在或参数错误", new Object[]{serviceName, methodName}));
		}
		
		//返回结果
		ServiceConfig serviceConfig = new ServiceConfig();
		serviceConfig.setService(service);
		serviceConfig.setMethod(destMethod);
		serviceConfig.setParamArgs(parameterArgs);
		return serviceConfig;
	}
	
	private Map<String,Object> responseService(Object result){
		if(result instanceof Map){
			try{
				return (Map<String,Object>)result;
			}catch(Exception e){
				return BeanUtil.convert(result, Map.class);
			}
		}else if(result instanceof List){
			log.warn("调用service出现警告,service返回值为List");
			Map<String,Object> map = new HashMap<String,Object>();
			try{
				map.put("list", result);
			}catch(Exception e){
				map.put("list", BeanUtil.convert(result,List.class));
			}
			return map;
		}else{
			return BeanUtil.convert(result, Map.class);
		}
	}
}
