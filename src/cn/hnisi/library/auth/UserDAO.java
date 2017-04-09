package cn.hnisi.library.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import cn.hnisi.frame.dao.BaseSpringDAO;
import cn.hnisi.frame.exception.AppException;
import cn.hnisi.library.dict.Valid;

@Repository
public class UserDAO extends BaseSpringDAO{

	public User authenticate(String username,String password){
		String sql = "select userid,username,realname,email from t_user where username = :username and password = :password and valid = :valid";
		Map<String,Object> paramMap = new HashMap<String,Object>();
		paramMap.put("username", username);
		paramMap.put("password", password);
		paramMap.put("valid", Valid.TRUE.getKey());
		RowMapper<User> rowMapper = new BeanPropertyRowMapper<User>(User.class);
		try{
			return namedParameterJdbcTemplate.queryForObject(sql, paramMap, rowMapper);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
	}
	
	public int insert(User user){
		String sql = "insert into t_user(username,password,realname,email) values(:username,:password,:realname,:email)";
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(user);
		try{
			return namedParameterJdbcTemplate.update(sql, paramSource);
		}catch(DuplicateKeyException e){
			throw new AppException("用户已存在",e);
		}
	}
	
}
