package com.ge.util.ga;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
class GeneticAlgorithm {
    private List<Chromosome> population = new ArrayList<>();
    /**
     * 种群数量
     */
    private int popSize;
    /**
     * 染色体最大长度
     */
    private int geneSize;
    /**
     * 最大迭代次数
     */
    private int maxIterNum;
    /**
     * 基因交叉变异的概率
     */
    private double crossRate;
    /**
     * 基因突变的概率
     */
    private double mutationRate;
    /**
     * 订单信息
     */
    private int[][] orderInfo;
    /**
     * 生产线切换生产的花费
     */
    private int changeCost;
    /**
     * 生产线每天的生产能力
     */
    private int linePower;
    /**
     * 生产线的数量
     */
    private int lineNum;
    /**
     * 最大变异步长
     */
    private int maxMutationNum = 3;
    /**
     * 当前遗传到第几代
     */
    private int generation = 1;

    private double bestScore;// 最好得分
    private double worstScore;// 最坏得分
    private double totalScore;// 总得分
    private double averageScore;// 平均得分

    private List<Integer> x = new ArrayList<>(); // 记录历史种群中最好的X值
    private double y; // 记录历史种群中最好的Y值
    private int changeSum;
    private int penalty;
    private int geneI;// x y所在代数

    private DynamicDataWindow ddWindow;
    private long tp;

    GeneticAlgorithm(int geneSize, int popSize, int maxIterNum, double crossRate, double mutationRate) {
        this.geneSize = geneSize;
        this.popSize = popSize;
        this.maxIterNum = maxIterNum;
        this.crossRate = crossRate;
        this.mutationRate = mutationRate;
    }

    List<Integer> calculate() {

        // 初始化种群
        initPopulation();
        for (generation = 1; generation < maxIterNum; generation++) {
            // 1.计算种群适应度
            calculateScore();
            // 2.种群遗传和基因交叉变异
            evolve();
            // 3.基因突变
            mutation();
            // 4.打印信息
            //print();
        }
        return x;
    }

    /**
     * @Description: 初始化种群
     */
    private void initPopulation() {
        // System.out.println("生成初始种群...");
        for (int i = 0; i < popSize; i++) {
            Chromosome chro = new Chromosome(geneSize);
            population.add(chro);
        }

        setChromosomeScore(population.get(0));
        bestScore = population.get(0).getScore();
        worstScore = population.get(0).getScore();
        y = population.get(0).getScore();
    }

    /**
     * @Description: 计算种群适应度
     */
    private void calculateScore() {
        // System.out.println("1>计算种群适应度...");
        totalScore = 0;
        for (Chromosome chro : population) {
            setChromosomeScore(chro);
            if (chro.getScore() < bestScore) { // 设置最好基因值
                bestScore = chro.getScore();
                if (y > bestScore) {
                    List<Integer> gene = new ArrayList<>(chro.getGene().length);
                    Collections.addAll(gene, chro.getGene());
                    x.clear();
                    for (int i = 0; i < gene.size(); i++) {
                        x.add(i, gene.get(i));
                    }
                    y = bestScore;
                    changeSum = chro.getChangeSum();
                    penalty = chro.getPenalty();
                    geneI = generation;
                }
            }
            if (chro.getScore() > worstScore) { // 设置最坏基因值
                worstScore = chro.getScore();
            }
            totalScore += chro.getScore();
        }
        averageScore = totalScore / popSize;
        // 因为精度问题导致的平均值小于最好值，将平均值设置成最好值
        averageScore = averageScore < bestScore ? bestScore : averageScore;
    }

    /**
     * @Description: 轮盘赌法选择可以遗传下一代的染色体<br>
     * 选择运算
     */
    private Chromosome getParentChromosome() {
        while (true) {
            double slice = Math.random() * totalScore;
            double sum = 0;
            for (Chromosome chro : population) {
                sum += chro.getScore();
                // System.out.println("测试：sum=" + sum + " chro.getScore()=" +
                // chro.getScore());
                if (sum > slice && chro.getScore() <= bestScore * 1.2) {
                    return chro;
                }
            }
        }
    }

