package com.rzzx.parser;

import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import com.rzzx.bean.MyAdvisor;
import com.rzzx.bean.ProxyAdvisor;
import com.rzzx.proxy.MyAopProxy;
import com.rzzx.proxy.MyCglibAopProxy;
import com.rzzx.proxy.MyJdkDynamicAopProxy;
import com.rzzx.utils.AspectJAnnotationUtils;
import com.rzzx.utils.MyAwareAspectJAutoProxyCreatorUtils;
import com.rzzx.utils.MyProxyCreationContext;

public class MyAwareAspectJAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware{

	private ConfigurableListableBeanFactory beanFactory;
	
	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if(bean != null) {
			return wrapIfNecessary(bean, beanName);
		}
		return null;
	}
	
	private Object wrapIfNecessary(Object bean, String beanName) {
		//1. �������е� beanName Ѱ�Ҵ���Aspect ע�����
		List<MyAdvisor> candidateAdvisors = findCandidateAdvisors();
		
		//2. �ҵ��������õ���ǰbean��Aspect ע�����, ����ӵ���ǰbean��
		List<MyAdvisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, 
				bean.getClass(), beanName);
		
		//3. ʵ������ǰbean
		Object proxy = createProxy(bean.getClass(), beanName, eligibleAdvisors);
		return proxy;
	}

	private Object createProxy(Class<? extends Object> clazz, String beanName, List<MyAdvisor> eligibleAdvisors) {
		ProxyAdvisor proxyAdvisor = new ProxyAdvisor(clazz, beanName, eligibleAdvisors, clazz.getInterfaces());
		return createAopProxy(proxyAdvisor).getProxy();
	}
	
	public MyAopProxy createAopProxy(ProxyAdvisor proxyAdvisor) {
		
		//���targetClass�ǽӿ��࣬ʹ��JDK������Proxy
		if (proxyAdvisor.isInterface()) {
			return new MyJdkDynamicAopProxy(proxyAdvisor);
		}
		//������ǽӿ���Ҫ����Proxy����ôʹ��CGLIB������
		return new MyCglibAopProxy(proxyAdvisor);
	}

	private List<MyAdvisor> findAdvisorsThatCanApply(List<MyAdvisor> candidateAdvisors, 
			Class<? extends Object> beanClass, String beanName) {
		MyProxyCreationContext.setCurrentProxiedBeanName(beanName);
		try {
			List<MyAdvisor> eligibleAdvisors = new LinkedList<MyAdvisor>();
			for (MyAdvisor candidate : candidateAdvisors) {
				//������ͨbean�Ĵ���
				if (canApply(candidate, beanClass)) {
					eligibleAdvisors.add(candidate);
				}
			}
			return eligibleAdvisors;
		}
		finally {
			MyProxyCreationContext.setCurrentProxiedBeanName(null);
		}
	}

	private boolean canApply(MyAdvisor candidate, Class<? extends Object> beanClass) {
		try {
			return MyAwareAspectJAutoProxyCreatorUtils.match(beanClass.getName(), 
					candidate.getPointcutExpression());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return false;
	}

	private List<MyAdvisor> findCandidateAdvisors() {
		//��ȡ���е� beanName
		String[] beanNames =
				BeanFactoryUtils.beanNamesForTypeIncludingAncestors(this.beanFactory, 
						Object.class, true, false);
		List<MyAdvisor> myAdvises = new LinkedList<MyAdvisor>();
		for (String beanName : beanNames) {
			Class beanType = this.beanFactory.getType(beanName);
			if (beanType == null) {
				continue;
			}
			
			if(AnnotationUtils.findAnnotation(beanType, Aspect.class) != null) {
				for (Method method : getAdvisorMethods(beanType)) {
					MyAdvisor myAdvise = new MyAdvisor();
					Annotation aspectJAnnotation =
							findAspectJAnnotationOnMethod(method);
					if(aspectJAnnotation == null) {
						continue;
					}
					try {
						myAdvise.setPointcutExpression(AspectJAnnotationUtils.resolveExpression(aspectJAnnotation));
					} catch (Exception e) {
						e.printStackTrace();
					}
					myAdvise.setAspectClazz(beanType);
					myAdvise.setMethod(method);
					myAdvises.add(myAdvise);
				}
			}
		}
		return myAdvises;	
	}
	
	protected static Annotation findAspectJAnnotationOnMethod(Method method) {
		//�������е�ע����
		Class<? extends Annotation>[] classesToLookFor = new Class[] {
				Before.class, Around.class, After.class, AfterReturning.class, AfterThrowing.class};
		for (Class<? extends Annotation> c : classesToLookFor) {
			Annotation foundAnnotation = AnnotationUtils.findAnnotation(method, c);
			if (foundAnnotation != null) {
				return foundAnnotation;
			}
		}
		return null;
	}
	
	private List<Method> getAdvisorMethods(Class<?> aspectClass) {
		final List<Method> methods = new LinkedList<Method>();
		ReflectionUtils.doWithMethods(aspectClass, new ReflectionUtils.MethodCallback() {
			public void doWith(Method method) throws IllegalArgumentException {
				// Exclude pointcuts
				//����ΪPointcut�ķ���������
				if (AnnotationUtils.getAnnotation(method, Pointcut.class) == null) {
					methods.add(method);
				}
			}
		});
		return methods;
	}

	@Override
	public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
		return null;
	}

	public boolean postProcessAfterInstantiation(Object bean, String beanName) {
		return true;
	}

	public PropertyValues postProcessPropertyValues(
			PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) {
		return pvs;
	}

	public Object postProcessBeforeInitialization(Object bean, String beanName) {
		return bean;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
	}

}
