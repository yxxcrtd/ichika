package com.ichika.service;

import com.ichika.entity.Advertisement;
import com.ichika.utils.ResultMsg;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AdvertisementService {

    Advertisement findById(Long id);

    Advertisement save(Advertisement advertisement);

    Page<Advertisement> findAllWithPage(Pageable pageable, String keyword, int status);

    ResultMsg status(Long id);

}
