package com.mycompany.webapp.dao;

import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface EventDao {
	public int selectCount(String ecode);
	public void decrementCount(int count);
}
