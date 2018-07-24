package com.ge.modules.schedule.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.mapper.OrderMapper;
import com.ge.modules.order.mapper.ProductMapper;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.model.Product;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleService;
import com.ge.modules.schedule.vo.InventorySchedule;
import com.ge.util.ga.GeneticAlgorithmUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Transactional
@Service
public class ScheduleServiceImpl extends BaseServiceImpl<ScheduleInfo> implements ScheduleService {

    @Resource
    private
    OrderMapper orderMapper;
    @Resource
    private
    ProductMapper productMapper;
    @Resource
    private ScheduleService scheduleService;

    @Override
    public List<Long> getOrderScheduleResult(List<Object> ids) {
        ScheduleInfo scheduleInfo = new ScheduleInfo();
        scheduleInfo.setOrderId(ids.toString());

        Example example = new Example(OrderInfo.class);
        example.createCriteria().andIn("id", ids);
        List<OrderInfo> orderInfos = orderMapper.selectByExample(example);

        return GeneticAlgorithmUtils.calculate(orderInfos);
    }

    @Override
    public List<Product> getProducts(List<Object> ids) {
        Example example = new Example(Product.class);
        example.createCriteria().andIn("id", ids);
        List<Product> products = productMapper.selectByExample(example);

        return products;
    }

    @Override
    public List<InventorySchedule> getInventoryScheduleResult(Map map, ScheduleInfo scheduleInfo) {
        String[] amounts = (String[]) map.get("amount");
        String[] productlines = (String[]) map.get("productline");
        productlines = productlines[0].split("，");
        String[] ids = (String[]) map.get("id");

        scheduleInfo.setProductline(Arrays.toString(productlines));
        scheduleInfo.setProductId(Arrays.toString(ids));
        scheduleService.saveSelective(scheduleInfo);

        List<int[]> list = new ArrayList<>();
        for (String amount : amounts) {
            int num = Integer.valueOf(amount);
            int[] endPoints = new int[productlines.length + 1];
            for (int i = 0; i < productlines.length - 1; i++) {
                endPoints[i] = (int) (Math.random() * num);
            }
            endPoints[productlines.length - 1] = 0;
            endPoints[productlines.length] = num;
            Arrays.sort(endPoints);

            int[] result = new int[productlines.length];
            for (int i = 0; i < endPoints.length - 1; i++) {
                result[i] = endPoints[i + 1] - endPoints[i];
            }
            list.add(result);
        }

        List<InventorySchedule> inventoryScheduleList = new ArrayList<>();
        for (int j = 0; j < productlines.length; j++) {
            String productline = productlines[j];
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < ids.length; i++) {
                Long id = Long.valueOf(ids[i]);
                Product product = productMapper.selectByPrimaryKey(id);

                result.append(product.getType()).append(product.getName()).append("生产");
                int[] ints = list.get(i);
                result.append(ints[j]).append("件\r\n");
            }
            InventorySchedule inventorySchedule = new InventorySchedule();
            inventorySchedule.setProductline(productline);
            inventorySchedule.setResult(result.toString());
            inventoryScheduleList.add(inventorySchedule);
        }

        return inventoryScheduleList;
    }
}
