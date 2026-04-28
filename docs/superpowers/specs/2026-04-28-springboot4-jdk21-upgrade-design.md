# Spring Boot 4.0.6 + JDK 21 整体升级设计

**日期：** 2026-04-28  
**分支：** master_jdk21

---

## 一、目标版本矩阵

| 组件 | 当前版本 | 目标版本 |
|---|---|---|
| Spring Boot | 2.7.13 | 4.0.6 |
| Spring Cloud | 2021.0.8 | 2025.1.1 |
| Spring Framework | 5.3.28 | 7.x（由 Boot 4 管理） |
| Java | 1.8 | 21 |
| xxl-job（内部 fork） | 2.4.0 | 3.4.0 代码 + SB 4.0.6 |
| druid-spring-boot-3-starter | 1.2.18（旧 artifact） | 1.2.28 |
| mybatis-plus-spring-boot3-starter | 3.5.5（旧 artifact） | 3.5.16 |
| mybatis-spring-boot-starter | 2.3.1 | 4.0.1 |
| jakarta.annotation-api | 1.3.2（javax） | 3.0.0（jakarta） |

---

## 二、根 pom.xml 变更

### 2.1 parent 版本

```xml
<!-- 旧 -->
<version>2.7.13</version>
<!-- 新 -->
<version>4.0.6</version>
```

### 2.2 properties 层变更

| 属性 | 操作 | 旧值 | 新值 |
|---|---|---|---|
| `spring-boot.version` | 修改 | 2.7.13 | 4.0.6 |
| `spring-cloud.version` | 修改 | 2021.0.8 | 2025.1.1 |
| `spring.version` | 删除 | 5.3.28 | — |
| `java.version` | 修改 | 1.8 | 21 |
| `maven.compiler.source` | 修改 | 1.8 | 21 |
| `maven.compiler.target` | 修改 | 1.8 | 21 |
| `logback-classic.version` | 删除 | 1.2.11 | — |
| `hibernate-validator.version` | 删除 | 6.1.0.Final | — |
| `druid-spring-boot-starter.version` | 删除，改名 | 1.2.18 | 新增 `druid-spring-boot-3-starter.version=1.2.28` |
| `mybatis-plus-boot-starter.version` | 删除，改名 | 3.5.5 | 新增 `mybatis-plus-spring-boot3-starter.version=3.5.16` |
| `mybatis-spring-boot-starter.version` | 修改 | 2.3.1 | 4.0.1 |
| `javax.annotation-api.version` | 删除，改名 | 1.3.2 | 新增 `jakarta.annotation-api.version=3.0.0` |
| `netty-all.version` | 删除 | 4.1.85.Final | — |
| `maven-compiler-plugin.version` | 修改 | 3.8.1 | 3.13.0 |

### 2.3 dependencyManagement 层变更

| 旧 artifact | 操作 | 新 artifact |
|---|---|---|
| `org.hibernate:hibernate-validator` | groupId 变更 | `org.hibernate.validator:hibernate-validator` |
| `javax.servlet:javax.servlet-api` | 删除 | — |
| `javax.annotation:javax.annotation-api` | 替换 | `jakarta.annotation:jakarta.annotation-api ${jakarta.annotation-api.version}` |
| `mysql:mysql-connector-java` | groupId/artifactId 变更 | `com.mysql:mysql-connector-j` |
| `com.alibaba:druid-spring-boot-starter` | artifactId 变更 | `com.alibaba:druid-spring-boot-3-starter` |
| `com.baomidou:mybatis-plus-boot-starter` | artifactId 变更 | `com.baomidou:mybatis-plus-spring-boot3-starter` |
| `com.baomidou:mybatis-plus-annotation` | 保持不变 | 版本跟随新属性 |
| `ch.qos.logback:logback-classic` | 删除显式版本管理 | Boot 4 自动管理 |
| `org.springframework.boot:spring-boot-starter-cache` | 删除显式版本管理 | Boot 4 parent 管理 |

---

## 三、各子模块 pom.xml 变更

### 3.1 halo-rest

- 移除 `javax.servlet:javax.servlet-api` 依赖
- 移除 `junit:junit` 依赖（改用 JUnit 5，已由 `spring-boot-starter-test` 引入）
- 移除 `com.sun.mail:javax.mail`，**新增** `spring-boot-starter-mail`（提供 `jakarta.mail.*`，EmailSender.java 依赖此 API）
- `spring-boot-maven-plugin` 版本从硬编码 `2.6.2` 改为 `${spring-boot.version}`

