-- ----------------------------
-- 就诊记录索引补丁
-- 作用：把旧版 registration_id + del_flag 唯一索引调整为状态感知查询索引。
-- 原因：作废就诊记录要保留历史，但不应该继续占用挂号记录。
-- ----------------------------

set names utf8mb4;

set @schema_name = database();

set @drop_sql = (
  select if(count(*) > 0,
    'alter table hospital_visit_record drop index uk_hospital_visit_registration',
    'select 1')
  from information_schema.statistics
  where table_schema = @schema_name
    and table_name = 'hospital_visit_record'
    and index_name = 'uk_hospital_visit_registration'
);
prepare stmt from @drop_sql;
execute stmt;
deallocate prepare stmt;

set @add_sql = (
  select if(count(*) = 0,
    'alter table hospital_visit_record add index idx_hospital_visit_registration (registration_id, del_flag, status)',
    'select 1')
  from information_schema.statistics
  where table_schema = @schema_name
    and table_name = 'hospital_visit_record'
    and index_name = 'idx_hospital_visit_registration'
);
prepare stmt from @add_sql;
execute stmt;
deallocate prepare stmt;
