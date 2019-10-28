package com.ichika.service.impl;

import com.ichika.entity.User;
import com.ichika.repository.UserRepository;
import com.ichika.service.UserService;
import com.ichika.utils.MD5;
import com.ichika.utils.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.List;

import static com.ichika.utils.Constants.FAIL;
import static com.ichika.utils.Constants.LOGIN_USERNAME_SALT;
import static com.ichika.utils.ResultMsg.resultFail;
import static com.ichika.utils.ResultMsg.resultSuccess;


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

    @Transactional
    public User save(User user) {
        return userRepository.save(user);
    }

    public Page<User> findAllWithPage(Pageable pageable, String keyword) {
        return userRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = Lists.newArrayList();
            Path<String> phone = root.get("phone");
            Predicate phonePredicate = criteriaBuilder.like(phone, "%" + keyword + "%");
            list.add(criteriaBuilder.or(phonePredicate));
            criteriaQuery.where(list.toArray(new Predicate[]{}));
            return null;
        }, pageable);
    }

    @Transactional
    public ResultMsg audit(Long id) {
        try {
            User user = userRepository.getOne(id);
            user.setStatus(1);
            user.setPassword(MD5.toMD5("123456" + LOGIN_USERNAME_SALT));
            userRepository.save(user);
            return resultSuccess("审核成功");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

}
