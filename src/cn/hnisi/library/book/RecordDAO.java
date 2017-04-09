package cn.hnisi.library.book;

import java.util.Map;

import org.springframework.stereotype.Repository;

import cn.hnisi.frame.dao.BaseSpringDAO;

@Repository
public class RecordDAO extends BaseSpringDAO {

	public int insert(Map<String,Object> inMap){
		String sql = "insert into t_record(bookid,reader,status) values(:bookid,:reader,:status)";
		return update(sql, inMap);
	}
}
