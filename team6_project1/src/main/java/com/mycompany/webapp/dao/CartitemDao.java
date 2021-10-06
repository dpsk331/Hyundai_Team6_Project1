package com.mycompany.webapp.dao;

import java.util.ArrayList;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.mycompany.webapp.dto.Cartitem;
import com.mycompany.webapp.dto.CartitemJoinProduct;

@Mapper
public interface CartitemDao {
	List<CartitemJoinProduct> selectCartitemJoinProductByPcodePcolorPsize(@Param("mid") String mid, 
																				 @Param("pcode") ArrayList<String> pcode, 
																				 @Param("pcolor") ArrayList<String> pcolor, 
																				 @Param("psize") ArrayList<String> psize);

	List<Cartitem> selectAllByMid(String mid);
	Cartitem selectOne(@Param("mid") String mid,@Param("pcode")  String pcode,@Param("pcolor")  String pcolor,@Param("psize")  String psize);
	void deleteItem(@Param("mid") String mid, @Param("pcode") String pcode, @Param("pcolor") String pcolor, @Param("psize") String psize);
	void insertItem(Cartitem cartitem);
	void updateItem(Cartitem cartitem);
	Cartitem selectItem(Cartitem cartitem);
	int selectCount(String mid);
}