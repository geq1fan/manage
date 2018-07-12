package com.ge.controller.schedule;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ge.controller.base.BaseController;
import com.ge.modules.schedule.service.ScheduleService;

@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController extends BaseController {

	private static final String BASE_PATH = "admin/schedule/";

	@Resource
	private ScheduleService scheduleService;
	
	/**
	 * 
	 * @return
	 */
	@RequiresPermissions("schedule:inventory")
	@GetMapping("/inventory")
	public String inventorySchedule() {

		return BASE_PATH + "schedule-inventory";
	}
	
	/**
	 * 
	 * @return
	 */
	@RequiresPermissions("schedule:order")
	@GetMapping("/order")
	public String orderSchedule() {

		return BASE_PATH + "schedule-order";
	}

}
