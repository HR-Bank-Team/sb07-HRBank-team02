package com.codeit.hrbank.global.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Ipv4Converter {
    public String getClientIp(HttpServletRequest request) {
        // request에서 client의 실제 IP 추출
        String ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
            ip = ip.split(",")[0].trim();
        } else {
            ip = request.getRemoteAddr();
        }

        // IPv6 → IPv4 변환
        if ("0:0:0:0:0:0:0:1".equals(ip) || "::1".equals(ip)) { // 로컬의 경우
            ip = "127.0.0.1";
        } else if (ip.startsWith("::ffff:")) {
            ip = ip.substring(7);
        }

        return ip;
    }
}
