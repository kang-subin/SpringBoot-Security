package com.hospital.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.domain.dto.UserJoinRequest;

import com.hospital.review.domain.dto.UserLoginRequest;
import com.hospital.review.exeption.ErrorCode;
import com.hospital.review.exeption.HospitalReviewAppException;
import com.hospital.review.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void join_success() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("수빈")
                .password("123456")
                .email("subin@gmail.com")
                .build();

        when(userService.join(any())).thenReturn(mock(UserDto.class));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 실패")
    void join_fail() throws Exception {
        UserJoinRequest userJoinRequest = UserJoinRequest.builder()
                .userName("수빈")
                .password("123456")
                .email("subin@gmail.com")
                .build();

        when(userService.join(any()))
                        .thenThrow(new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userJoinRequest)))
                .andDo(print())
                .andExpect(status().isConflict());
    }



    @Test
    @DisplayName("로그인 성공")
    @WithMockUser
    void login_success() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("짭수빈")
                .password("12345")
                .build();

        when(userService.login(any(), any())) // username, password 를 받고
                .thenReturn("token"); // 성공 할 경우 토큰 리턴


        mockMvc.perform(post("/api/v1/users/login") // 호출
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().isOk());
    }




    @Test
    @DisplayName("로그인 실패 - userName 없음")
    @WithMockUser //
    void login_fail1() throws Exception {
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("짭수빈")
                .password("12345")
                .build();

        // id, pw를 보내서
        when(userService.login(any(), any()))
                .thenThrow(new HospitalReviewAppException(ErrorCode.USERNAME_NOT_FOUND, ""));

        // NOT_FOUND를 받으면 잘 만든 것이다
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }


    @Test
    @DisplayName("로그인 실패 - password 불일치")
    @WithMockUser
    void login_fail2() throws Exception{

        // 요청이 들어왔다고 가정
        UserLoginRequest userLoginRequest = UserLoginRequest.builder()
                .userName("짭수빈")
                .password("12345")
                .build();
        // 요청에 따라 서비스의 login 메소드드 실행 시 pw 불일치일 경우 HospitalReviewAppException 발생
        when(userService.login(any(), any())) // username, password 를 받고
                .thenThrow(new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,"")); // 성공 할 경우 토큰 리턴

        // controller 실행 결과 : controller 가 INVALID_PASSWORD exception을 잘 리턴하는 지 확
        mockMvc.perform(post("/api/v1/users/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(userLoginRequest))) // 요청 한 json 형태 출력
                .andExpect(status().isUnauthorized()); // 리턴 된 httpstatus 결과
    }

    }











