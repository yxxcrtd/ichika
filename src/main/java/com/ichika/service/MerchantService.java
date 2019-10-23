package com.ichika.service;

import com.ichika.entity.Merchant;
import com.ichika.utils.ResultMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface MerchantService {

    Merchant findById(Long id);

    Merchant save(Merchant merchant);

    Page<Merchant> findAllWithPage(Pageable pageable, String keyword, int status);

    ResultMsg status(Long id);

}
