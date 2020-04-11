package com.rzzx.utils;

import org.aspectj.weaver.patterns.NamePattern;
import org.aspectj.weaver.tools.ContextBasedMatcher;
import org.aspectj.weaver.tools.FuzzyBoolean;
import org.aspectj.weaver.tools.MatchingContext;
import org.aspectj.weaver.tools.PointcutDesignatorHandler;

public class MyBeanNamePointcutDesignatorHandler implements PointcutDesignatorHandler {

	private static final String BEAN_DESIGNATOR_NAME = "bean";

	public String getDesignatorName() {
		return BEAN_DESIGNATOR_NAME;
	}

	public ContextBasedMatcher parse(String expression) {
		return new BeanNameContextMatcher(expression);
	}
	
	private class BeanNameContextMatcher implements ContextBasedMatcher {

		private final NamePattern expressionPattern;

		public BeanNameContextMatcher(String expression) {
			this.expressionPattern = new NamePattern(expression);
		}

		public boolean couldMatchJoinPointsInType(Class someClass) {
			return (contextMatch(someClass) == FuzzyBoolean.YES);
		}

		public boolean couldMatchJoinPointsInType(Class someClass, MatchingContext context) {
			return (contextMatch(someClass) == FuzzyBoolean.YES);
		}

		public boolean matchesDynamically(MatchingContext context) {
			return true;
		}

		public FuzzyBoolean matchesStatically(MatchingContext context) {
			return contextMatch(null);
		}

		public boolean mayNeedDynamicTest() {
			return false;
		}

		private FuzzyBoolean contextMatch(Class<?> targetType) {
			String advisedBeanName = MyProxyCreationContext.getCurrentProxiedBeanName();
			return FuzzyBoolean.fromBoolean(matchesBeanName(advisedBeanName));
		}

		private boolean matchesBeanName(String advisedBeanName) {
			return this.expressionPattern.matches(advisedBeanName);
		}
	}

}
