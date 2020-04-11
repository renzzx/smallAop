package com.rzzx.proxy;

import java.lang.reflect.Method;
import java.util.Iterator;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;

import com.rzzx.bean.MyAdvisor;

public class MyJdkProceedingJoinPointImpl implements ProceedingJoinPoint {
	
	private Object target;
	private Object[] args; 
	private Method method;
	private Iterator<MyAdvisor> iterator;
	
	public MyJdkProceedingJoinPointImpl(Object target, Object[] args, 
			Method method, Iterator<MyAdvisor> iterator) {
		this.target = target;
		this.args = args;
		this.method = method;
		this.iterator = iterator;
	}
	
	@Override
	public Object proceed() throws Throwable {
		if(iterator.hasNext()) {
			MyAdvisor advisor = iterator.next();
			advisor.getMethod().invoke(advisor.getAspectClazz().newInstance(), this);
		} else {
			return method.invoke(target, args);
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
