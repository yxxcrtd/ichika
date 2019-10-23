package com.ichika.service;

import com.ichika.entity.News;
import com.ichika.utils.ResultMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface NewsService {

    News findById(Long id);

    News save(News news);

    Page<News> findAllWithPage(Pageable pageable, String keyword, int status);

    ResultMsg status(Long id);

}
