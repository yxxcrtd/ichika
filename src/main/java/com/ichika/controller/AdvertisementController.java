package com.ichika.controller;

import com.ichika.entity.Advertisement;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.ichika.utils.Constants.NEWS_KEY;
import static com.ichika.utils.Constants.PAGE_SIZE;
import static com.ichika.utils.FileUtil.uploadFile;
import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;


@Slf4j
@RestController
@RequestMapping("manage/advertisement")
public class AdvertisementController extends BaseController {

    @GetMapping("")
    ModelAndView list(HttpServletRequest request, @RequestParam(value = "p", defaultValue = "1") int p, @RequestParam(value = "k", defaultValue = "", required = false) String k) {
        ModelAndView mav = new ModelAndView();
        mav.addObject("k", k);
        Sort sort = Sort.by(new Sort.Order(ASC, "status"), new Sort.Order(DESC, "id"));
        Pageable pageable = PageRequest.of(p - 1, PAGE_SIZE, sort);
        Page<Advertisement> pages = advertisementService.findAllWithPage(pageable, k, -1);
        pager = new Pager();
        pager.setPageNo(p);
        long count = pages.getTotalElements();
        pager.setTotalCount(count);
        mav.addObject("count", count);
        mav.addObject("pager", pager);
        mav.addObject("list", pages.getContent());
        mav.addObject("active", "advertisement");
        mav.setViewName("advertisement/AdvertisementList");
        mav.addObject("tips", request.getSession().getAttribute(NEWS_KEY));
        return mav;
    }

    /**
     * Edit
     */
    @GetMapping("edit/{id:[\\d]+}")
    ModelAndView edit(@PathVariable(value = "id") Long id) {
        ModelAndView mav = new ModelAndView();

        if (0 == id) {
            advertisement = new Advertisement();
            advertisement.setId(0L);
        } else {
            advertisement = advertisementService.findById(id);
        }
        mav.addObject("obj", null == advertisement ? new Advertisement() : advertisement);
        mav.setViewName("advertisement/AdvertisementEdit");
        mav.addObject("active", "advertisement");
        return mav;
    }

    /**
     * Save
     */
    @PostMapping("save")
    ModelAndView save(HttpServletRequest request, @ModelAttribute("obj") @Valid Advertisement advertisement, BindingResult result, @RequestParam(value = "pictureFile", required = false) MultipartFile pictureFile) throws Exception {
        ModelAndView mav = new ModelAndView();

        if (result.hasErrors()) {
            mav.addObject("obj", advertisement);
            mav.addObject("active", "advertisement");
            mav.setViewName("advertisement/AdvertisementEdit");
            return mav;
        }

        String pictureName = "";
        if (null != pictureFile && !pictureFile.isEmpty()) {
            pictureName = uploadFile(pictureFile, UPLOAD_PATH + "advertisement/");
            log.info("上传的广告图片：{}", pictureName);
            if (!"".equals(pictureName) && null != pictureName) {
                advertisement.setPicture(pictureName);
            }
        }

        try {
            Advertisement advertisementDb = advertisementService.save(advertisement);
            request.getSession().setAttribute(NEWS_KEY, "广告" + (0 == advertisement.getId() ? "保存" : "修改") + "成功！");
            request.getSession().setMaxInactiveInterval(3);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new ModelAndView("redirect:/manage/advertisement");
    }

    /**
     * Change Status
     */
    @GetMapping("status/{id}")
    ResultMsg status(@PathVariable Long id) {
        return advertisementService.status(id);
    }

}









