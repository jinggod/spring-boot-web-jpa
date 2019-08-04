package com.frontsurf.springwebjpa.common.utils.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created with Intellij IDEA
 * Author:  Banehallow
 * Date:    2019/4/8
 * Description：
 *  日期格式校验类
 **/
@Inherited
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE })
@Retention(RUNTIME)
@Constraint(validatedBy = CheckDateFormatValidator.class)
@Documented
public @interface CheckDateFormat {
    String message() default "{message.key}";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

    String pattern();
}
