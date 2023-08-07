package com.halo.controller;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.halo.api.PeopleServiceApi;
import com.halo.common.Result;
import com.halo.entity.People;
import com.halo.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @Description:
 * @author: halo_ry
 * @Date: 2023/8/7 19:35
 */
@RestController
@RequestMapping("/QRCode")
@Slf4j
public class QRCodeController {

    @Autowired
    private PeopleServiceApi peopleServiceApi;

    /**
     * 生成二维码
     * @return
     */
    @GetMapping("/genQRCodeImage")
    public Object genQRCodeImage() {
        // 要生成二维码的数据
        String data = "This is my first QR Code";
        // 二维码图片的宽度
        int width = 300;
        // 二维码图片的高度
        int height = 300;

        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(data, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建BufferedImage对象并绘制二维码
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // 保存生成的二维码图片
            File qrCodeFile = new File("qrcode.png");
            ImageIO.write(image, "png", qrCodeFile);

            System.out.println("二维码生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 根据用户id生成二维码
     * @param id
     * @return
     */
    @GetMapping("/genQRCodeByPeopleId")
    public Result<Object> genQRCodeByPeopleId(@RequestParam("id") String id) {
        People people = peopleServiceApi.getOne(new LambdaQueryWrapper<People>().eq(People::getId, id));
        if (people == null) {
            throw new BusinessException("用户不存在");
        }
        String peopleStr = JSON.toJSONString(people);
        // 二维码图片的宽度
        int width = 300;
        // 二维码图片的高度
        int height = 300;

        // 设置二维码参数
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");

        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(peopleStr, BarcodeFormat.QR_CODE, width, height, hints);

            // 创建BufferedImage对象并绘制二维码
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bitMatrix.get(x, y) ? Color.BLACK.getRGB() : Color.WHITE.getRGB());
                }
            }

            // 保存生成的二维码图片
            File qrCodeFile = new File(people.getName() + "-qrcode.png");
            ImageIO.write(image, "png", qrCodeFile);

            System.out.println("二维码生成成功！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Result.getSuccess("二维码生成成功！");
    }
}
