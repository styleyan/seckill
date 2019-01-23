package com.isyxf.dao;

import com.isyxf.entity.SuccessKilled;

public interface SuccessKilledDao {

    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     * 插入的行数
     */
    int insertSuccessKilled(long seckillId, long userPhone);

    /**
     * 根据id查询 SuccessKilled 并携带喵喵是产品对象实体
     * @param seckillId
     * @return
     */
    SuccessKilled queryByIdWithSeckill(long seckillId);
}
