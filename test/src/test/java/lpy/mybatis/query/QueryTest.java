package lpy.mybatis.query;

import lpy.mybatis.Application;
import lpy.mybatis.dao.DepartmentDao;
import lpy.mybatis.dao.EmployeeDao;
import lpy.mybatis.entity.Employee;
import lpy.mybatis.entity.helper.DepartmentHelper;
import lpy.mybatis.entity.helper.EmployeeHelper;
import lpy.mybatis.mapper.EmployeeMapper;
import lpy.mybatis.sqlbuilder.builder.Query;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

/**
 * @author peiyou
 * @version 2.4.0
 * @className QueryTest
 * @date 2022/1/10 4:56 下午
 **/
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)
public class QueryTest {

	@Autowired
	private EmployeeDao employeeDao;

	@Autowired
	private EmployeeMapper employeeMapper;

	@Autowired
	private DepartmentDao departmentDao;

	/**
	 * 简单查询
	 * @author Peiyou
	 * @date 2022/1/10 5:32 下午
	 * @return void
	 */
	@Test
	public void testSimple() {
		// 单表，条件查询 都可以直接用实体类直接查询
		// 如 employ表以name字段查询
		Employee employee = new Employee();
		employee.setName("张三");
		List<Employee> employeeList =  employeeDao.findAllByProviderEntity(employee);
	}


	// 比如查询Employee表，条件可能不是简单的等于
	@Test
	public void testWhere() {
		// 创建一个查询表
		EmployeeHelper t1 = new EmployeeHelper("t1");
		String name = "name";
		// query对象，就是查询对象
		Query query = new Query(t1)
				.select(t1.id()) // 可以不填，查询对象是t1，即上面创建EmployeeHelper对象时的别名写的t1，生成的sql也会是t1的。select方法不填时，默认查所有字段
				.where()
				.and(t1.name().eq("张三")) // t1.name = '张三'
				.and(t1.name().eq(name), name != null && name.length() > 0) // 相当于xml中 <if test="name != null and name.length > 0">t1.name = #{name}</if>
				.groupBy(t1.id(), t1.name()) // 分组
				.orderBy(t1.id().desc()); // 按id 倒序
		List<Employee> employeeList = employeeMapper.findByList(query);
	}

	@Test
	public void testMulTable() {
		EmployeeHelper t1 = new EmployeeHelper("t1");
		DepartmentHelper t2 = new DepartmentHelper("t2");
		// 以t1为主表，内联接t2表。
		Query query = new Query(t1)
				.innerJoin(t2).on(t1.departmentId().eq(t2.id())) // on t1.departmentId = t2.id
				.select(t1.id(), t1.name(), t2.name().as("departmentName"))
				.where()
				.and(t1.name().eq("张三"))
				.and(t2.name().eq("事业部"))
				.orderBy(t2.id().desc());
		// 因为查询结果是一个新的类型（非直接实体类，则需要先拿到map对象，将map转成实体类）
		List<Map<String, Object>> result = employeeMapper.findByListToMap(query);
	}

	@Test
	public void testMulTable2() {
		// 子查询
		EmployeeHelper t1 = new EmployeeHelper("t1");
		Query subQuery = new Query(t1)
				.select(
						"sum(t1.salary) as salary"
				).select(t1.departmentId()) // 有函数的查询字段与无函数的可以分开多个select方法
				.where()
				.and(t1.name().ne("张三"))
				.groupBy(t1.departmentId());

		DepartmentHelper t2 = new DepartmentHelper("t2");
		Query query = new Query(t2)
				.select(t2.name(), t1.departmentId()) // 此处的t1需要与下面的rightJoin中的别名一致
				.select("t1.salary")
				.rightJoin(subQuery, "t1").on(t1.departmentId().eq(t2.id()))
				.orderBy(t2.name());

		List<Map<String, Object>> result = employeeMapper.findByListToMap(query);

	}
}
