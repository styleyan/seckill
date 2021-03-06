package com.isyxf.service.impl;

import com.isyxf.dao.SeckillDao;
import com.isyxf.dao.SuccessKilledDao;
import com.isyxf.dao.cache.RedisDao;
import com.isyxf.dto.Exposer;
import com.isyxf.dto.SeckillExecution;
import com.isyxf.entity.Seckill;
import com.isyxf.entity.SuccessKilled;
import com.isyxf.enums.SeckillStatEnum;
import com.isyxf.exception.RepeatKillExecption;
import com.isyxf.exception.SeckillCloseExecption;
import com.isyxf.exception.SeckillException;
import com.isyxf.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 注入 Service 依赖, 从 spring 中获取 seckillDao 对象
     */
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Autowired
    private RedisDao redisDao;
    /**
     * 混淆
     */
    private final String slat = "sdfs@erer#ddFF23ere.423";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 5);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    /**
     * 输出秒杀接口地址
     *
     * @param seckillId
     * @return
     */
    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        // 可优化点: 使用redis缓存优化，超时维护
        // 1. 先从 redis 获取
        Seckill seckill = redisDao.getSeckill(seckillId);

        if (seckill == null) {
            logger.info("redis is empty, go Mysql get seckill");
            // 2: redis没获取到在访问数据库
            seckill = seckillDao.queryById(seckillId);

            // 数据库中也没取到直接返回错误
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                // 3: 数据库中存在，则放入redis中
                logger.info("add redis key");
                redisDao.putSeckill(seckill);
            }
        }else {
            logger.info("from redis, not open mySql");
        }

        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        // 当前系统时间
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime() || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        // 转换特定字符串的过程, 不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    /**
     * 获取md5值
     *
     * @param seckillId
     * @return
     */
    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

        return md5;
    }

    @Override
    @Transactional
    /**
     * Transactional 事务注解
     * 使用注解控制事务方法的优点:
     * 1. 开发团队达成一致约定，明确标注事务方法的编程风格。
     * 2. 保证事务方法的执行时间尽可能短，不要穿插其他网络操作 RPC/HTTP 请求，或者剥离到事务方法外部
     * 3. 不是所有的方法都需要事务，如只有一条修改操作，只读操作不需要事务控制
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillExecption, SeckillCloseExecption {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("秒杀数据被重写了");
        }
        // 执行秒杀逻辑: 减库存 + 记录购买行为
        Date nowTime = new Date();
        try {
            // 记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            // 唯一校验: seckillId, userPhone
            if (insertCount <= 0) {
                // 重复秒杀
                throw new RepeatKillExecption("重复秒杀");
            } else {
                // 减库存, 热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
                if (updateCount <= 0) {
                    // 没有更新到记录
                    throw new SeckillCloseExecption("秒杀结束");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId, userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseExecption e1) {
            throw e1;
        } catch (RepeatKillExecption e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
