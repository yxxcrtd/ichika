package com.ichika.service.impl;

import com.ichika.entity.Merchant;
import com.ichika.repository.MerchantRepository;
import com.ichika.service.MerchantService;
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
public class MerchantServiceImpl implements MerchantService {

    @Resource
    private MerchantRepository merchantRepository;

    public Merchant findById(Long id) {
        return merchantRepository.findById(id).get();
    }

    @Transactional
    public Merchant save(Merchant merchant) {
        return merchantRepository.save(merchant);
    }

    public Page<Merchant> findAllWithPage(Pageable pageable, String keyword, int status) {
        return merchantRepository.findAll((root, criteriaQuery, criteriaBuilder) -> {
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
            Merchant merchant = merchantRepository.getOne(id);
            if (0 == merchant.getStatus()) {
                merchant.setStatus(1);
                status = "<span class=\"red\">下线</span>";
            } else {
                merchant.setStatus(0);
                status = "<span class=\"green\">上线</span>";
            }
            merchantRepository.save(merchant);
            return resultSuccess(SUCCESS, status);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultFail(FAIL);
    }

}
