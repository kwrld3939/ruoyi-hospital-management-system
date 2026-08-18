-- Hospitalize RuoYi default post data.
-- This script only adjusts sys_post display data and does not bind posts to business permissions.

UPDATE sys_post
SET post_code = 'sys_admin',
    post_name = '系统管理员',
    post_sort = 1,
    status = '0',
    update_by = 'admin',
    update_time = NOW()
WHERE post_id = 1;

UPDATE sys_post
SET post_code = 'director',
    post_name = '院长',
    post_sort = 2,
    status = '0',
    update_by = 'admin',
    update_time = NOW()
WHERE post_id = 2;

UPDATE sys_post
SET post_code = 'doctor',
    post_name = '门诊医生',
    post_sort = 3,
    status = '0',
    update_by = 'admin',
    update_time = NOW()
WHERE post_id = 3;

UPDATE sys_post
SET post_code = 'nurse',
    post_name = '护士',
    post_sort = 4,
    status = '0',
    update_by = 'admin',
    update_time = NOW()
WHERE post_id = 4;

INSERT INTO sys_post(post_id, post_code, post_name, post_sort, status, create_by, create_time, update_by, update_time, remark)
VALUES(5, 'registrar', '挂号收费员', 5, '0', 'admin', NOW(), '', NULL, '')
ON DUPLICATE KEY UPDATE
    post_code = VALUES(post_code),
    post_name = VALUES(post_name),
    post_sort = VALUES(post_sort),
    status = VALUES(status),
    update_by = 'admin',
    update_time = NOW();
