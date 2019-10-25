package com.ichika.service;

import com.ichika.entity.User;
import com.ichika.utils.ResultMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface UserService {

    User findById(long id);

    User findByPhone(String phone);

    User save(User user);

    Page<User> findAllWithPage(Pageable pageable, String keyword);

    ResultMsg audit(Long id);

}
