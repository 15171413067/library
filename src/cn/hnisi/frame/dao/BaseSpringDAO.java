package cn.hnisi.frame.dao;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import org.springframework.context.ApplicationContext;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.web.context.ContextLoader;

import cn.hnisi.frame.exception.AppException;
import cn.hnisi.frame.util.Util;

@Repository
public class BaseSpringDAO {
	
	protected JdbcTemplate jdbcTemplate;
	
	protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public BaseSpringDAO(){
		ApplicationContext ctx = ContextLoader.getCurrentWebApplicationContext();
		jdbcTemplate = ctx.getBean("jdbcTemplate",JdbcTemplate.class);
		namedParameterJdbcTemplate = ctx.getBean("namedParameterJdbcTemplate",NamedParameterJdbcTemplate.class);
	}
	
	protected <T> T queryForObject(String table,Map<String,String> reference,T bean){
		String sql = MessageFormat.format("select * from {0}", table);
		sql += dealReference(reference);
		
		BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>((Class<T>) bean.getClass());
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(bean);
		T result = namedParameterJdbcTemplate.queryForObject(sql, paramSource, rowMapper);
		return result;
			
	}
	
	protected <T> T queryForObject(String table,Class<T> cls){
		try{
			return queryForObject(table,null,cls.newInstance());
		}catch(Exception e){
			throw new AppException(e);
		}
		
	}
	
	protected <T> List<T> query(String table,Map<String,String> reference,T bean){
		String sql = MessageFormat.format("select * from {0}", table);
		sql += dealReference(reference);
		
		BeanPropertyRowMapper<T> rowMapper = new BeanPropertyRowMapper<T>((Class<T>) bean.getClass());
		SqlParameterSource paramSource = new BeanPropertySqlParameterSource(bean);
		List<T> result = namedParameterJdbcTemplate.query(sql, paramSource, rowMapper);
		return result;
	}
	
	protected <T> List<T> query(String table,Class<T> cls){
		try {
			return query(table,null,cls.newInstance());
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
	
	protected Map<String,Object> queryForMap(String sql,Map<String,Object> paramMap){
		try{
			return namedParameterJdbcTemplate.queryForMap(sql, paramMap);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		
	}
	
	protected List<Map<String,Object>> queryForList(String sql,Map<String,Object> paramMap){
		try{
			return namedParameterJdbcTemplate.queryForList(sql, paramMap);
		}catch(EmptyResultDataAccessException e){
			return null;
		}
		
	}
	
	protected int update(String sql,Map<String,Object> paramMap){
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	protected void execute(String sql){
		jdbcTemplate.execute(sql);
	}
	
	protected String dealReference(Map<String,String> reference){
		StringBuffer sql = new StringBuffer();
		if(!Util.isEmpty(reference)){
			sql.append(" where");
			for(Entry<String,String> entry : reference.entrySet()){
				int i = 0;
				String colName = entry.getKey();
				String propertyName = entry.getValue();
				if(i > 0){
					sql.append(" and");
				}
				sql.append(MessageFormat.format(" {0} = :{1}", colName,propertyName));
				i++;
			}
		}
		
		return sql.toString();
	}
}
