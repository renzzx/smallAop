package com.rzzx.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.util.StringUtils;

public class AspectJAnnotationUtils {
	
	private static final String[] EXPRESSION_PROPERTIES = new String[] {"value", "pointcut"};
	
	public static String resolveExpression(Annotation annotation) throws Exception {
		String expression = null;
		for (String methodName : EXPRESSION_PROPERTIES) {
			Method method;
			try {
				method = annotation.getClass().getDeclaredMethod(methodName);
			}
			catch (NoSuchMethodException ex) {
				method = null;
			}
			if (method != null) {
				String candidate = (String) method.invoke(annotation);
				if (StringUtils.hasText(candidate)) {
					expression = candidate;
				}
			}
		}
		return expression;
	}
}
