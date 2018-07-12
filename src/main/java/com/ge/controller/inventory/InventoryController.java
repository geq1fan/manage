package com.ge.controller.inventory;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.inventory.model.Product;
import com.ge.modules.inventory.service.ProductService;
import com.github.pagehelper.PageInfo;

@RequestMapping("/admin/inventory")
@Controller
public class InventoryController extends BaseController {

	private static final String BASE_PATH = "admin/inventory/";

	@Resource
	private ProductService productService;

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
			PageInfo<Product> pageInfo = productService.findPage(pageNum, PAGESIZE, productType);
			log.info("分页查询产品列表结果！ pageInfo = {}", pageInfo);
			modelMap.put("pageInfo", pageInfo);
			modelMap.put("productType", productType);
		} catch (Exception e) {
			log.error("分页查询产品列表失败! e = {}", e);
		}

		return BASE_PATH + "inventory";
	}

	/**
	 * 根据id删除产品信息
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("inventory:delete")
	@DeleteMapping(value = "/product/{id}")
	public ResponseEntity<String> deleteProductInformation(@PathVariable("id") Long id) {
		try {
			log.debug("删除产品信息! id = {}", id);
			productService.deleteById(id);
			log.info("删除产品信息成功! id = {}", id);
			return ResponseEntity.ok("已删除!");
		} catch (Exception e) {
			log.error("删除产品信息失败! id = {}, e = {}", id, e);
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
	@GetMapping("/product/edit/{id}")
	public String editProductInformation(@PathVariable("id") Long id, ModelMap modelMap) {
		Product product = productService.findById(id);
		modelMap.put("model", product);
		return BASE_PATH + "inventory-product-edit";
	}

	/**
	 * 更新产品信息
	 * 
	 * @param id
	 * @param product
	 * @return
	 */
	@FormToken(remove = true)
	@ResponseBody
	@PutMapping(value = "/product/{id}")
	public ModelMap updateProductInformation(@PathVariable("id") Long id, Product product) {
		ModelMap modelMap = new ModelMap();
		product.setId(id);
		productService.updateSelective(product);
		log.info("编辑产品信息成功! productId= {}, product = {}", id, product);
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
	@GetMapping("/product/add")
	public String addProductInformation() {
		return BASE_PATH + "inventory-product-add";
	}

	@FormToken(remove = true)
	@ResponseBody
	@PostMapping("/product/add")
	public ModelMap saveProductInformation(Long id, Product product) {
		ModelMap modelMap = new ModelMap();

		return modelMap;
	}

	/**
	 * 根据产品名称和类型判断产品是否存在
	 * 
	 * @param id
	 * @param productName
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@GetMapping("/product/isExist")
	public Boolean isProductExist(Long id, String type) throws Exception {
		boolean flag = true;
		Product record = productService.findByProductName(type);
		if (null != record && !record.getId().equals(id)) {
			flag = false;
		}
		log.info("检验产品名是否存在结果! flag = {}", flag);
		return flag;
	}
}
