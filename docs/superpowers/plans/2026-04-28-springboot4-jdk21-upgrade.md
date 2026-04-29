# Spring Boot 4.0.6 + JDK 21 整体升级 Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将整个 MyProject 从 Spring Boot 2.7.13 / Java 1.8 升级到 Spring Boot 4.0.6 / JDK 21，并将内部 xxl-job fork 升级到 v3.4.0 代码基础。

**Architecture:** 分六个阶段：① 根 pom 版本属性与依赖管理 → ② 各子模块 pom → ③ 源码 javax→jakarta 批量迁移 → ④ application.yml Spring Boot 4 配置适配 → ⑤ xxl-job v3.4.0 源码替换 → ⑥ 编译验证与修复。

**Tech Stack:** Spring Boot 4.0.6, Spring Cloud 2025.1.1, Spring Framework 7.x, JDK 21, MyBatis-Plus 3.5.16, Druid 1.2.28, xxl-job 3.4.0

---

## 文件总览

| 文件 | 操作 |
|---|---|
| `pom.xml` | 修改：parent 版本、properties、dependencyManagement |
| `halo-entity/pom.xml` | 修改：hibernate-validator groupId |
| `halo-dao/pom.xml` | 修改：mysql connector、mybatis-plus artifact |
| `halo-common/pom.xml` | 修改：移除 servlet-api、改 hibernate-validator、移除 logback 显式版本 |
| `halo-rest/pom.xml` | 修改：移除 servlet-api、junit、mail；新增 starter-mail；改插件版本 |
| `halo-xxl-job-core/pom.xml` | 修改：annotation-api、新增 xxl-tool、更新版本号 |
| `halo-xxl-job-admin/pom.xml` | 修改：mybatis 版本、新增 xxl-sso-core |
| `halo-rest/src/main/resources/application.yml` | 修改：spring.redis→spring.data.redis，移除 spring.http.encoding |
| `halo-rest/src/main/resources/application-lry.yml` | 修改：同上 |
| `halo-*/src/**/*.java`（约 50 个文件）| 修改：javax→jakarta import 批量替换 |
| `halo-xxl-job-core/src/`（全量） | 替换：使用 xxl-job v3.4.0 xxl-job-core 源码 |
| `halo-xxl-job-admin/src/`（全量） | 替换：使用 xxl-job v3.4.0 xxl-job-admin 源码 |

---

## Task 1: 更新根 pom.xml — parent 版本与 properties

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: 更新 parent Spring Boot 版本**

在 `pom.xml` 第 17-22 行，将 `spring-boot-starter-parent` 版本从 `2.7.13` 改为 `4.0.6`：

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.6</version>
    <relativePath/>
