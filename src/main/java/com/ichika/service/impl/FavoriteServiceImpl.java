package com.ichika.service.impl;

import com.ichika.entity.Favorite;
import com.ichika.repository.FavoriteRepository;
import com.ichika.service.FavoriteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Slf4j
@Service
public class FavoriteServiceImpl implements FavoriteService {

    @Resource
    protected FavoriteRepository favoriteRepository;

    @Override
    public Favorite findById(long id) {
        return favoriteRepository.getOne(id);
    }

}
