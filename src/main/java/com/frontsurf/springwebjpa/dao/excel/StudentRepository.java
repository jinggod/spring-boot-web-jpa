package com.frontsurf.springwebjpa.dao.excel;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.domain.excel.Student;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/24 15:55
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

public interface StudentRepository extends BaseRepository<Student,String> {
    Student findByStudentNum(String studentNum);
}
