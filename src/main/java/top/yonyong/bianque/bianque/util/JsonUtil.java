package top.yonyong.bianque.bianque.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/2 23:03
 */
public class JsonUtil {
  /**
   * json array to object list
   * @param jsonArray jsonArray
   * @param tClass tClass
   * @param <T> T
   * @return object list
   */
  public static <T> List<T> toList(JsonArray jsonArray,Class<T> tClass){
    List<T> list = new ArrayList<>();
    for (int i = 0; i < jsonArray.size(); i++) {
      JsonObject json = jsonArray.getJsonObject(i);
      list.add(json.mapTo(tClass));
    }
    return list;
  }

  /**
   * json array to object list
   * @param list list
   * @param <T> T
   * @return JsonArray
   */
  public static <T> JsonArray toJsonArray(List<T> list){
    JsonArray jsonArray = new JsonArray();
    list.forEach(o -> jsonArray.add(JsonObject.mapFrom(o)));
    return jsonArray;
  }

  /**
   * json array to object list
   * @param jsonObject jsonObject
   * @param tClass tClass
   * @param <T> T
   * @return object list
   */
  public static <T> T toBean(JsonObject jsonObject,Class<T> tClass){
    return jsonObject.mapTo(tClass);
  }
}
