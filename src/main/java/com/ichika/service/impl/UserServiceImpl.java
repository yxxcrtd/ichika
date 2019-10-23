package com.ichika.service.impl;

import com.ichika.entity.User;
import com.ichika.repository.UserRepository;
import com.ichika.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Resource
    protected UserRepository userRepository;

    public User findById(long id) {
        return userRepository.getOne(id);
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

}
