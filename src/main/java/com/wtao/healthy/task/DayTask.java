package com.wtao.healthy.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wtao.healthy.entity.User;
import com.wtao.healthy.service.IUseDateService;
import com.wtao.healthy.service.IUserService;
import com.wtao.healthy.util.SendMsgUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Configuration
@Slf4j
public class DayTask {

    @Autowired
    private IUserService userService;
    @Autowired
    private IUseDateService useDateService;

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    @Scheduled(cron = "0 0 9,19 * * ? ")
    public void dayTask() throws ParseException {
        String currentDay = DATE_FORMAT.format(new Date());
        log.info("当前时间是:{}", currentDay);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("id", "SELECT user_id FROM use_date WHERE use_date = '" + currentDay + "'");
        List<User> users = userService.list(queryWrapper);

        StringBuilder sb = new StringBuilder();
        sb.append("今天是：" + currentDay + "\r\n");
        if (users.size() > 0) {
            sb.append("需要提醒的人有:[");
            for (int i = 0; i < users.size(); i++) {
                if (i != 0) {
                    sb.append("、");
                }
                sb.append( users.get(i).getName());
            }
            sb.append("]");
        } else {
            sb.append("恭喜你！今天没有需要提醒的人。");
        }

        String msg = sb.toString();
        SendMsgUtil.mySend("18211036881", msg);
        // SendMsgUtil.mySend("13426194436", msg);
        log.info("已经发送短信,内容为:{}", msg);
    }

}
