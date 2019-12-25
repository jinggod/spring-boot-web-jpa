package com.frontsurf.springwebjpa.common.utils.exception;


import com.frontsurf.springwebjpa.common.utils.web.Return;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.List;

/**
 * @Author xu.xiaojing
 * @Date 2019/3/27 16:24
 * @Email xu.xiaojing@frontsurf.com
 * @Description 全局异常捕获处理
 */
@RestControllerAdvice
public class CommonExceptionHandler {

    Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

    /**
     * 处理请求对象属性不满足校验规则的异常信息
     *
     * @param exception
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = BindException.class)
    public Return bindException(BindException exception) {
        exception.printStackTrace();
        BindingResult result = exception.getBindingResult();
        final List<ObjectError> objectErrors = result.getAllErrors();
        StringBuilder builder = new StringBuilder();
        builder.append("参数校验异常：");
//        for (FieldError error : fieldErrors) {
//            builder.append(error.getDefaultMessage() + "\n");
//        }
        builder.append(objectErrors.get(objectErrors.size()-1).getDefaultMessage());
        return Return.fail(400, builder.toString());
    }

//    BeanPropertyBindingResult

    /**
     * 捕获参数缺失异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public Return missingRequestParamException(MissingServletRequestParameterException exception) {

        exception.printStackTrace();
        return Return.fail("请求失败，缺少参数：" + exception.getMessage());
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public Return constraintViolationException(ConstraintViolationException exception) {
        exception.printStackTrace();
        StringBuilder builder = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : exception.getConstraintViolations()) {
            builder.append(constraintViolation.getMessage() + "\n");
        }
        return Return.fail(400, builder.toString());
    }

    /**
     * 捕获提交的参数的类型不匹配异常
     *
     * @return
     */
    @ExceptionHandler(value = {MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class})
    public Return handleArgumentTypeMismatchException(Exception exception) {
        exception.printStackTrace();

        return Return.fail("请求失败，参数类型不匹配：" + exception.getMessage());
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    @ResponseStatus(HttpStatus.BAD_REQUEST) 将会是400
    public Return handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        StringBuilder message = new StringBuilder();
        List<FieldError> list = exception.getBindingResult().getFieldErrors();
        for (FieldError fieldError : list) {
            message.append(fieldError.getDefaultMessage() + "、");
        }
        message.deleteCharAt(message.length() - 1);
        return Return.fail(message.toString());
    }


    /**
     * 处理业务逻辑上的异常
     */
    @ExceptionHandler(value = DataException.class)
    public Return hanleDataException(DataException exception) {
        exception.printStackTrace();
        if(exception.getDetail() == null){
            return Return.fail(exception.getCode(),  exception.getMessage());
        }else{
            return Return.fail(exception.getCode(),exception.getErrorMessage(),exception.getDetail());
        }
    }

    /**
     * 处理其他异常
     *
     * @param exception
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    public Return exception(Exception exception) {
        exception.printStackTrace();
        if (exception.getCause() instanceof DataException) {
            return Return.fail("数据异常，请联系管理员，错误提示：" + ((DataException) exception.getCause()).getErrorMessage());
        }
        return Return.fail(500, "服务端异常：" + exception.getMessage());
    }


//    @ExceptionHandler(value = NumberFormatException.class)
//    public Return NumberFormatException(NumberFormatException exception) {
//        return Return.fail(1, exception.toString());
//    }
}
