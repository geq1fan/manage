package com.ge.modules.schedule.service.impl;

import com.ge.common.service.impl.BaseServiceImpl;
import com.ge.modules.order.mapper.OrderMapper;
import com.ge.modules.order.mapper.ProductMapper;
import com.ge.modules.order.model.OrderInfo;
import com.ge.modules.order.model.Product;
import com.ge.modules.schedule.model.ScheduleInfo;
import com.ge.modules.schedule.service.ScheduleInfoService;
import com.ge.modules.schedule.vo.InventorySchedule;
import com.ge.modules.schedule.vo.OrderSchedule;
import com.ge.util.ga.GeneticAlgorithmUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.*;

@Transactional
@Service
public class ScheduleInfoServiceImpl extends BaseServiceImpl<ScheduleInfo> implements ScheduleInfoService {

    @Resource
    private
    OrderMapper orderMapper;
    @Resource
    private
    ProductMapper productMapper;
    @Resource
    private ScheduleInfoService scheduleInfoService;

    @Override
    public PageInfo<OrderSchedule> getOrderScheduleResult(Integer pageNum, Integer pageSize, List<Object> ids, String productline, ScheduleInfo scheduleInfo) {
        Example example = new Example(OrderInfo.class);
        example.createCriteria().andIn("id", ids);
        List<OrderInfo> orderInfos = orderMapper.selectByExample(example);

        List<Long> result = GeneticAlgorithmUtils.calculate(orderInfos);

        List<OrderSchedule> list = new LinkedList<>();
        String[] productlineName = productline.split("，");
        for (Long id : result) {
            OrderInfo orderInfo = orderMapper.selectByPrimaryKey(id);
            OrderSchedule orderSchedule = new OrderSchedule();
            orderSchedule.setOrderInfo(orderInfo);
            int index = (int) (Math.random() * productlineName.length);
            orderSchedule.setProductline(productlineName[index]);
            list.add(orderSchedule);
        }

        scheduleInfo.setOrderId(ids.toString());
        scheduleInfo.setResult(result.toString());
        List<String> productlineNameList = new ArrayList<>();
        for (OrderSchedule orderSchedule : list) {
            productlineNameList.add(orderSchedule.getProductline());
        }
        scheduleInfo.setProductline(productlineNameList.toString());
        scheduleInfoService.saveSelective(scheduleInfo);

        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo<>(list);
    }

    @Override
    public List<Product> getProducts(List<Object> ids) {
        Example example = new Example(Product.class);
        example.createCriteria().andIn("id", ids);

        return productMapper.selectByExample(example);
    }

    @Override
    public List<InventorySchedule> getInventoryScheduleResult(Map map, ScheduleInfo scheduleInfo) {
        String[] amounts = (String[]) map.get("amount");
        String[] productlines = (String[]) map.get("productline");
        productlines = productlines[0].split("，");
        String[] ids = (String[]) map.get("id");

        List<int[]> list = new ArrayList<>();
        for (String amount : amounts) {
            int num = Integer.valueOf(amount.trim());
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

        String[] resultArray = new String[productlines.length];
        List<InventorySchedule> inventoryScheduleList = new ArrayList<>();
        for (int j = 0; j < productlines.length; j++) {
            String productline = productlines[j];
            StringBuilder result = new StringBuilder();
            int[] amount = new int[ids.length];
            for (int i = 0; i < ids.length; i++) {
                Long id = Long.valueOf(ids[i].trim());
                Product product = productMapper.selectByPrimaryKey(id);

                result.append(product.getType()).append(product.getName()).append("生产");
                int[] ints = list.get(i);
                result.append(ints[j]).append("件\r\n");
                amount[i] = ints[j];
            }
            resultArray[j] = Arrays.toString(amount);
            InventorySchedule inventorySchedule = new InventorySchedule();
            inventorySchedule.setProductline(productline);
            inventorySchedule.setResult(result.toString());
            inventoryScheduleList.add(inventorySchedule);
        }
        scheduleInfo.setProductline(Arrays.toString(productlines));
        scheduleInfo.setProductId(Arrays.toString(ids));
        scheduleInfo.setResult(Arrays.toString(resultArray));
        scheduleInfoService.saveSelective(scheduleInfo);

        return inventoryScheduleList;
    }

}
