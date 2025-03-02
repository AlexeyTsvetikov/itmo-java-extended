package com.example.itmo.extended.service.impl;

import com.example.itmo.extended.exception.CommonBackendException;
import com.example.itmo.extended.model.db.entity.User;
import com.example.itmo.extended.model.db.repository.UserRepository;
import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import com.example.itmo.extended.model.enums.UserStatus;
import com.example.itmo.extended.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
    public void addUserExists() {
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
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");
        String apiKey = "SmF2YSBEZXZlbG9wZXI=";

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        UserInfoResponse userInfoResponse = userService.getUser(apiKey, user.getId());
        assertEquals(user.getEmail(), userInfoResponse.getEmail());

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
        User user = new User();
        user.setId(1L);
        user.setEmail("test@test.com");

        UserInfoRequest req = new UserInfoRequest();
        req.setEmail("user@mail.com");

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        userService.updateUser(user.getId(), req);
        assertEquals(req.getEmail(), user.getEmail());

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
    public void getAllUsersWithFilter() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortField = "name";
        Sort.Direction sortDirection = Sort.Direction.ASC;
        String filter = "active";

        Pageable pageable = PaginationUtils.getPageRequest(pageNumber, pageSize, sortField, sortDirection);

        User user = new User();
        UserInfoResponse userInfoResponse = new UserInfoResponse();

        when(userRepository.findAllFiltered(pageable, filter)).thenReturn(new PageImpl<>(Arrays.asList(user)));
        when(mapper.convertValue(user, UserInfoResponse.class)).thenReturn(userInfoResponse);

        Page<UserInfoResponse> result = userService.getAllUsers(pageNumber, pageSize, sortField, sortDirection, filter);

        assertEquals(1, result.getContent().size());
        assertEquals(userInfoResponse, result.getContent().get(0));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void getAllUsersWithoutFilter() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortField = "name";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Pageable pageable = PaginationUtils.getPageRequest(pageNumber, pageSize, sortField, sortDirection);
        User user = new User();
        UserInfoResponse userInfoResponse = new UserInfoResponse();

        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(user)));
        when(mapper.convertValue(user, UserInfoResponse.class)).thenReturn(userInfoResponse);

        Page<UserInfoResponse> result = userService.getAllUsers(pageNumber, pageSize, sortField, sortDirection, null);

        assertEquals(1, result.getContent().size());
        assertEquals(userInfoResponse, result.getContent().get(0));
        assertEquals(1, result.getTotalElements());
    }

    @Test
    public void getAllUsersEmptyResult() {
        int pageNumber = 0;
        int pageSize = 10;
        String sortField = "name";
        Sort.Direction sortDirection = Sort.Direction.ASC;

        Pageable pageable = PaginationUtils.getPageRequest(pageNumber, pageSize, sortField, sortDirection);
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList()));

        Page<UserInfoResponse> result = userService.getAllUsers(pageNumber, pageSize, sortField, sortDirection, null);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    public void updateCarList() {
        User updatedUser = new User();
        updatedUser.setId(1L);

        when(userRepository.save(updatedUser)).thenReturn(updatedUser);

        User result = userService.updateCarList(updatedUser);

        assertNotNull(result);
        assertEquals(updatedUser.getId(), result.getId());
        verify(userRepository, times(1)).save(updatedUser);
    }
}