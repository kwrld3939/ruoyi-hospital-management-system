-- ----------------------------
-- 医院业务-科室管理（学习版）
-- 执行前请确认 menu_id 未被占用；如已占用，可整体调整 2000-2007。
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_department;
create table hospital_department (
  department_id     bigint(20)      not null auto_increment    comment '科室ID',
  dept_id           bigint(20)      default null                comment '关联若依部门ID',
  department_code   varchar(50)     not null                   comment '科室编码',
  department_name   varchar(100)    not null                   comment '科室名称',
  department_type   varchar(50)     default ''                 comment '科室类型',
  director_name     varchar(50)     default ''                 comment '科室负责人',
  phone             varchar(30)     default ''                 comment '联系电话',
  location          varchar(200)    default ''                 comment '科室位置',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (department_id),
  unique key uk_hospital_department_code (department_code)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院科室表';

insert into hospital_department values
(1, 101, 'DEPT_INTERNAL', '内科', '临床科室', '张主任', '010-10000001', '门诊楼2层', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(2, 101, 'DEPT_SURGERY', '外科', '临床科室', '李主任', '010-10000002', '门诊楼3层', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(3, 103, 'DEPT_IT', '信息科', '行政后勤', '王主任', '010-10000003', '行政楼5层', '0', '0', 'admin', sysdate(), '', null, '学习演示数据');

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2000, 2001, 2002, 2003, 2004, 2005, 2006);
insert into sys_menu values('2000', '医院业务', '0', '5', 'hospital', null, '', '', 1, 0, 'M', '0', '0', '', 'tree', 'admin', sysdate(), '', null, '医院业务目录');
insert into sys_menu values('2001', '科室管理', '2000', '1', 'department', 'hospital/department/index', '', '', 1, 0, 'C', '0', '0', 'hospital:department:list', 'tree', 'admin', sysdate(), '', null, '科室管理菜单');
insert into sys_menu values('2002', '科室查询', '2001', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:department:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2003', '科室新增', '2001', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:department:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2004', '科室修改', '2001', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:department:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2005', '科室删除', '2001', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:department:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2006', '科室导出', '2001', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:department:export', '#', 'admin', sysdate(), '', null, '');
