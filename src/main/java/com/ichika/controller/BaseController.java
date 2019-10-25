package com.ichika.controller;

import com.ichika.entity.Advertisement;
import com.ichika.entity.Category;
import com.ichika.entity.Comment;
import com.ichika.entity.Config;
import com.ichika.entity.Favorite;
import com.ichika.entity.News;
import com.ichika.entity.Share;
import com.ichika.entity.User;
import com.ichika.entity.Video;
import com.ichika.service.AdvertisementService;
import com.ichika.service.CategoryService;
import com.ichika.service.CommentService;
import com.ichika.service.ConfigService;
import com.ichika.service.FavoriteService;
import com.ichika.service.NewsService;
import com.ichika.service.ShareService;
import com.ichika.service.UserService;
import com.ichika.service.VideoService;
import com.ichika.utils.Pager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


@Slf4j
public class BaseController {

    @Value("${spring.http.multipart.location}")
    protected String UPLOAD_PATH;

    protected Pager pager;

    protected Video video;

    protected User user;

    protected Favorite favorite;

    protected Share share;

    protected Comment comment;

    protected News news;

    protected Category category;

    protected Advertisement advertisement;

    @Autowired
    protected UserService userService;

    @Autowired
    protected VideoService videoService;

    @Autowired
    protected ConfigService configService;

    @Autowired
    protected FavoriteService favoriteService;

    @Autowired
    protected ShareService shareService;

    @Autowired
    protected CommentService commentService;

    @Autowired
    protected NewsService newsService;

    @Autowired
    protected CategoryService categoryService;

    @Autowired
    protected AdvertisementService advertisementService;

    protected Config getConfig(String key) {
        return configService.findByKey(key);
    }

    protected Map<String, String> getCategoryMap() {
        Map<String, String> categoryMap = new LinkedHashMap<>();
        List<Category> list = categoryService.findAllParent(0L);
        categoryMap.put("0", "请选择商品类型");
        for (Category category : list) {
            categoryMap.put(String.valueOf(category.getId()), category.getName());
        }
        return categoryMap;
    }

    public static String generateCode() {
        String code = "";
        for (int i = 0; i < 6; i++) {
            code += (int) (Math.random() * 9);
        }
        return " " + code + " ";
    }

}
