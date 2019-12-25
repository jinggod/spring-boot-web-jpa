package com.frontsurf.springwebjpa.service.demo.impl;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.frontsurf.springwebjpa.common.utils.ExcelUtils;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.JPAUtils;
import com.frontsurf.springwebjpa.common.validate.Import;
import com.frontsurf.springwebjpa.dao.excel.DeviceRepository;
import com.frontsurf.springwebjpa.dao.excel.SchoolRepository;
import com.frontsurf.springwebjpa.dao.excel.StudentRepository;
import com.frontsurf.springwebjpa.domain.excel.Device;
import com.frontsurf.springwebjpa.domain.excel.School;
import com.frontsurf.springwebjpa.domain.excel.Student;
import com.frontsurf.springwebjpa.service.demo.ExcelService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.frontsurf.springwebjpa.common.utils.web.Return.VALIDATION_ERROR;

/**
 * @Author xu.xiaojing
 * @Date 2019/10/23 8:59
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    DeviceRepository deviceRepository;

    @Autowired
    SchoolRepository schoolRepository;

    @Autowired
    StudentRepository studentRepository;

    @Override
    public void importDevice(InputStream inputStream) throws DataException {
        ImportParams params = new ImportParams();
        //开启valid数据校验
        params.setNeedVerfiy(true);
        params.setHeadRows(2);
        params.setVerfiyGroup(new Class[]{Import.class});
        List<Device> devices = ExcelUtils.importFile(Device.class, inputStream, params);
        if (devices == null || devices.size() == 0) {
            throw new DataException("导入数据为空！");
        }
        // todo 录入数据判断：录入的数据里面是否包含重复的 设备编码、学生的学号，此处为了省事，就不写了

        // todo 额外起个方法去定义事务，减少事务的粒度（好像不对，就不知道事务的起点）
        this.batchAddDevice(devices);
    }


    @Override
    public void exportDevice(HttpServletResponse response) throws IOException {

        List<Device> devices = deviceRepository.findAll();
        for (Device device : devices) {
            JPAUtils.lazyLoad(device,"students");
        }
        response.setHeader("content-Type","application/vnd.ms-excel");
        response.setHeader("Contene-Disposition","attachment;filename=User.xls");
        Workbook workbook = ExcelExportUtil.exportBigExcel(new ExportParams("计算机一班学生", "学生"), Device.class, devices);
        workbook.write(response.getOutputStream());
    }


    @Transactional(rollbackFor = Exception.class, isolation = Isolation.REPEATABLE_READ, propagation = Propagation.REQUIRED)
    void batchAddDevice(List<Device> devices) throws DataException {
        //与数据库的数进行对比，判断设备编码是否已存在，判断学生是否存在,判断学校是否存在
        for (Device device : devices) {
            //判断设备编码是否已存在
            Device existDevice = deviceRepository.findByCode(device.getCode());
            if (existDevice != null)
                throw new DataException(VALIDATION_ERROR,"导入失败，设备编码已存在");
           //判断学校是否存在
            School existSchool = schoolRepository.findByName(device.getSchool().getName());
            if(existSchool == null){
                throw new DataException(VALIDATION_ERROR,"导入失败，学校["+device.getSchool().getName()+"]不存在");
            }
            device.setSchool(existSchool);
            //判断学生是否存在
            Set<Student> existStudents = new HashSet<>();
            for (Student stu : device.getStudents()) {
                Student existStudent = studentRepository.findByStudentNum(stu.getStudentNum());
                if(existStudent == null){
                    throw new DataException(VALIDATION_ERROR,"学生不存在，学生学号["+stu.getStudentNum()+"]");
                }
                existStudents.add(existStudent);
            }
            device.setStudents(existStudents);
          //保存设备，以及  学生-设备关系
            deviceRepository.save(device);
        }
    }
}
