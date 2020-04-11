package com.rzzx.proxy;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.LinkedList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.util.ClassUtils;

import com.rzzx.bean.MyAdvisor;
import com.rzzx.bean.ProxyAdvisor;

/**
 * JDK-based {@link AopProxy} implementation for the Spring AOP framework,
 * based on JDK {@link java.lang.reflect.Proxy dynamic proxies}.
 *
 * <p>Creates a dynamic proxy, implementing the interfaces exposed by
 * the AopProxy. Dynamic proxies <i>cannot</i> be used to proxy methods
 * defined in classes, rather than interfaces.
 *
 * <p>Objects of this type should be obtained through proxy factories,
 * configured by an {@link AdvisedSupport} class. This class is internal
 * to Spring's AOP framework and need not be used directly by client code.
 *
 * <p>Proxies created using this class will be thread-safe if the
 * underlying (target) class is thread-safe.
 *
 * <p>Proxies are serializable so long as all Advisors (including Advices
 * and Pointcuts) and the TargetSource are serializable.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @author Rob Harrop
 * @author Dave Syer
 * @see java.lang.reflect.Proxy
 * @see AdvisedSupport
 * @see ProxyFactory
 */
public class MyJdkDynamicAopProxy implements MyAopProxy, InvocationHandler, Serializable {

	/** use serialVersionUID from Spring 1.2 for interoperability */
	private static final long serialVersionUID = 5531744639992436476L;
	
	private ProxyAdvisor proxyAdvisor;
	
	public MyJdkDynamicAopProxy(ProxyAdvisor proxyAdvisor) {
		this.proxyAdvisor = proxyAdvisor;
	}

	public Object getProxy() {
		return getProxy(ClassUtils.getDefaultClassLoader(), this.proxyAdvisor);
	}

	public Object getProxy(ClassLoader classLoader, ProxyAdvisor proxyAdvisor) {
		this.proxyAdvisor = proxyAdvisor;
		Class<?>[] proxiedInterfaces = proxyAdvisor.getInterfaces();
		return Proxy.newProxyInstance(classLoader, proxiedInterfaces, this);
	}

	/**
	 * Implementation of {@code InvocationHandler.invoke}.
	 * <p>Callers will see exactly the exception thrown by the target,
	 * unless a hook method throws an exception.
	 */
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		LinkedList<MyAdvisor> myAdvisors = (LinkedList<MyAdvisor>) proxyAdvisor.getMyAdvisor();
		Iterator<MyAdvisor> iterator = myAdvisors.iterator();
		ProceedingJoinPoint proceedingJoinPoint = new MyJdkProceedingJoinPointImpl(
				proxyAdvisor.getTargetClazz().newInstance(), args, method, iterator);
		
		MyAdvisor advisor = iterator.next();
		Object retVal = advisor.getMethod().invoke(advisor.getAspectClazz().newInstance(), 
				proceedingJoinPoint);
		return retVal;
	}
}
