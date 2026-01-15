package com.halo.demo;

import com.halo.utils.HmacSha256Util;

/**
 * HMAC-SHA256签名测试类
 * 模拟A服务和B服务的签名生成和验证流程
 * 
 * @author halo_ry
 */
public class HmacSha256Test {

    /**
     * B服务分配给A服务的密钥
     */
    private static final String SECRET_KEY = "default-secret-key-123456";
    
    /**
     * 时间戳有效期（毫秒），10分钟
     */
    private static final long TIMESTAMP_VALID_PERIOD = 10 * 60 * 1000L;

    public static void main(String[] args) {
        System.out.println("========== HMAC-SHA256签名测试开始 ==========\n");
        
        // 测试1: 正常流程测试
        testNormalFlow();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // 测试2: 签名错误测试
        testInvalidSign();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // 测试3: 时间戳过期测试
        testExpiredTimestamp();
        
        System.out.println("\n" + "=".repeat(50) + "\n");
        
        // 测试4: 多次测试不同empNo
        testMultipleEmpNos();
        
        System.out.println("\n========== HMAC-SHA256签名测试结束 ==========");
    }

    /**
     * 测试正常流程：A服务生成签名，B服务验证签名
     */
    private static void testNormalFlow() {
        System.out.println("【测试1】正常流程测试");
        System.out.println("-".repeat(50));
        
        String empNo = "EMP001";
        
        // A服务：生成签名
        long timestamp = System.currentTimeMillis();
        String signData = HmacSha256Util.buildSignData(empNo, timestamp);
        String sign = HmacSha256Util.generateSign(signData, SECRET_KEY);
        
        System.out.println("A服务生成签名:");
        System.out.println("  empNo: " + empNo);
        System.out.println("  timestamp: " + timestamp);
        System.out.println("  signData: " + signData);
        System.out.println("  sign: " + sign);
        
        // B服务：验证签名
        boolean isValid = HmacSha256Util.verifySign(signData, SECRET_KEY, sign);
        long currentTime = System.currentTimeMillis();
        long timeDiff = Math.abs(currentTime - timestamp);
        boolean isTimestampValid = timeDiff <= TIMESTAMP_VALID_PERIOD;
        
        System.out.println("\nB服务验证结果:");
        System.out.println("  签名验证: " + (isValid ? "✓ 通过" : "✗ 失败"));
        System.out.println("  时间戳验证: " + (isTimestampValid ? "✓ 通过 (时间差: " + timeDiff + "ms)" : "✗ 失败"));
        System.out.println("  最终结果: " + (isValid && isTimestampValid ? "✓ 验证成功" : "✗ 验证失败"));
    }

    /**
     * 测试签名错误的情况
     */
    private static void testInvalidSign() {
        System.out.println("【测试2】签名错误测试");
        System.out.println("-".repeat(50));
        
        String empNo = "EMP002";
        
        // A服务：生成正确的签名
        long timestamp = System.currentTimeMillis();
        String signData = HmacSha256Util.buildSignData(empNo, timestamp);
        String correctSign = HmacSha256Util.generateSign(signData, SECRET_KEY);
        
        // 模拟签名被篡改
        String wrongSign = correctSign + "tampered";
        
        System.out.println("A服务生成签名:");
        System.out.println("  empNo: " + empNo);
        System.out.println("  timestamp: " + timestamp);
        System.out.println("  signData: " + signData);
        System.out.println("  正确签名: " + correctSign);
        System.out.println("  错误签名: " + wrongSign);
        
        // B服务：验证错误的签名
        boolean isValid = HmacSha256Util.verifySign(signData, SECRET_KEY, wrongSign);
        
        System.out.println("\nB服务验证结果:");
        System.out.println("  签名验证: " + (isValid ? "✓ 通过" : "✗ 失败"));
        System.out.println("  最终结果: " + (isValid ? "✓ 验证成功" : "✗ 验证失败（签名不匹配）"));
    }

    /**
     * 测试时间戳过期的情况
     */
    private static void testExpiredTimestamp() {
        System.out.println("【测试3】时间戳过期测试");
        System.out.println("-".repeat(50));
        
        String empNo = "EMP003";
        
        // 模拟11分钟前的时间戳（已过期）
        long expiredTimestamp = System.currentTimeMillis() - (11 * 60 * 1000L);
        String signData = HmacSha256Util.buildSignData(empNo, expiredTimestamp);
        String sign = HmacSha256Util.generateSign(signData, SECRET_KEY);
        
        System.out.println("A服务生成签名（使用过期时间戳）:");
        System.out.println("  empNo: " + empNo);
        System.out.println("  timestamp: " + expiredTimestamp + " (11分钟前)");
        System.out.println("  signData: " + signData);
        System.out.println("  sign: " + sign);
        
        // B服务：验证签名和时间戳
        boolean isValid = HmacSha256Util.verifySign(signData, SECRET_KEY, sign);
        long currentTime = System.currentTimeMillis();
        long timeDiff = Math.abs(currentTime - expiredTimestamp);
        boolean isTimestampValid = timeDiff <= TIMESTAMP_VALID_PERIOD;
        
        System.out.println("\nB服务验证结果:");
        System.out.println("  签名验证: " + (isValid ? "✓ 通过" : "✗ 失败"));
        System.out.println("  时间戳验证: " + (isTimestampValid ? "✓ 通过" : "✗ 失败 (时间差: " + timeDiff + "ms，超过10分钟)"));
        System.out.println("  最终结果: " + (isValid && isTimestampValid ? "✓ 验证成功" : "✗ 验证失败（时间戳已过期）"));
    }

    /**
     * 测试多个不同的empNo
     */
    private static void testMultipleEmpNos() {
        System.out.println("【测试4】多个empNo测试");
        System.out.println("-".repeat(50));
        
        String[] empNos = {"EMP001", "EMP002", "EMP003", "EMP999"};
        
        for (String empNo : empNos) {
            long timestamp = System.currentTimeMillis();
            String signData = HmacSha256Util.buildSignData(empNo, timestamp);
            String sign = HmacSha256Util.generateSign(signData, SECRET_KEY);
            boolean isValid = HmacSha256Util.verifySign(signData, SECRET_KEY, sign);
            
            System.out.println(String.format("empNo: %s -> 签名: %s -> 验证: %s", 
                    empNo, sign.substring(0, Math.min(20, sign.length())) + "...", 
                    isValid ? "✓" : "✗"));
        }
        
        System.out.println("\n所有empNo签名验证均通过！");
    }
}
