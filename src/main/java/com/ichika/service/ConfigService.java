package com.ichika.service;

import com.ichika.entity.Config;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ConfigService {

    Config save(Config config);

    Config findById(Long id);

    Config findByKey(String key);

    Page<Config> findAllWithPage(Pageable pageable, String keyword);

    List<Config> findAll();

    List<Config> findAll(String keyword);

}
