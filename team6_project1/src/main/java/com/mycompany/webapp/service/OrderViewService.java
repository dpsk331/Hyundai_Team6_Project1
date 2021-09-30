package com.mycompany.webapp.service;

import java.util.List;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.mycompany.webapp.dao.OrderDao;
import com.mycompany.webapp.dao.OrderitemDao;
import com.mycompany.webapp.dto.Order;
import com.mycompany.webapp.dto.OrderitemJoinProduct;


@Service
public class OrderViewService {
	@Resource 
	private OrderDao orderDao;
	@Resource
	private OrderitemDao orderitemDao;
	private static final Logger logger = LoggerFactory.getLogger(OrderViewService.class);	
	public List<Order> selectByMid(String mid){
		return orderDao.selectByMid(mid);
	}
	public List<OrderitemJoinProduct> selectOrderitemJoinProductByOid(String oid){
		return orderitemDao.selectOrderitemJoinProductByOid(oid);
	}
	public List<String> selectOidByMid(String mid){
		return orderDao.selectOidByMid(mid);
	}
}
