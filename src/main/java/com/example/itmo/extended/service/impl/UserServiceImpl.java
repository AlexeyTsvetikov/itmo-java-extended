package com.example.itmo.extended.service.impl;

import com.example.itmo.extended.clients.RemoteClient;
import com.example.itmo.extended.exception.CommonBackendException;
import com.example.itmo.extended.model.db.entity.User;
import com.example.itmo.extended.model.db.repository.UserRepository;
import com.example.itmo.extended.model.dto.request.UserInfoRequest;
import com.example.itmo.extended.model.dto.response.UserInfoResponse;
import com.example.itmo.extended.model.enums.UserStatus;
import com.example.itmo.extended.service.UserService;
import com.example.itmo.extended.utils.PaginationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.itmo.extended.config.secret.Constants.validateKey;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService { // для межмодульной архитектуры
    private final ObjectMapper mapper;
    private final UserRepository userRepository;

    private final RemoteClient remoteClient;

    @Value("#${app.base-url:itmo.ru}")
    private String baseUrl;

    @Override
    public UserInfoResponse addUser(UserInfoRequest request) {
        if (!EmailValidator.getInstance().isValid(request.getEmail())) {
            throw new CommonBackendException("Email invalid", HttpStatus.BAD_REQUEST);
        }

        userRepository.findByEmail(request.getEmail()).ifPresent(user -> {
            throw new CommonBackendException("User already exists", HttpStatus.CONFLICT);
        });

        User user = mapper.convertValue(request, User.class);
        user.setStatus(UserStatus.CREATED);

        User save = userRepository.save(user);
        return mapper.convertValue(save, UserInfoResponse.class);
    }

    @Override
    public UserInfoResponse getUser(String apiKey, Long id) {
        validateKey(apiKey);
        User user = getUserFromDB(id);
        return mapper.convertValue(user, UserInfoResponse.class);
    }

    @Override
    public User getUserFromDB(Long id) {
        Optional<User> optionalUser = userRepository.findById(id);
        final String errMsg = String.format("User with id: %s not found", id);
        return optionalUser
                .orElseThrow(() -> new CommonBackendException(errMsg, HttpStatus.NOT_FOUND));
    }

    @Override
    public UserInfoResponse updateUser(Long id, UserInfoRequest request) {
        User userFromDB = getUserFromDB(id);
        User userReq = mapper.convertValue(request, User.class);

        userFromDB.setEmail(userReq.getEmail() == null ? userFromDB.getEmail() : userReq.getEmail());
        userFromDB.setPassword(userReq.getPassword() == null ? userFromDB.getPassword() : userReq.getPassword());
        userFromDB.setFirstName(userReq.getFirstName() == null ? userFromDB.getFirstName() : userReq.getFirstName());
        userFromDB.setLastName(userReq.getLastName() == null ? userFromDB.getLastName() : userReq.getLastName());
        userFromDB.setMiddleName(userReq.getMiddleName() == null ? userFromDB.getMiddleName() : userReq.getMiddleName());
        userFromDB.setAge(userReq.getAge() == null ? userFromDB.getAge() : userReq.getAge());
        userFromDB.setGender(userReq.getGender() == null ? userFromDB.getGender() : userReq.getGender());

        userFromDB = userRepository.save(userFromDB);
        return mapper.convertValue(userFromDB, UserInfoResponse.class);
    }

    @Override
    public void deleteUser(Long id) {
        User userFromDB = getUserFromDB(id);
        userFromDB.setStatus(UserStatus.DELETED);
        userRepository.save(userFromDB);
    }

    @Override
    public Page<UserInfoResponse> getAllUsers(Integer page, Integer perPage, String sort, Sort.Direction order, String filter) {

        Pageable pageRequest = PaginationUtils.getPageRequest(page, perPage, sort, order);
        Page<User> users;

        if (StringUtils.hasText(filter)) {
            users = userRepository.findAllFiltered(pageRequest, filter);
        } else {
            users = userRepository.findAll(pageRequest);
        }

        List<UserInfoResponse> content = users.getContent().stream()
                .map(u -> mapper.convertValue(u, UserInfoResponse.class))
                .collect(Collectors.toList());

        return new PageImpl<>(content, pageRequest, users.getTotalElements());
    }

    @Override
    public User updateCarList(User updatedUser) {
        return userRepository.save(updatedUser);
    }

    @Override
    public void invalidateSessions() {
        //logic

        String email = UserInfoRequest.Fields.email;
        String ade = UserInfoRequest.Fields.age;
    }

    @Override
    public UserInfoResponse getYaUser(Long id, String apiKey) {
        ResponseEntity<UserInfoResponse> yaUserResponse = remoteClient.getYaUser(id, apiKey);

        if (yaUserResponse.getStatusCode().is2xxSuccessful()) {
            return yaUserResponse.getBody();
        } else {
            String response = yaUserResponse.toString();

            throw new CommonBackendException(response, HttpStatus.valueOf(yaUserResponse.getStatusCode().value()));
        }
    }
}
