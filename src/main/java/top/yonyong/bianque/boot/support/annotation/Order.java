package top.yonyong.bianque.boot.support.annotation;


import top.yonyong.bianque.boot.support.Ordered;

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
public @interface Order {

  /**
   * The order value.
   * <p>Default is {@link Ordered#DEFAULT_VALUE}.
   * @see Ordered#getOrder()
   */
  int value() default Ordered.DEFAULT_VALUE;
}
