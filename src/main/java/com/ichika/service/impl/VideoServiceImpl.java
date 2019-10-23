package com.ichika.service.impl;

import com.ichika.entity.Comment;
import com.ichika.entity.Favorite;
import com.ichika.entity.Share;
import com.ichika.entity.Video;
import com.ichika.repository.CommentRepository;
import com.ichika.repository.FavoriteRepository;
import com.ichika.repository.ShareRepository;
import com.ichika.repository.VideoRepository;
import com.ichika.service.VideoService;
import com.ichika.utils.ResultMsg;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.List;

import static com.ichika.utils.Constants.FAIL;
import static com.ichika.utils.Constants.STATUS_2;
import static com.ichika.utils.Constants.SUCCESS;
import static com.ichika.utils.ResultMsg.resultFail;
import static com.ichika.utils.ResultMsg.resultSuccess;


@Slf4j
@Service
public class VideoServiceImpl implements VideoService {

    @Resource
    private VideoRepository videoRepository;

    @Resource
    private FavoriteRepository favoriteRepository;

    @Resource
    private ShareRepository shareRepository;

    @Resource
    private CommentRepository commentRepository;

    @Override
    public Video findById(Long id) {
        return videoRepository.getOne(id);
    }

    @Override
    public Video save(Video video) {
        return videoRepository.save(video);
    }

    @Override
    public ResultMsg delete(Long vid) {
        Video video = videoRepository.getOne(vid);

        try {
            video.setStatus(STATUS_2);
            videoRepository.save(video);

            return resultSuccess(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

    public Page<Video> findAllWithPage(Pageable pageable, String keyword) {
        return videoRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = Lists.newArrayList();
            Path<String> title = root.get("title");
            Predicate titlePredicate = criteriaBuilder.like(title, "%" + keyword + "%");
            list.add(criteriaBuilder.or(titlePredicate));
            criteriaQuery.where(list.toArray(new Predicate[]{}));
            return null;
        }, pageable);
    }

    @Override
    public ResultMsg increase(int type, Long vid, Long uid) {
        Video video = videoRepository.getOne(vid);

        try {
            // 1，计算视频的点赞数
            if (0 == type) {
                video.setFavorite(video.getFavorite() + 1);
            }
            if (1 == type) {
                video.setFavorite(video.getFavorite() - 1);
            }
            videoRepository.save(video);

            // 2，记录用户的点赞记录
            Favorite favorite = new Favorite();
            favorite.setVideoId(video.getId());
            favorite.setUserId(uid);
            favorite.setType(type);
            favoriteRepository.save(favorite);

            return resultSuccess(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

    @Override
    public ResultMsg share(Share share) {
        Video video = videoRepository.getOne(share.getVideoId());

        try {
            // 1，计算视频的分享数
            video.setShare(video.getShare() + 1);
            videoRepository.save(video);

            // 2，记录用户的分享记录
            shareRepository.save(share);

            return resultSuccess(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

    @Override
    public ResultMsg comment(Comment comment) {
        Video video = videoRepository.getOne(comment.getVideoId());

        try {
            // 1，计算视频的分享数
            video.setComment(video.getComment() + 1);
            videoRepository.save(video);

            // 2，记录用户的分享记录
            commentRepository.save(comment);

            return resultSuccess(SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

}
