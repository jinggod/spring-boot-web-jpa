package com.frontsurf.springwebjpa.service.user.impl;

import com.frontsurf.springwebjpa.common.base.repository.BaseRepository;
import com.frontsurf.springwebjpa.common.base.service.BaseServiceImpl;
import com.frontsurf.springwebjpa.common.utils.exception.DataException;
import com.frontsurf.springwebjpa.common.utils.jpa.JPAUtils;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryParam;
import com.frontsurf.springwebjpa.common.utils.jpa.QueryTypeEnum;
import com.frontsurf.springwebjpa.common.utils.web.ListRequestParam;
import com.frontsurf.springwebjpa.common.utils.web.Pager;
import com.frontsurf.springwebjpa.common.utils.web.Return;
import com.frontsurf.springwebjpa.dao.user.UserRepository;
import com.frontsurf.springwebjpa.domain.user.User;
import com.frontsurf.springwebjpa.service.user.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @Author xu.xiaojing
 * @Date 2019/6/24 1:35
 * @Email xu.xiaojing@frontsurf.com
 * @Description
 */

@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

    @Autowired
    UserRepository userRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public BaseRepository<User, String> getDao() {
        return userRepository;
    }

    @Override
    public User findByUsername(String userName) {
        return userRepository.findByUsername(userName);
    }

    @Override
    public Pager<User> listUserPager(ListRequestParam listRequestParam, User user) {

        Sort sort = null;
        if (listRequestParam.getOrderBy() != null) {
            Sort.Direction direction = Sort.Direction.ASC;
            if (listRequestParam.getDesc() != null && listRequestParam.getDesc() == 1) {
                direction = Sort.Direction.DESC;
            }
            switch (listRequestParam.getOrderBy()) {
                case 0:
                    sort = new Sort(direction, "username");
            }
        }
        //sort不能为null，或者不传
        PageRequest pageRequest;
        if (sort != null) {
            pageRequest = PageRequest.of(listRequestParam.getPageNum(), listRequestParam.getPageSize(), sort);
        } else {
            pageRequest = PageRequest.of(listRequestParam.getPageNum(), listRequestParam.getPageSize());
        }

        //模糊查询
        List<QueryParam> params = new LinkedList<>();
        if (user != null) {
            if (StringUtils.isNotBlank(user.getUsername())) {
                QueryParam param = new QueryParam("username", QueryTypeEnum.like, user.getUsername());
                params.add(param);
            }
        }
        Pager<User> list = this.listWithDefaultSort(params, pageRequest);
        //触发懒加载
        for (User item : list.getList()) {
            JPAUtils.lazyLoad(item, "roles");
        }
        return list;
    }

    @Override
    public User findByIdUseSql(String id) {

        String sql = "SELECT * FROM user WHERE id=?";
//        Query nativeQuery = entityManager.createNativeQuery(sql,User.class);
//        nativeQuery.unwrap(User.class);
//        List<User> resultList = nativeQuery.getResultList();
        User user = this.queryForObject(User.class, sql, id);
        List<User> users = this.queryForList(User.class, sql, id);
        Map<String, Object> map = this.queryForMap(sql, id);
        List<Map<String, Object>> maps = this.queryForListMap(sql, id);
        return user;
    }

    @Override
    public void saveUser(User user) throws DataException {
        User existUser = this.findByUsername(user.getUsername());
        if (existUser != null) {
            throw new DataException(Return.VALIDATION_ERROR, "新增失败，用户已存在");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("123456"));
        User save = this.save(user);
    }

    @Override
    public void updateUser(User user) throws DataException {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode("123456"));
        User existUser = this.findByUsername(user.getUsername());
        if (!existUser.getId().equals(user.getId())) {
            throw new DataException(Return.VALIDATION_ERROR, "更新失败，用户名已存在");
        }
        User save = this.save(user);
    }
}
