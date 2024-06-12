package top.yonyong.bianque.boot.support.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 16:54
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Verticle {

  /**
   * configure instance size
   * @return instance size
   */
  int instance() default 1;

  /**
   * is a work verticle
   * @return true yes/false no
   */
  boolean isWork() default false;

  /**
   * configure verticle pool name
   * @return verticle pool name
   */
  String workPoolName() default "";

  /**
   * configure pool size
   * @return pool size
   */
  int workPoolSize() default 20;
}
