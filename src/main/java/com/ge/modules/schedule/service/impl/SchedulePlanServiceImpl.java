package com.ge.modules.schedule.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.inventory.service.InventoryService;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.model.Product;
import com.ge.modules.order.service.OrderService;
import com.ge.modules.order.service.ProductService;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.model.SchedulePlan;
import com.ge.modules.schedule.service.ScheduleInfoService;
import com.ge.modules.schedule.service.SchedulePlanService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

@Transactional
@Service
public class SchedulePlanServiceImpl extends BaseServiceImpl<SchedulePlan> implements SchedulePlanService {
    @Resource
    private ScheduleInfoService scheduleInfoService;
    @Resource
    private ProductService productService;
    @Resource
    private OrderService orderService;
    @Resource
    private InventoryService inventoryService;

    /**
     * 根据生产线名称分页获取生产计划
     *
     * @param pageNum
     * @param pageSize
     * @param productlineName
     * @return
     */
    @Transactional(readOnly = true)
    @Override
    public PageInfo getSchedulePage(Integer pageNum, Integer pageSize, String productlineName, Integer status) {
        Example example = new Example(SchedulePlan.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotEmpty(productlineName)) {
            criteria.andLike("productline", "%" + productlineName + "%");
        }
        if (Objects.nonNull(status)) {
            criteria.andEqualTo("status", status);
        }
        example.orderBy("createTime");
        PageHelper.startPage(pageNum, pageSize);
        List<SchedulePlan> list = this.selectByExample(example);
        return new PageInfo<>(list);
    }

    /**
     * 根据调度结果更新生产计划
     *
     * @param scheduleInfoId
     * @return
     */
    @Override
    public Boolean updateSchedulePlan(Long scheduleInfoId) {
        ScheduleInfo scheduleInfo = scheduleInfoService.findById(scheduleInfoId);
        if (scheduleInfo.getIsCommit()) {
            String productlines = scheduleInfo.getProductline();
            String results = scheduleInfo.getResult();
            String orderIds = scheduleInfo.getOrderId();
            String productIds = scheduleInfo.getProductId();

            String[] resultArray;
            String[] productIdArray;
            String[] productlineArray = productlines.substring(productlines.indexOf("[") + 1,
                    productlines.lastIndexOf("]")).split(",");

            LinkedList<SchedulePlan> schedulePlanList = new LinkedList<>();
            if (StringUtils.isNotEmpty(orderIds)) {
                resultArray = results.substring(results.indexOf("[") + 1,
                        results.lastIndexOf("]")).split(",");
                for (int i = 0; i < productlineArray.length; i++) {
                    String productline = productlineArray[i];
                    Long orderId = Long.valueOf(resultArray[i].trim());
                    //将订单设置为已调度状态
                    orderService.updateScheduledOrder(orderId);
                    OrderInfo orderInfo = orderService.findById(orderId);

                    SchedulePlan schedulePlan = new SchedulePlan();
                    schedulePlan.setTargetId(orderId);
                    schedulePlan.setProductline(productline);
                    schedulePlan.setName(orderInfo.getName());
                    schedulePlan.setType(orderInfo.getType());
                    schedulePlan.setAmount(orderInfo.getAmount());
                    schedulePlan.setStatus(-1);
                    schedulePlan.setCreateTime(new Date(new Date().getTime() + i * 1000));
                    schedulePlan.setModifyTime(schedulePlan.getCreateTime());
                    schedulePlanList.add(schedulePlan);
                }
            }

            if (StringUtils.isNotEmpty(productIds)) {
                productIdArray = productIds.substring(productIds.indexOf("[") + 1,
                        productIds.lastIndexOf("]")).split(",");
                results = results.substring(results.indexOf("[") + 1,
                        results.lastIndexOf("]"));
                resultArray = results.substring(results.indexOf("[") + 1,
                        results.lastIndexOf("]")).split(Pattern.quote("], ["));
                for (int i = 0; i < productlineArray.length; i++) {
                    String productline = productlineArray[i];
                    String[] amounts = resultArray[i].split(",");
                    for (int j = 0; j < productIdArray.length; j++) {
                        Long productId = Long.valueOf(productIdArray[j].trim());
                        Product product = productService.findById(productId);

                        SchedulePlan schedulePlan = new SchedulePlan();
                        schedulePlan.setTargetId(productId);
                        schedulePlan.setProductline(productline);
                        schedulePlan.setName(product.getName());
                        schedulePlan.setType(product.getType());
                        schedulePlan.setAmount(Integer.valueOf(amounts[j].trim()));
                        schedulePlan.setStatus(-1);
                        schedulePlan.setCreateTime(new Date(new Date().getTime() + j * 1000));
                        schedulePlan.setModifyTime(schedulePlan.getCreateTime());
                        schedulePlanList.add(schedulePlan);
                    }
                }
            }

            return this.saveList(schedulePlanList) == schedulePlanList.size();
        }
        return false;
    }

    /**
     * 设置生产计划为进行中
     *
     * @param ids
     */
    @Override
    public void setSchedulePlanDoing(List<Object> ids) {
        Example example = new Example(SchedulePlan.class);
        example.createCriteria().andIn("id", ids);
        List<SchedulePlan> schedulePlanList = this.selectByExample(example);
        for (SchedulePlan schedulePlan : schedulePlanList) {
            schedulePlan.setStatus(0);
            schedulePlan.setModifyTime(new Date());
            this.updateSelective(schedulePlan);
        }
    }

    /**
     * 设置生产计划为已完成
     *
     * @param ids
     */
    @Override
    public void setSchedulePlanDone(List<Object> ids) {
        Example example = new Example(SchedulePlan.class);
        example.createCriteria().andIn("id", ids);
        List<SchedulePlan> schedulePlanList = this.selectByExample(example);
        for (SchedulePlan schedulePlan : schedulePlanList) {
            schedulePlan.setStatus(1);
            schedulePlan.setModifyTime(new Date());
            //将订单设置为已完成状态
            orderService.updateCompletedOrder(schedulePlan.getTargetId());
            inventoryService.updateCompletedInventory(schedulePlan.getTargetId());
            this.updateSelective(schedulePlan);
        }
    }

}
