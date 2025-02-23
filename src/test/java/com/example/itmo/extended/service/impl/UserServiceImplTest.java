package com.example.itmo.extended.service.impl;

import com.example.itmo.extended.exception.CommonBackendException;
import com.example.itmo.extended.model.db.entity.User;
import com.example.itmo.extended.model.db.repository.UserRepository;
import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import com.example.itmo.extended.model.enums.UserStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Spy
    private ObjectMapper mapper;


    @Test
    public void addUser() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserInfoResponse userInfoResponse = userService.addUser(request);
        assertEquals(user.getId(), userInfoResponse.getId());
    }

    @Test(expected = CommonBackendException.class)
    public void addUserInvalidEmail() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("testtest.com");

        userService.addUser(request);

    }

    @Test(expected = CommonBackendException.class)
    public void addUserUserExists() {
        UserInfoRequest request = new UserInfoRequest();
        request.setEmail("test@test.com");

        User user = new User();
        user.setId(1L);
        user.setEmail(request.getEmail());

        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
        userService.addUser(request);

    }

    @Test
    public void getUser() {

    }

    @Test
    public void getUserFromDB() {
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User userFromDB = userService.getUserFromDB(user.getId());

        assertEquals(user.getEmail(), userFromDB.getEmail());

    }

    @Test(expected = CommonBackendException.class)
    public void getUserFromDBNotFound() {
        userService.getUserFromDB(1L);
    }

    @Test
    public void updateUser() {
    }

    @Test
    public void deleteUser() {
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        userService.deleteUser(user.getId());
        verify(userRepository, times(1)).save(any(User.class));
        assertEquals(UserStatus.DELETED, user.getStatus());
    }

    @Test
    public void getAllUsers() {
    }

    @Test
    public void updateCarList() {
    }

    @Test
    public void invalidateSessions() {
    }
}