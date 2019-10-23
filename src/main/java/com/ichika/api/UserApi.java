package com.ichika.api;

import com.ichika.controller.BaseController;
import com.ichika.entity.News;
import com.ichika.utils.ResultMsg;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ichika.utils.Constants.EMPTY;
import static com.ichika.utils.Constants.SUCCESS;
import static com.ichika.utils.ResultMsg.resultSuccess;
import static org.springframework.data.domain.Sort.Direction.DESC;


@Slf4j
@RestController
@RequestMapping("api/user")
public class UserApi extends BaseController {

    /**
     * List
     */
    @ApiOperation("用户列表")
    @GetMapping("list/{page}/{row}")
    ResultMsg list(@PathVariable int page, @PathVariable int row) {
        Sort sort = Sort.by(new Sort.Order(DESC, "id"));
        Pageable pageable = PageRequest.of(page - 1, row, sort);
        Page<News> result = newsService.findAllWithPage(pageable, "", 0);
        return resultSuccess(SUCCESS, 0 < result.getSize() ? result : EMPTY);
    }

    /**
     * Save
     */
    @ApiOperation("用户注册")
    @PostMapping("save")
    ResultMsg save(@RequestBody News news) {
        log.info("接口提交的用户对象：{}", news);
        return resultSuccess(SUCCESS, newsService.save(news));
    }

    /**
     * Detail
     */
    @ApiOperation("用户详情")
    @GetMapping("detail/{id}")
    ResultMsg detail(@PathVariable Long id) {
        news = newsService.findById(id);
        return resultSuccess(SUCCESS, null == news ? EMPTY : news);
    }

}
