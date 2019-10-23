package com.ichika.service;

import com.ichika.entity.Category;
import com.ichika.utils.ResultMsg;
import org.springframework.data.domain.Sort;

import java.util.List;


public interface CategoryService {

    List<Category> findAll(Sort sort);

    List<Category> findAllParent(Long parentId);

    Category findById(Long id);

    ResultMsg save(Category category);

    ResultMsg delete(Long cid);

}
