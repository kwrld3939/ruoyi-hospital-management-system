# 若依医院管理系统

基于 RuoYi-Vue Spring Boot 3 进行二次开发的医院门诊管理系统。项目围绕门诊业务闭环展开，覆盖科室、医生、患者、排班、号源、预约、就诊、病历、医生工作台和权限数据边界等核心场景。

## 项目定位

本项目不是单纯的 CRUD 练习，而是基于若依后台管理框架完成医院业务模块扩展，重点实践：

- 业务表设计与模块拆分
- 预约挂号到就诊记录的状态流转
- 排班、号源、预约之间的数据关联
- 医生账号绑定与数据边界控制
- 若依角色、菜单、按钮权限体系的二次开发

## 技术栈

后端：

- Spring Boot 3
- Spring Security
- MyBatis
- MySQL
- Redis
- JWT
- Druid

前端：

- Vue 2
- Vue CLI
- Element UI
- Vuex
- Axios

基础框架：

- RuoYi-Vue 3.9.2 Spring Boot 3 版本

## 若依官方入口

项目基于若依官方框架二次开发，这里保留原项目入口，方便查看基础能力和文档：

- 官方仓库：<https://gitee.com/y_project/RuoYi-Vue>
- 官方文档：<https://doc.ruoyi.vip/ruoyi-vue/>
- GitHub 仓库：<https://github.com/yangzongzhuan/RuoYi-Vue>

## 核心模块

医院业务模块：

- 科室管理：维护医院业务科室，并关联若依组织机构
- 医生管理：维护医生基础信息，支持医生与系统账号绑定
- 患者管理：维护患者基础档案
- 医生排班：维护医生出诊日期、时段和排班状态
- 号源管理：基于排班维护可预约号源
- 预约挂号：患者选择号源完成预约，并维护预约状态
- 就诊记录：医生完成接诊后形成就诊记录
- 首页看板：展示门诊业务统计数据
- 医生工作台：医生查看自己的今日预约、待接诊和已接诊数据
- 患者病历：以患者为中心只读汇总就诊记录和预约信息
- 权限收口：管理员查看全部数据，医生账号仅查看自己的业务数据

若依基础能力保留：

- 用户管理
- 角色管理
- 菜单管理
- 岗位管理
- 部门管理
- 字典管理
- 操作日志
- 登录日志
- 代码生成器

## 业务闭环

核心链路：

```text
科室 -> 医生 -> 排班 -> 号源 -> 预约挂号 -> 就诊记录 -> 患者病历
```

设计思路：

- 预约不直接绑定排班，而是先生成号源，再由患者预约号源
- 预约记录保存患者、医生、科室、号源和预约状态
- 就诊记录由预约流转而来，表示医生实际接诊后的业务结果
- 患者病历不重复建表，作为患者维度的只读汇总页面
- 医生工作台通过医生账号绑定实现“只看自己”的业务边界

## 权限与数据边界

项目基于若依原有权限体系进行收口：

- 超级管理员可以查看全部医院业务数据
- 医生账号绑定医生档案后，只能查看自己的工作台、预约和就诊记录
- 普通业务账号通过菜单和按钮权限控制可见页面和可操作按钮
- 按钮权限仍使用若依 `perms` 标识控制新增、修改、删除、导出等操作

## 目录结构

```text
RuoYi-Vue-springboot3
├── ruoyi-admin       # 后端启动模块与 controller
├── ruoyi-system      # 系统与医院业务 service、mapper、domain
├── ruoyi-framework   # 若依框架、安全、配置
├── ruoyi-common      # 通用工具与基础类
├── ruoyi-ui          # Vue 前端项目
└── sql               # 初始化 SQL 与医院业务增量 SQL
```

## 本地启动

### 1. 初始化数据库

创建 MySQL 数据库：

```sql
create database `ry-vue` default character set utf8mb4 collate utf8mb4_general_ci;
```

推荐导入顺序：

```text
sql/ry_20260417.sql
sql/quartz.sql
sql/hospital_department.sql
sql/hospital_doctor.sql
sql/hospital_patient.sql
sql/hospital_schedule.sql
sql/hospital_schedule_source.sql
sql/hospital_registration.sql
sql/hospital_visit_record.sql
sql/hospital_doctor_user_bind.sql
sql/hospital_doctor_workbench.sql
sql/hospital_patient_medical_record.sql
sql/hospital_sys_dept_cleanup_20260818.sql
sql/hospital_sys_post_cleanup_20260818.sql
```

数据库连接支持环境变量，也保留了本地默认值：

```text
DB_HOST=localhost
DB_PORT=3306
DB_NAME=ry-vue
DB_USERNAME=root
DB_PASSWORD=your-local-password
```

本地直接跑也可以不配这些变量，默认值已经写在 `application-druid.yml` 里。

### 2. 启动后端

```bash
cd ruoyi-admin
mvn spring-boot:run
```

默认后端地址：

```text
http://localhost:8080
```

### 3. 启动前端

```bash
cd ruoyi-ui
npm install
npm run dev -- --port 81
```

默认前端地址：

```text
http://localhost:81
```

## 默认账号

```text
admin / admin123
```

项目中也可以通过用户管理新建医生账号，并在医生管理中绑定对应医生档案，用于验证医生数据边界。

## 项目亮点

- 基于成熟后台框架进行医院业务二开，而不是从零搭建脚手架
- 形成了从基础档案到预约就诊的完整业务闭环
- 引入号源表，避免预约直接绑定排班导致业务边界混乱
- 医生工作台和医生账号绑定结合，实现服务端数据隔离
- 患者病历采用只读汇总，避免与就诊记录重复维护
- 对若依默认部门、岗位、菜单和权限进行医院场景化整理

## 说明

本项目用于 Java 后端二开能力、业务建模能力和后台管理系统开发能力展示。基础框架来源于 RuoYi-Vue，医院业务模块为二次开发内容。
