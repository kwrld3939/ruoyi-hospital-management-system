-- ----------------------------
-- 医院业务-就诊记录（学习版）
-- 执行前建议已导入 hospital_registration.sql，并已有已预约挂号记录
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_visit_record;
create table hospital_visit_record (
  visit_id          bigint(20)      not null auto_increment    comment '就诊记录ID',
  registration_id   bigint(20)      not null                   comment '挂号ID',
  patient_id        bigint(20)      not null                   comment '患者ID',
  department_id     bigint(20)      not null                   comment '科室ID',
  doctor_id         bigint(20)      not null                   comment '医生ID',
  visit_time        datetime        not null                   comment '就诊时间',
  chief_complaint   varchar(500)    not null                   comment '主诉',
  diagnosis         varchar(500)    not null                   comment '初步诊断',
  treatment_advice  varchar(1000)   default ''                 comment '处理意见',
  status            char(1)         default '0'                comment '状态（0已就诊 1作废）',
  del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (visit_id),
  key idx_hospital_visit_registration (registration_id, del_flag, status),
  key idx_hospital_visit_patient (patient_id),
  key idx_hospital_visit_department (department_id),
  key idx_hospital_visit_doctor (doctor_id),
  key idx_hospital_visit_time (visit_time),
  key idx_hospital_visit_status (status)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院就诊记录表';

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2060, 2061, 2062, 2063, 2064, 2065);
insert into sys_menu values('2060', '就诊记录', '2000', '7', 'visit', 'hospital/visit/index', '', '', 1, 0, 'C', '0', '0', 'hospital:visit:list', 'documentation', 'admin', sysdate(), '', null, '就诊记录菜单');
insert into sys_menu values('2061', '就诊查询', '2060', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:visit:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2062', '就诊新增', '2060', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:visit:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2063', '就诊修改', '2060', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:visit:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2064', '就诊删除', '2060', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:visit:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2065', '就诊导出', '2060', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:visit:export', '#', 'admin', sysdate(), '', null, '');
