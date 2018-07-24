package com.ge.controller.inventory;


import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.inventory.model.Inventory;
import com.ge.modules.inventory.service.InventoryService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/admin/inventory")
@Controller
public class InventoryController extends BaseController {

    private static final String BASE_PATH = "admin/inventory/";

    @Resource
    private InventoryService inventoryService;

    /**
     * 分页查询产品信息
     *
     * @param pageNum
     * @param productType
     * @param modelMap
     * @return
     */
    @RequiresPermissions("inventory:list")
    @GetMapping("/list")
    public String listProductInformation(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                         String productType, ModelMap modelMap) {
        try {
            PageInfo<Inventory> pageInfo = inventoryService.findProductPage(pageNum, PAGESIZE, productType);
            log.info("分页查询产品列表结果！ pageInfo = {}", pageInfo);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("productType", productType);
        } catch (Exception e) {
            log.error("分页查询产品列表失败! e = {}", e);
        }

        return BASE_PATH + "inventory";
    }

    /**
     * 根据id删除库存信息
     *
     * @param id
     * @return
     */
    @RequiresPermissions("inventory:delete")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteProductInformation(@PathVariable("id") Long id) {
        try {
            log.debug("删除库存信息! id = {}", id);
            inventoryService.deleteById(id);
            log.info("删除库存信息成功! id = {}", id);
            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("删除库存信息失败! id = {}, e = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 返回产品编辑视图
     *
     * @return
     */
    @RequiresPermissions("inventory:edit")
    @FormToken(save = true)
    @GetMapping("/edit/{id}")
    public String editProductInformation(@PathVariable("id") Long id, ModelMap modelMap) {
        Inventory inventory = inventoryService.findById(id);
        modelMap.put("model", inventory);
        return BASE_PATH + "inventory-edit";
    }

    /**
     * 产品出库
     *
     * @param id
     * @param inventory
     * @return
     */
    @FormToken(remove = true)
    @ResponseBody
    @PutMapping(value = "/{id}")
    public ModelMap updateProductInformation(@PathVariable("id") Long id, Inventory inventory) {
        ModelMap modelMap = new ModelMap();
        inventory.setId(id);
        inventoryService.updateSelective(inventory);
        log.info("产品出库成功! productId= {}, inventory = {}", id, inventory);
        if (inventoryService.updateProductInventory(inventory)) {
            log.info("{}成品库存更新成功", inventory.getType());
        }
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "编辑成功!");
        return modelMap;
    }

    /**
     * 返回产品添加视图
     *
     * @return
     */
    @RequiresPermissions("inventory:add")
    @FormToken(save = true)
    @GetMapping("/add")
    public String addProductInformation() {
        return BASE_PATH + "inventory-add";
    }

    /**
     * 产品入库
     *
     * @param inventory
     * @return
     */
    @ResponseBody
    @FormToken(remove = true)
    @PostMapping()
    public ModelMap saveProductInformation(Inventory inventory) {
        ModelMap modelMap = new ModelMap();
        inventoryService.save(inventory);
        log.info("添加产品信息成功! productId = {}", inventory.getId());
        if (inventoryService.updateProductInventory(inventory)) {
            log.info("{}成品库存更新成功", inventory.getType());
        }
        modelMap.put("status", SUCCESS);
        modelMap.put("message", "添加成功!");
        return modelMap;
    }

}
