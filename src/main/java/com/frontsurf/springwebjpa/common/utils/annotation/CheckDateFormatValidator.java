package com.frontsurf.springwebjpa.common.utils.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with Intellij IDEA
 * Author:  Banehallow
 * Date:    2019/4/8
 **/
public class CheckDateFormatValidator implements ConstraintValidator<CheckDateFormat, String> {
    private String pattern;

    @Override
    public void initialize(CheckDateFormat constraintAnnotation) {
        this.pattern = constraintAnnotation.pattern();
    }

    @Override
    public boolean isValid(String object, ConstraintValidatorContext constraintContext) {
        if ( object == null ) {
            return true;
        }
        if (object.length() != pattern.length()) {
            return false;
        }
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            sdf.setLenient(false);
            Date date = sdf.parse(object);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
