package com.isyxf.dto;

import com.isyxf.entity.Seckill;
import com.isyxf.entity.SuccessKilled;
import com.isyxf.enums.SeckillStatEnum;

import java.util.List;

/**
 * 分装秒杀执行后的结果
 */
public abstract class SeckillExecution {
    // 秒杀id
    private long seckillId;

    // 秒杀执行结果状态
    private int state;

    // 状态表示
    private String stateInfo;

    // 秒杀成功对象
    private SuccessKilled successKilled;

    /**
     * 成功状态
     * @param seckillId
     * @param statEnum
     * @param successKilled
     */
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    /**
     * 失败状态
     * @param seckillId
     * @param statEnum
     */
    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    public abstract List<Seckill> getSeckillList();

    public abstract Seckill getById(long seckillId);

    public abstract Exposer exposerSeckillUrl(long seckillId);

    public abstract SeckillExecution executionSeckill(long seckillId, long userPhone);

    public abstract SeckillExecution executionSeckill(long seckillId, long userPhone, String md5);
}
