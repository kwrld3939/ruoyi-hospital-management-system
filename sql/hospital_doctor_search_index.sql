-- 医院业务-医生远程搜索索引
-- 仅用于已有 hospital_doctor 表的增量优化

use `ry-vue`;
set names utf8mb4;

alter table hospital_doctor
  add key idx_hospital_doctor_name (doctor_name),
  add key idx_hospital_doctor_department_status (department_id, status);
