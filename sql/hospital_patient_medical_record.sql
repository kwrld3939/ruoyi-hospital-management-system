-- ----------------------------
-- Hospital business - patient medical record view
-- No new business table. This script only adds the menu entry.
-- Data source: hospital_patient, hospital_registration, hospital_visit_record
-- ----------------------------

set names utf8mb4;

delete from sys_menu where menu_id in (2080);
insert into sys_menu values(
  '2080',
  convert(0xE682A3E88085E79785E58E86 using utf8mb4),
  '2000',
  '9',
  'medicalRecord',
  'hospital/medicalRecord/index',
  '',
  '',
  1,
  0,
  'C',
  '0',
  '0',
  'hospital:medicalRecord:list',
  'documentation',
  'admin',
  sysdate(),
  '',
  null,
  convert(0xE682A3E88085E79785E58E86E69FA5E8AFA2E88F9CE58D95 using utf8mb4)
);
