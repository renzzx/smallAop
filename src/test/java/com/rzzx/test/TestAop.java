package com.rzzx.test;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rzzx.bean.Person;

public class TestAop {
    public static void main(String[] args){
    	System.setProperty("fileName", "testAop.xml");
    	//读取bean.xml中的内容
    	AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("${fileName}");
        //创建bean的引用对象
    	Person u = ctx.getBean("testBean", Person.class);
//    	IPerson u = ctx.getBean("testBean", IPerson.class);
        u.say();
    	
        ctx.close();
    }
}