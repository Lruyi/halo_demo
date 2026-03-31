package com.halo.controller;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.nio.file.Files;

/**
 * @Description: 网页截图控制器（目前版本不对，启动不了）
 * @Author: liuruyi
 * @Date: 2026/2/5 18:52
 */
public class WebpageScreenshotController {

    public static void main(String[] args) throws Exception {
        // ChromeDriver 路径（需要自行下载 chromedriver 并确保版本匹配 Chrome/Chromium）
//        System.setProperty("webdriver.chrome.driver", "/path/to/chromedriver");
        WebDriverManager.chromedriver().setup();
        // 设置 headless，以无界面模式运行
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1280,1024");
//        options.addArguments("--headless", "--disable-gpu", "--window-size=1280,1024");

        WebDriver driver = new ChromeDriver(options);

        try {
//            String url = "https://musk-online.fbcontent.cn/pub-musk-ai-studio/workflow/file/document/LMgXQ5vsgnKQQgqZ5Bx46J.html";
            String url = "https://musk-online.fbcontent.cn/pub-musk-ai-studio/workflow/file/document/LMgXQ5vsgnKQQgqZ5Bx46J.html";
            driver.get(url);

            // 截图
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // 保存到本地
            Files.copy(screenshot.toPath(), new File("page_screenshot.png").toPath());
            System.out.println("Saved screenshot to page_screenshot.png");

        } finally {
            driver.quit();
        }
    }
}
