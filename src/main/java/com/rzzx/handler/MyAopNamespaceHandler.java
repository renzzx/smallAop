package com.rzzx.handler;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.rzzx.parser.MyAspectJAutoProxyBeanDefinitionParser;

public class MyAopNamespaceHandler extends NamespaceHandlerSupport {  
  
    @Override 
    public void init() {  
        registerBeanDefinitionParser("aspectj-autoproxy", new MyAspectJAutoProxyBeanDefinitionParser());  
    }  

}