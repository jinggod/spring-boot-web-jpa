package com.frontsurf.springwebjpa.common.task;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/19 14:55
 * @Email xu.xiaojing@frontsurf.com
 * @Description 异步线程执行器, 所有异步执行的任务统一放此处，进行统一管理
 */

@Component
public class AsyncTaskExecutor {

    @Async
    public void aysncTask1(){
        //执行的内容...
    }
}
