package com.ge.controller.productline;

import com.ge.common.aop.OperationLog;
import com.ge.controller.base.BaseController;
import com.ge.modules.productline.model.ProductlineInfo;
import com.ge.modules.productline.model.ProductlineParam;
import com.ge.modules.productline.service.ProductlineInfoService;
import com.ge.modules.productline.service.ProductlineParamService;
import com.github.abel533.echarts.Option;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@RequestMapping("/admin/productline")
public class ProductlineController extends BaseController {

    private static final String BASE_PATH = "admin/productline/";

    @Resource
    private ProductlineInfoService productlineInfoService;
    @Resource
    private ProductlineParamService productlineParamService;

    @RequiresPermissions("productline:set")
    @GetMapping(value = {"/set/{productlineName}"})
    public String setProductline(@PathVariable("productlineName") String productlineName, ModelMap modelMap) {
        ProductlineParam record = new ProductlineParam();
        record.setProductlineName(productlineName);
        ProductlineParam productlineParam = productlineParamService.findOne(record);
        modelMap.put("model", productlineParam);
        return BASE_PATH + "productline-param";
    }

    @ResponseBody
    @PostMapping("/set")
    public ModelMap saveProductlineParam(ProductlineParam productlineParam) {
        ModelMap modelMap = new ModelMap();
        productlineParamService.update(productlineParam);
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "参数设置成功!");
        return modelMap;
    }

    /**
     * 查看所有生产线状态
     *
     * @param modelMap
     * @return
     */
    @RequiresPermissions("productline:status")
    @GetMapping("/status")
    public String listProductline(ModelMap modelMap) {
        List<ProductlineInfo> listInfo = productlineInfoService.findNewestHistoryData();
        modelMap.put("list", listInfo);
        return BASE_PATH + "productline-status";
    }

    /**
     * 查看生产线状态详情
     *
     * @param productlineName
     * @param modelMap
     * @return
     */
    @RequiresPermissions("productline:view")
    @GetMapping("/status/{productlineName}")
    public String viewProductline(@PathVariable("productlineName") String productlineName,
                                  @RequestParam("type") String type, ModelMap modelMap) {
        modelMap.put("productlineName", productlineName);
        modelMap.put("type", type);
        return BASE_PATH + "productline-status-view";
    }

    /**
     * 查看生产线熔窑压力实时数据
     *
     * @param productlineName
     * @return
     */
    @ResponseBody
    @GetMapping("/status/{productlineName}/press")
    public ModelMap viewPressStatus(@PathVariable("productlineName") String productlineName) {
        ModelMap modelMap = new ModelMap();
        try {
            Option option = productlineInfoService.selectProductlinePress(productlineName);
            modelMap.put("status", SUCCESS);
            modelMap.put("option", option);
        } catch (Exception e) {
            log.error("根据生产线名称取出option失败，productlineName={}", productlineName);
        }

        return modelMap;
    }

    /**
     * 查看生产线熔窑温度实时数据
     *
     * @param productlineName
     * @return
     */
    @ResponseBody
    @GetMapping("/status/{productlineName}/temp")
    public ModelMap viewTempStatus(@PathVariable("productlineName") String productlineName) {
        ModelMap modelMap = new ModelMap();
        try {
            Option option = productlineInfoService.selectProductlineTemp(productlineName);
            modelMap.put("status", SUCCESS);
            modelMap.put("option", option);
        } catch (Exception e) {
            log.error("根据生产线名称取出option失败，productlineName={}", productlineName);
        }

        return modelMap;
    }

    /**
     * 查看生产线熔窑液面高度实时数据
     *
     * @param productlineName
     * @return
     */
    @ResponseBody
    @GetMapping("/status/{productlineName}/height")
    public ModelMap viewHeightStatus(@PathVariable("productlineName") String productlineName) {
        ModelMap modelMap = new ModelMap();
        try {
            Option option = productlineInfoService.selectProductlineHeight(productlineName);
            modelMap.put("status", SUCCESS);
            modelMap.put("option", option);
        } catch (Exception e) {
            log.error("根据生产线名称取出option失败，productlineName={}", productlineName);
        }

        return modelMap;
    }

    /**
     * 分页查询历史数据列表
     *
     * @param pageNum         当前页码
     * @param productlineName
     * @param startTime       开始时间
     * @param endTime         结束时间
     * @param modelMap
     * @return
     */
    @RequiresPermissions("productline:history")
    @GetMapping("/history")
    public String listHistoryData(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  String productlineName, String startTime, String endTime, ModelMap modelMap) {
        try {
            log.debug("分页查询生产线历史数据参数! pageNum = {}, productlineName = {}, startTime = {}, endTime = {}", pageNum,
                    productlineName, startTime, endTime);
            PageInfo<ProductlineInfo> pageInfo = productlineInfoService.findHistoryDataPage(pageNum, PAGESIZE, productlineName,
                    startTime, endTime);
            log.info("分页查询生产线历史数据结果！ pageInfo = {}", pageInfo);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("productlineName", productlineName);
            modelMap.put("startTime", startTime);
            modelMap.put("endTime", endTime);
        } catch (Exception e) {
            log.error("分页查询生产线历史记录失败! e = {}", e);
        }

        return BASE_PATH + "productline-history";
    }

    /**
     * 查询历史数据详情
     *
     * @param id
     * @return
     */
    @RequiresPermissions("productline:view")
    @GetMapping("/history/{id}")
    public String viewHistoryData(@PathVariable("id") Long id, ModelMap modelMap) {
        ProductlineInfo productlineInfo = productlineInfoService.findById(id);
        modelMap.put("model", productlineInfo);

        return BASE_PATH + "productline-history-view";
    }

    /**
     * 根据主键ID删除生产线历史数据
     *
     * @param id
     * @return
     */
    @OperationLog(value = "删除生产线历史数据")
    @RequiresPermissions("productline:delete")
    @DeleteMapping(value = "/history/{id}")
    public ResponseEntity<String> deleteHistoryData(@PathVariable("id") Long id) {
        try {
            log.debug("删除生产线历史数据! id = {}", id);

            productlineInfoService.deleteById(id);
            log.info("删除生产线历史数据! id = {}", id);

            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("删除生产线历史数据失败! id = {}, e = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 批量删除生产线历史数据
     *
     * @param ids
     * @return
     */
    @OperationLog(value = "批量删除生产线历史数据")
    @RequiresPermissions("productline:delete")
    @DeleteMapping(value = "/history/batch/{ids}")
    public ResponseEntity<String> deleteBatchHistoryData(@PathVariable("ids") List<Object> ids) {
        try {
            log.debug("批量删除生产线历史数据! ids = {}", ids);

            if (null == ids) {
                log.info("批量删除生产线历史数据不存在! ids = {}", ids);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            productlineInfoService.deleteByCondition(ProductlineInfo.class, "id", ids);
            log.info("批量删除生产线历史数据成功! ids = {}", ids);

            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("批量删除生产线历史数据失败! ids = {}, e = {}", ids, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
