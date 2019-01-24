package com.isyxf.exception;

import com.isyxf.dto.SeckillExecution;

/**
 * 重复秒杀异常(运行期异常)
 */
public class RepeatKillExecption extends SeckillExecution {
    public RepeatKillExecption(String message) {
        super(message);
    }

    public RepeatKillExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
