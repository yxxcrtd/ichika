package com.ichika.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping("manage")
public class ManageController extends BaseController {

    @GetMapping("")
    ModelAndView manage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("Index");
        mav.addObject("active", "index");
        return mav;
    }

}
