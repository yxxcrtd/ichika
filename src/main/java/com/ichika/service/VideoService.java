package com.ichika.service;

import com.ichika.entity.Comment;
import com.ichika.entity.Share;
import com.ichika.entity.Video;
import com.ichika.utils.ResultMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface VideoService {

    Video findById(Long id);

    Video save(Video video);

    ResultMsg delete(Long vid);

    Page<Video> findAllWithPage(Pageable pageable, String keyword);

    ResultMsg increase(int type, Long vid, Long uid);

    ResultMsg share(Share share);

    ResultMsg comment(Comment comment);

}
