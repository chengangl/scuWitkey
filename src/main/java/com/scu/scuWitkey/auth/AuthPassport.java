package com.scu.scuWitkey.auth;

import java.lang.annotation.*;

/**
 * Created by Administrator on 2016/4/8/0008.
 * 添加自定义注解AuthPassport
 */
@Documented
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AuthPassport {
    boolean validate() default true;
}
