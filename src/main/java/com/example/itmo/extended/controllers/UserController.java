package com.example.itmo.extended.controllers;

import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import com.example.itmo.extended.model.enums.Gender;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/users")
public class UserController {

    @GetMapping
    public UserInfoResponse getUser(){
        return UserInfoResponse.builder()
                .id(1L)
                .email("test@test.ru")
                .age(40)
                .firstName("Ivan")
                .lastName("Ivanov")
                .middleName("Ivanovich")
                .gender(Gender.MALE)
                .password("12345")
                .build();
    }

    @PostMapping
    public UserInfoRequest addUser(@RequestBody UserInfoRequest request){
        return request;
    }
}
