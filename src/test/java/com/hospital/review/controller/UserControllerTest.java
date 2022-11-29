package com.hospital.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.exeption.ErrorCode;
import com.hospital.review.exeption.HospitalReviewAppException;
import com.hospital.review.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired // Controller 테스트 하기 위해서 사용
    MockMvc mockMvc;

    @MockBean // 가짜객체 생성
    UserService userService;

    @Autowired
    ObjectMapper objectMapper; // 자바 객체 json 형태로 변경

    @Test
    @DisplayName("회원가입 성공")
    void join_success() throws Exception {

        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("subin")
                .password("1234")
                .email("subin@naver.com")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));
        //userService의 join메소드 실행 시 thenThrow 리턴
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @DisplayName("회원가입 실패")
    void join_fail() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("subin")
                .password("1234")
                .email("subin@naver.com")
                .build();

        when(userService.join(any())).thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));
        //userService의 join메소드 실행 시 thenThrow 리턴
        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict()); // 409에러
    }
}