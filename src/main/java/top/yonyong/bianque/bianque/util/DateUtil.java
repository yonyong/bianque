package top.yonyong.bianque.bianque.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 23:47
 */
public class DateUtil {

  public static String format(Date date) {
    return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(date);
  }
}
