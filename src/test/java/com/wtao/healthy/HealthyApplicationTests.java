package com.wtao.healthy;

import com.wtao.healthy.service.IUseDateService;
import com.wtao.healthy.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class HealthyApplicationTests {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUseDateService useDateService;

    @Test
    public void contextLoads() {


    }

}
