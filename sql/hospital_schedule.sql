-- ----------------------------
-- 医院业务-医生排班（学习版）
-- 执行前建议已导入 hospital_department.sql、hospital_doctor.sql
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_schedule;
create table hospital_schedule (
  schedule_id    bigint(20)      not null auto_increment    comment '排班ID',
  department_id  bigint(20)      not null                   comment '科室ID',
  doctor_id      bigint(20)      not null                   comment '医生ID',
  schedule_date  date            not null                   comment '排班日期',
  time_slot      char(1)         not null                   comment '时间段（1上午 2下午 3晚上）',
  location       varchar(200)    default ''                 comment '出诊地点',
  status         char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag       char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by      varchar(64)     default ''                 comment '创建者',
  create_time    datetime                                   comment '创建时间',
  update_by      varchar(64)     default ''                 comment '更新者',
  update_time    datetime                                   comment '更新时间',
  remark         varchar(500)    default ''                 comment '备注',
  primary key (schedule_id),
  unique key uk_hospital_schedule_doctor_time (doctor_id, schedule_date, time_slot, del_flag),
  key idx_hospital_schedule_department (department_id),
  key idx_hospital_schedule_date (schedule_date)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院医生排班表';

insert into hospital_schedule values
(1, 1, 1, '2026-08-13', '1', '门诊楼一层内科诊室1', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(2, 2, 2, '2026-08-13', '2', '门诊楼二层外科诊室2', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(3, 3, 3, '2026-08-14', '1', '行政楼信息科会议室', '0', '0', 'admin', sysdate(), '', null, '学习演示数据');

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2030, 2031, 2032, 2033, 2034, 2035);
insert into sys_menu values('2030', '医生排班', '2000', '4', 'schedule', 'hospital/schedule/index', '', '', 1, 0, 'C', '0', '0', 'hospital:schedule:list', 'date', 'admin', sysdate(), '', null, '医生排班菜单');
insert into sys_menu values('2031', '排班查询', '2030', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:schedule:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2032', '排班新增', '2030', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:schedule:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2033', '排班修改', '2030', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:schedule:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2034', '排班删除', '2030', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:schedule:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2035', '排班导出', '2030', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:schedule:export', '#', 'admin', sysdate(), '', null, '');
