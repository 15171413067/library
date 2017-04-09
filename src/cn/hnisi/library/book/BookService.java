package cn.hnisi.library.book;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.util.Util;
import cn.hnisi.library.auth.User;

@Service
public class BookService {

	@Autowired
	private BookDAO bookDAO;
	
	@Autowired
	private RecordDAO recordDAO;
	
	public void borrow(String bookid,User user){
		//TODO事务控制
		
		//校验书状态
		Map<String,Object> book = bookDAO.queryByBookid(bookid);
		String status = (String) book.get("status");
		if(Util.isEmpty(status)){
			throw new AppException("状态异常");
		}
		
		if(!status.equals(Status.Idle.getKey())){
			throw new AppException("当前不可借阅");
		}
		
		//写入流程表Record
		Map<String,Object> record = new HashMap<String,Object>();
		record.put("bookid",bookid);
		record.put("reader",user.getUserid());
		record.put("status",Status.Reading.getKey());
		recordDAO.insert(record);
		
		//写入结果表Book
		book.put("bookid",bookid);
		book.put("reader", user.getUserid());
		book.put("status",Status.Reading.getKey());
		bookDAO.borrow(book);
	}
}
