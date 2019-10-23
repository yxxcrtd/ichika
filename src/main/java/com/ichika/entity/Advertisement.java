package com.ichika.entity;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.Date;


@Data
@Entity
@Table(name = "t_advertisement")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "请输入标题！")
    private String title;

    private String picture;

    @NotBlank(message = "请输入链接！")
    private String link;

    private int hits;

    private int status;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime = new Date();

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    private String phone;

}
