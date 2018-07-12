package com.ge.controller.order;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ge.controller.base.BaseController;
import com.ge.modules.order.service.OrderService;

@Controller
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

	private static final String BASE_PATH = "admin/order/";
	
	@Resource
	private OrderService orderService;

	@GetMapping("/list")
	public String listProduct(ModelMap modelMap) {

		
		return BASE_PATH + "order";
	}
	
	@GetMapping("/add")
	public String addProduct(){
		
		return BASE_PATH+"order-add";
	}
	
}
