package com.hospital.review.domain;

import com.hospital.review.domain.dto.UserJoinResponse;
import com.hospital.review.domain.dto.UserLoginResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Response <T> {
    private String resultCode;
    private T result;

    public static Response<Void> error(String resultCode) {
        return new Response<>(resultCode, null);
    }

    public static <T> Response<T> suceess(T result) {
        return new Response("SUCCESS", result);
    }

}
