package com.ichika.controller;

import com.ichika.entity.User;
import com.ichika.utils.MD5;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ichika.utils.Constants.LOGIN_PASSWORD_EMPTY;
import static com.ichika.utils.Constants.LOGIN_SESSION_KEY;
import static com.ichika.utils.Constants.LOGIN_USERNAME_EMPTY;
import static com.ichika.utils.Constants.LOGIN_USERNAME_OR_PASSWORD_WRONG;
import static com.ichika.utils.Constants.LOGIN_USERNAME_SALT;

@Slf4j
@RestController
@RequestMapping("login")
public class LoginController extends BaseController {

    @GetMapping("")
    ModelAndView index() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("user", new User());
        mav.setViewName("Login");
        return mav;
    }

    @PostMapping("check")
    ModelAndView check(HttpServletRequest request, @ModelAttribute("user") @Valid User user, RedirectAttributes redirectAttributes) {
        if (null == user.getPhone() || "".equals(user.getPhone())) {
            redirectAttributes.addFlashAttribute("tips", LOGIN_USERNAME_EMPTY);
            return new ModelAndView("redirect:/login");
        }
        if (null == user.getPassword() || "".equals(user.getPassword())) {
            redirectAttributes.addFlashAttribute("tips", LOGIN_PASSWORD_EMPTY);
            return new ModelAndView("redirect:/login");
        }
        User u = userService.findByPhone(user.getPhone());
        if (null != u) {
            if (!MD5.toMD5(user.getPassword() + LOGIN_USERNAME_SALT).equals(u.getPassword())) {
                redirectAttributes.addFlashAttribute("tips", LOGIN_USERNAME_OR_PASSWORD_WRONG);
                return new ModelAndView("redirect:/login");
            } else {
                request.getSession().setAttribute(LOGIN_SESSION_KEY + request.getRequestedSessionId(), u);
                log.info("用户：{} 登录成功！", u.getPhone());
            }
        }
        return new ModelAndView("redirect:/manage");
    }

}
