package com.ge.controller.schedule;

import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.model.Product;
import com.ge.modules.order.service.OrderService;
import com.ge.modules.order.service.ProductService;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleInfoService;
import com.ge.modules.schedule.service.SchedulePlanService;
import com.ge.modules.schedule.vo.InventorySchedule;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Controller
@RequestMapping("/admin/schedule")
public class ScheduleController extends BaseController {

    private static final String BASE_PATH_ORDER = "admin/schedule/order/";
    private static final String BASE_PATH_INVENTORY = "admin/schedule/inventory/";
    private static final String BASE_PATH_SCHEDULE = "admin/schedule/";

    @Resource
    private ScheduleInfoService scheduleInfoService;
    @Resource
    private SchedulePlanService schedulePlanService;
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

    /**
     * 返回需求预测视图
     *
     * @return
     */
    @FormToken(save = true)
    @GetMapping("/inventory/predict")
    public String inventoryPredict() {
        return BASE_PATH_INVENTORY + "schedule-inventory-predict";
    }

    /**
     * 返回需求预测结果
     *
     * @param name
     * @param type
     * @param period
     * @return
     */
    @ResponseBody
    @FormToken(remove = true)
    @PostMapping("/inventory/predict")
    public ModelMap inventoryPredictResult(String name, String type, int period) {
        ModelMap modelMap = new ModelMap();
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "预测结果为：2000");
        return modelMap;
    }

    /**
     * 返回库存调度参数视图
     *
     * @param ids
     * @param modelMap
     * @return
     */
    @FormToken(save = true)
    @GetMapping("/inventory/{ids}")
    public String inventoryScheduleParam(@PathVariable("ids") List<Object> ids, ModelMap modelMap) {
        List<Product> products = scheduleInfoService.getProducts(ids);
        modelMap.put("models", products);
        return BASE_PATH_INVENTORY + "schedule-inventory-param";
    }

    /**
     * 返回库存调度结果
     *
     * @param request
     * @param modelMap
     * @return
     */
    @FormToken(remove = true)
    @PostMapping("/inventory/param")
    public String ScheduleResult(HttpServletRequest request, ModelMap modelMap) {
        Map map = request.getParameterMap();
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        List<InventorySchedule> inventoryScheduleList = scheduleInfoService.getInventoryScheduleResult(map, scheduleInfo);
        modelMap.put("models", inventoryScheduleList);
        modelMap.put("id", scheduleInfo.getId());
        return BASE_PATH_INVENTORY + "schedule-result";
    }

    /**
     * 确认库存调度结果
     *
     * @param id
     * @param isCommit
     * @return
     */
    @ResponseBody
    @PostMapping("/inventory")
    public ModelMap inventoryScheduleResultConfirm(Long id, Boolean isCommit) {
        ScheduleInfo scheduleInfo = scheduleInfoService.findById(id);
        scheduleInfo.setIsCommit(isCommit);
        scheduleInfoService.updateSelective(scheduleInfo);
        if (schedulePlanService.updateSchedulePlan(id)) {
            log.info("生产计划更新成功！");
        }
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
    public String orderSchedule(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                ModelMap modelMap) {
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
     * 返回订单调度参数视图
     *
     * @param ids
     * @param modelMap
     * @return
     */
    @FormToken(save = true)
    @GetMapping("/order/{ids}")
    public String orderScheduleParam(@PathVariable("ids") List<Object> ids, ModelMap modelMap) {
        modelMap.put("ids", StringUtils.join(ids, ","));
        return BASE_PATH_ORDER + "schedule-order-param";
    }

    /**
     * 查看订单生产调度结果
     *
     * @param
     * @param modelMap
     * @return
     */
    @FormToken(remove = true)
    @PostMapping("/order/param")
    public String orderScheduleResult(String productline, String ids, ModelMap modelMap) {
        try {
            if (ids == null) {
                log.error("生产调度订单不存在! ids = {}", ids);
            }
            List<Object> idList = Arrays.asList(Objects.requireNonNull(ids).split(","));
            ScheduleInfo scheduleInfo = new ScheduleInfo();
            PageInfo pageInfo = scheduleInfoService.getOrderScheduleResult(PAGENUM, PAGESIZE, idList,
                    productline, scheduleInfo);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("id", scheduleInfo.getId());
        } catch (Exception e) {
            log.error("订单生产调度失败! ids = {}, e = {}", ids, e);
        }

        return BASE_PATH_ORDER + "schedule-result";
    }

    /**
     * 确认订单生产调度结果
     *
     * @param id
     * @param isCommit
     * @return
     */
    @ResponseBody
    @PostMapping("/order")
    public ModelMap orderScheduleResultConfirm(Long id, Boolean isCommit) {
        ScheduleInfo scheduleInfo = scheduleInfoService.findById(id);
        scheduleInfo.setIsCommit(isCommit);
        scheduleInfoService.updateSelective(scheduleInfo);
        if (schedulePlanService.updateSchedulePlan(id)) {
            log.info("生产计划更新成功！");
        }
        log.info("订单调度结果确认成功!");
        ModelMap modelMap = new ModelMap();
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "订单调度结果确认成功!");
        return modelMap;
    }

    /**
     * 根据productlineName查看生产计划
     *
     * @param pageNum
     * @param productlineName
     * @param modelMap
     * @return
     */
    @RequiresPermissions("schedule:view")
    @GetMapping("/view")
    public String ScheduleView(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                               String productlineName, Integer status, ModelMap modelMap) {
        PageInfo pageInfo = schedulePlanService.getSchedulePage(pageNum, PAGESIZE, productlineName, status);
        modelMap.put("pageInfo", pageInfo);
        modelMap.put("productlineName", productlineName);
        modelMap.put("status", status);
        return BASE_PATH_SCHEDULE + "schedule-view";
    }

    /**
     * 执行生产任务
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @GetMapping("/execute/{ids}")
    public ModelMap ScheduleExecute(@PathVariable("ids") List<Object> ids) {
        ModelMap modelMap = new ModelMap();
        schedulePlanService.setSchedulePlanDoing(ids);
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "确认成功");
        return modelMap;
    }

    /**
     * 反馈生产任务
     *
     * @param ids
     * @return
     */
    @ResponseBody
    @GetMapping("/feedback/{ids}")
    public ModelMap ScheduleFeedback(@PathVariable("ids") List<Object> ids) {
        ModelMap modelMap = new ModelMap();
        schedulePlanService.setSchedulePlanDone(ids);
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "确认成功");
        return modelMap;
    }
}
