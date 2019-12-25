### 项目介绍
 本项目是一个 spring boot jpa的demo项目，技术框架具体如下：
 
 #### 技术框架
 - 基础框架：Spring Boot 2.1.3.RELEASE
 
 - 持久层框架：JPA+Hibernate
 
 - 安全框架：Spring security
 
 - 数据库连接池：阿里巴巴Druid 1.1.10
 
 - 缓存框架：redis、Ehcache
 
 - 日志打印：log4j2
 
 - 监控：spring actuator、spring Boot admin
 
 - 其他：fastjson，poi，Swagger-ui，quartz, lombok（简化代码）等。
 
 #### 已实现的功能
 
 - JPA的基础框架搭建；
 - spring MVC的@Validated的校验demo；
 - log4j2日志框架整合；
 - spring security的整合
   - 自定义身份加载
   - 自定义资源服务器、权限决策器；
   - 开启了CSRF保护，每个POST请求(除登陆登出外，可配置)需要加 `_csrf`参数，或者另一个header参数；
   - 简单的错误次数达到阈值后，进行账号封锁；
   - 登陆时，使用RSA公钥加密登陆密码，身份验证前使用 RSA的私钥解密登陆密码。
 - 异常统一捕获拦截器
 - 应用程序监控：spring actuator + spring Boot admin
 - 整合Redis作为分布式缓存，且配置两个Redis Bean，用于不同的场景；
 - 配置Spring的缓存（使用Ehcahe、redis、GuavaCache）（未做）
 - 整合Ehcache做为查询的二级缓存(未做);
 - 测试框架的搭建以及使用内存数据库（未做）；
 - 其他常用模块的实现：
   - 切面编程的系统日志`@AutoLog`;
   - 消息中心的设计和实现（未做）；
   - EasyPoi的demo（未做）；
 
 #### 目录规范说明
 
 
 
 #### 接口说明
 
 - 登陆: 
   - URL: /login
   - 请求方法：post请求
   - 参数：username、password
 - 登出：
   - URL: /logout
   - 请求方法：post请求
 
 #### 冗余实现的模块（可选，多选一）
 - spring cache
 - spring 线程池配置 
 
 
 
 #### JPA的动态sql的写法介绍
 
 **描述**：
 
 JPA在能解决掉80%~90%的sql，这些sql相对来说，逻辑简单，但面对 多表连查有点不足；
 
 最难以处理的是 多表查询 + 动态sql（约占 5%~10%），目前的看了网上的解决方案，有四种：
 
 - EntityManager：JPA的类，应该可以使用JPA的一些特性（像二级缓存等），优先考虑此方案；
 - JdbcTemplate：在复杂的sql不多的情况下，可以考虑，而且也比较轻量。（https://billykorando.com/2019/05/06/jpa-or-sql-in-a-spring-boot-application-why-not-both）
 - QueryDSL：QueryDSL仅仅是一个通用的查询框架，专注于通过Java API构建类型安全的SQL查询。适用于自定义sql场景比较复杂、比较多的情况下：https://blog.csdn.net/qq_30054997/article/details/79420141
 - JPA+mybatis：这种方式可能会有坑，两个ORM框架，怎么感觉都有点重，这个几乎不可能考虑；

**解决方案**：

1. `BaseRepoService` 提供了简单的动态查询的方法，满足大部分的情况；
2. `BaseService` 继承了`BaseRepoService`接口，并提供了 原生sql的接口，可以满足所有情况。



JPA sql的使用介绍：
https://www.petrikainulainen.net/programming/spring-framework/spring-data-jpa-tutorial-three-custom-queries-with-query-methods/



### 问题总结：

- 在JPA中，所有的查询所需的字段 都是指 实体类的属性，即在JPA不使用数据库字段；
- 遇到 @OneToOne,OneToMany 查询出异常时，第一个要排查的是关联关系是否正确；
- 懒加载时，有时候使用需要使用 @Transication 注解一个事务，才可以调用懒加载属性


@ManyToOne、@OnetoOne 默认都是 饿加载。使用这两个关系，在JPA看来是个子表，所以如果这个关系为空，即子表存在某条记录没有关联到主表上，就会抛出 `EntityNotFoundException`异常，但业务上又允许，此时 可以使用 ` @NotFound(action=NotFoundAction.IGNORE)` 来不进行非空校验， 注意`@NotFound`强制使用饿加载；
@OneToMany、@ManyToMany，默认都是 懒加载




后来发现问题是因为设置了Cascade.All，当执行save的时候，会调用onPersist()方法，这个方法会递归调用外联类（即Role）的onPersist（）进行级联新增，但是roles已经添加了，因此报detached entity passed to persist。后来我将级联操作取消就ok了（其实只要将cascadeType.persist去掉就可以）。




    

```java 
//自关联的问题：触发子查询的懒加载时，会将此类下发到子查询结果的每条数据中的 parent字段，作为父级，从而导致序列化时的死循环,使用@JsonIgnoreProperties来中断序列化
 @JsonIgnoreProperties(value = {"childCourseType"})
    @ManyToOne(cascade = {CascadeType.MERGE,CascadeType.DETACH,CascadeType.REFRESH},fetch = FetchType.LAZY,optional = true)
    @JoinColumn(name = "parent_id")
    public JtyCourseType getParentCourseType() {
        return parentCourseType;
    }
```

 
 @JsonGetter(value = "transientProperty")



**遇到的问题**:

1. 【spring data jpa】报错如下：Caused by: javax.persistence.EntityNotFoundException: Unable to find com.rollong.chinatower.server.persistence.entity.staff.Department with id 0

原因：外键找不到就抛异常
解决方案：添加此注解 `@NotFound(action=NotFoundAction.IGNORE)`


2. easypoi 有不少BUG，出BUG，记得去看其issue（https://gitee.com/lemur/easypoi/issues）
 
- 遇到过的问题：Set类型的属性，导入时无法初始化，会导致 NullPointExecption，所以需要预先初始化。List类型的不用
- easypoi 导入时无法判断空字符串行、如果存在集合，最后一行是1对1的就会莫名其妙的出现多行。这些都需要手动处理；
- 与easypoi类似的，阿里团队也开源了一个easyexcel 工具。easyexcel 主要解决大数据量导入的内存占用大的问题（有ehcache组件），额外支持简单的注解导入，特性远没有easypoi多，但easypoi是没有解决大数据量导入的问题。
 
 
