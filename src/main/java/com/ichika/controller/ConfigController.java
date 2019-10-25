package com.ichika.controller;

import com.ichika.entity.Config;
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

import static com.ichika.utils.Constants.CONFIG_KEY;
import static com.ichika.utils.Constants.LOGIN_SESSION_KEY;

/**
 * Config Controller
 */
@Slf4j
@RestController
@RequestMapping("manage/config")
public class ConfigController extends BaseController {

    /**
     * List
     */
    @GetMapping("")
    ModelAndView list(HttpServletRequest request) {
        ModelAndView mav = new ModelAndView();
        List<Config> configList = configService.findAll();
        mav.addObject("list", configList);
        mav.addObject("count", configList.size());
        mav.addObject("active", "config");
        mav.setViewName("config/ConfigList");
        mav.addObject("tips", request.getSession().getAttribute(CONFIG_KEY));
        mav.addObject("user", request.getSession().getAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId()));
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id:[\\d]+}")
    ModelAndView edit(HttpServletRequest request, @PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("config", configService.findById(id));
        mav.addObject("active", "config");
        mav.setViewName("config/ConfigEdit");
        mav.addObject("user", request.getSession().getAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId()));
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    ModelAndView save(HttpServletRequest request, @ModelAttribute("config") @Valid Config config, BindingResult result) {
        if (result.hasErrors()) {
            ModelAndView mav = new ModelAndView();
            mav.addObject("config", config);
            mav.addObject("active", "config");
            mav.setViewName("config/ConfigEdit");
            return mav;
        }

        try {
            configService.save(config);
            request.getSession().setAttribute(CONFIG_KEY, "修改成功！");
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ModelAndView("redirect:/manage/config");
    }

}
