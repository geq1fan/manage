package com.ge.controller.productline;

import java.util.List;

import javax.annotation.Resource;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ge.common.aop.OperationLog;
import com.ge.controller.base.BaseController;
import com.ge.modules.productline.model.ProductlineInfo;
import com.ge.modules.productline.service.ProductlineService;
import com.github.pagehelper.PageInfo;

@Controller
@RequestMapping("/admin/productline")
public class ProductlineController extends BaseController {

	private static final String BASE_PATH = "admin/productline/";

	@Resource
	private ProductlineService productlineService;

	/**
	 * 查看所有生产线状态
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("productline:status")
	@GetMapping("/status")
	public String listProductline(ModelMap modelMap){
		List<ProductlineInfo> listInfo = productlineService.findNewestHistoryData();
		modelMap.put("list", listInfo);
		return BASE_PATH+"productline-status";
	}
	
	/**
	 * 查看生产线状态详情
	 * 
	 * @param id
	 * @return
	 */
	@RequiresPermissions("productline:view")
	@GetMapping("/status/{id}")
	public String viewProductline(@PathVariable("id") Long id, ModelMap modelMap) {
		
		return BASE_PATH + "productline-status-view";
	}
	
	
	/**
	 * 分页查询历史数据列表
	 *
	 * @param pageNum
	 *            当前页码
	 * @param username
	 *            用户名
	 * @param startTime
	 *            开始时间
	 * @param endTime
	 *            结束时间
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
			PageInfo<ProductlineInfo> pageInfo = productlineService.findHistoryDataPage(pageNum, PAGESIZE, productlineName,
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
		ProductlineInfo productlineInfo = productlineService.findById(id);
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

			productlineService.deleteById(id);
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
			productlineService.deleteByCondition(ProductlineInfo.class, "id", ids);
			log.info("批量删除生产线历史数据成功! ids = {}", ids);

			return ResponseEntity.ok("已删除!");
		} catch (Exception e) {
			log.error("批量删除生产线历史数据失败! ids = {}, e = {}", ids, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
		}
	}
}
