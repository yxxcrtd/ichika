package com.ichika.api;

import com.ichika.controller.BaseController;
import com.ichika.entity.Comment;
import com.ichika.entity.Share;
import com.ichika.entity.Video;
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
import org.springframework.web.multipart.MultipartFile;

import static com.ichika.utils.Constants.EMPTY;
import static com.ichika.utils.Constants.STATUS_0;
import static com.ichika.utils.Constants.STATUS_1;
import static com.ichika.utils.Constants.SUCCESS;
import static com.ichika.utils.Constants.USER_VIDEO_AUDIT;
import static com.ichika.utils.FileUtil.uploadFile;
import static com.ichika.utils.ResultMsg.resultSuccess;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Slf4j
@RestController
@RequestMapping("api/video")
public class VideoApi extends BaseController {

    /**
     * List
     */
    @ApiOperation("视频列表")
    @GetMapping("list/{page}/{row}")
    ResultMsg list(@PathVariable int page, @PathVariable int row) {
        Sort sort = Sort.by(new Sort.Order(DESC, "id"));
        Pageable pageable = PageRequest.of(page - 1, row, sort);
        Page<Video> result = videoService.findAllWithPage(pageable, "");
        return resultSuccess(SUCCESS, 0 < result.getSize() ? result : EMPTY);
    }

    /**
     * Save
     */
    @ApiOperation("视频保存 TODO")
    @PostMapping(value = "save", consumes = "multipart/*", headers = "Content-Type=multipart/form-data")
    ResultMsg save(Video video, MultipartFile file, MultipartFile picture) throws Exception {
        log.info("接口提交的视频对象：{}", video);
        log.info("接口提交的视频文件：{}", file);
        log.info("接口提交的视频截图文件：{}", picture);

        String fileName;
        if (null != file && !file.isEmpty()) {
            fileName = uploadFile(file, UPLOAD_PATH + "invoice/");
            log.info("上传的视频文件：{}", fileName);
            if (!"".equals(fileName) && null != fileName) {
                video.setFile(fileName);
            }
        }

        String pictureName;
        if (null != picture && !picture.isEmpty()) {
            pictureName = uploadFile(picture, UPLOAD_PATH + "invoice/");
            log.info("上传的视频截图文件：{}", pictureName);
            if (!"".equals(pictureName) && null != pictureName) {
                video.setPicture(pictureName);
            }
        }

        // 如果配置项为：0：不用审核直接上线，那个此时的视频状态是：1-已审核
        if (STATUS_0 == Integer.valueOf(getConfig(USER_VIDEO_AUDIT).getValue())) {
            video.setStatus(STATUS_1);
        } else {
            video.setStatus(STATUS_0);
        }

        return resultSuccess(SUCCESS, videoService.save(video));
    }

    /**
     * Delete
     */
    @ApiOperation("视频删除")
    @PostMapping("delete")
    ResultMsg delete(@RequestBody Long vid) {
        return videoService.delete(vid);
    }

    /**
     * Favorite
     */
    @ApiOperation("视频点赞/取消（type值：0-点赞；1-取消）")
    @PostMapping("favorite/{type}/{vid}/{uid}")
    ResultMsg favorite(@PathVariable int type, @PathVariable Long vid, @PathVariable Long uid) {
        return videoService.increase(type, vid, uid);
    }

    /**
     * Share
     */
    @ApiOperation("视频分享")
    @PostMapping("share")
    ResultMsg share(@RequestBody Share share) {
        log.info("接口提交的视频分享对象：{}", share);
        return videoService.share(share);
    }

    /**
     * Comment
     */
    @ApiOperation("视频评论")
    @PostMapping("comment")
    ResultMsg comment(@RequestBody Comment comment) {
        log.info("接口提交的视频评论对象：{}", comment);
        return videoService.comment(comment);
    }

}
