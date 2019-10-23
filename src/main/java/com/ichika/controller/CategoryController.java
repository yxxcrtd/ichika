package com.ichika.controller;

import com.ichika.entity.Category;
import com.ichika.utils.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

import static com.ichika.utils.Constants.CATEGORY_KEY;

/**
 * Category Controller
 */
@Slf4j
@RestController
@RequestMapping("manage/category")
public class CategoryController extends BaseController {

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        List<Category> list = categoryService.findAllParent(0L);
        mav.addObject("list", list);
        mav.addObject("count", list.size());
        mav.addObject("active", "category");
        mav.setViewName("category/CategoryList");
        mav.addObject("tips", request.getSession().getAttribute(CATEGORY_KEY));
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id:[\\d]+}")
    ModelAndView edit(@PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();

        if (0 == id) {
            category = new Category();
            category.setId(0L);
            category.setParentId(0L);
        } else {
            category = categoryService.findById(id);
        }
        mav.addObject("category", null == category ? new Category() : category);
        mav.setViewName("category/CategoryEdit");
        mav.addObject("active", "category");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    ModelAndView save(HttpServletRequest request, @ModelAttribute("category") @Valid Category category, BindingResult result) {
        ModelAndView mav = new ModelAndView();

        if (result.hasErrors()) {
            mav.addObject("category", category);
            mav.addObject("active", "category");
            mav.setViewName("category/CategoryEdit");
            return mav;
        }

        try {
            categoryService.save(category);
            request.getSession().setAttribute(CATEGORY_KEY, "分类" + (0 == category.getId() ? "保存" : "修改") + "成功！");
            request.getSession().setMaxInactiveInterval(3);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ModelAndView("redirect:/manage/category");
    }

    /**
     * Delete
     */
    @GetMapping("delete/{id}")
    ResultMsg delete(@PathVariable Long id) {
        return categoryService.delete(id);
    }

    /**
     * Sub List
     */
    @GetMapping("sub/{id:[\\d]+}")
    ModelAndView sub(@PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("category", categoryService.findById(id));
        mav.addObject("list", categoryService.findAllParent(id));
        mav.setViewName("category/CategorySub");
        return mav;
    }

    /**
     * Sub Edit
     */
    @GetMapping("sub/edit/{id}/{pid}")
    ModelAndView subEdit(@PathVariable(value = "id") Long id, @PathVariable(value = "pid") Long pid) {
        ModelAndView mav = new ModelAndView();
        if (0 == pid) {
            category = new Category();
            category.setId(pid);
            category.setParentId(id);
        } else {
            category = categoryService.findById(id);
        }
        mav.addObject("category", category);
        mav.setViewName("category/CategorySubEdit");
        return mav;
    }

    /**
     * Sub Save
     */
    @PostMapping("sub/save")
    ResultMsg save(@ModelAttribute("category") @Valid Category category) {
        return categoryService.save(category);
    }

}
