package com.ichika.service;

import com.ichika.entity.User;


public interface UserService {

    User findById(long id);

    User findByPhone(String phone);

}
