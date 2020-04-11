package com.rzzx.test;

import org.aspectj.weaver.patterns.NamePattern;

public class TestNamePattern {

	public static void main(String[] args) {
		NamePattern t = new NamePattern("com.rzzx.bean.*");
		System.out.println(t.matches("com.rzzx.bean.person"));
	}

}