    /**
     * @Description:种群进行遗传
     */
    private void evolve() {
        List<Chromosome> childPopulation = new ArrayList<>();
        // 生成下一代种群
        while (childPopulation.size() < popSize) {
            // 进行选择运算
            Chromosome parent1 = getParentChromosome();
            Chromosome parent2 = getParentChromosome();
            // 进行交叉运算
            if (Math.random() <= crossRate) { // 发生基因交叉变异
                List<Chromosome> children = Chromosome.genetic(parent1, parent2);
                if (children != null) {
                    childPopulation.addAll(children);
                }
                // System.out.println("2.2>发生基因交叉变异...");
            } else {
                childPopulation.add(parent1);
                childPopulation.add(parent2);
            }
        }
        // System.out.println("2.3>产生子代种群...");
        // 新种群替换旧种群
        population.clear();
        population = childPopulation;
    }

    /**
     * 基因突变
     */
    private void mutation() {
        for (Chromosome chro : population) {
            if (Math.random() <= mutationRate) { // 发生基因突变
                // 变异步长，即有几个位置发生基因突变
                int mutationNum = (int) (Math.random() * maxMutationNum);
                chro.mutation(mutationNum);
                // System.out.println("3>发生基因突变...");
            }
        }
    }

    /**
     * @Description: 输出结果
     */
    private void print() {
        System.out.println("--------------------------------");
        System.out.println("the generation is:" + generation);
        System.out.println("the best fitness is:" + bestScore);
        System.out.println("the worst fitness is:" + worstScore);
        System.out.println("the average fitness is:" + averageScore);
        System.out.println("the total fitness is:" + totalScore);
        System.out.println("the best gene emerge:" + geneI + "\t the best gene is:" + x.toString() + "\t score:" + (y));
        System.out.println("the change sum is:" + changeSum + "\t the penalty is:" + penalty);
        System.out.println("--------------------------------");

        long millis = System.currentTimeMillis();
        if (millis - tp > 100) {
            tp = millis;
            ddWindow.addData(millis, y, changeSum, penalty, x.toString());
        }

        try {
            Thread.sleep(10L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param chro
     * @Description: 计算并设置染色体适应度
     */
    private void setChromosomeScore(Chromosome chro) {
        if (chro == null) {
            return;
        }
        Integer[] orderSeq = chro.getGene();
        int score;
        int changeSum = 0;
        int penalty = 0;
        int flagType = 0;
        for (Integer anOrderSeq : orderSeq) {
            int type = getOrderType(anOrderSeq);// 这里type从1开始
            if (flagType != type) {
                changeSum += changeCost;
            }
            flagType = type;
        }

        int productionCount = 0;
        for (int i = 1, j = 0; j < geneSize; i++) {
            productionCount += linePower;
            int num = orderSeq[j];
            if (productionCount >= getOrderAmount(num)) {
                productionCount -= getOrderAmount(num);
                if (i > getOrderDeadline(orderSeq[j])) {
                    penalty += (i - getOrderDeadline(num)) * getOrderPenalty(num);
                }
                j++;
            }
        }
        score = (int) (0.5 * changeSum + 0.5 * penalty);
        chro.setChangeSum(changeSum);
        chro.setPenalty(penalty);
        chro.setScore(score);
    }

    private int getOrderType(int orderNum) {
        return orderInfo[orderNum][GeneticAlgorithmUtils.TYPE];
    }

    private int getOrderAmount(int orderNum) {
        return orderInfo[orderNum][GeneticAlgorithmUtils.AMOUNT];
    }

    private int getOrderDeadline(int orderNum) {
        return orderInfo[orderNum][GeneticAlgorithmUtils.DEADLINE];
    }

    private int getOrderPenalty(int orderNum) {
        return orderInfo[orderNum][GeneticAlgorithmUtils.PENALTY];
    }
}
