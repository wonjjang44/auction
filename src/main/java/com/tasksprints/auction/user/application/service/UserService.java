package com.tasksprints.auction.user.application.service;

import com.tasksprints.auction.user.domain.dto.request.UserRequest;
import com.tasksprints.auction.user.domain.dto.response.UserDetailResponse;
import com.tasksprints.auction.user.domain.dto.response.UserSummaryResponse;
import com.tasksprints.auction.user.domain.entity.User;

import java.util.List;

public interface UserService {
    UserDetailResponse createUser(UserRequest.Register user);

    UserDetailResponse getUserDetailsById(Long id);

    List<UserSummaryResponse> getUsersSummary();

    UserDetailResponse updateUser(Long id, UserRequest.Update user);

    void deleteUser(Long id);

    User getUserById(Long id);
}
