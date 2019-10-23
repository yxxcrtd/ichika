package com.ichika.api;

import com.ichika.controller.BaseController;
import com.ichika.utils.ResultMsg;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.ichika.utils.Constants.EMPTY;
import static com.ichika.utils.Constants.SUCCESS;
import static com.ichika.utils.ResultMsg.resultSuccess;


@Slf4j
@RestController
@RequestMapping("api/advertisement")
public class AdvertisementApi extends BaseController {

    /**
     * Detail
     */
    @ApiOperation("广告详情")
    @GetMapping("detail/{id}")
    ResultMsg detail(@PathVariable Long id) {
        advertisement = advertisementService.findById(id);
        return resultSuccess(SUCCESS, null == advertisement ? EMPTY : advertisement);
    }

}
