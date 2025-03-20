//package com.essence;
//
//import com.essence.job.backjob.video.CameraStatusTask;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
///**
// * @author zhy
// * @since 2022/10/21 15:46
// */
//@Component
//public class WithRun implements ApplicationRunner {
//
//    @Autowired
//    private CameraStatusTask cameraStatusTask;
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // 启动项目执行一次定时任务
//        cameraStatusTask.syncCameraStatus();
//    }
//}