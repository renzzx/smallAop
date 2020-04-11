package com.rzzx.proxy;

import java.util.Iterator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.springframework.cglib.proxy.MethodProxy;

import com.rzzx.bean.MyAdvisor;

public class MyCglibProceedingJoinPointImpl implements ProceedingJoinPoint {
	
	private Object proxy;
	private Object[] args; 
	private MethodProxy methodProxy;
	private Iterator<MyAdvisor> iterator;
	
	public MyCglibProceedingJoinPointImpl(Object proxy, Object[] args, 
			MethodProxy methodProxy, Iterator<MyAdvisor> iterator) {
		this.proxy = proxy;
		this.args = args;
		this.methodProxy = methodProxy;
		this.iterator = iterator;
	}
	
	@Override
	public Object proceed() throws Throwable {
		if(iterator.hasNext()) {
			MyAdvisor advisor = iterator.next();
			advisor.getMethod().invoke(advisor.getAspectClazz().newInstance(), this);
		} else {
			return methodProxy.invokeSuper(proxy, args);
		}
		return null;
	}

	@Override
	public String toShortString() {
		return null;
	}

	@Override
	public String toLongString() {
		return null;
	}

	@Override
	public Object getThis() {
		return null;
	}

	@Override
	public Object getTarget() {
		return null;
	}

	@Override
	public Object[] getArgs() {
		return null;
	}

	@Override
	public Signature getSignature() {
		return null;
	}

	@Override
	public SourceLocation getSourceLocation() {
		return null;
	}

	@Override
	public String getKind() {
		return null;
	}

	@Override
	public StaticPart getStaticPart() {
		return null;
	}

	@Override
	public void set$AroundClosure(AroundClosure arc) {

	}

	@Override
	public Object proceed(Object[] args) throws Throwable {
		return null;
	}

}
