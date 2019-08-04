package com.frontsurf.springwebjpa.controller.demo;

import com.frontsurf.springwebjpa.common.utils.web.Return;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/15 7:34
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 *
 * Excel 导入导出的demo
 *
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {


    @GetMapping("/export")
    public void exprotUserExcel(){

    }

    @PostMapping("/import")
    public Return importUser(){

        return Return.success("导入成功");
    }

}
