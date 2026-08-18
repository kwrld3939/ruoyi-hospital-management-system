-- ----------------------------
-- 医院业务-医生工作台（学习版）
-- 本模块不新增业务表，只新增菜单权限
-- ----------------------------

set names utf8mb4;

delete from sys_menu where menu_id in (2070, 2071);
insert into sys_menu values('2070', '医生工作台', '2000', '8', 'doctorWorkbench', 'hospital/workbench/doctor', '', '', 1, 0, 'C', '0', '0', 'hospital:workbench:doctor', 'monitor', 'admin', sysdate(), '', null, '医生工作台菜单');
insert into sys_menu values('2071', '工作台查询', '2070', '1', '#', '', '', '', 1, 0, 'F', '0', '0', 'hospital:workbench:doctor', '#', 'admin', sysdate(), '', null, '');
