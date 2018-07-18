package com.ge.util.ga;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Chromosome {
    private Integer[] gene;// 基因序列
    private int changeSum = 0;// 切换生产的耗费
    private int penalty = 0;// 逾期罚金
    private double score;// 对应的函数得分

    /**
     * @param size
     * @Description 随机生成基因序列<br>
     * 有参构造函数
     */
    Chromosome(int size) {
        if (size <= 0) {
            return;
        }
        initGene(size);
    }

    /**
     * 生成一个新基因<br>
     * 因为定义了构造函数，则默认的无参构造函数失效，需自己定义
     */
    Chromosome() {

    }

    /**
     * @param gene 生成一个自定义的基因
     */
    public Chromosome(int[] gene) {
        this.gene = new Integer[gene.length];
        for (int i = 0; i < gene.length; i++) {
            this.gene[i] = gene[i];
        }
    }

    /**
     * @param size
     * @Description 初始化基因
     */
    private void initGene(int size) {
        if (size <= 0) {
            return;
        }
        int i, j;
        Integer[] g = new Integer[size];
        for (i = 0; i < size; ) {
            g[i] = ((int) (Math.random() * size)) % size;
            for (j = 0; j < i; j++) {
                if (g[i].equals(g[j])) {
                    break;
                }
            }
            if (j == i) {
                i++;
            }
        }
        gene = g;
    }

    /**
     * @param c
     * @return
     * @Description: 克隆基因
     */
    public static Chromosome clone(final Chromosome c) {
        if (c == null || c.gene == null) {
            return null;
        }
        Chromosome copy = new Chromosome();
        copy.initGene(c.gene.length);
        System.arraycopy(c.gene, 0, copy.gene, 0, c.gene.length);
        return copy;
    }

    /**
     * @param p1
     * @param p2
     * @Description: 交叉运算
     */
    static List<Chromosome> genetic(Chromosome p1, Chromosome p2) {
        if (p1 == null || p2 == null) { // 染色体有一个为空，不产生下一代
            return null;
        }
        if (p1.gene == null || p2.gene == null) { // 染色体有一个没有基因序列，不产生下一代
            return null;
        }
        if (p1.gene.length != p2.gene.length) { // 染色体基因序列长度不同，不产生下一代
            return null;
        }

        Chromosome c1 = clone(p1);
        Chromosome c2 = clone(p2);
        // 随机产生交叉互换位置
        int size = c1.gene.length;
        int a = ((int) (Math.random() * size)) % size;
        int b = ((int) (Math.random() * size)) % size;
        int min = a > b ? b : a;
        int max = a > b ? a : b;
        // 对位置上的基因进行交叉互换
        int i, j, k, flag;
        for (i = 0, j = max; j < size; i++, j++) {
            c2.gene[i] = p1.gene[j];
        }
        flag = i;
        for (k = 0, j = flag; j < size; ) {
            c2.gene[j] = p2.gene[k++];
            for (i = 0; i < flag; i++) {
                if (c2.gene[i].equals(c2.gene[j])) {
                    break;
                }
            }
            if (i == flag) {
                j++;
            }
        }

        for (k = 0, j = 0; k < size; ) {
            c1.gene[j] = p1.gene[k++];
            for (i = 0; i < min; i++) {
                if (p2.gene[i].equals(c1.gene[j])) {
                    break;
                }
            }
            if (i == min) {
                j++;
            }
        }
        for (i = 0, j = size - min; j < size; i++, j++) {
            c1.gene[j] = p2.gene[i];
        }

        List<Chromosome> list = new ArrayList<>();
        list.add(c1);
        list.add(c2);
        return list;
    }

    /**
     * @param num
     * @Description: 基因num个位置发生变异
     */
    void mutation(int num) {
        // 允许变异
        int size = gene.length;
        for (int i = 0; i < num; i++) {
            // 寻找变异位置
            int at1 = ((int) (Math.random() * size)) % size;
            int at2 = ((int) (Math.random() * size)) % size;
            if (at1 == at2) {
                continue;
            }
            // 变异后的值
            int temp = gene[at1];
            gene[at1] = gene[at2];
            gene[at2] = temp;
        }
    }
}
