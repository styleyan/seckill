/**
 * dao层 用于把数据库中的数据映射到对象
 */
package com.isyxf.dao;

import com.isyxf.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {

    /**
     * 减库存 TODO: 不添加 @Param("")，过不了单元测试。
     * @param seckillId
     * @param killTime
     * @return 如果影响行数 > 1, 表示更新的记录行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     * 根据 id 查询秒杀对象
     * @param seckillId
     * @return
     */
    Seckill queryById(@Param("seckillId") long seckillId);

    /**
     * 根据偏移量查询秒杀商品列表，TODO: 不添加 @Param("")，过不了单元测试。
     * @param offset
     * @param limit
     * @return
     */
    List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
}
