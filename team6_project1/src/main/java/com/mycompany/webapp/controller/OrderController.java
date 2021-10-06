package com.mycompany.webapp.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.mycompany.webapp.dto.CartitemJoinProduct;
import com.mycompany.webapp.dto.Member;
import com.mycompany.webapp.dto.Order;
import com.mycompany.webapp.dto.Orderitem;
import com.mycompany.webapp.dto.OrderitemJoinProduct;
import com.mycompany.webapp.dto.Product;
import com.mycompany.webapp.service.CartitemService;
import com.mycompany.webapp.service.ListviewService;
import com.mycompany.webapp.service.MemberService;
import com.mycompany.webapp.service.OrderService;
import com.mycompany.webapp.service.OrderitemService;

@Controller
@RequestMapping("/order")
public class OrderController {
	private static final Logger logger = LoggerFactory.getLogger(OrderController.class);
	
	@Resource
	private MemberService memberService;
	@Resource 
	private OrderService orderService;
	@Resource
	private OrderitemService orderitemService;
	@Resource 
	private CartitemService cartitemService;
	@Resource
	ListviewService productService;

	//장바구니에서 선택한 상품 주문하기
	@RequestMapping("/orderPage")
	public String orderPage(@RequestParam(value="orderPcode", required=true) ArrayList<String> orderPcode, 
							@RequestParam(value="orderPcolor", required=true) ArrayList<String> orderPcolor, 
							@RequestParam(value="orderPsize", required=true) ArrayList<String> orderPsize,
							Model model,
							HttpServletResponse response) throws Exception {
		logger.info("Run order/orderPage");
		
		logger.info(orderPcode.toString());
		logger.info(orderPcolor.toString());
		logger.info(orderPsize.toString());
		
		//mid 정보 가져오기
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mid = authentication.getName();

		//장바구니에서 선택한 상품 데이터를 DB에서 불러와 상품 주문 페이지로 전달
		List<CartitemJoinProduct> cartitemJoinProduct = cartitemService.selectCartitemJoinProductByPcodePcolorPsize(mid, orderPcode, orderPcolor, orderPsize);
		model.addAttribute("orderProducts", cartitemJoinProduct);

		//주문자 정보(Member Table)에서 가져와서 주문자 정보와 배송지 정보로 전달
		Member orderMember = memberService.selectByMid(mid);
		model.addAttribute("orderMember", orderMember);
			
		return "order/orderPage";
	}
	
	//주문페이지에서 주문완료하기
	@RequestMapping("/orderComplete")
	public String orderComplete(@RequestParam(value="pcode", required=true) ArrayList<String> pcode, 
								@RequestParam(value="pcolor", required=true) ArrayList<String> pcolor, 
								@RequestParam(value="psize", required=true) ArrayList<String> psize,
								@RequestParam(value="pquantity", required=true) ArrayList<Integer> pquantity,
								String oname,
								String otel,
								String oaddress,
								String ocomment,
								String opaymentmethod,
								Model model) {
		logger.info("Run order/orderComplete");
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String mid = authentication.getName();
		long oidTime = System.currentTimeMillis();
		String oid = mid + oidTime;
		
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
		Date date = new Date();
		date.setTime(oidTime);
		String otime = simpleDateFormat.format(date);
				
		//사용자가 작성한 주문 데이터(Order Table)를 DB에 저장
		Order order = new Order(oid, otime, mid, oname, otel, oaddress, ocomment, opaymentmethod, '1');
		orderService.insertOrder(order);
		
		//주문상품 데이터(Orderitem Table)를 DB에 저장
		for(int i=0; i<pcode.size(); i++) {
			Orderitem orderitem = new Orderitem(oid, pcode.get(i), pcolor.get(i), psize.get(i), pquantity.get(i));
			orderitemService.insertOrderitem(orderitem);
			
			//주문 완료 상품의 상품(Product) 재고 수정
			int pstock = productService.selectPquantity(pcode.get(i), pcolor.get(i), psize.get(i));
			int quantity = pstock - pquantity.get(i);
			productService.updatePstock(pcode.get(i), pcolor.get(i), psize.get(i), quantity);

			//주문 완료 상품의 장바구니(Cartitem) 데이터 삭제
			cartitemService.deleteItem(mid, pcode.get(i), pcolor.get(i), psize.get(i));
		}
		
		List<Orderitem> orders = orderService.selectByOid(oid);
		
		ArrayList<OrderitemJoinProduct> ordereditems = new ArrayList<OrderitemJoinProduct>();
		int totalprice = 0;		//총 주문 비용
		int totalnumber = 0;	//총 주문 삼품 갯수
		
		for(Orderitem orderitem : orders) {
			Product product = productService.selectOne(orderitem.getPcode(), orderitem.getPcolor(),orderitem.getPsize());
			ordereditems.add(new OrderitemJoinProduct(product.getPname(),orderitem.getPquantity(),product.getPimage1(),
													  product.getPbrand(),orderitem.getPcolor(),orderitem.getPsize(),
													  orderitem.getPcode(),orderitem.getOid(),product.getPprice()));
			totalnumber += orderitem.getPquantity();
			totalprice += product.getPprice() * orderitem.getPquantity();
		}
		Member orderMember = memberService.selectByMid(mid);
		model.addAttribute("orderMember", orderMember);
		
		model.addAttribute("order", order);
		model.addAttribute("ordereditems", ordereditems);
		model.addAttribute("totalnumber", totalnumber);
		model.addAttribute("totalprice", totalprice);
		
		return "order/orderComplete";
	}
	
	   @RequestMapping("/delete")
	   public String delete(String oid) {
	      logger.info("Run order/delete");
	      
	      //주문 상태 0으로 변경
	      Order order = orderService.selectOneByOid(oid);
	      order.setOstatus('0');
	      orderService.update(order);
	      
	      //재고 복원
	      List<Orderitem> orderItems = orderService.selectByOid(oid);
	      for(Orderitem orderitem : orderItems) {
	         Product product = productService.selectOne(orderitem.getPcode(), orderitem.getPcolor(), orderitem.getPsize());
	         productService.updatePstock(product.getPcode(), product.getPcolor(), product.getPsize(), product.getPstock() + orderitem.getPquantity());
	      }
	      return "main";
	   }
	   
	   @RequestMapping("/count")
	   @ResponseBody
	   public String count() {
	      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	      String mid = authentication.getName();
	      int itemCount = cartitemService.selectCount(mid);
	      return String.valueOf(itemCount);
	   }
	
}  