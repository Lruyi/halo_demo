package com.halo.controller;


import com.alibaba.dashscope.audio.tts.SpeechSynthesisAudioFormat;
import com.alibaba.dashscope.audio.tts.SpeechSynthesisParam;
import com.alibaba.dashscope.audio.tts.SpeechSynthesizer;

import java.io.File;
import java.io.FileOutputStream;

/**
 * @Description: 同步调用示例（一次性获取结果）
 * https://help.aliyun.com/zh/model-studio/sambert-java-sdk#a727da82951p6
 * @Author: halo_ry
 * @Date: 2025/8/5 18:32
 */
public class SyncTTSController {

    public static void main(String[] args) throws Exception {
        // 执行以下命令来将环境变量设置追加到 ~/.bash_profile 文件中。
        // # 用您的百炼API Key代替YOUR_DASHSCOPE_API_KEY  sk-f967bc183ab443639fd1fc76064274ee
        // echo "export DASHSCOPE_API_KEY='YOUR_DASHSCOPE_API_KEY'" >> ~/.bash_profile

        // 1. 初始化合成参数
        SpeechSynthesisParam param = SpeechSynthesisParam.builder()
                // 若没有将API Key配置到环境变量中，需将下面这行代码注释放开，并将apiKey替换为自己的API Key
//                .apiKey("yourApikey")
//                .text("今天天气怎么样")                     // 合成文本
                .text("<speak voice=\"sambert-zhimao-v1\" rate=\"-200\" volume=\"80\">\n" +
                        "  这不是我的影子！\n" +
                        "</speak>")                     // 合成文本
                .model("sambert-zhichu-v1")               // Sambert知厨发音人模型
//                .rate(1.75f)                            // 语速（默认1.0）
                .sampleRate(48000)                        // 采样率（支持16000/48000）
                .format(SpeechSynthesisAudioFormat.WAV)   // 音频格式（默认WAV）
                .build();

        // 2. 执行合成并获取结果
        SpeechSynthesizer synthesizer = new SpeechSynthesizer();
        byte[] audioData = synthesizer.call(param).array();

        // 3. 保存音频文件
        String outputPath = "/Users/liuruyi/Downloads/output19.wav";
        File outputFile = new File(outputPath);

        // 确保父目录存在
        File parentDir = outputFile.getParentFile();
        if (!parentDir.exists()) {
            parentDir.mkdirs();
        }
        try (FileOutputStream fos = new FileOutputStream(outputFile)) {
            fos.write(audioData);
        }
        System.out.println("合成完成，音频已保存至 output17.wav");
    }
}
