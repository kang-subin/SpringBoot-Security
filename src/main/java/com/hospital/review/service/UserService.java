package com.hospital.review.service;

import com.hospital.review.util.JwtTokenUtil;
import com.hospital.review.domain.User;
import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.domain.dto.UserDto;
import com.hospital.review.exeption.ErrorCode;
import com.hospital.review.exeption.HospitalReviewAppException;
import com.hospital.review.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder; // 비밀번호를 암호화 하는데 사용할 수 있는 메소드를 가진 클래스

    @Value("${jwt.token.secret}")
    private String secretKey;

    private long expireTimeMs = 10000 * 60 * 60 ; // 1시간


    public UserDto join(UserJoinRequest request) {
        // 비즈니스 로직 - 회원 가입
        // 회원 userName(id) 중복 Check
        // 중복이면 회원가입 x --> Exception(예외)발생
        // 있으면 에러처리
        userRepository.findByUserName(request.getUserName())
                .ifPresent(user -> {
                    throw new HospitalReviewAppException(ErrorCode.DUPLICATED_USER_NAME, String.format("UserName:%s", request.getUserName()));
                });

        // 회원가입 .save()
        User savedUser = userRepository.save(request.toEntity(encoder.encode(request.getPassword())));
        return UserDto.builder()
                .id(savedUser.getId())
                .userName(savedUser.getUserName())
                .email(savedUser.getEmailAddress())
                .build();
    }

    public String login(String userName, String password) {
        // 1.id(userName) 있는지 여부 확인
        User user = userRepository.findByUserName(userName).orElseThrow(
                () -> {throw new HospitalReviewAppException(
                        ErrorCode.NOT_FOUND, String.format("username %s이 없습니다", userName)
                );} );

        // 2.password 일치 하는지 여부 확인
        if (!encoder.matches(password, user.getPassword())) {
            throw new HospitalReviewAppException(ErrorCode.INVALID_PASSWORD,"해당 userName의 password가 잘못됐습니다");
        }
        //두 가지 확인중 예외 안났으면 Token발행

        return JwtTokenUtil.generateToken(userName,secretKey,expireTimeMs);
    }

    }








