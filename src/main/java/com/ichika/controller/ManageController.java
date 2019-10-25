package com.ichika.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

import static com.ichika.utils.Constants.LOGIN_SESSION_KEY;


@Slf4j
@RestController
@RequestMapping("manage")
public class ManageController extends BaseController {

    @GetMapping("")
    ModelAndView manage(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        mav.setViewName("Manage");
        mav.addObject("active", "index");
        mav.addObject("user", request.getSession().getAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId()));
        return mav;
    }

}
