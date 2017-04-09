package cn.hnisi.library.book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.exception.AuthenticationException;
import cn.hnisi.frame.util.Util;
import cn.hnisi.library.auth.User;

@Service
public class BookAjaxService {
	
	@Autowired
	private BookDAO bookDAO;
	
	@Autowired
	private HttpSession session;
	
	@Autowired
	private BookService bookService;

	public Map<String,Object> query(Map<String,Object> inMap){
		List<Map<String,Object>> books;
		if(Util.isEmpty(inMap)){
			//查询所有
			books = bookDAO.queryAll();
		}else{
			books = bookDAO.query(inMap);
		}
		
		Map<String,Object> rtnMap = new HashMap<String,Object>();
		rtnMap.put("books", books);
		return rtnMap;
	}
	
	public void borrow(Map<String,Object> inMap){
		if(Util.isEmpty(inMap)){
			throw new AppException("亲，您要借哪本呢？");
		}
		
		String bookid = (String) inMap.get("bookid");
		if(Util.isEmpty(bookid)){
			throw new AppException("亲，您要借哪本呢？");
		}
		
		User user = (User) session.getAttribute("user");
		if(Util.isEmpty(user)){
			throw new AuthenticationException("用户未登录");
		}
		
		bookService.borrow(bookid,user);
	}
	
}
