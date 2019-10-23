package com.ichika.service.impl;

import com.ichika.entity.Advertisement;
import com.ichika.repository.AdvertisementRepository;
import com.ichika.service.AdvertisementService;
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
public class AdvertisementServiceImpl implements AdvertisementService {

    @Resource
    private AdvertisementRepository advertisementRepository;

    public Advertisement findById(Long id) {
        return advertisementRepository.findById(id).get();
    }

    @Transactional
    public Advertisement save(Advertisement advertisement) {
        return advertisementRepository.save(advertisement);
    }

    public Page<Advertisement> findAllWithPage(Pageable pageable, String keyword, int status) {
        return advertisementRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> list = Lists.newArrayList();
            Path<String> title = root.get("title");
            Predicate titlePredicate = criteriaBuilder.like(title, "%" + keyword + "%");
            list.add(criteriaBuilder.or(titlePredicate));

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
            Advertisement advertisement = advertisementRepository.getOne(id);
            if (0 == advertisement.getStatus()) {
                advertisement.setStatus(1);
                status = "<span class=\"red\">下线</span>";
            } else {
                advertisement.setStatus(0);
                status = "<span class=\"green\">上线</span>";
            }
            advertisementRepository.save(advertisement);
            return resultSuccess(SUCCESS, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

}
