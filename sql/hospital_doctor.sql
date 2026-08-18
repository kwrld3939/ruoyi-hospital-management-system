-- ----------------------------
-- 医院业务-医生管理（学习版）
-- 执行前建议已导入 hospital_department.sql
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_doctor;
create table hospital_doctor (
  doctor_id        bigint(20)      not null auto_increment    comment '医生ID',
  department_id    bigint(20)      not null                   comment '所属科室ID',
  user_id          bigint(20)      default null               comment '绑定系统用户ID',
  doctor_code      varchar(50)     not null                   comment '医生编码',
  doctor_name      varchar(50)     not null                   comment '医生姓名',
  gender           char(1)         not null                   comment '性别（0男 1女）',
  title            varchar(50)     default ''                 comment '职称',
  specialty        varchar(200)    default ''                 comment '专长',
  phone            varchar(30)     default ''                 comment '联系电话',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag         char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default ''                 comment '备注',
  primary key (doctor_id),
  unique key uk_hospital_doctor_code (doctor_code),
  unique key uk_hospital_doctor_user (user_id),
  key idx_hospital_doctor_department (department_id),
  key idx_hospital_doctor_name (doctor_name),
  key idx_hospital_doctor_department_status (department_id, status)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院医生表';

insert into hospital_doctor (
  doctor_id, department_id, user_id, doctor_code, doctor_name, gender,
  title, specialty, phone, status, del_flag, create_by, create_time,
  update_by, update_time, remark
) values
(1, 1, null, 'DOC_INTERNAL_001', '张医生', '0', '主任医师', '心血管方向', '13800000001', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(2, 2, null, 'DOC_SURGERY_001', '李医生', '1', '副主任医师', '普外方向', '13800000002', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(3, 3, null, 'DOC_IT_001', '王医生', '0', '主治医师', '信息系统支持', '13800000003', '0', '0', 'admin', sysdate(), '', null, '学习演示数据');

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2010, 2011, 2012, 2013, 2014, 2015);
insert into sys_menu values('2010', '医生管理', '2000', '2', 'doctor', 'hospital/doctor/index', '', '', 1, 0, 'C', '0', '0', 'hospital:doctor:list', 'user', 'admin', sysdate(), '', null, '医生管理菜单');
insert into sys_menu values('2011', '医生查询', '2010', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:doctor:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2012', '医生新增', '2010', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:doctor:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2013', '医生修改', '2010', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:doctor:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2014', '医生删除', '2010', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:doctor:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2015', '医生导出', '2010', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:doctor:export', '#', 'admin', sysdate(), '', null, '');
