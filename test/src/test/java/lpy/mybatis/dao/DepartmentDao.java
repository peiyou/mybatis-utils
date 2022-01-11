package lpy.mybatis.dao;

import java.lang.Integer;
import java.util.List;
import lpy.mybatis.entity.Department;

public interface DepartmentDao {
  int save(Department entity);

  int saveBatch(List<Department> entityList);

  int updateById(Department entity);

  int deleteById(Integer id);

  int deleteByEntity(Department entity);

  List<Department> findAll();

  Department findById(Integer id);

  List<Department> findAllByProviderEntity(Department entity);

  Department findByOne(Department entity);
}
