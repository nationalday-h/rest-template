package com.rest.hgq.common.exceptions;

import com.google.common.base.Strings;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Collection;

final public class ExceptionHelper {

    private ExceptionHelper() {
    }

    public static HerenException onException(String message, RuntimeException e) {
        //自定义异常直接抛出，503
        if (e instanceof HerenException) {
            return (HerenException) e;
        }
        //过滤校验异常，抛出ValidationException
        if (e.getCause() instanceof ConstraintViolationException) {
            Errors errors = Errors.errors(message);
            for (ConstraintViolation<?> constraintViolation : ((ConstraintViolationException) e.getCause()).getConstraintViolations()) {
                errors.add(constraintViolation.getRootBeanClass().getName(), constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            }
            // 412
            return new ValidationException(e, errors);
        }
        // 其他系统异常，如PersistenceException等，抛出系统异常500
        return new SystemException(message + "\n" + e.getMessage(), e);
    }

    public static ValidationExceptionHelper validation(Class beanClass) {
        return validation("", beanClass);
    }

    public static ValidationExceptionHelper validation(String message, Class beanClass) {
        return new ValidationExceptionHelper(message, beanClass);
    }

    public static BusinessExceptionHelper business() {
        return new BusinessExceptionHelper();
    }

    public static class BusinessExceptionHelper {
        public BusinessExceptionHelper() {
            super();
        }

        public BusinessExceptionHelper check(boolean expression, String message) {
            if (!expression) {
                throw new BusinessException(message);
            }
            return this;
        }

        public BusinessExceptionHelper checkNotNullAndEmpty(String s, String message) {
            if (Strings.isNullOrEmpty(s)) {
                throw new BusinessException(message);
            }
            return this;
        }

        public BusinessExceptionHelper checkNullOrEmpty(String s, String message) {
            if (!Strings.isNullOrEmpty(s)) {
                throw new BusinessException(message);
            }
            return this;
        }

        public BusinessExceptionHelper checkNotNullAndEmpty(Collection collection, String message) {
            if (collection == null || collection.isEmpty()) {
                throw new BusinessException(message);
            }
            return this;
        }

        public BusinessExceptionHelper checkBeforeMoreThanAfter(Long before, Long after, String message) {
            if (before == null || after == null) {

            } else if (before <= after) {
                throw new BusinessException(message);
            }
            return this;
        }

        public BusinessExceptionHelper checkBeforeMoreThanAndEqualToAfter(Long before, Long after, String message) {
            if (before == null || after == null) {

            } else if (before < after) {
                throw new BusinessException(message);
            }
            return this;
        }

        public BusinessExceptionHelper checkBeforeEqualToAfter(Long before, Long after, String message) {
            if (before == null || after == null) {

            } else if (before != after) {
                throw new BusinessException(message);
            }
            return this;
        }

        public void end() {
        }
    }

    public static class ValidationExceptionHelper {
        protected Errors errors;
        protected Class beanClass;

        public ValidationExceptionHelper(String message, Class beanClass) {
            this.beanClass = beanClass;
            this.errors = Errors.errors(message);
        }

        public ValidationExceptionHelper check(boolean expression, String field, String message) {
            if (!expression) {
                errors.add(beanClass.getName(), field, message);
            }
            return this;
        }

        public ValidationExceptionHelper checkNotNullAndEmpty(Collection collection, String field, String message) {
            if (collection == null || collection.isEmpty()) {
                errors.add(beanClass.getName(), field, message);
            }
            return this;
        }

        public ValidationExceptionHelper checkNotNullAndEmpty(String s, String field, String message) {
            if (Strings.isNullOrEmpty(s)) {
                errors.add(beanClass.getName(), field, message);
            }
            return this;
        }

        public ValidationExceptionHelper checkIsNullOrEmpty(String s, String field, String message) {
            if (!Strings.isNullOrEmpty(s)) {
                errors.add(beanClass.getName(), field, message);
            }
            return this;
        }

        public void end() {
            if (errors.isEmpty()) {
                return;
            }
            throw new ValidationException(null, errors);
        }
    }
}
