package top.yonyong.bianque.bianque.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestEntity {
    private String ip;

    private String resource;

    private String createTime;
}
