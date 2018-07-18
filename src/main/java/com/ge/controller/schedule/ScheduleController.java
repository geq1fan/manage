package com.ge.controller.schedule;

import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.service.OrderService;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController extends BaseController {

    private static final String BASE_PATH = "admin/schedule/";

    @Resource
    private ScheduleService scheduleService;
    @Resource
    private OrderService orderService;

    /**
     * @return
     */
    @RequiresPermissions("schedule:inventory")
    @GetMapping("/inventory")
    public String inventorySchedule() {

        return BASE_PATH + "schedule-inventory";
    }

    /**
     * @param modelMap
     * @return
     */
    @RequiresPermissions("schedule:order")
    @GetMapping("/order")
    public String orderSchedule(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, ModelMap modelMap) {
        try {
            PageInfo<OrderInfo> pageInfo = orderService.findProcessedOrderPage(pageNum, PAGESIZE);
            log.info("分页查询已审核但未过期订单列表结果！ pageInfo = {}", pageInfo);
            modelMap.put("pageInfo", pageInfo);
        } catch (Exception e) {
            log.error("分页查询已审核但未过期订单列表失败! e = {}", e);
        }
        return BASE_PATH + "order/schedule-order";
    }

    @FormToken(save = true)
    @GetMapping("/order/{ids}")
    public String orderScheduleResult(@PathVariable("ids") List<Object> ids, ModelMap modelMap) {
        try {
            if (ids == null) {
                log.error("生产调度订单不存在! ids = {}", ids);
            }
            List<Long> result = scheduleService.getOrderScheduleResult(ids);
            log.info("生产调度已完成！result = {}", result);
            PageInfo<OrderInfo> pageInfo = orderService.findOrderPageByIds(PAGENUM, PAGESIZE, result);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("result", result.toString());
        } catch (Exception e) {
            log.error("订单生产调度失败! ids = {}, e = {}", ids, e);
        }

        return BASE_PATH + "order/schedule-result";
    }

    @ResponseBody
    @FormToken(remove = true)
    @PostMapping("/order")
    public ModelMap orderScheduleResultConfirm(String result, Boolean isCommit) {
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setResult(result);
        scheduleInfo.setIsCommit(isCommit);
        scheduleService.saveSelective(scheduleInfo);

        log.info("订单调度结果确认成功! result = {}", result);
        ModelMap modelMap = new ModelMap();
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "订单调度结果确认成功!");
        return modelMap;
    }

}
