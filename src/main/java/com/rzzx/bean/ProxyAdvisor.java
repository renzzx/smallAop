package com.rzzx.bean;

import java.util.List;

public class ProxyAdvisor {
	
	private Class<? extends Object> targetClazz;

	private String beanName;
	private List<MyAdvisor> myAdvisor;
	private Class<?>[] interfaces;
	
	public ProxyAdvisor(Class<? extends Object> targetClazz, String beanName, List<MyAdvisor> myAdvisor, Class<?>[] interfaces) {
		this.targetClazz = targetClazz;
		this.beanName = beanName;
		this.myAdvisor = myAdvisor;
		this.interfaces = interfaces;
	}
	
	public boolean isInterface() {
		return interfaces != null && interfaces.length > 0;
	}
	
	public Class<?>[] getInterfaces() {
		return interfaces;
	}



	public void setInterfaces(Class<?>[] interfaces) {
		this.interfaces = interfaces;
	}



	public Class<? extends Object> getTargetClazz() {
		return targetClazz;
	}

	public void setTargetClazz(Class<? extends Object> targetClazz) {
		this.targetClazz = targetClazz;
	}

	public String getBeanName() {
		return beanName;
	}
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	public List<MyAdvisor> getMyAdvisor() {
		return myAdvisor;
	}
	public void setMyAdvisor(List<MyAdvisor> myAdvisor) {
		this.myAdvisor = myAdvisor;
	}
	
}
