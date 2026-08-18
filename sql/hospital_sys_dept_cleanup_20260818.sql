set names utf8mb4;

-- 清理若依默认部门残留，组织树只保留医院项目需要的四个组织节点。
update sys_user set dept_id = 101 where dept_id in (104, 105, 106, 107, 108, 109);
delete from sys_role_dept where dept_id in (104, 105, 106, 107, 108, 109);
delete from sys_dept where dept_id in (104, 105, 106, 107, 108, 109);

-- 修正学习演示科室的归属组织：临床科室归门诊部，信息科归行政后勤部。
update hospital_department set dept_id = 101, update_by = 'admin', update_time = sysdate()
where department_code in ('DEPT_INTERNAL', 'DEPT_SURGERY');

update hospital_department set dept_id = 103, update_by = 'admin', update_time = sysdate()
where department_code = 'DEPT_IT';

-- 清理本地调试产生的重复科室前，先把引用迁回现有信息科。
update hospital_doctor set department_id = 3, update_by = 'admin', update_time = sysdate()
where department_id in (4, 5);

delete from hospital_department where department_id in (4, 5);
