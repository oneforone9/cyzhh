package com.essence.job.backjob.video;

import com.essence.common.utils.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cameraStatusTask")
public class CameraStatusTaskController {

    @Autowired
    private CameraStatusTask cameraStatusTask;

    @GetMapping("/test")
    public ResponseResult TaskCameraStatusTask(@RequestParam String code) {
        cameraStatusTask.syncCameraStatus(code);
        return ResponseResult.success("执行成功", 200);
    }
}
