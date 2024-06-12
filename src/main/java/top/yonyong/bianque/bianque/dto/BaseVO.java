package top.yonyong.bianque.bianque.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yonyong
 * @version 1.0
 * @date 2023/5/3 15:54
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseVO<T> {

  private Long cost;

  private T data;

}
