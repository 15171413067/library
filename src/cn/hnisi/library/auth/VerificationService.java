package cn.hnisi.library.auth;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.util.Util;
import cn.hnisi.mail.iface.IMailConfig;
import cn.hnisi.mail.iface.IMailService;
import cn.hnisi.mail.object.MailConfig;

@Service
public class VerificationService {
	
	private Log log = LogFactory.getLog(this.getClass());

	@Autowired
	private IMailService mailService;
	
	@Autowired
	private HttpServletRequest request;
	
	public void send(Verification verification){
		String type = verification.getType();
		String method = verification.getMethod();
		String destination = verification.getDestination();
		if(Util.isEmpty(type)){
			throw new AppException("发送验证码目的不明确");
		}
		
		if(Util.isEmpty(method)){
			method = "mail";//默认邮件发送
		}
		
		if(Util.isEmpty(destination)){
			throw new AppException("验证码发送对象不存在");
		}
		
		//获取验证码
		String code = generate(4);
		verification.setVerification(code);
		
		//存储验证码
		HttpSession session = request.getSession();
		session.setAttribute("registerVerification", verification);
		
		
		//暂时只支持邮件
		if(!method.equals("mail")){
			throw new AppException("暂时只支持邮件");
		}
		
		IMailConfig mailConfig = new MailConfig();
		mailConfig.setHost("smtp.qq.com");
		mailConfig.setPort("587");
		mailConfig.setFromMail("463552890@qq.com");
		mailConfig.setUser("463552890");
		mailConfig.setPassword("fyxkqifautpjcahh");
		mailConfig.setToMail(destination);
		mailConfig.setMailTitle("Library邮件注册认证");
		mailConfig.setMailContent("<div>验证码为: "+code+"</div>");
		
		try{
			mailService.sendMail(mailConfig);			
		}catch(Exception e){
			log.error("发送邮件出现异常",e);
			throw new AppException("发送邮件出现异常",e);
		}
		
	}
	
	public static String generate(int size){
		return String.valueOf(Math.random()).substring(2,2+size);
	}
	
}
