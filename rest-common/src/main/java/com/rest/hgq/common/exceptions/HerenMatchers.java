package com.rest.hgq.common.exceptions;

import com.google.inject.matcher.AbstractMatcher;
import com.google.inject.matcher.Matcher;

import java.io.Serializable;

public class HerenMatchers {
    public static Matcher<Class> inSubpackageAndClassNameEndWithGiven(
            final String targetPackageName, final String endClassName) {
        return new InSubpackageAndClassNameEndWithResource(targetPackageName, endClassName);
    }

    private static class InSubpackageAndClassNameEndWithResource extends AbstractMatcher<Class>
            implements Serializable {
        private final String targetPackageName;
        private final String endClassName;

        public InSubpackageAndClassNameEndWithResource(String targetPackageName, String endClassName) {
            this.targetPackageName = targetPackageName;
            this.endClassName = endClassName;
        }

        public boolean matches(Class c) {
            String classPackageName = c.getPackage().getName();
            String className = c.getName();
            return (classPackageName.equals(targetPackageName)
                    || classPackageName.startsWith(targetPackageName + "."))
                    && className.endsWith(endClassName);
        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            return super.equals(obj);
        }

        @Override
        public String toString() {
            return super.toString();
        }

        private static final long serialVersionUID = 0;
    }
}
