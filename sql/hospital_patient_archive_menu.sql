-- ----------------------------
-- 医院业务-归档患者菜单升级脚本
-- 不重建 hospital_patient 表，只补菜单入口和按钮权限。
-- 归档患者数据仍保存在 hospital_patient，del_flag = '2'。
-- ----------------------------

set names utf8mb4;

delete from sys_menu where menu_id in (2026, 2027, 2028);
update sys_menu set menu_name = '患者停用' where menu_id = 2024;

insert into sys_menu values('2026', '归档患者', '2000', '10', 'patientArchive', 'hospital/patient/archive', '', '', 1, 0, 'C', '0', '0', 'hospital:patient:archive:list', 'peoples', 'admin', sysdate(), '', null, '归档患者菜单');
insert into sys_menu values('2027', '归档患者查询', '2026', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:archive:query', '#', 'admin', sysdate(), '', null, '');
insert into sys_menu values('2028', '归档患者恢复', '2026', '2', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:patient:archive:restore', '#', 'admin', sysdate(), '', null, '');
