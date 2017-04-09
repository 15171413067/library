package cn.hnisi.library.auth;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.util.Util;

@Service
public class LoginAjaxService {
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private UserDAO userDAO;

	public User login(Map<String,Object> inMap){
		String username = (String) inMap.get("username");
		String password = (String) inMap.get("password");
		
		if(Util.isEmpty(username)){
			throw new AppException("用户名为空");
		}
		
		if(Util.isEmpty(password)){
			throw new AppException("密码为空");
		}
		
		User user = userDAO.authenticate(username, password);
		if(Util.isEmpty(user)){
			throw new AppException("用户名或密码错误");
		}
		
		//销毁当前session
		HttpSession session = request.getSession(false);
		if(!Util.isEmpty(session)){
			session.invalidate();
		}
		
		//重建新的session
		session = request.getSession();
		session.setAttribute("user", user);
		
		return user;
	}
	
	public void logout(){
		//销毁当前session
		HttpSession session = request.getSession(false);
		if(!Util.isEmpty(session)){
			session.invalidate();
		}
		request.getSession();
	}
}
