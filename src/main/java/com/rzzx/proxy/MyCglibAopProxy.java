/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.rzzx.proxy;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.LinkedList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.cglib.proxy.Callback;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;
import org.springframework.util.ClassUtils;

import com.rzzx.bean.MyAdvisor;
import com.rzzx.bean.ProxyAdvisor;

/**
 * CGLIB-based {@link AopProxy} implementation for the Spring AOP framework.
 *
 * <p>Formerly named {@code Cglib2AopProxy}, as of Spring 3.2, this class depends on
 * Spring's own internally repackaged version of CGLIB 3.</i>.
 *
 * <p>Objects of this type should be obtained through proxy factories,
 * configured by an {@link AdvisedSupport} object. This class is internal
 * to Spring's AOP framework and need not be used directly by client code.
 *
 * <p>{@link DefaultAopProxyFactory} will automatically create CGLIB-based
 * proxies if necessary, for example in case of proxying a target class
 * (see the {@link DefaultAopProxyFactory attendant javadoc} for details).
 *
 * <p>Proxies created using this class are thread-safe if the underlying
 * (target) class is thread-safe.
 *
 * @author Rod Johnson
 * @author Rob Harrop
 * @author Juergen Hoeller
 * @author Ramnivas Laddad
 * @author Chris Beams
 * @author Dave Syer
 * @see org.springframework.cglib.proxy.Enhancer
 * @see AdvisedSupport#setProxyTargetClass
 * @see DefaultAopProxyFactory
 */
@SuppressWarnings("serial")
public class MyCglibAopProxy implements MyAopProxy, Serializable {

	private ProxyAdvisor proxyAdvisor;
	
	public MyCglibAopProxy(ProxyAdvisor proxyAdvisor) {
		this.proxyAdvisor = proxyAdvisor;
	}

	@Override
	public Object getProxy() {
		return getProxy(ClassUtils.getDefaultClassLoader(), this.proxyAdvisor);
	}

	public Object getProxy(ClassLoader classLoader, ProxyAdvisor proxyAdvisor) {
		
		//从advised中取得在IoC容器中配置的target对象
		Enhancer enhancer = createEnhancer();
		enhancer.setSuperclass(proxyAdvisor.getTargetClazz());

		//设置拦截器
		try {
			enhancer.setCallbacks(getCallbacks());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enhancer.create();
	}

	/**
	 * Creates the CGLIB {@link Enhancer}. Subclasses may wish to override this to return a custom
	 * {@link Enhancer} implementation.
	 */
	protected Enhancer createEnhancer() {
		return new Enhancer();
	}

	private Callback[] getCallbacks() throws Exception {

		// Choose an "aop" interceptor (used for AOP calls).
		//将拦截器封装在 DynamicAdvisedInterceptor 中
		Callback aopInterceptor = new DynamicAdvisedInterceptor(this.proxyAdvisor);

		return new Callback[] {aopInterceptor};
	}

	/**
	 * General purpose AOP callback. Used when the target is dynamic or when the
	 * proxy is not frozen.
	 */
	private static class DynamicAdvisedInterceptor implements MethodInterceptor, Serializable {

		private final ProxyAdvisor proxyAdvisor;
		
		public DynamicAdvisedInterceptor(ProxyAdvisor proxyAdvisor) {
			this.proxyAdvisor = proxyAdvisor;
		}

		public Object intercept(Object proxy, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
			
			LinkedList<MyAdvisor> myAdvisors = (LinkedList<MyAdvisor>) proxyAdvisor.getMyAdvisor();
			Iterator<MyAdvisor> iterator = myAdvisors.iterator();
			ProceedingJoinPoint proceedingJoinPoint = new MyCglibProceedingJoinPointImpl(proxy, args, 
					methodProxy, iterator);
			MyAdvisor advisor = iterator.next();
			Object retVal = advisor.getMethod().invoke(advisor.getAspectClazz().newInstance(), 
					proceedingJoinPoint);
			return retVal;
		}
	}

}
