package com.ge.controller.device;

import com.ge.common.interceptor.FormToken;
import com.ge.controller.base.BaseController;
import com.ge.modules.device.model.Device;
import com.ge.modules.device.service.DeviceService;
import com.github.pagehelper.PageInfo;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RequestMapping("/admin/device")
@Controller
public class DeviceController extends BaseController {
    private static final String BASE_PATH = "admin/device/";

    @Resource
    private DeviceService deviceService;

    /**
     * 根据条件分页查看设备信息
     *
     * @param pageNum
     * @param condition
     * @param status
     * @param modelMap
     * @return
     */
    @RequiresPermissions("device:list")
    @GetMapping("/list")
    public String listDeviceInformation(@RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                        String condition, Integer status, ModelMap modelMap) {
        try {
            PageInfo pageInfo = deviceService.findDevicePageByConditon(pageNum, PAGESIZE, condition, status);
            log.info("通过{}分页查询设备信息列表结果！ pageInfo = {}", condition, pageInfo);
            modelMap.put("pageInfo", pageInfo);
            modelMap.put("condition", condition);
            modelMap.put("status", status);
        } catch (Exception e) {
            log.error("通过{}分页查询设备信息列表失败! e = {}", condition, e);
        }
        return BASE_PATH + "device";
    }

    /**
     * 删除设备信息
     *
     * @param id
     * @return
     */
    @RequiresPermissions("device:delete")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<String> deleteDeviceInformation(@PathVariable("id") Long id) {
        try {
            log.debug("删除设备信息! id = {}", id);
            deviceService.deleteById(id);
            log.info("删除设备信息成功! id = {}", id);
            return ResponseEntity.ok("已删除!");
        } catch (Exception e) {
            log.error("删除设备信息失败! id = {}, e = {}", id, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 查看设备详情
     *
     * @param deviceId
     * @param modelMap
     * @return
     */
    @GetMapping("/view/{id}")
    public String viewDeviceInformation(@PathVariable("id") Long deviceId, ModelMap modelMap) {
        Device device = deviceService.findById(deviceId);
        modelMap.put("model", device);
        return BASE_PATH + "device-view";
    }

    /**
     * 返回设备信息编辑视图
     *
     * @param deviceId
     * @param modelMap
     * @return
     */
    @RequiresPermissions("device:edit")
    @FormToken(save = true)
    @GetMapping("/edit/{id}")
    public String editDeviceInformation(@PathVariable("id") Long deviceId, ModelMap modelMap) {
        Device device = deviceService.findById(deviceId);
        modelMap.put("model", device);
        return BASE_PATH + "device-edit";
    }

    /**
     * 更新设备
     *
     * @param id
     * @param device
     * @return
     */
    @FormToken(remove = true)
    @ResponseBody
    @PutMapping(value = "/{id}")
    public ModelMap updateDeviceInformation(@PathVariable("id") Long id, Device device) {
        ModelMap modelMap = new ModelMap();
        device.setId(id);
        deviceService.updateSelective(device);
        log.info("更新设备信息成功! inventory = {}", device);

        modelMap.put("status", SUCCESS);
        modelMap.put("message", "编辑成功!");
        return modelMap;
    }

    /**
     * 返回设备信息添加视图
     *
     * @return
     */
    @RequiresPermissions("device:add")
    @FormToken(save = true)
    @GetMapping("/add")
    public String addDeviceInformation() {
        return BASE_PATH + "device-add";
    }

    /**
     * 添加设备信息
     *
     * @param device
     * @return
     */
    @ResponseBody
    @FormToken(remove = true)
    @PostMapping()
    public ModelMap saveDeviceInformation(Device device) {
        ModelMap modelMap = new ModelMap();
        deviceService.saveSelective(device);
        log.info("添加设备信息成功! productId = {}", device.getId());

        modelMap.put("status", SUCCESS);
        modelMap.put("message", "添加成功!");
        return modelMap;
    }
}
