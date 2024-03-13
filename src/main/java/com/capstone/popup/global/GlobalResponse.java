package com.capstone.popup.global;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GlobalResponse<T> {

    private String statusCode;
    private String msg;
    private T data;

    public static <T> GlobalResponse<T> of(String statusCode, String msg) {
        GlobalResponse<T> globalResponse = new GlobalResponse<T>();

        globalResponse.setStatusCode(statusCode);
        globalResponse.setMsg(msg);

        return globalResponse;
    }

    public static <T> GlobalResponse<T> of(String statusCode, String msg, T data) {
        GlobalResponse<T> globalResponse = new GlobalResponse<T>();

        globalResponse.setStatusCode(statusCode);
        globalResponse.setMsg(msg);
        globalResponse.setData(data);

        return globalResponse;
    }
}
