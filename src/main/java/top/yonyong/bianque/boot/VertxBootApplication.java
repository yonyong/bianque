package top.yonyong.bianque.boot;

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
public @interface VertxBootApplication {

  /**
   * is a work verticle
   * @return true yes/false no
   */
  String basePackage() default "";
}
