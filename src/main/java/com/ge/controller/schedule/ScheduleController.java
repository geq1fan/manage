package com.ge.controller.schedule;

import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.model.Product;
import com.ge.modules.order.service.OrderService;
import com.ge.modules.order.service.ProductService;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleService;
import com.ge.modules.schedule.vo.InventorySchedule;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController extends BaseController {

    private static final String BASE_PATH_ORDER = "admin/schedule/order/";
    private static final String BASE_PATH_INVENTORY = "admin/schedule/inventory/";

    @Resource
    private ScheduleService scheduleService;
    @Resource
    private OrderService orderService;
    @Resource
    private ProductService productService;

    /**
     * 返回库存生产调度视图
     *
     * @return
     */
    @RequiresPermissions("schedule:inventory")
    @GetMapping("/inventory")
    public String inventorySchedule(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                    String productType, ModelMap modelMap) {
        try {
            PageInfo<Product> pageInfo = productService.findProductPage(pageNum, PAGESIZE, productType);
            log.info("分页查询产品库存列表结果！ pageInfo = {}", pageInfo);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("productType", productType);
        } catch (Exception e) {
            log.error("分页查询产品库存列表失败! e = {}", e);
        }
        return BASE_PATH_INVENTORY + "schedule-inventory";
    }

    @FormToken(save = true)
    @GetMapping("/inventory/predict")
    public String inventoryPredict() {
        return BASE_PATH_INVENTORY + "schedule-inventory-predict";
    }

    @ResponseBody
    @FormToken(remove = true)
    @PostMapping("/inventory/predict")
    public ModelMap inventoryPredictResult(String name, String type, int period) {
        ModelMap modelMap = new ModelMap();
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "预测结果为：2000");
        return modelMap;
    }

    @FormToken(save = true)
    @GetMapping("/inventory/{ids}")
    public String inventoryScheduleResult(@PathVariable("ids") List<Object> ids, ModelMap modelMap) {
        List<Product> products = scheduleService.getProducts(ids);
        modelMap.put("models", products);
        return BASE_PATH_INVENTORY + "schedule-inventory-param";
    }

    @FormToken(remove = true)
    @PostMapping("/inventory/param")
    public String ScheduleResult(HttpServletRequest request, ModelMap modelMap) {
        Map map = request.getParameterMap();
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        List<InventorySchedule> inventoryScheduleList = scheduleService.getInventoryScheduleResult(map, scheduleInfo);
        modelMap.put("models", inventoryScheduleList);
        modelMap.put("id", scheduleInfo.getId());
        return BASE_PATH_INVENTORY + "schedule-result";
    }

    @ResponseBody
    @PostMapping("/inventory")
    public ModelMap orderScheduleResultConfirm(Long id, Boolean isCommit) {
        ScheduleInfo scheduleInfo = scheduleService.findById(id);
        scheduleInfo.setIsCommit(isCommit);
        scheduleService.updateSelective(scheduleInfo);

        log.info("库存调度结果确认成功!");
        ModelMap modelMap = new ModelMap();
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "库存调度结果确认成功!");
        return modelMap;
    }

    /**
     * 返回订单生产调度视图<br/>
     * 分页查询订单列表
     *
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
        return BASE_PATH_ORDER + "schedule-order";
    }

    /**
     * 查看订单生产调度结果
     *
     * @param ids
     * @param modelMap
     * @return
     */
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
            modelMap.put("ids", ids);
        } catch (Exception e) {
            log.error("订单生产调度失败! ids = {}, e = {}", ids, e);
        }

        return BASE_PATH_ORDER + "schedule-result";
    }

    /**
     * 确认订单生产调度结果
     *
     * @param result
     * @param isCommit
     * @return
     */
    @ResponseBody
    @FormToken(remove = true)
    @PostMapping("/order")
    public ModelMap orderScheduleResultConfirm(String result, String ids, Boolean isCommit) {
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setResult(result);
        if (ids.startsWith("[,")) {
            ids = ids.substring(2);
        }
        scheduleInfo.setOrderId(ids);
        scheduleInfo.setIsCommit(isCommit);
        scheduleService.saveSelective(scheduleInfo);

        log.info("订单调度结果确认成功! result = {}", result);
        ModelMap modelMap = new ModelMap();
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "订单调度结果确认成功!");
        return modelMap;
    }

}
