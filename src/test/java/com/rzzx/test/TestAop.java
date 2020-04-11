package com.rzzx.test;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.rzzx.bean.Person;

public class TestAop {
    public static void main(String[] args){
    	System.setProperty("fileName", "testAop.xml");
    	//��ȡbean.xml�е�����
    	AbstractApplicationContext ctx = new ClassPathXmlApplicationContext("${fileName}");
        //����bean�����ö���
    	Person u = ctx.getBean("testBean", Person.class);
//    	IPerson u = ctx.getBean("testBean", IPerson.class);
        u.say();
    	
        ctx.close();
    }
}