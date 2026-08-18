-- ----------------------------
-- 医院业务-医生绑定账号升级脚本
-- 用于已存在 hospital_doctor 表的数据库升级
-- ----------------------------

set names utf8mb4;

alter table hospital_doctor
  add column user_id bigint(20) default null comment '绑定系统用户ID' after department_id;

alter table hospital_doctor
  add unique key uk_hospital_doctor_user (user_id);
