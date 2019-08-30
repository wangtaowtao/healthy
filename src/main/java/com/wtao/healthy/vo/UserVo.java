package com.wtao.healthy.vo;

import com.wtao.healthy.entity.UseDate;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class UserVo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;
    private String name;
    private Date createDate;
    private String demo;

    private Date nextUseDate;

}
