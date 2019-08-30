package com.wtao.healthy.service.impl;

import com.wtao.healthy.entity.User;
import com.wtao.healthy.mapper.UserMapper;
import com.wtao.healthy.service.IUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author jobob
 * @since 2019-08-18
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

}