</parent>
```

- [ ] **Step 2: 更新 properties 区域**

将 `pom.xml` 中 `<properties>` 块完整替换为：

```xml
<properties>
    <spring-boot.version>4.0.6</spring-boot.version>
    <spring-cloud.version>2025.1.1</spring-cloud.version>
    <java.version>21</java.version>
    <junit-jupiter.version>5.8.2</junit-jupiter.version>
    <commons-lang3.version>3.12.0</commons-lang3.version>
    <commons-io.version>2.13.0</commons-io.version>
    <commons-collections.version>3.2.2</commons-collections.version>
    <commons-collections4.version>4.4</commons-collections4.version>
    <hutool-all.version>5.8.41</hutool-all.version>
    <lombok.version>1.18.30</lombok.version>
    <fastjson2.version>2.0.46</fastjson2.version>
    <fastjson.version>2.0.46</fastjson.version>
    <jackson-databind.version>2.16.1</jackson-databind.version>
    <guava.version>32.0.1-jre</guava.version>
    <druid-spring-boot-3-starter.version>1.2.28</druid-spring-boot-3-starter.version>
    <druid.version>1.2.28</druid.version>
    <mybatis-plus-spring-boot3-starter.version>3.5.16</mybatis-plus-spring-boot3-starter.version>
    <mybatis-spring-boot-starter.version>4.0.1</mybatis-spring-boot-starter.version>
    <mysql-connector-j.version>8.0.32</mysql-connector-j.version>
    <xxl-job.version>3.4.0</xxl-job.version>
    <xxl-tool.version>2.5.0</xxl-tool.version>
    <xxl-sso.version>2.4.0</xxl-sso.version>
    <logstash-logback-encoder.version>7.2</logstash-logback-encoder.version>
    <apm-toolkit-logback-1.x.version>8.12.0</apm-toolkit-logback-1.x.version>
    <easyexcel.version>4.0.3</easyexcel.version>
    <zxing.version>3.5.2</zxing.version>
    <jackson.version>2.15.2</jackson.version>
    <dashscope-sdk-java.version>2.21.3</dashscope-sdk-java.version>
    <cos_api.version>5.6.253</cos_api.version>
    <cos-sts_api.version>3.1.1</cos-sts_api.version>
    <thumbnailator.version>0.4.21</thumbnailator.version>
    <webp-imageio-core.version>0.1.6</webp-imageio-core.version>
    <netty.version>4.2.12.Final</netty.version>
    <gson.version>2.13.2</gson.version>
    <slf4j-api.version>2.0.17</slf4j-api.version>
    <jakarta.annotation-api.version>3.0.0</jakarta.annotation-api.version>
    <maven-source-plugin.version>3.4.0</maven-source-plugin.version>
    <maven-javadoc-plugin.version>3.12.0</maven-javadoc-plugin.version>
    <maven-gpg-plugin.version>3.2.8</maven-gpg-plugin.version>
    <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
    <maven.compiler.encoding>UTF-8</maven.compiler.encoding>
    <maven.compiler.source>21</maven.compiler.source>
    <maven.compiler.target>21</maven.compiler.target>
    <maven.test.skip>true</maven.test.skip>
    <maven-war-plugin.version>3.3.2</maven-war-plugin.version>
    <opencsv.version>5.10</opencsv.version>
</properties>
```

- [ ] **Step 3: 验证**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject
grep -E "<java.version>|<spring-boot.version>|<spring-cloud.version>|<maven.compiler.source>" pom.xml
```

期望输出含：`21`, `4.0.6`, `2025.1.1`

---

## Task 2: 更新根 pom.xml — dependencyManagement

**Files:**
- Modify: `pom.xml`

- [ ] **Step 1: 替换 dependencyManagement 整块**