### 3.2 halo-common

- 移除 `javax.servlet:javax.servlet-api` 依赖
- `org.hibernate:hibernate-validator` → `org.hibernate.validator:hibernate-validator`
- 移除 `ch.qos.logback:logback-classic` 显式版本（Boot 4 管理）

### 3.3 halo-entity

- `org.hibernate:hibernate-validator` → `org.hibernate.validator:hibernate-validator`

### 3.4 halo-dao

- `mysql:mysql-connector-java` → `com.mysql:mysql-connector-j`
- `com.baomidou:mybatis-plus-boot-starter` → `com.baomidou:mybatis-plus-spring-boot3-starter`

### 3.5 halo-xxl-job-core

- `javax.annotation:javax.annotation-api` → `jakarta.annotation:jakarta.annotation-api`
- 移除 `spring-context` 的显式 `${spring.version}`（Spring Boot 4 parent 管理）

### 3.6 halo-xxl-job-admin

- `mybatis-spring-boot-starter`：2.3.1 → 4.0.1
- `spring-boot-maven-plugin` 版本跟随 `${spring-boot.version}`（已正确，确认即可）

---

## 四、源码 javax → jakarta 迁移

### 迁移规则

| 原命名空间 | 新命名空间 | 备注 |
|---|---|---|
| `javax.servlet.*` | `jakarta.servlet.*` | 21 个文件 |
| `javax.annotation.*` | `jakarta.annotation.*` | 23 个文件（仅 annotation-api 的类，非 JDK 内置） |
| `javax.validation.*` | `jakarta.validation.*` | 5 个文件 |
| `javax.mail.*` | `jakarta.mail.*` | 2 个文件 |
| `javax.crypto.*` | **不变** | JDK 内置 |
| `javax.imageio.*` | **不变** | JDK 内置 |
| `javax.net.*` | **不变** | JDK 内置 |
| `javax.sql.*` | **不变** | JDK 内置 |

### 涉及文件清单

**需迁移的文件（共约 50 个）：**

```
halo-common:
  - com/halo/dto/req/CustomPageQueryReq.java           (javax.validation)
  - com/halo/dto/req/StudentSummaryReq.java            (javax.validation)
  - com/halo/exception/GlobalExceptionHandler.java     (javax.servlet)
  - com/halo/filter/CheckResourceFilter.java           (javax.servlet)
  - com/halo/interceptor/CheckResourceInterceptor.java (javax.servlet)
  - com/halo/utils/TencentCOSUtil.java                 (javax.annotation)
  - com/halo/utils/WebPThumbnailUtil.java              (javax.annotation)

halo-core:
  - com/halo/service/PeopleService.java                (javax.annotation)

halo-dao:
  - com/halo/config/MyBatisPlusConfig.java             (javax.annotation)

halo-entity:
  - com/halo/entity/People.java                        (javax.validation)

halo-rest (main):
  - com/halo/config/DruidDataSourceConfig.java         (javax.annotation/servlet)
  - com/halo/controller/EasyExcelController.java       (javax.servlet)
  - com/halo/controller/EmailSender.java               (javax.mail)
  - com/halo/controller/GreetingController.java        (javax.servlet)
  - com/halo/controller/LessonCheckingController.java  (javax.servlet)
  - com/halo/controller/QRCodeController.java          (javax.servlet)
  - com/halo/controller/ServiceAController.java        (javax.annotation)
  - com/halo/controller/ServiceBController.java        (javax.annotation)
  - com/halo/filter/CachingRequestWrapper.java         (javax.servlet)
  - com/halo/filter/XesPlatformFilter.java             (javax.servlet)
  - com/halo/utils/HmacSha256Util.java                 (javax.annotation)

halo-rest (test):
  - com/halo/controller/EasyExcelControllerTest.java   (javax.annotation)
  - com/halo/demo/HaloApplicationTests.java            (javax.annotation)

halo-xxl-job-admin (main + test):
  - controller/IndexController.java                    (javax.servlet)
  - controller/JobApiController.java                   (javax.servlet)
  - controller/JobCodeController.java                  (javax.servlet)
  - controller/JobGroupController.java                 (javax.servlet)
  - controller/JobInfoController.java                  (javax.servlet)
  - controller/JobLogController.java                   (javax.servlet)
  - controller/UserController.java                     (javax.servlet)
  - controller/interceptor/CookieInterceptor.java      (javax.servlet)
  - controller/interceptor/PermissionInterceptor.java  (javax.servlet)
  - controller/interceptor/WebMvcConfig.java           (javax.servlet)
  - controller/resolver/WebExceptionResolver.java      (javax.servlet)
  - core/alarm/impl/EmailJobAlarm.java                 (javax.mail)
  - core/conf/XxlJobAdminConfig.java                   (javax.annotation)
  - core/util/CookieUtil.java                          (javax.servlet)
  - service/LoginService.java                          (javax.servlet)
  - service/impl/XxlJobServiceImpl.java                (javax.annotation)
  - test 类 (5 个)                                     (javax.annotation)

halo-xxl-job-core:
  - core/glue/impl/SpringGlueFactory.java              (javax.annotation)
  - core/util/XxlJobRemotingUtil.java                  (javax.net - 不变)
```

