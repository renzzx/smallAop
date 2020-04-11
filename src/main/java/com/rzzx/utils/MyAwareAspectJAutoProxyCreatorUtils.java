package com.rzzx.utils;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;
import org.aspectj.weaver.tools.ShadowMatch;

public class MyAwareAspectJAutoProxyCreatorUtils {

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println(match("com.rzzx.bean.Person", "execution(* com.rzzx.bean..*(..))"));
	}

	public static boolean match(String beanClass, String pointcutExpression) throws ClassNotFoundException {
		PointcutParser pointcutParser = initializePointcutParser(MyAwareAspectJAutoProxyCreatorUtils.class
				.getClassLoader());
		Class clazz = Class.forName(beanClass);
		Method[] methods = clazz.getDeclaredMethods();
		
		PointcutExpression pe = pointcutParser.parsePointcutExpression(pointcutExpression);
		for(Method method : methods) {
			ShadowMatch shadowMatch = pe
				.matchesMethodExecution(method);
			
			if (shadowMatch.alwaysMatches()) {
				return true;
			} else if (shadowMatch.neverMatches()) {
				return false;
			}
		}
		return false;
	}
	
	private static PointcutParser initializePointcutParser(ClassLoader cl) {
		PointcutParser parser = PointcutParser
				.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(
						SUPPORTED_PRIMITIVES, cl);
		parser.registerPointcutDesignatorHandler(new MyBeanNamePointcutDesignatorHandler());
		return parser;
	}
	
	private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

	static {
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.ARGS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.REFERENCE);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.THIS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.TARGET);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.WITHIN);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ANNOTATION);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_WITHIN);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_ARGS);
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.AT_TARGET);
	}

}
