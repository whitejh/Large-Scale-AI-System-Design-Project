package com.team11.gateway.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {

    private String username;
    private Collection<String> roles;

}