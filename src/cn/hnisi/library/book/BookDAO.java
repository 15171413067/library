package cn.hnisi.library.book;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.hnisi.frame.dao.BaseSpringDAO;
import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.util.CaseConvertUtil;
import cn.hnisi.frame.util.Util;
import cn.hnisi.library.dict.Valid;

@Repository
public class BookDAO extends BaseSpringDAO{

	private String querySql = "select bookid,title,description,(select realname from t_user where userid = a.reader) reader,status from t_book a ";
	
	public List<Map<String,Object>> query(Map<String,Object> inMap){
		if(Util.isEmpty(inMap)){
			String sql = querySql + " where valid = :valid";
			Map<String,Object> paramMap = new HashMap<String,Object>();
			paramMap.put("valid", Valid.TRUE.getKey());
			return CaseConvertUtil.underscoreToCamel(queryForList(sql,paramMap));
		}else{
			throw new AppException("开发中，请稍后再试");
		}
	}
	
	public Map<String,Object> queryByBookid(String bookid){
		Map<String,Object> inMap = new HashMap<String,Object>();
		inMap.put("bookid", bookid);
		inMap.put("valid", Valid.TRUE.getKey());
		String sql = "select * from t_book where bookid = :bookid and valid = :valid";
		return CaseConvertUtil.underscoreToCamel(queryForMap(sql,inMap));
	}
	
	public List<Map<String,Object>> queryAll(){
		return query(null);
	}
	
	public int borrow(Map<String,Object> inMap){
		String sql = "update t_book set reader = :reader,status = :status where bookid = :bookid";
		return update(sql,inMap);
	}
}
