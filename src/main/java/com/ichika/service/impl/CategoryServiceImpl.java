package com.ichika.service.impl;

import com.ichika.entity.Category;
import com.ichika.repository.CategoryRepository;
import com.ichika.service.CategoryService;
import com.ichika.utils.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;

import static com.ichika.utils.ResultMsg.resultFail;
import static com.ichika.utils.ResultMsg.resultSuccess;

@Slf4j
@Service
public class CategoryServiceImpl implements CategoryService {

    @Resource
    private CategoryRepository categoryRepository;

    public List<Category> findAll(Sort sort) {
        return categoryRepository.findAll(sort);
    }

    public List<Category> findAllParent(Long parentId) {
        return categoryRepository.findByParentIdOrderByIdAsc(parentId);
    }

    public Category findById(Long id) {
        return categoryRepository.findById(id).get();
    }

    @Transactional
    public ResultMsg save(Category category) {
        Category categoryDb = categoryRepository.save(category);
        String s = "子";
        if (0 == categoryDb.getParentId()) {
            s = "";
        }
        return resultSuccess(s + "分类保存成功！", categoryDb);
    }

    @Transactional
    public ResultMsg delete(Long id) {
        String s = "子";
        Category categoryDb = categoryRepository.getOne(id);
        try {
            if (0 == categoryDb.getParentId()) {
                s = "";
                categoryRepository.deleteCategoryByParentId(categoryDb.getId());
            }
            categoryRepository.delete(categoryDb);
            return resultSuccess(s + "分类删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(s + "分类删除失败！");
    }

}
