package com.example.itmo.extended.service;

import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService { // для межмодульной архитектуры

    UserInfoResponse addUser(UserInfoRequest request);

    UserInfoResponse getUser(Long id);

    UserInfoResponse updateUser(Long id, UserInfoRequest request);

    void deleteUser(Long id);

    List<UserInfoResponse> getAllUsers();

    UserInfoResponse getUser(String email, String lastName);
}
