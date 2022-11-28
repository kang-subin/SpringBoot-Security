package com.hospital.review.service;

import com.hospital.review.domain.dto.UserJoinRequest;
import com.hospital.review.domain.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public UserDto join(UserJoinRequest request) {

        return new UserDto("", "", "");
    }
}
