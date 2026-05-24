package com.cts.logichain360.service;

import com.cts.logichain360.dto.request.*;
import com.cts.logichain360.dto.response.*;
import org.springframework.http.ResponseEntity;
import java.util.List;

public interface UserService {
    ResponseEntity<UserRegistrationResponse> registerUser(UserRegistrationRequest request);
    ResponseEntity<LoginResponse>            login(LoginRequest request);
    ResponseEntity<UserResponse>             getUserById(Long id);
    ResponseEntity<List<UserResponse>>       getAllUsers();
    ResponseEntity<UserResponse>             updateUser(Long id, UpdateUserRequest request);
    ResponseEntity<UserResponse>             updateUserStatus(Long id, UserStatusRequest request);
    ResponseEntity<Void>                     deleteUser(Long id);
}