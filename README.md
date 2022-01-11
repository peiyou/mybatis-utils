要把本项目集成进自己项目时，要保证自己项目是支持spring IOC的。生成dao层时，会生成@Repository与@Autowired 

写这个项目只是为了玩，基于mybatis做SQL查询时，对于复杂查询代码化。

现在只支持mysql。可以改造一下，变成支持多数据库，不过改动应该比较大。

先是生成对应的表结构，可以参考test项目中的test目录lpy.mybatis.generator.GeneratorTest

生成对应的实体类与mapper与dao层。

简单SQL查询可以直接用dao层的方法，应该都比较好理解的。

复杂查询，如多表连接查询、多条件查询等。


项目启动前，先用idea -> build -> Build project. 会生成对应实体的Helper的工具类
如Employee的实体类会生成 EmployeeHelper 的类，可以使用该类来做查询
```
    SELECT t1.id AS id FROM employee AS t1 WHERE t1.name = ? AND t1.name = ? GROUP BY t1.id, t1.name ORDER BY t1.id DESC 
    
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
```

多表联查

```
SELECT t1.id AS id, t1.name AS name, t2.name AS departmentName FROM employee AS t1 INNER JOIN department AS t2 ON t1.departmentId = t2.id WHERE t1.name = ? AND t2.name = ? ORDER BY t2.id DESC 

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

```
子查询

```
    SELECT t2.name AS name, t1.departmentId AS departmentId, t1.salary FROM department AS t2 RIGHT JOIN (SELECT sum(t1.salary) as salary, t1.departmentId AS departmentId FROM employee AS t1 WHERE t1.name <> ? GROUP BY t1.departmentId) AS t1 ON t1.departmentId = t2.id ORDER BY t2.name ASC 
    
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
```
