package com.hospital.review.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class UserDto {  // Dto 역할 : Service → Dto → Response<T>

    private Long id;
    private String userName;
    private String password;
    private String email;

}
