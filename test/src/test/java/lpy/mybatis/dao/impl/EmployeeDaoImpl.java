package lpy.mybatis.dao.impl;

import java.lang.Integer;
import java.lang.Override;
import java.util.List;
import lpy.mybatis.dao.EmployeeDao;
import lpy.mybatis.entity.Employee;
import lpy.mybatis.mapper.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeDaoImpl implements EmployeeDao {
  @Autowired
  private EmployeeMapper mapper;

  @Override
  public int save(Employee entity) {
    return mapper.save(entity);
  }

  @Override
  public int saveBatch(List<Employee> entityList) {
    return mapper.saveBatch(entityList);
  }

  @Override
  public int updateById(Employee entity) {
    return mapper.updateById(entity);
  }

  @Override
  public int deleteById(Integer id) {
    Employee entity = new Employee();
    entity.setId(id);
    return mapper.deleteById(entity);
  }

  @Override
  public int deleteByEntity(Employee entity) {
    return mapper.deleteByEntity(entity);
  }

  @Override
  public List<Employee> findAll() {
    return mapper.findAll(Employee.class);
  }

  @Override
  public Employee findById(Integer id) {
    Employee entity = new Employee();
    entity.setId(id);
    return mapper.findById(entity);
  }

  @Override
  public List<Employee> findAllByProviderEntity(Employee entity) {
    return mapper.findAllByProviderEntity(entity);
  }

  @Override
  public Employee findByOne(Employee entity) {
    return mapper.findByOne(entity);
  }
}
