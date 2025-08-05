package com.halo.controller;

import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisResult;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;
import com.alibaba.dashscope.common.ResultCallback;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

/**
 * @Description: 流式调用示例（实时获取中间结果）
 *  https://help.aliyun.com/zh/model-studio/sambert-java-sdk#a727da82951p6
 * @Author: halo_ry
 * @Date: 2025/8/5 20:03
 */
public class StreamTTSController {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1); // 控制主线程等待
        ByteArrayOutputStream audioBuffer = new ByteArrayOutputStream();
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
//                 .apiKey("yourApikey")
//                .text("今天天气怎么样")
                .text("Good afternoon! The weather is great today. Let's go cycling and enjoy the beautiful day!")
                .model("sambert-donna-v1")
                .sampleRate(48000)
                .enableWordTimestamp(true)      // 启用单词级时间戳
                .enablePhonemeTimestamp(true)   // 启用音素级时间戳
                .build();

        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        synthesizer.call(param, new ResultCallback<SpeechSynthesisResult>() {
            @Override
            public void onEvent(SpeechSynthesisResult result) {
                if (result.getAudioFrame() != null) {
                    try {
                        // 将音频帧数据写入缓冲区
                        audioBuffer.write(result.getAudioFrame().array());
                        System.out.println("收到音频数据块，长度：" + result.getAudioFrame().array().length);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                if (result.getTimestamp() != null) {
                    // 处理时间戳信息
                    System.out.println("timestamp: " + result.getTimestamp());
                }
            }

            @Override
            public void onComplete() {
                System.out.println("合成完成");
                try {
                    saveAudioToFile(audioBuffer.toByteArray());
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                latch.countDown(); // 通知主线程结束
            }

            @Override
            public void onError(Exception e) {
                System.err.println("合成失败：" + e.getMessage());
                latch.countDown();
            }
        });

        latch.await(); // 等待合成完成
    }

    private static void saveAudioToFile(byte[] audioData) throws Exception {
        String outputPath = "/Users/liuruyi/Downloads/output_stream.wav";
        File outputFile = new File(outputPath);

        // 确保父目录存在
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }

        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(audioData);
        }
        System.out.println("音频已保存至 " + outputFile.getAbsolutePath());
    }
}