将 `pom.xml` 中 `<dependencyManagement>` 块完整替换为：

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${spring-cloud.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>

        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-core</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-dao</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-job</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-entity</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-rest</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-remote</artifactId>
            <version>${project.version}</version>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <version>${junit-jupiter.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-lang3</artifactId>
            <version>${commons-lang3.version}</version>
        </dependency>
        <dependency>
            <groupId>commons-io</groupId>
            <artifactId>commons-io</artifactId>
            <version>${commons-io.version}</version>
        </dependency>
        <dependency>
            <groupId>commons-collections</groupId>
            <artifactId>commons-collections</artifactId>
            <version>${commons-collections.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.commons</groupId>
            <artifactId>commons-collections4</artifactId>
            <version>${commons-collections4.version}</version>
        </dependency>
        <dependency>
            <groupId>cn.hutool</groupId>
            <artifactId>hutool-all</artifactId>
            <version>${hutool-all.version}</version>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <version>${lombok.version}</version>
            <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>com.alibaba.fastjson2</groupId>
            <artifactId>fastjson2</artifactId>
            <version>${fastjson2.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>${fastjson.version}</version>
        </dependency>
        <dependency>
            <groupId>com.fasterxml.jackson.core</groupId>
            <artifactId>jackson-databind</artifactId>
            <version>${jackson-databind.version}</version>
        </dependency>

        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>${guava.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>${gson.version}</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>easyexcel</artifactId>
            <version>${easyexcel.version}</version>
        </dependency>

        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>${mysql-connector-j.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
            <version>${druid.version}</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-3-starter</artifactId>
            <version>${druid-spring-boot-3-starter.version}</version>
        </dependency>

        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <version>${mybatis-plus-spring-boot3-starter.version}</version>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-annotation</artifactId>
            <version>${mybatis-plus-spring-boot3-starter.version}</version>
            <scope>compile</scope>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis-spring-boot-starter.version}</version>
        </dependency>

        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-job-core</artifactId>
            <version>${xxl-job.version}</version>
        </dependency>
        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-tool</artifactId>
            <version>${xxl-tool.version}</version>
        </dependency>
        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-sso-core</artifactId>
            <version>${xxl-sso.version}</version>
        </dependency>

        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
            <version>${logstash-logback-encoder.version}</version>
        </dependency>
        <dependency>
            <groupId>org.apache.skywalking</groupId>
            <artifactId>apm-toolkit-logback-1.x</artifactId>
            <version>${apm-toolkit-logback-1.x.version}</version>
        </dependency>

        <dependency>
            <groupId>com.opencsv</groupId>
            <artifactId>opencsv</artifactId>
            <version>${opencsv.version}</version>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>dashscope-sdk-java</artifactId>
            <version>${dashscope-sdk-java.version}</version>
        </dependency>

        <dependency>
            <groupId>com.qcloud</groupId>
            <artifactId>cos_api</artifactId>
            <version>${cos_api.version}</version>
        </dependency>
        <dependency>
            <groupId>com.qcloud</groupId>
            <artifactId>cos-sts_api</artifactId>
            <version>${cos-sts_api.version}</version>
        </dependency>

        <dependency>
            <groupId>net.coobird</groupId>
            <artifactId>thumbnailator</artifactId>
            <version>${thumbnailator.version}</version>
        </dependency>
        <dependency>
            <groupId>com.luciad.imageio.webp</groupId>
            <artifactId>webp-imageio-core</artifactId>
            <version>${webp-imageio-core.version}</version>
        </dependency>
        <dependency>
            <groupId>dev.matrixlab</groupId>
            <artifactId>webp4j</artifactId>
            <version>1.4.0</version>
        </dependency>

        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-codec-http</artifactId>
            <version>${netty.version}</version>
        </dependency>
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j-api.version}</version>
        </dependency>
        <dependency>
            <groupId>jakarta.annotation</groupId>
            <artifactId>jakarta.annotation-api</artifactId>
            <version>${jakarta.annotation-api.version}</version>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
            <version>4.10.0</version>
        </dependency>
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
            <version>5.7.0</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

- [ ] **Step 2: 更新 build 插件版本**

将 `pom.xml` 中 `<build>` 块替换为：

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.13.0</version>
            <configuration>
                <source>${java.version}</source>
                <target>${java.version}</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

- [ ] **Step 3: 验证 pom.xml 语法**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject
mvn help:effective-pom -N 2>&1 | tail -5
```

期望：无 BUILD FAILURE

- [ ] **Step 4: Commit**

```bash
git add pom.xml
git commit -m "upgrade: root pom to Spring Boot 4.0.6 / JDK 21 / Spring Cloud 2025.1.1"
```

---

## Task 3: 更新 halo-entity/pom.xml

**Files:**
- Modify: `halo-entity/pom.xml`

- [ ] **Step 1: 修改 hibernate-validator groupId**

将 `halo-entity/pom.xml` 中：
```xml
<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```
替换为：
```xml
<dependency>
    <groupId>org.hibernate.validator</groupId>
    <artifactId>hibernate-validator</artifactId>
</dependency>
```

- [ ] **Step 2: Commit**

```bash
git add halo-entity/pom.xml
git commit -m "upgrade: halo-entity pom - hibernate-validator groupId"
```

---

## Task 4: 更新 halo-dao/pom.xml

**Files:**
- Modify: `halo-dao/pom.xml`

- [ ] **Step 1: 更新 mysql connector 和 mybatis-plus artifact**

将 `halo-dao/pom.xml` 完整的 `<dependencies>` 替换为：

```xml
<dependencies>
    <dependency>
        <groupId>com.halo.demo</groupId>
        <artifactId>halo-entity</artifactId>
    </dependency>

    <dependency>
        <groupId>com.baomidou</groupId>
        <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
    </dependency>

    <dependency>
        <groupId>com.mysql</groupId>
        <artifactId>mysql-connector-j</artifactId>
    </dependency>

    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>
</dependencies>
```

- [ ] **Step 2: Commit**

```bash
git add halo-dao/pom.xml
git commit -m "upgrade: halo-dao pom - mysql-connector-j, mybatis-plus-spring-boot3-starter"
```

---

## Task 5: 更新 halo-common/pom.xml

**Files:**
- Modify: `halo-common/pom.xml`

- [ ] **Step 1: 更新 dependencies**

将 `halo-common/pom.xml` 完整的 `<dependencies>` 替换为：

```xml
<dependencies>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
    </dependency>

    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>

    <dependency>
        <groupId>commons-collections</groupId>
        <artifactId>commons-collections</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-collections4</artifactId>
    </dependency>
    <dependency>
        <groupId>org.apache.commons</groupId>
        <artifactId>commons-lang3</artifactId>
    </dependency>

    <dependency>
        <groupId>com.google.guava</groupId>
        <artifactId>guava</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>easyexcel</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
    </dependency>

    <dependency>
        <groupId>org.hibernate.validator</groupId>
        <artifactId>hibernate-validator</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-tx</artifactId>
    </dependency>

    <dependency>
        <groupId>com.opencsv</groupId>
        <artifactId>opencsv</artifactId>
    </dependency>

    <dependency>
        <groupId>com.alibaba</groupId>
        <artifactId>dashscope-sdk-java</artifactId>
    </dependency>

    <dependency>
        <groupId>com.qcloud</groupId>
        <artifactId>cos_api</artifactId>
    </dependency>
    <dependency>
        <groupId>com.qcloud</groupId>
        <artifactId>cos-sts_api</artifactId>
    </dependency>

    <dependency>
        <groupId>net.coobird</groupId>
        <artifactId>thumbnailator</artifactId>
    </dependency>
    <dependency>
        <groupId>dev.matrixlab</groupId>
        <artifactId>webp4j</artifactId>
        <version>1.4.0</version>
    </dependency>

    <dependency>
        <groupId>cn.hutool</groupId>
        <artifactId>hutool-all</artifactId>
    </dependency>
</dependencies>
```

- [ ] **Step 2: Commit**

```bash
git add halo-common/pom.xml
git commit -m "upgrade: halo-common pom - remove javax.servlet-api, logback, fix hibernate-validator groupId"
```

---

## Task 6: 更新 halo-rest/pom.xml

**Files:**
- Modify: `halo-rest/pom.xml`

- [ ] **Step 1: 更新 dependencies 和 build 插件**

将 `halo-rest/pom.xml` 完整替换为：

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.halo.demo</groupId>
        <artifactId>MyProject</artifactId>
        <version>1.4.10-RELEASE</version>
    </parent>

    <artifactId>halo-rest</artifactId>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>

        <dependency>
            <groupId>net.logstash.logback</groupId>
            <artifactId>logstash-logback-encoder</artifactId>
        </dependency>
        <dependency>
            <groupId>org.apache.skywalking</groupId>
            <artifactId>apm-toolkit-logback-1.x</artifactId>
        </dependency>

        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-common</artifactId>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-job</artifactId>
        </dependency>
        <dependency>
            <groupId>com.halo.demo</groupId>
            <artifactId>halo-remote</artifactId>
        </dependency>

        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-3-starter</artifactId>
        </dependency>

        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
        </dependency>
        <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>

        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
        </dependency>

        <dependency>
            <groupId>org.seleniumhq.selenium</groupId>
            <artifactId>selenium-java</artifactId>
        </dependency>
        <dependency>
            <groupId>io.github.bonigarcia</groupId>
            <artifactId>webdrivermanager</artifactId>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
                <configuration>
                    <executable>true</executable>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
        </resources>
    </build>
</project>
```

- [ ] **Step 2: Commit**

```bash
git add halo-rest/pom.xml
git commit -m "upgrade: halo-rest pom - remove javax.servlet-api/junit/mail, add starter-mail, fix plugin version"
```

---

## Task 7: 更新 halo-xxl-job-core/pom.xml

**Files:**
- Modify: `halo-xxl-job/halo-xxl-job-core/pom.xml`

- [ ] **Step 1: 完整替换 pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.halo.demo.xxljob</groupId>
        <artifactId>halo-xxl-job</artifactId>
        <version>2.4.0</version>
    </parent>

    <artifactId>halo-xxl-job-core</artifactId>
    <packaging>jar</packaging>
    <name>${project.artifactId}</name>

    <dependencies>
        <!-- slf4j -->
        <dependency>
            <groupId>org.slf4j</groupId>
            <artifactId>slf4j-api</artifactId>
            <version>${slf4j-api.version}</version>
        </dependency>

        <!-- jakarta.annotation-api -->
        <dependency>
            <groupId>jakarta.annotation</groupId>
            <artifactId>jakarta.annotation-api</artifactId>
            <version>${jakarta.annotation-api.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- embed server: netty + gson -->
        <dependency>
            <groupId>io.netty</groupId>
            <artifactId>netty-codec-http</artifactId>
            <version>${netty.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.code.gson</groupId>
            <artifactId>gson</artifactId>
            <version>${gson.version}</version>
        </dependency>

        <!-- xxl-tool -->
        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-tool</artifactId>
            <version>${xxl-tool.version}</version>
        </dependency>

        <!-- groovy -->
        <dependency>
            <groupId>org.apache.groovy</groupId>
            <artifactId>groovy</artifactId>
            <version>5.0.5</version>
        </dependency>

        <!-- spring-context -->
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-context</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

- [ ] **Step 2: Commit**

```bash
git add halo-xxl-job/halo-xxl-job-core/pom.xml
git commit -m "upgrade: halo-xxl-job-core pom - jakarta.annotation, xxl-tool, netty/gson versions"
```

---

## Task 8: 更新 halo-xxl-job-admin/pom.xml

**Files:**
- Modify: `halo-xxl-job/halo-xxl-job-admin/pom.xml`

- [ ] **Step 1: 完整替换 pom.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    <parent>
        <groupId>com.halo.demo.xxljob</groupId>
        <artifactId>halo-xxl-job</artifactId>
        <version>2.4.0</version>
    </parent>

    <artifactId>halo-xxl-job-admin</artifactId>
    <packaging>jar</packaging>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-parent</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>

        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>${mybatis-spring-boot-starter.version}</version>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>${mysql-connector-j.version}</version>
        </dependency>

        <!-- xxl-job-core -->
        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-job-core</artifactId>
            <version>${project.parent.version}</version>
        </dependency>

        <!-- xxl-sso -->
        <dependency>
            <groupId>com.xuxueli</groupId>
            <artifactId>xxl-sso-core</artifactId>
            <version>${xxl-sso.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <version>${spring-boot.version}</version>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

- [ ] **Step 2: Commit**

```bash
git add halo-xxl-job/halo-xxl-job-admin/pom.xml
git commit -m "upgrade: halo-xxl-job-admin pom - mybatis 4.0.1, xxl-sso-core"
```

---

## Task 9: 批量 javax → jakarta 源码迁移

**Files:**
- Modify: 所有 `halo-*/src/**/*.java`（排除 halo-xxl-job，其源码将在 Task 11/12 整体替换）

- [ ] **Step 1: 批量替换 import**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject

# 只处理非 xxl-job 的模块
for module in halo-common halo-core halo-dao halo-entity halo-job halo-remote halo-rest; do
  find ${module}/src -name "*.java" -exec sed -i '' \
    -e 's/import javax\.servlet\./import jakarta.servlet./g' \
    -e 's/import javax\.validation\./import jakarta.validation./g' \
    -e 's/import javax\.annotation\.PostConstruct/import jakarta.annotation.PostConstruct/g' \
    -e 's/import javax\.annotation\.PreDestroy/import jakarta.annotation.PreDestroy/g' \
    -e 's/import javax\.annotation\.Resource/import jakarta.annotation.Resource/g' \
    -e 's/import javax\.mail\./import jakarta.mail./g' \
    {} \;
done
```

- [ ] **Step 2: 验证迁移结果**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject
# 检查是否还有需要迁移的 javax 导入（不含 JDK 内置包）
grep -r "^import javax\.\(servlet\|validation\|annotation\.PostConstruct\|annotation\.PreDestroy\|annotation\.Resource\|mail\)\." \
  halo-common halo-core halo-dao halo-entity halo-job halo-remote halo-rest \
  --include="*.java" | grep -v "target/"
```

期望：无输出（0 条匹配）

- [ ] **Step 3: 确认 JDK 内置 javax 包未被修改**

```bash
grep -r "import javax\." \
  halo-common halo-core halo-dao halo-entity halo-job halo-remote halo-rest \
  --include="*.java" | grep -v "target/" | grep -v "//import"
```

期望：只看到 `javax.crypto`、`javax.imageio`、`javax.net`、`javax.sql` 相关行

- [ ] **Step 4: Commit**

```bash
git add halo-common halo-core halo-dao halo-entity halo-job halo-remote halo-rest
git commit -m "upgrade: migrate javax→jakarta imports in all non-xxl-job modules"
```

---

## Task 10: 更新 application.yml — Spring Boot 4.x 配置适配

**Files:**
- Modify: `halo-rest/src/main/resources/application.yml`
- Modify: `halo-rest/src/main/resources/application-lry.yml`

- [ ] **Step 1: 修改 application.yml**

在 `application.yml` 中做以下修改：

**a) 删除 `server.http.encoding` 块**（第 8-10 行），从：
```yaml
  http:
    encoding:
      charset: UTF-8
```
删除这 3 行。

**b) 删除 `spring.http.encoding` 块**（约第 29-33 行），从：
```yaml
  http:
    encoding:
      force: true
      charset: UTF-8
      enabled: true
```
删除这 5 行。

**c) 将 Redis 配置从 `spring.redis.*` 改为 `spring.data.redis.*`**，找到：
```yaml
# Redis配置
  redis:
    database: 2
    host: 192.168.158.5
    port: 6379
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: 1000
```
替换为（注意缩进在 `spring:` 下）：
```yaml
# Redis配置
  data:
    redis:
      database: 2
      host: 192.168.158.5
      port: 6379
      jedis:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: 1000
```

- [ ] **Step 2: 修改 application-lry.yml**

同样将 Redis 配置改为 `spring.data.redis.*`，找到：
```yaml
# Redis配置
  redis:
    database: 2
#    host: 192.168.158.5
    host: 127.0.0.1
    port: 6379
    jedis:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: 1000
```
替换为：
```yaml
# Redis配置
  data:
    redis:
      database: 2
#      host: 192.168.158.5
      host: 127.0.0.1
      port: 6379
      jedis:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 0
          max-wait: 1000
```

删除 `server.http.encoding` 块（如存在）。

- [ ] **Step 3: Commit**

```bash
git add halo-rest/src/main/resources/application.yml \
        halo-rest/src/main/resources/application-lry.yml
git commit -m "upgrade: application.yml - spring.redis→spring.data.redis, remove spring.http.encoding"
```

---

## Task 11: 拉取 xxl-job v3.4.0 源码

**Files:**
- 无源码修改，操作临时目录

- [ ] **Step 1: Clone xxl-job v3.4.0 到临时目录**

```bash
git clone --depth 1 --branch v3.4.0 https://github.com/xuxueli/xxl-job.git /tmp/xxl-job-v3.4.0
```

期望：克隆成功，看到 `Receiving objects: 100%`

- [ ] **Step 2: 验证源码存在**

```bash
ls /tmp/xxl-job-v3.4.0/xxl-job-core/src/main/java/com/xxl/job/core/
ls /tmp/xxl-job-v3.4.0/xxl-job-admin/src/main/java/com/xxl/job/admin/
```

期望：看到 biz、executor、glue 等目录

---

## Task 12: 替换 halo-xxl-job-core 源码为 v3.4.0

**Files:**
- Replace: `halo-xxl-job/halo-xxl-job-core/src/`

- [ ] **Step 1: 备份并替换源码**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject

# 清空旧源码
rm -rf halo-xxl-job/halo-xxl-job-core/src/

# 复制 v3.4.0 xxl-job-core 源码
cp -r /tmp/xxl-job-v3.4.0/xxl-job-core/src \
      halo-xxl-job/halo-xxl-job-core/src
```

- [ ] **Step 2: 验证**

```bash
find halo-xxl-job/halo-xxl-job-core/src -name "*.java" | wc -l
```

期望：文件数量大于 0（v3.4.0 应有约 30+ 个 Java 文件）

- [ ] **Step 3: 确认已使用 jakarta 命名空间**

```bash
grep -r "import jakarta\.annotation" halo-xxl-job/halo-xxl-job-core/src/ | head -3
grep -r "import javax\.annotation" halo-xxl-job/halo-xxl-job-core/src/ | grep -v "//import"
```

期望：第一条有输出，第二条无输出

- [ ] **Step 4: Commit**

```bash
git add halo-xxl-job/halo-xxl-job-core/src/
git commit -m "upgrade: replace halo-xxl-job-core source with xxl-job v3.4.0"
```

---

## Task 13: 替换 halo-xxl-job-admin 源码为 v3.4.0

**Files:**
- Replace: `halo-xxl-job/halo-xxl-job-admin/src/`

- [ ] **Step 1: 清空并复制**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject

rm -rf halo-xxl-job/halo-xxl-job-admin/src/

cp -r /tmp/xxl-job-v3.4.0/xxl-job-admin/src \
      halo-xxl-job/halo-xxl-job-admin/src
```

- [ ] **Step 2: 验证**

```bash
find halo-xxl-job/halo-xxl-job-admin/src -name "*.java" | wc -l
```

期望：文件数量大于 0（v3.4.0 应有 50+ 个 Java 文件）

- [ ] **Step 3: 确认已使用 jakarta 命名空间**

```bash
grep -r "import jakarta\.servlet" halo-xxl-job/halo-xxl-job-admin/src/ | wc -l
grep -r "import javax\.servlet" halo-xxl-job/halo-xxl-job-admin/src/ | grep -v "//import"
```

期望：第一条 > 0，第二条无输出

- [ ] **Step 4: Commit**

```bash
git add halo-xxl-job/halo-xxl-job-admin/src/
git commit -m "upgrade: replace halo-xxl-job-admin source with xxl-job v3.4.0"
```

---

## Task 14: 编译验证

**Files:**
- 无修改，仅验证

- [ ] **Step 1: 执行全量编译**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject
mvn clean compile -DskipTests 2>&1 | tail -30
```

期望：`BUILD SUCCESS`

- [ ] **Step 2: 如有编译错误，记录错误类型**

若 BUILD FAILURE，执行：
```bash
mvn clean compile -DskipTests 2>&1 | grep "\[ERROR\]" | head -30
```

常见错误及修复方向：
- `cannot find symbol: javax.xxx` → 检查是否有遗漏的 javax import，重跑 Task 9 的 sed 命令
- `package does not exist` → 检查对应模块 pom 中依赖是否已添加
- `xxl-sso-core not found` → 验证 Maven 网络连接，执行 `mvn dependency:resolve`
- `incompatible types` → Spring Boot 4 / Spring 7 API 变更，需具体分析

- [ ] **Step 3: 若有遗漏的 javax import 修复**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject
# 检查所有模块中遗留的 javax 迁移问题
grep -rn "^import javax\.\(servlet\|validation\|annotation\.PostConstruct\|annotation\.PreDestroy\|annotation\.Resource\|mail\)\." \
  --include="*.java" . | grep -v "target/"
```

对每个找到的文件手动修正 import 后重新编译。

- [ ] **Step 4: 编译成功后 Commit**

```bash
git add -A
git commit -m "upgrade: fix compilation errors post Spring Boot 4.0.6 migration"
```

---

## Task 15: 清理临时文件

- [ ] **Step 1: 删除临时克隆目录**

```bash
rm -rf /tmp/xxl-job-v3.4.0
```

- [ ] **Step 2: 最终验证**

```bash
cd /Users/liuruyi/IdeaProjects/MyProject
mvn clean compile -DskipTests 2>&1 | grep -E "BUILD (SUCCESS|FAILURE)"
```

期望：`BUILD SUCCESS`

- [ ] **Step 3: 查看本次升级所有提交**

```bash
git log --oneline master_jdk21 | head -20
```

---

## 数据库迁移（手动执行）

升级 halo-xxl-job-admin 数据库，执行以下 SQL：

```sql
CREATE INDEX I_jobgroup ON xxl_job_log (job_group);

ALTER TABLE xxl_job_group
    MODIFY title VARCHAR(64) NOT NULL COMMENT '执行器名称';

ALTER TABLE xxl_job_registry
    MODIFY id BIGINT(20) NOT NULL AUTO_INCREMENT;

ALTER TABLE xxl_job_info
    MODIFY executor_param TEXT NULL COMMENT '任务参数';

ALTER TABLE xxl_job_log
    MODIFY executor_param TEXT NULL COMMENT '任务参数';
```
