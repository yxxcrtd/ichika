package com.ichika.api;

import com.ichika.controller.BaseController;
import com.ichika.entity.Category;
import com.ichika.utils.ResultMsg;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.ichika.utils.Constants.SUCCESS;
import static com.ichika.utils.ResultMsg.resultSuccess;
import static org.springframework.data.domain.Sort.Direction.ASC;


@Slf4j
@RestController
@RequestMapping("api/category")
public class CategoryApi extends BaseController {

    /**
     * List
     */
    @ApiOperation("分类列表")
    @GetMapping("")
    ResultMsg list() {
        Sort sort = Sort.by(new Sort.Order(ASC, "id"));
        List<Category> categoryList = categoryService.findAll(sort);
        List<Map<String, Object>> list = new ArrayList<>();
        List<Map<String, Object>> childList = new ArrayList<>();
        Map<String, Object> map;
        Map<String, Object> childMap;

        for (Category c : categoryList) {
            if (0 == c.getParentId()) {
                map = new LinkedHashMap<>();
                map.put("id", c.getId());
                map.put("parentId", c.getParentId());
                map.put("name", c.getName());
                list.add(map);
            } else {
                map = new LinkedHashMap<>();
                map.put("id", c.getId());
                map.put("parentId", c.getParentId());
                map.put("name", c.getName());
                childList.add(map);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            List<Map<String, Object>> childTempList = new ArrayList<>();
            for (int j = 0; j < childList.size(); j++) {
                if (list.get(i).get("id") == childList.get(j).get("parentId")) {
                    childMap = new LinkedHashMap<>();
                    childMap.put("id", childList.get(j).get("id"));
                    childMap.put("parentId", childList.get(j).get("parentId"));
                    childMap.put("name", childList.get(j).get("name"));
                    childTempList.add(childMap);
                }
            }
            list.get(i).put("child", childTempList);
        }
        return resultSuccess(SUCCESS, list);
    }

}
