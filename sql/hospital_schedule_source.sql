-- ----------------------------
-- 医院业务-号源管理（学习版）
-- 执行前建议已导入 hospital_department.sql、hospital_doctor.sql、hospital_schedule.sql
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_schedule_source;
create table hospital_schedule_source (
  source_id      bigint(20)      not null auto_increment    comment '号源ID',
  schedule_id    bigint(20)      not null                   comment '排班ID',
  department_id  bigint(20)      not null                   comment '科室ID',
  doctor_id      bigint(20)      not null                   comment '医生ID',
  total_num      int(11)         not null default 0         comment '总号数',
  remain_num     int(11)         not null default 0         comment '剩余号数',
  status         char(1)         default '0'                comment '状态（0可预约 1停用 2约满）',
  del_flag       char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by      varchar(64)     default ''                 comment '创建者',
  create_time    datetime                                   comment '创建时间',
  update_by      varchar(64)     default ''                 comment '更新者',
  update_time    datetime                                   comment '更新时间',
  remark         varchar(500)    default ''                 comment '备注',
  primary key (source_id),
  unique key uk_hospital_source_schedule (schedule_id, del_flag),
  key idx_hospital_source_department (department_id),
  key idx_hospital_source_doctor (doctor_id),
  key idx_hospital_source_status (status)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院排班号源表';

insert into hospital_schedule_source values
(1, 1, 1, 1, 30, 30, '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(2, 2, 2, 2, 25, 8, '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(3, 3, 3, 3, 10, 0, '2', '0', 'admin', sysdate(), '', null, '学习演示数据');

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2040, 2041, 2042, 2043, 2044, 2045);
insert into sys_menu values('2040', '号源管理', '2000', '5', 'source', 'hospital/source/index', '', '', 1, 0, 'C', '0', '0', 'hospital:source:list', 'number', 'admin', sysdate(), '', null, '号源管理菜单');
insert into sys_menu values('2041', '号源查询', '2040', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:source:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2042', '号源新增', '2040', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:source:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2043', '号源修改', '2040', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:source:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2044', '号源删除', '2040', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:source:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2045', '号源导出', '2040', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:source:export', '#', 'admin', sysdate(), '', null, '');
