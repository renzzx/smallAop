package com.rzzx.parser;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.parsing.BeanComponentDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class MyAspectJAutoProxyBeanDefinitionParser implements BeanDefinitionParser {  

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		RootBeanDefinition beanDefinition = new RootBeanDefinition(
				MyAwareAspectJAutoProxyCreator.class);
		String beanName = "internalAutoProxyCreator";
		parserContext.getRegistry().registerBeanDefinition(beanName, beanDefinition);
		BeanComponentDefinition componentDefinition =
				new BeanComponentDefinition(beanDefinition, beanName);
		parserContext.registerComponent(componentDefinition);
		return null;
	}  
}