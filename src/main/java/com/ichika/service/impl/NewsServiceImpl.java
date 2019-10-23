package com.ichika.service.impl;

import com.ichika.entity.News;
import com.ichika.repository.NewsRepository;
import com.ichika.service.NewsService;
import com.ichika.utils.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.List;

import static com.ichika.utils.Constants.FAIL;
import static com.ichika.utils.Constants.SUCCESS;
import static com.ichika.utils.ResultMsg.resultFail;
import static com.ichika.utils.ResultMsg.resultSuccess;


@Slf4j
@Service
public class NewsServiceImpl implements NewsService {

    @Resource
    private NewsRepository newsRepository;

    public News findById(Long id) {
        return newsRepository.findById(id).get();
    }

    @Transactional
    public News save(News news) {
        return newsRepository.save(news);
    }

    public Page<News> findAllWithPage(Pageable pageable, String keyword, int status) {
        return newsRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = Lists.newArrayList();
            Path<String> title = root.get("title");
            Path<String> content = root.get("content");
            Predicate titlePredicate = criteriaBuilder.like(title, "%" + keyword + "%");
            Predicate contentPredicate = criteriaBuilder.like(content, "%" + keyword + "%");
            list.add(criteriaBuilder.or(titlePredicate, contentPredicate));

            if (0 == status) {
                Path<Integer> statusPath = root.get("status");
                Predicate statusPredicate = criteriaBuilder.equal(statusPath, status);
                list.add(criteriaBuilder.and(statusPredicate));
            }

            criteriaQuery.where(list.toArray(new Predicate[]{}));
            return null;
        }, pageable);
    }

    public ResultMsg status(Long id) {
        try {
            String status = "";
            News news = newsRepository.getOne(id);
            if (0 == news.getStatus()) {
                news.setStatus(1);
                status = "<span class=\"red\">下线</span>";
            } else {
                news.setStatus(0);
                status = "<span class=\"green\">上线</span>";
            }
            newsRepository.save(news);
            return resultSuccess(SUCCESS, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

}
