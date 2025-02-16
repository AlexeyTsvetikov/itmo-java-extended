package com.example.itmo.extended.service;

import com.example.itmo.extended.model.db.entity.User;
import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService { // для межмодульной архитектуры

    UserInfoResponse addUser(UserInfoRequest request);

    UserInfoResponse getUser(Long id);

    User getUserFromDB(Long id);

    UserInfoResponse updateUser(Long id, UserInfoRequest request);

    void deleteUser(Long id);

    Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter);

    User updateCarList(User userFromDB);
}
