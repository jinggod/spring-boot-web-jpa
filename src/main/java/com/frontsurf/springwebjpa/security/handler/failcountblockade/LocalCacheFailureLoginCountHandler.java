package com.frontsurf.springwebjpa.security.handler.failcountblockade;

import com.frontsurf.springwebjpa.security.exception.AutenticationExecptionType;
import com.frontsurf.springwebjpa.security.exception.CustomAutenticationExecption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/20 23:31
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Component
public class LocalCacheFailureLoginCountHandler extends FailureLoginCountHandler {

    //todo 改成缓存架构，方便设置过期时间
    private Map<String, FailureRecord> map = new HashMap<>();


    /**
     * 登陆失败次数的最大阈值
     */
    @Value("${login.failure.threshold}")
    private int THRESHOLD = 5;

    /**
     * 锁住时间,10分钟
     */
    @Value("${login.failure.locktime}")
    private int LOCKTIME = 30 * 1000;


    @Override
    public void isAccountLocked(String userName) throws CustomAutenticationExecption {
        FailureRecord failureRecord = map.get(userName);
        if (failureRecord != null) {
            if (failureRecord.getCount() >= THRESHOLD) {
                long time = System.currentTimeMillis() - failureRecord.getFailureTimeMillis();
                if (time <= LOCKTIME) {
                    time = Math.round((LOCKTIME - time) / 1000);

                    if (time >= 60) {
                        time = Math.round(time / 60);
                        throw new UsernameNotFoundException(String.format("您已经连续登陆失败超过%d次，账号已被锁住，请%d分钟后再尝试", failureRecord.getCount(), time));
                    }
                    time = time == 0 ? 1 : time;
                    throw new CustomAutenticationExecption(AutenticationExecptionType.FAIL_LOGIN_EXCEED_THRESHOLD, String.format("您已经连续登陆失败超过%d次，账号已被锁住，请%d秒后再尝试", failureRecord.getCount(), time));
                } else {
                    //账号锁住时间已经过去，移除锁住状态信息。
                    map.remove(userName);
                }
            }
        }
    }

    /**
     * 登陆失败的处理
     *
     * @param userName
     */
    @Override
    public void failLogin(String userName) {
        FailureRecord failureRecord = map.get(userName);
        if (failureRecord == null) {
            failureRecord = new FailureRecord();
            map.put(userName, failureRecord);
        }

        //达到阈值，开始计算账号的锁住时间
        if (failureRecord.getCount() == THRESHOLD - 1) {
            failureRecord.setFailureTimeMillis(System.currentTimeMillis());
        }
        if (failureRecord.getCount() < THRESHOLD) {
            failureRecord.setCount(failureRecord.getCount() + 1);
        }
    }

    /**
     * 登陆成功的处理
     *
     * @param userName
     */
    @Override
    public void successLogin(String userName) {
        map.remove(userName);
    }

    private static class FailureRecord {
        /**
         * 失败次数
         */
        private int count;

        /**
         * 账号锁住的开始时刻
         */
        private long failureTimeMillis;

        public FailureRecord() {
            this.count = 0;
            this.failureTimeMillis = System.currentTimeMillis();
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public long getFailureTimeMillis() {
            return failureTimeMillis;
        }

        public void setFailureTimeMillis(long failureTimeMillis) {
            this.failureTimeMillis = failureTimeMillis;
        }
    }
}
