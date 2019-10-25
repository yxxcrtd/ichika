package com.ichika.controller;

import com.ichika.entity.News;
import com.ichika.utils.Pager;
import com.ichika.utils.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ichika.utils.Constants.LOGIN_SESSION_KEY;
import static com.ichika.utils.Constants.NEWS_KEY;
import static com.ichika.utils.Constants.PAGE_SIZE;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;


@Slf4j
@RestController
@RequestMapping("manage/news")
public class NewsController extends BaseController {

    @GetMapping("")
    ModelAndView list(HttpServletRequest request, @RequestParam(value = "p", defaultValue = "1") int p, @RequestParam(value = "k", defaultValue = "", required = false) String k) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("k", k);
        Sort sort = Sort.by(new Sort.Order(ASC, "status"), new Sort.Order(DESC, "id"));
        Pageable pageable = PageRequest.of(p - 1, PAGE_SIZE, sort);
        Page<News> pages = newsService.findAllWithPage(pageable, k, -1);
        pager = new Pager();
        pager.setPageNo(p);
        long count = pages.getTotalElements();
        pager.setTotalCount(count);
        mav.addObject("count", count);
        mav.addObject("pager", pager);
        mav.addObject("list", pages.getContent());
        mav.addObject("active", "news");
        mav.setViewName("news/NewsList");
        mav.addObject("tips", request.getSession().getAttribute(NEWS_KEY));
        mav.addObject("user", request.getSession().getAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId()));
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id:[\\d]+}")
    ModelAndView edit(HttpServletRequest request, @PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();

        if (0 == id) {
            news = new News();
            news.setId(0L);
        } else {
            news = newsService.findById(id);
        }
        mav.addObject("news", null == news ? new News() : news);
        mav.setViewName("news/NewsEdit");
        mav.addObject("active", "news");
        mav.addObject("user", request.getSession().getAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId()));
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    ModelAndView save(HttpServletRequest request, @ModelAttribute("news") @Valid News news, BindingResult result) {
        ModelAndView mav = new ModelAndView();

        if (result.hasErrors()) {
            mav.addObject("news", news);
            mav.addObject("active", "news");
            mav.setViewName("news/NewsEdit");
            return mav;
        }

        try {
            News newsDb = newsService.save(news);
            request.getSession().setAttribute(NEWS_KEY, "系统消息" + (0 == news.getId() ? "保存" : "修改") + "成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ModelAndView("redirect:/manage/news");
    }

    /**
     * Change Status
     */
    @GetMapping("status/{id}")
    ResultMsg status(@PathVariable Long id) {
        return newsService.status(id);
    }

}









