package com.tasksprints.auction.domain.user.service;

import com.tasksprints.auction.domain.user.dto.UserDetailsDTO;
import com.tasksprints.auction.domain.user.dto.UserRequest;
import com.tasksprints.auction.domain.user.dto.UserSummaryDTO;
import com.tasksprints.auction.domain.user.exception.UserNotFoundException;
import com.tasksprints.auction.domain.user.model.User;
import com.tasksprints.auction.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetailsDTO createUser(UserRequest.Register request) {
        User user = User.create(request.getName(),request.getEmail(), request.getPassword(), request.getNickname());

        User newUser = userRepository.save(user);
        return UserDetailsDTO.of(newUser);
    }

    @Override
    public UserDetailsDTO getUserDetailsById(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        return UserDetailsDTO.of(foundUser);
    }

    @Override
    public List<UserSummaryDTO> getUsersSummary() {
        List<User> foundUsers = userRepository.findAll();
        return foundUsers.stream()
                .map(UserSummaryDTO::new)
                .collect(Collectors.toList());
    }

    @Override
    public UserDetailsDTO updateUser(Long id, UserRequest.Update request) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));

        user.update(request.getName(), request.getPassword(), request.getNickname());
        User updatedUser = userRepository.save(user);
        return UserDetailsDTO.of(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with id " + id));
        user.delete(); // 사용자 상태를 '삭제됨'으로 변경
        userRepository.save(user); // 상태 업데이트를 저장
    }

}