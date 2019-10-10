package com.wtao.healthy.service.impl;

import com.wtao.healthy.entity.Admin;
import com.wtao.healthy.mapper.AdminMapper;
import com.wtao.healthy.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-10-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
