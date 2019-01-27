package com.isyxf.service.impl;

import com.isyxf.dao.SeckillDao;
import com.isyxf.dao.SuccessKilledDao;
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

    // 注入 Service 依赖
    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    // md5盐值字符串，用于混淆MD5
    private final String slat = "sdf#$$#sdfsdf~!sfsdf~";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposerSeckillUrl (long seckillId) {
        Seckill seckill = seckillDao.queryById(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();

        if (nowTime.getTime() < startTime.getTime()
            || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId,nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }

        // 转化特定字符串的过程，不可逆
        String md5 = getMD5(seckillId);
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + slat;
        // 生成 md5
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());

        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点, TODO: 事务
     * 1: 开发团队达成一致约定，明确标注事务方法的编程风格
     * 2: 保证事务方法的执行时间尽可能短，不要穿插其他网络操作，RPC/HTTP请求或者玻璃到事务方法外部
     * 3: 不是所有的方法都需要事务，如只有一条修改操作，或操作不需要事务控制
     */
    public SeckillExecution executionSeckill(long seckillId, long userPhone, String md5)
        throws SeckillException, RepeatKillExecption, SeckillCloseExecption {

        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            throw new SeckillException("seckill data rewrite");
        }
        // 执行秒杀逻辑: 减库存 + 记录购买行为

        Date nowTime = new Date();

        try {
            // 减库存
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);

            if (updateCount <= 0) {
                // 没有更新到记录，秒杀结束
                throw new SeckillCloseExecption("seckill is closed");
            } else {
                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                // 唯一: seckillId, userPhone
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillExecption("seckill repeated");
                } else {
                    // 秒杀成功
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillException(seckillId, 1, SeckillStatEnum.SUCCESS, successKilled);
                }
            }
        } catch (SeckillCloseExecption e1) {
            throw e1;
        } catch (RepeatKillExecption e2) {
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            // 索引编译期异常 转化为运行期异常
            throw new SeckillException("seckill inner error:" + e.getMessage());
        }
    }
}
