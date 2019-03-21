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
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

public class SeckillServiceImpl implements SeckillService {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private SeckillDao seckillDao;

    private SuccessKilledDao successKilledDao;

    /**
     * 混淆
     */
    private final String slat = "sdfs@erer#ddFF23ere.423";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
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
        Seckill seckill = seckillDao.queryById(seckillId);

        if (seckill == null) {
            return new Exposer(false, seckillId);
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
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillExecption, SeckillCloseExecption {
        if (md5 == null || md5.equals(getMD5(seckillId))) {
            throw new SeckillException("秒杀数据被重写了");
        }

        // 执行秒杀逻辑: 减库存 + 记录购买行为
        Date nowTime = new Date();

        try {
            int updateCount = seckillDao.reduceNumber(seckillId, nowTime);
            if (updateCount <= 0) {
                // 没有更新到记录
                throw new SeckillCloseExecption("秒杀结束");
            } else {
                // 记录购买行为
                int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
                // 唯一校验: seckillId, userPhone
                if (insertCount <= 0) {
                    // 重复秒杀
                    throw new RepeatKillExecption("重复秒杀");
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
