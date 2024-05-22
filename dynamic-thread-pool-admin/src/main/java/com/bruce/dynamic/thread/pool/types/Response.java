package com.bruce.dynamic.thread.pool.types;

import lombok.*;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Response<T> implements Serializable {
    private static final long serialVersionUID = 1l;

    private String code;
    private String info;
    private T data;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    public enum Code {
        SUCCESS("0000", "调用成功！"),
        UN_ERROR("0001", "调用失败！"),
        ILLEGAL_PARAMETER("0002", "参数不合法！"),
        ;

        private String code;
        private String info;
    }

}
