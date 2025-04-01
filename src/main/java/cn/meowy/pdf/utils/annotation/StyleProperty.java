package cn.meowy.pdf.utils.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 样式注解
 *
 * @author: Mr.Zou
 * @date: 2025-03-29
 **/
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface StyleProperty {

    String value() default "";

}
