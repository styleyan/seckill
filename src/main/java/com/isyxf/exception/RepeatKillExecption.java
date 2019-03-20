package com.isyxf.exception;
/**
 * 重复秒杀异常(运行期异常)
 */
public class RepeatKillExecption extends SeckillException {
    public RepeatKillExecption(String message) {
        super(message);
    }

    public RepeatKillExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
