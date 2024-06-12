package top.yonyong.bianque.boot.support;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 20:23
 */
public interface Ordered {
  /**
   * Useful constant for the highest precedence value.
   * @see Integer#MIN_VALUE
   */
  int HIGHEST_PRECEDENCE = Integer.MIN_VALUE;

  /**
   * Useful constant for the lowest precedence value.
   * @see Integer#MAX_VALUE
   */
  int LOWEST_PRECEDENCE = Integer.MAX_VALUE;

  /**
   * Useful constant for the default value.
   * @see Integer
   */
  int DEFAULT_VALUE = 0;

  /**
   * Get the order value of this object.
   * <p>Higher values are interpreted as lower priority. As a consequence,
   * the object with the lowest value has the highest priority (somewhat
   * analogous to Servlet {@code load-on-startup} values).
   * <p>Same order values will result in arbitrary sort positions for the
   * affected objects.
   * @return the order value
   * @see #HIGHEST_PRECEDENCE
   * @see #LOWEST_PRECEDENCE
   */
  int getOrder();
}
