package com.example.auto.Controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.yun.comom.Test1;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
    @Reference
    public Test1 test1;
    @RequestMapping("test")
    public void say(){
        String b = test1.say();
        System.out.println(b);
    }
}
