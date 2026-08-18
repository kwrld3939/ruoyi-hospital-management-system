-- ----------------------------
-- 医院业务-预约挂号（学习版）
-- 执行前建议已导入 hospital_patient.sql、hospital_schedule.sql、hospital_schedule_source.sql
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_registration;
create table hospital_registration (
  registration_id    bigint(20)      not null auto_increment    comment '挂号ID',
  registration_no    varchar(50)     not null                   comment '挂号单号',
  patient_id         bigint(20)      not null                   comment '患者ID',
  source_id          bigint(20)      not null                   comment '号源ID',
  schedule_id        bigint(20)      not null                   comment '排班ID',
  department_id      bigint(20)      not null                   comment '科室ID',
  doctor_id          bigint(20)      not null                   comment '医生ID',
  registration_time  datetime        not null                   comment '挂号时间',
  visit_date         date            not null                   comment '就诊日期',
  time_slot          char(1)         not null                   comment '时间段（1上午 2下午 3晚上）',
  status             char(1)         default '0'                comment '状态（0已预约 1已取消 2已就诊 3爽约）',
  del_flag           char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by          varchar(64)     default ''                 comment '创建者',
  create_time        datetime                                   comment '创建时间',
  update_by          varchar(64)     default ''                 comment '更新者',
  update_time        datetime                                   comment '更新时间',
  remark             varchar(500)    default ''                 comment '备注',
  primary key (registration_id),
  unique key uk_hospital_registration_no (registration_no),
  unique key uk_hospital_registration_patient_source_status (patient_id, source_id, status, del_flag),
  key idx_hospital_registration_patient (patient_id),
  key idx_hospital_registration_source (source_id),
  key idx_hospital_registration_department (department_id),
  key idx_hospital_registration_doctor (doctor_id),
  key idx_hospital_registration_visit_date (visit_date),
  key idx_hospital_registration_status (status)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院预约挂号表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2050, 2051, 2052, 2053, 2054, 2055, 2056);
insert into sys_menu values('2050', '预约挂号', '2000', '6', 'registration', 'hospital/registration/index', '', '', 1, 0, 'C', '0', '0', 'hospital:registration:list', 'form', 'admin', sysdate(), '', null, '预约挂号菜单');
insert into sys_menu values('2051', '挂号查询', '2050', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:registration:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2052', '挂号新增', '2050', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:registration:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2053', '挂号修改', '2050', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:registration:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2054', '挂号删除', '2050', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:registration:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2055', '挂号导出', '2050', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:registration:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2056', '挂号取消', '2050', '6', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:registration:cancel', '#', 'admin', sysdate(), '', null, '');