---

## 五、halo-xxl-job 升级到 v3.4.0

### 5.1 策略

halo-xxl-job 是 xxl-job 2.4.0 的内部 fork。v3.4.0 官方版本使用 Spring Boot 4.0.5，本次适配改为 4.0.6。

升级步骤：
1. 从 `https://github.com/xuxueli/xxl-job/tree/v3.4.0` 拉取 `xxl-job-core` 和 `xxl-job-admin` 源码
2. 全量替换 `halo-xxl-job-core/src` 和 `halo-xxl-job-admin/src`
3. pom 中 Spring Boot 版本锁定为 4.0.6（替代 v3.4.0 原始的 4.0.5）
4. 执行数据库迁移脚本

### 5.2 v3.4.0 主要功能改进

- 集成 OpenClaw：新增 `openClawJobHandler` AI 任务处理器
- 批量合并调度：高频场景降低 SQL 操作（配置项 `xxl.job.schedule.batchsize`）
- 调度日志新增执行器维度查询
- 一致性哈希算法优化
- Cron 解析 day-of-month L 问题修复
- 执行器注册表 ID 类型改为 bigint

### 5.3 数据库迁移脚本

```sql
-- 调度日志表：添加索引（提升查询性能）
CREATE INDEX I_jobgroup ON xxl_job_log (job_group);

-- 执行器表：名称字段长度调整（64 字符）
ALTER TABLE xxl_job_group
    MODIFY title VARCHAR(64) NOT NULL COMMENT '执行器名称';

-- 执行器注册表：ID 类型改为 bigint（防止大规模集群溢出）
ALTER TABLE xxl_job_registry
    MODIFY id BIGINT(20) NOT NULL AUTO_INCREMENT;

-- 任务表：参数字段改为 text（最长 2048 字符）
ALTER TABLE xxl_job_info
    MODIFY executor_param TEXT NULL COMMENT '任务参数';

-- 日志表：参数字段改为 text
ALTER TABLE xxl_job_log
    MODIFY executor_param TEXT NULL COMMENT '任务参数';
```

---

## 六、实施顺序

1. **根 pom.xml** — 版本属性 + dependencyManagement
2. **各子模块 pom.xml** — 按依赖关系顺序：entity → dao → common → core → job → remote → rest → xxl-job
3. **源码 javax → jakarta 迁移** — 批量替换，逐模块验证
4. **halo-xxl-job v3.4.0 源码替换** — 拉取上游代码，适配 SB 4.0.6
5. **编译验证** — `mvn clean compile -DskipTests`
6. **运行验证** — 启动主应用，检查基础功能

---

## 七、注意事项

- `mybatis-plus-spring-boot3-starter` 命名仍为 "boot3"，实际兼容 Spring Boot 4.x（底层使用标准 Spring Boot autoconfigure API）
- `druid-spring-boot-3-starter` 同上，1.2.28 已支持 Jakarta EE，兼容 Boot 4.x
- `XxlJobRemotingUtil.java` 中 `javax.net.ssl.*` 属于 JDK 内置，**不迁移**
- `javax.crypto.*` / `javax.imageio.*` / `javax.sql.*` 同属 JDK 内置，**不迁移**
- Spring Cloud 2025.1.1 的 OpenFeign 已完全支持 Jakarta EE + Spring Framework 7
