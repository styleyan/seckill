package com.isyxf.web;

import com.isyxf.dto.Peolpe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

// 类似 @Service @Component 放入 spring 容器当中
@Controller
@RequestMapping("/test")
public class TestController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/yxf/{ids}",
            method = RequestMethod.POST,
            produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public Peolpe Yxf(@PathVariable("ids") String ids) {
        Peolpe peolpe = new Peolpe();
        peolpe.setValues(ids);
        peolpe.setTimer(System.currentTimeMillis());

        logger.warn("ids value={}", ids);
        logger.info("peolpe toString, value={}", peolpe.toString());
        return peolpe;
    }
}
