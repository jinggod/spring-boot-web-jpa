package com.frontsurf.springwebjpa.controller.demo;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.domain.excel.School;
import com.frontsurf.springwebjpa.service.demo.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/15 7:34
 * @Email xu.xiaojing@frontsurf.com
 * @Description Excel 导入导出的demo
 *
 * 这里只是两个例子，更多的导入导出参考官方文档：
 * http://easypoi.mydoc.io/#text_202975
 *
 */
@RestController
@RequestMapping("/excel")
public class ExcelController {

    @Autowired
    ExcelService excelService;

    @GetMapping("/export")
    public void exportDevice(HttpServletResponse response) throws IOException {
        excelService.exportDevice(response);
    }

    /**
     * 导入设备,场景相对复杂，有onetoone、onetomany的场景
     *
     * @return
     */
    @PostMapping("/import")
    public Return importDevice(@RequestParam(name = "file") MultipartFile file) throws IOException, DataException {
        excelService.importDevice(file.getInputStream());
        return Return.success("导入成功");
    }

    @PostMapping("/import_school")
    public Return importSchool(@RequestParam(name = "file") MultipartFile file) throws IOException {
        return Return.success("导入成功");
    }

}
