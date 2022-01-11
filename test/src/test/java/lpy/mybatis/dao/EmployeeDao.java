package lpy.mybatis.dao;

import java.lang.Integer;
import java.util.List;
import lpy.mybatis.entity.Employee;

public interface EmployeeDao {
  int save(Employee entity);

  int saveBatch(List<Employee> entityList);

  int updateById(Employee entity);

  int deleteById(Integer id);

  int deleteByEntity(Employee entity);

  List<Employee> findAll();

  Employee findById(Integer id);

  List<Employee> findAllByProviderEntity(Employee entity);

  Employee findByOne(Employee entity);
}
