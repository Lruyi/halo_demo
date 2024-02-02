package com.halo.controller;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.TypeReference;
import com.google.common.collect.Lists;
import com.halo.common.CommonServerResult;
import com.halo.common.Result;
import com.halo.entity.UserInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description: 去读文件
 * @author: halo_ry
 * @Date: 2024/1/22 11:05
 */
@RestController
@RequestMapping("/file")
public class FileExampleController {

    /**
     * 读取txt文件
     * @return
     */
    @GetMapping("/readTxtFile")
    public Object readFile() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student.txt");
        try {
            // 读取文件所有行
            List<String> lines = Files.readAllLines(filePath);
            for (String line : lines) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Result.getSuccess("读取文件完成");
    }

    /**
     * 将内容写入txt文件
     * @return
     */
    @GetMapping("/writeTxtFile")
    public Object writeFile() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/writeTxt.txt");
        ArrayList<String> content = Lists.newArrayList("student_uid", "class_id", "create_time");
        try {
            Files.write(filePath, content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.getSuccess("写入文件成功");
    }

    /**
     * 复制文件
     */
    @GetMapping("/copeTxtFile")
    public Object copeFile() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student.txt");
        // copy到指定的新文件
        Path filePath2 = Paths.get("/Users/liuruyi/Downloads/student2.txt");
        try {
            Files.copy(filePath, filePath2);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.getSuccess("复制文件成功");
    }

    /**
     * 删除文件
     */
    @GetMapping("/deleteTxtFile")
    public Object deleteFile() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student2.txt");
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.getSuccess("删除文件成功");
    }

    /**
     * 判断文件是否存在
     */
    @GetMapping("/fileTxtExists")
    public void fileExists() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student2.txt");
        try {
            boolean exists = Files.exists(filePath);
            System.out.println("File exists: " + exists);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 读取指定TXT文件，并一次性全部写入指定TXT文件
     */
    @GetMapping("/readWriteTxtFile")
    public Object readWriteTxtFile() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student.txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            // 写入文件
            Path filePath2 = Paths.get("/Users/liuruyi/Downloads/student3.txt");
            // 一次性全部写入
            List<String> lineFormates = lines.stream().map(line -> line.replace("\t", "~").replace("\t", "~")).collect(Collectors.toList());

            Files.write(filePath2, lineFormates);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.getSuccess("操作成功");
    }

    /**
     * 读取指定TXT文件，并一行一行写入指定TXT文件
     */
    @GetMapping("/readWriteTxtFile4OneLine")
    public Object readWriteTxtFile4OneLine() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student.txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            // 写入文件
            Path filePath2 = Paths.get("/Users/liuruyi/Downloads/student4.txt");
            BufferedWriter writer = Files.newBufferedWriter(filePath2);
            // 逐行写入内容
            boolean isFirstLine = false;
            for (String line : lines) {
                String lineReplace = line.replace("\t", "   ").replace("\t", "  ");
                if (!isFirstLine) {
                    writer.write(lineReplace + "    " + "update_time");
                    writer.newLine(); // 换行
                    isFirstLine = true;
                    continue;
                }
                writer.write(lineReplace + "    " + new Date());
                writer.newLine(); // 换行
            }
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.getSuccess("操作成功");
    }

    /**
     * 读取指定TXT文件，并一行一行写入指定TXT文件,调用远程服务拼接内容
     */
    @GetMapping("/readWriteTxtFile4OneLineRemoteServer")
    public Object readWriteTxtFile4OneLineRemoteServer() {
        // 读取文件
        Path filePath = Paths.get("/Users/liuruyi/Downloads/student_copy.txt");
        try {
            List<String> lines = Files.readAllLines(filePath);
            // 写入文件
            Path filePath2 = Paths.get("/Users/liuruyi/Downloads/student5.txt");
            BufferedWriter writer = Files.newBufferedWriter(filePath2);
            // 逐行写入内容
            boolean isFirstLine = false;
            for (String line : lines) {
                String lineReplace = line.replace("\t", "   ").replace("\t", "  ");
                if (!isFirstLine) {
                    lineReplace = lineReplace + "    " + "userCard" + "    " + "userName";
                    writer.write(lineReplace);
                    writer.newLine(); // 换行
                    isFirstLine = true;
                    continue;
                }
                UserInfo userInfo = this.getUserInfo(line);
                lineReplace = lineReplace + "    " + userInfo.getCard() + "    " + userInfo.getUsername();
                writer.write(lineReplace);
                writer.newLine(); // 换行
            }
            // 以确保缓冲区的内容被刷新到文件
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return Result.getSuccess("操作成功");
    }

    private UserInfo getUserInfo(String line) {
        String[] split = line.replace("\t", "|").split("\\|");
        String userUid = split[0];
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("uid", userUid);
        paramMap.put("loginCity", "010");
        paramMap.put("isAcceptSms", 1);
        String url = "ucenter-one-beta-inner.speiyou.cn/users/get";
        String s3 = HttpUtil.get(url, paramMap);
//      String s3 = HttpUtil.get("ucenter-one-beta-inner.speiyou.cn/users/get?uid=1021373462&loginCity=010&isAcceptSms=1");
        CommonServerResult<UserInfo> result = JSON.parseObject(s3, new TypeReference<CommonServerResult<UserInfo>>() {});
        return Optional.ofNullable(result.getData()).orElse(new UserInfo());
    }
}
