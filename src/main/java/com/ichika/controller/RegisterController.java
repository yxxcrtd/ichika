package com.ichika.controller;

import com.ichika.entity.Merchant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

import static com.ichika.utils.FileUtil.uploadFile;


@Slf4j
@RestController
@RequestMapping("register")
public class RegisterController extends BaseController {

    @GetMapping("")
    ModelAndView register() {
        ModelAndView mav = new ModelAndView();
        mav.addObject("merchant", new Merchant());
        mav.addObject("categoryMap", getCategoryMap());
        mav.setViewName("Register");
        return mav;
    }

    @PostMapping("save")
    ModelAndView save(@ModelAttribute("merchant") @Valid Merchant merchant, BindingResult result,
                      @RequestParam(value = "companyFile", required = false) MultipartFile companyFile,
                      @RequestParam(value = "idFace", required = false) MultipartFile idFace,
                      @RequestParam(value = "idBack", required = false) MultipartFile idBack) throws Exception {
        ModelAndView mav = new ModelAndView();

        String companyFileName = "";
        if (null != companyFile && !companyFile.isEmpty()) {
            companyFileName = uploadFile(companyFile, UPLOAD_PATH + "companyFile/");
            log.info("上传的营业执照：{}", companyFileName);
            if (!"".equals(companyFileName) && null != companyFileName) {
                merchant.setCompanyFile(companyFileName);
            }
        }

        String idFaceName = "";
        if (null != idFace && !idFace.isEmpty()) {
            idFaceName = uploadFile(idFace, UPLOAD_PATH + "idFace/");
            log.info("上传的身份证正面：{}", idFaceName);
            if (!"".equals(idFaceName) && null != idFaceName) {
                merchant.setIdFace(idFaceName);
            }
        }

        String idBackName = "";
        if (null != idBack && !idBack.isEmpty()) {
            idBackName = uploadFile(idBack, UPLOAD_PATH + "idBack/");
            log.info("上传的身份证正面：{}", idBackName);
            if (!"".equals(idBackName) && null != idBackName) {
                merchant.setIdBack(idBackName);
            }
        }

        try {


            merchant.setPassword(generateCode());
            merchantService.save(merchant);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ModelAndView("redirect:/login");
    }


}
