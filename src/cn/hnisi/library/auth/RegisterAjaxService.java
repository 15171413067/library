package cn.hnisi.library.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.util.Util;

@Service
public class RegisterAjaxService {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserDAO userDAO;
	
	public void register(Map<String,Object> inMap){
		String username = (String) inMap.get("username");
		String mail = (String) inMap.get("mail");
		String verification = (String) inMap.get("verification");
		String password = (String) inMap.get("password");
		
		//校验注册信息
		if(Util.isEmpty(username)){
			throw new AppException("请输入用户名");
		}
		
		if(Util.isEmpty(mail)){
			throw new AppException("请输入邮箱");
		}else{
			int splitIndex = mail.indexOf("@");
			if(splitIndex < 0){
				throw new AppException("邮箱格式有误");
			}else{
				String suffix = "@sinobest.cn";
				if(!mail.substring(splitIndex).equals(suffix)){
					throw new AppException("请使用公司邮箱，后缀为"+suffix);
				}
			}
		}
		
		if(Util.isEmpty(password)){
			throw new AppException("请输入密码");
		}
		
		if(Util.isEmpty(verification)){
			throw new AppException("请输入验证码");
		}else{
			HttpSession session = request.getSession(false);
			if(Util.isEmpty(session) || session.getAttribute("registerVerification") == null){
				throw new AppException("请发送验证码");
			}
			Verification sessionVerification = (Verification) session.getAttribute("registerVerification");

			
			if(!verification.equals(sessionVerification.getVerification())){
				throw new AppException("验证码输入有误");
			}else{
				if(!mail.equals(sessionVerification.getDestination())){
					throw new AppException("注册邮箱与验证邮箱不一致");
				}
			}
		}
		
		
		//写入注册信息
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		user.setEmail(mail);
		user.setRealname(mail.substring(0,mail.indexOf("@")));//暂时为拼音，后续修改为中文
		userDAO.insert(user);
		
		
	}
}
