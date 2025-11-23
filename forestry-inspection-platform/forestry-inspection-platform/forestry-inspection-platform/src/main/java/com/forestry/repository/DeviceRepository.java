package com.forestry.repository;

import com.forestry.model.Device;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * MongoDB设备数据访问  DeviceRepository继承了MongoRepository<Device, String>接口，
 * 该接口内置了这些通用 CRUD 方法的默认实现
 */
//标识数据访问层（DAO/Repository）组件,标注@Repository后，Spring 会自动创建其实现类的代理对象
    //将其纳入 Spring 容器管理，成为可依赖注入的 Bean。
    //Bean 就是 Spring 容器创建、管理、装配的对象
    //Bean 之间通过依赖注入协作，无需硬编码依赖关系
// （如DeviceService无需手动new DeviceRepository()，只需@Autowired注入）；
@Repository
public interface DeviceRepository extends MongoRepository<Device, String> {

    //按照特定规则命名方法（如 findBy[字段名]），框架就会自动解析方法名
    List<Device> findByStatus(String status);

    List<Device> findByType(String type);
}