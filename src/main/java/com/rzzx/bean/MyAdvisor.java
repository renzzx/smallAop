package com.rzzx.bean;

import java.lang.reflect.Method;

public class MyAdvisor {
	private Class<? extends Object> aspectClazz;
	private String pointcutExpression;
	private Method method;

	public Class<? extends Object> getAspectClazz() {
		return aspectClazz;
	}

	public void setAspectClazz(Class<? extends Object> aspectClazz) {
		this.aspectClazz = aspectClazz;
	}

	public String getPointcutExpression() {
		return pointcutExpression;
	}

	public void setPointcutExpression(String pointcutExpression) {
		this.pointcutExpression = pointcutExpression;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

}
