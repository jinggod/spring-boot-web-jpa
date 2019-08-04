package com.frontsurf.springwebjpa.common.utils;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.excel.entity.result.ExcelImportResult;
import cn.afterturn.easypoi.exception.excel.ExcelImportException;
import com.frontsurf.springwebjpa.common.base.entity.DataEntity;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/7/15 8:57
 * @Email xu.xiaojing@frontsurf.com
 * @Description Excel工具类
 */

public class ExcelUtils {

    static Logger logger = LoggerFactory.getLogger(FileUtils.class);

    /**
     * Easypoi Excel导入的 异常情况统一处理工具类
     *
     * @param tClass
     * @param path
     * @param params
     * @param <T>
     * @return
     * @throws DataException
     */
    public static <T extends DataEntity> List<T> importFile(Class<T> tClass, String path, ImportParams params) throws DataException {

        ExcelImportResult<T> result = null;
        try {
            result = ExcelImportUtil.importExcelMore(new File(path), tClass, params);
        } catch (ExcelImportException e) {
            e.printStackTrace();
            logger.error("导入文件内容格式错误", e);
            throw new DataException(Return.VALIDATION_ERROR, "导入文件内容格式错误");
        } catch (Exception e) {
            logger.error("导入失败", e);
            throw new DataException(Return.VALIDATION_ERROR, "导入失败");
        }
        if (result.isVerfiyFail()) {
            StringBuilder builder = new StringBuilder();
            builder.append("数据异常，出错的行号以及错误信息如下：");
            for (T t : result.getFailList()) {
                builder.append("第" + t.getRowNum() + "行[" + t.getErrorMsg() + "]");
            }
            throw new DataException(Return.VALIDATION_ERROR, builder.toString());
        }
        return result.getList();
    }

    /**
     * Easypoi Excel导入的 异常情况统一处理工具类
     *
     * @param tClass
     * @param inputStream 输入流
     * @param params
     * @param <T>
     * @return
     * @throws DataException
     */
    public static <T extends DataEntity> List<T> importFile(Class<T> tClass, InputStream inputStream, ImportParams params) throws DataException {

        ExcelImportResult<T> result = null;
        try {
            result = ExcelImportUtil.importExcelMore(inputStream, tClass, params);
        } catch (ExcelImportException e) {
            e.printStackTrace();
            logger.error("导入文件内容格式错误", e);
            throw new DataException(Return.VALIDATION_ERROR, "导入文件内容格式错误");
        } catch (Exception e) {
            logger.error("导入失败", e);
            throw new DataException(Return.VALIDATION_ERROR, "导入失败");
        }
        if (result.isVerfiyFail()) {
            StringBuilder builder = new StringBuilder();
            builder.append("数据异常，出错的行号以及错误信息如下：");
            for (T t : result.getFailList()) {
                builder.append("第" + t.getRowNum() + "行[" + t.getErrorMsg() + "]");
            }
            throw new DataException(Return.VALIDATION_ERROR, builder.toString());
        }
        return result.getList();
    }
}
