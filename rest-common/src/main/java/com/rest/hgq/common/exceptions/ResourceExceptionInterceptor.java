package com.rest.hgq.common.exceptions;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.lang.annotation.Annotation;

import static com.rest.hgq.common.exceptions.ExceptionHelper.onException;

public class ResourceExceptionInterceptor implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        Object obj = null;
        try {
            obj = invocation.proceed();
        } catch (RuntimeException e) {
            Annotation[] annotations = invocation.getMethod().getDeclaredAnnotations();
            for (Annotation annotation : annotations){
                if (annotation instanceof OnException){
                    throw onException(((OnException) annotation).value(), e);
                }
            }
        }
        return obj;
    }
}
