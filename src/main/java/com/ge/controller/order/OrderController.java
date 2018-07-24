package com.ge.controller.order;

import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.model.Product;
import com.ge.modules.order.service.OrderService;
import com.ge.modules.order.service.ProductService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Controller
@RequestMapping("/admin/order")
public class OrderController extends BaseController {

    private static final String BASE_PATH = "admin/order/";

    @Resource
    private OrderService orderService;
    @Resource
    private ProductService orderProductService;

    /**
     * 根据是否审核分页查询订单列表
     *
     * @param pageNum
     * @param startTime
     * @param endTime
     * @param processed
     * @param modelMap
     * @return
     */
    @RequiresPermissions("order:list")
    @GetMapping("/list")
    public String listOrder(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, String startTime,
                            String endTime, Boolean processed, Boolean expired, ModelMap modelMap) {
        try {
            PageInfo<OrderInfo> pageInfo = orderService.findOrderPage(pageNum, PAGESIZE, endTime, startTime, processed, expired);
            log.info("分页查询订单列表结果！ pageInfo = {}", pageInfo);
            modelMap.put("pageInfo", pageInfo);
        } catch (Exception e) {
            log.error("分页查询订单列表失败! e = {}", e);
        }
        return BASE_PATH + "order";
    }

    /**
     * 根据id删除订单记录
     *
     * @param id
     * @return
     */
    @RequiresPermissions("order:delete")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteOrderInformation(@PathVariable("id") Long id) {
        try {
            log.debug("删除订单信息! id = {}", id);
            orderService.deleteById(id);
            log.info("删除订单信息成功! id = {}", id);
            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("删除订单信息失败! id = {}, e = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 返回订单审核窗口
     *
     * @param id
     * @param modelMap
     * @return
     */
    @RequiresPermissions("order:check")
    @FormToken(save = true)
    @GetMapping("/{id}")
    public String checkOrder(@PathVariable("id") Long id, ModelMap modelMap) {
        OrderInfo orderInfo = orderService.findById(id);
        modelMap.put("model", orderInfo);
        return BASE_PATH + "order-check";
    }

    /**
     * 审核订单信息
     *
     * @param id
     * @param orderInfo
     * @return
     */
    @RequiresPermissions("order:check")
    @FormToken(remove = true)
    @ResponseBody
    @PostMapping("/{id}")
    public ModelMap checkOrderInformation(@PathVariable("id") Long id, OrderInfo orderInfo) {
        ModelMap modelMap = new ModelMap();
        orderInfo.setId(id);
        orderService.updateSelective(orderInfo);
        log.info("订单审核成功! orderInfo = {}", orderInfo);
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "订单审核成功!");
        return modelMap;
    }

    /**
     * 管理商品信息
     *
     * @param pageNum
     * @param productType
     * @param modelMap
     * @return
     */
    @RequiresPermissions("order:product")
    @GetMapping("/product")
    public String manageProduct(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                String productType, ModelMap modelMap) {
        try {
            PageInfo<Product> pageInfo = orderProductService.findProductPage(pageNum, PAGESIZE, productType);
            log.info("分页查询产品列表结果！ pageInfo = {}", pageInfo);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("productType", productType);
        } catch (Exception e) {
            log.error("分页查询产品列表失败! e = {}", e);
        }
        return BASE_PATH + "product";
    }

    /**
     * 根据id删除商品信息
     *
     * @param id
     * @return
     */
    @RequiresPermissions("product:delete")
    @DeleteMapping(value = "/product/{id}")
    public ResponseEntity<String> deleteProductInformation(@PathVariable("id") Long id) {
        try {
            log.debug("删除产品信息! id = {}", id);
            orderProductService.deleteById(id);
            log.info("删除产品信息成功! id = {}", id);
            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("删除产品信息失败! id = {}, e = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 返回订单添加窗口
     *
     * @param id
     * @param modelMap
     * @return
     */
    @FormToken(save = true)
    @GetMapping("/add/{id}")
    public String addOrder(@PathVariable("id") Long id, ModelMap modelMap) {
        Product product = orderProductService.findById(id);
        modelMap.put("model", product);
        return BASE_PATH + "order-add";
    }

    /**
     * 添加订单信息
     *
     * @param orderInfo
     * @return
     */
    @FormToken(remove = true)
    @ResponseBody
    @PostMapping
    public ModelMap addOrderInformation(OrderInfo orderInfo) {
        ModelMap modelMap = new ModelMap();
        orderInfo.setProcessed(false);
        orderInfo.setExpired(false);
        orderService.save(orderInfo);
        log.info("添加订单成功! orderInfo = {}", orderInfo);
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "编辑成功!");
        return modelMap;
    }

    /**
     * 返回产品编辑视图
     *
     * @return
     */
    @RequiresPermissions("product:edit")
    @FormToken(save = true)
    @GetMapping("/product/edit/{id}")
    public String editProductInformation(@PathVariable("id") Long id, ModelMap modelMap) {
        Product product = orderProductService.findById(id);
        modelMap.put("model", product);
        return BASE_PATH + "order-product-edit";
    }

    /**
     * 更新产品信息
     *
     * @param id
     * @param product
     * @return
     */
    @RequiresPermissions("product:edit")
    @FormToken(remove = true)
    @ResponseBody
    @PutMapping(value = "/product/{id}")
    public ModelMap updateProductInformation(@PathVariable("id") Long id, Product product) {
        ModelMap modelMap = new ModelMap();
        product.setId(id);
        orderProductService.updateSelective(product);
        log.info("编辑产品信息成功! orderProductId= {}, product = {}", id, product);
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "编辑成功!");
        return modelMap;
    }

    /**
     * 返回产品添加视图
     *
     * @return
     */
    @RequiresPermissions("product:add")
    @FormToken(save = true)
    @GetMapping("/product/add")
    public String addProductInformation() {
        return BASE_PATH + "order-product-add";
    }

    @RequiresPermissions("product:add")
    @ResponseBody
    @FormToken(remove = true)
    @PostMapping("/product")
    public ModelMap saveProductInformation(Product product) {
        ModelMap modelMap = new ModelMap();
        orderProductService.save(product);
        log.info("添加产品信息成功! orderProductId = {}", product.getId());
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "添加成功!");
        return modelMap;
    }
}
