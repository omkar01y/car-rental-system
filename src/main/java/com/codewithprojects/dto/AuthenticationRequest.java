package com.codewithprojects.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;


@Data
public class AuthenticationRequest {

    private String email;
    private String password;
}
