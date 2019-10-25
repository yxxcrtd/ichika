package com.ichika.controller;

import com.ichika.entity.Video;
import com.ichika.utils.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.ichika.utils.Constants.LOGIN_SESSION_KEY;
import static com.ichika.utils.Constants.PAGE_SIZE;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;


@Slf4j
@RestController
@RequestMapping("manage/video")
public class VideoController extends BaseController {

    @GetMapping("")
    ModelAndView list(HttpServletRequest request, @RequestParam(value = "p", defaultValue = "1") int p, @RequestParam(value = "k", defaultValue = "", required = false) String k) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("k", k);
        Sort sort = Sort.by(new Sort.Order(ASC, "status"), new Sort.Order(DESC, "id"));
        Pageable pageable = PageRequest.of(p - 1, PAGE_SIZE, sort);
        Page<Video> pages = videoService.findAllWithPage(pageable, k);
        pager = new Pager();
        pager.setPageNo(p);
        long count = pages.getTotalElements();
        pager.setTotalCount(count);
        mav.addObject("count", count);
        mav.addObject("pager", pager);
        mav.addObject("list", pages.getContent());
        mav.addObject("active", "video");
        mav.setViewName("video/VideoList");
        mav.addObject("user", request.getSession().getAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId()));
        return mav;
    }

}









