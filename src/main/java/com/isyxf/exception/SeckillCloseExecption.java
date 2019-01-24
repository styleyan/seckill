package com.isyxf.exception;

/**
 * 秒杀关闭异常
 */
public class SeckillCloseExecption extends RuntimeException {
    public SeckillCloseExecption(String message) {
        super(message);
    }

    public SeckillCloseExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
