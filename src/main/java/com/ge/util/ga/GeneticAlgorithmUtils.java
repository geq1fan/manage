package com.ge.util.ga;

import com.ge.modules.order.model.OrderInfo;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithmUtils {

    /**
     * 种群数量
     */
    private static final int POP_SIZE = 100;
    /**
     * 最大迭代次数
     */
    private static final int MAX_ITER_NUM = 1000;
    /**
     * 基因交叉变异的概率
     */
    private static final double CROSS_RATE = 0.8;
    /**
     * 基因突变的概率
     */
    private static final double MUTATION_RATE = 0.01;
    /**
     * 订单信息
     * 依次为产品规格、数量、交货日期、逾期罚金
     */
    private static final int[][] ORDER_INFO = {{1, 400, 2, 600}, {3, 600, 5, 1400}, {2, 200, 10, 1300},
            {4, 400, 5, 350}, {3, 200, 8, 900}, {2, 600, 10, 400}, {4, 300, 6, 600}, {3, 150, 12, 850},
            {2, 300, 12, 2100}, {1, 450, 16, 1600}};
    /**
     * OrderInfo数组中参数的含义
     */
    static final int TYPE = 0;
    static final int AMOUNT = 1;
    static final int DEADLINE = 2;
    static final int PENALTY = 3;
    private static final int Id = 4;
    /**
     * 生产线切换生产的花费
     */
    private static final int CHANGE_COST = 200;
    /**
     * 生产线的数量
     */
    private static final int LINE_NUM = 1;
    /**
     * 生产线每天的生产能力
     */
    private static final int LINE_POWER = 300;

    private static int[][] transferOrderInfo(List<OrderInfo> orderInfos) {
        int[][] orderInfo = new int[orderInfos.size()][5];
        for (int i = 0; i < orderInfos.size(); i++) {
            OrderInfo info = orderInfos.get(i);
            String type = info.getType();
            type = type.split("mm")[0];
            orderInfo[i][GeneticAlgorithmUtils.TYPE] = Integer.parseInt(type);
            orderInfo[i][GeneticAlgorithmUtils.AMOUNT] = info.getAmount();
            orderInfo[i][GeneticAlgorithmUtils.DEADLINE] = info.getDeadline();
            orderInfo[i][GeneticAlgorithmUtils.PENALTY] = Integer.parseInt(info.getPenalty());
            orderInfo[i][GeneticAlgorithmUtils.Id] = info.getId().intValue();
        }
        return orderInfo;
    }

    /*public static List<Integer> calculate() {
        return calculate(ORDER_INFO);
    }*/

    public static List<Long> calculate(List<OrderInfo> orderInfos) {
        return calculate(orderInfos, CHANGE_COST, LINE_NUM, LINE_POWER);
    }

    public static List<Long> calculate(List<OrderInfo> orderInfos, int lineNum) {
        return calculate(orderInfos, CHANGE_COST, lineNum, LINE_POWER);
    }

    public static List<Long> calculate(List<OrderInfo> orderInfos, int changeCost, int lineNum, int linePower) {
        GeneticAlgorithm test = new GeneticAlgorithm(orderInfos.size(), POP_SIZE, MAX_ITER_NUM, CROSS_RATE, MUTATION_RATE);
        int[][] orderInfo = transferOrderInfo(orderInfos);
        test.setOrderInfo(orderInfo);
        test.setChangeCost(changeCost);
        test.setLineNum(lineNum);
        test.setLinePower(linePower * lineNum);

        /*//设置动态窗口
        DynamicDataWindow dynamicDataWindow = new DynamicDataWindow("订单生产调度最优化求解过程");
        //dynamicDataWindow.setVisible(true);
        test.setDdWindow(dynamicDataWindow);*/

        List<Integer> result = test.calculate();
        List<Long> ids = new ArrayList<>();
        for (Integer integer : result) {
            Long id = Integer.toUnsignedLong(orderInfo[integer][4]);
            ids.add(id);
        }

        return ids;
    }
}
