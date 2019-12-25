package com.frontsurf.springwebjpa.service.demo;

import com.frontsurf.springwebjpa.common.utils.exception.DataException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/23 8:58
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface ExcelService {


    void importDevice(InputStream inputStream) throws DataException;

    void exportDevice(HttpServletResponse response) throws IOException;
}
