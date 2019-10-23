package com.ichika.service.impl;

import com.ichika.entity.Config;
import com.ichika.repository.ConfigRepository;
import com.ichika.service.ConfigService;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;

@Service
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigRepository configRepository;

    @Transactional
    public Config save(Config config) {
        return configRepository.save(config);
    }

    public Config findById(Long id) {
        return configRepository.findById(id).get();
    }

    public Config findByKey(String key) {
        return configRepository.findByKey(key);
    }

    public Page<Config> findAllWithPage(Pageable pageable, String keyword) {
        return configRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.like(root.get("name"), "%" + keyword + "%");
            return criteriaBuilder.and(predicate);
        }, pageable);
    }

    public List<Config> findAll() {
        return configRepository.findAll(Sort.by(new Sort.Order(ASC, "id")));
    }

    public List<Config> findAll(String keyword) {
        return configRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = Lists.newArrayList();
            if (!"".equals(keyword)) {
                list.add(criteriaBuilder.like(root.get("name"), "%" + keyword + "%"));
            }
            criteriaQuery.where(list.toArray(new Predicate[]{}));
            return null;
        }, Sort.by(new Sort.Order(ASC, "id")));
    }

}
