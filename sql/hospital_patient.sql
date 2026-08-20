-- ----------------------------
-- 医院业务-患者管理（学习版）
-- ----------------------------

set names utf8mb4;

drop table if exists hospital_patient;
create table hospital_patient (
  patient_id        bigint(20)      not null auto_increment    comment '患者ID',
  patient_code      varchar(50)     not null                   comment '患者编码',
  patient_name      varchar(50)     not null                   comment '患者姓名',
  gender            char(1)         not null                   comment '性别（0男 1女）',
  birth_date        date            default null                comment '出生日期',
  id_card           varchar(30)     default ''                 comment '身份证号',
  phone             varchar(30)     not null                   comment '联系电话',
  address           varchar(200)    default ''                 comment '联系地址',
  emergency_contact varchar(50)     default ''                 comment '紧急联系人',
  emergency_phone   varchar(30)     default ''                 comment '紧急联系电话',
  status            char(1)         default '0'                comment '状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0存在 2删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (patient_id),
  unique key uk_hospital_patient_code (patient_code),
  key idx_hospital_patient_name (patient_name),
  key idx_hospital_patient_phone (phone),
  key idx_hospital_patient_id_card (id_card)
) engine=innodb auto_increment=1 default charset=utf8mb4 collate=utf8mb4_general_ci comment = '医院患者表';

insert into hospital_patient values
(1, 'PAT_20260812001', '张三', '0', '1990-01-15', '110101199001150011', '13900000001', '北京市朝阳区示例路1号', '李四', '13900000002', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(2, 'PAT_20260812002', '王芳', '1', '1988-06-20', '110101198806200022', '13900000003', '北京市海淀区示例路2号', '王明', '13900000004', '0', '0', 'admin', sysdate(), '', null, '学习演示数据'),
(3, 'PAT_20260812003', '赵强', '0', '1975-09-08', '110101197509080033', '13900000005', '北京市西城区示例路3号', '赵敏', '13900000006', '0', '0', 'admin', sysdate(), '', null, '学习演示数据');

-- ----------------------------
-- 菜单权限
-- ----------------------------
delete from sys_menu where menu_id in (2020, 2021, 2022, 2023, 2024, 2025, 2026, 2027, 2028);
insert into sys_menu values('2020', '患者管理', '2000', '3', 'patient', 'hospital/patient/index', '', '', 1, 0, 'C', '0', '0', 'hospital:patient:list', 'peoples', 'admin', sysdate(), '', null, '患者管理菜单');
insert into sys_menu values('2021', '患者查询', '2020', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2022', '患者新增', '2020', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:add', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2023', '患者修改', '2020', '3', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:edit', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2024', '患者停用', '2020', '4', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:remove', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2025', '患者导出', '2020', '5', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:export', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2026', '归档患者', '2000', '10', 'patientArchive', 'hospital/patient/archive', '', '', 1, 0, 'C', '0', '0', 'hospital:patient:archive:list', 'peoples', 'admin', sysdate(), '', null, '归档患者菜单');
insert into sys_menu values('2027', '归档患者查询', '2026', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:archive:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2028', '归档患者恢复', '2026', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:archive:restore', '#', 'admin', sysdate(), '', null, '');
