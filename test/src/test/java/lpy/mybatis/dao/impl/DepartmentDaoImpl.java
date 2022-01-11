package lpy.mybatis.dao.impl;

import java.lang.Integer;
import java.lang.Override;
import java.util.List;
import lpy.mybatis.dao.DepartmentDao;
import lpy.mybatis.entity.Department;
import lpy.mybatis.mapper.DepartmentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDaoImpl implements DepartmentDao {
  @Autowired
  private DepartmentMapper mapper;

  @Override
  public int save(Department entity) {
    return mapper.save(entity);
  }

  @Override
  public int saveBatch(List<Department> entityList) {
    return mapper.saveBatch(entityList);
  }

  @Override
  public int updateById(Department entity) {
    return mapper.updateById(entity);
  }

  @Override
  public int deleteById(Integer id) {
    Department entity = new Department();
    entity.setId(id);
    return mapper.deleteById(entity);
  }

  @Override
  public int deleteByEntity(Department entity) {
    return mapper.deleteByEntity(entity);
  }

  @Override
  public List<Department> findAll() {
    return mapper.findAll(Department.class);
  }

  @Override
  public Department findById(Integer id) {
    Department entity = new Department();
    entity.setId(id);
    return mapper.findById(entity);
  }

  @Override
  public List<Department> findAllByProviderEntity(Department entity) {
    return mapper.findAllByProviderEntity(entity);
  }

  @Override
  public Department findByOne(Department entity) {
    return mapper.findByOne(entity);
  }
}
