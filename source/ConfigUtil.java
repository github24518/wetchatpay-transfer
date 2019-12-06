package com.coding.happy.go.common.utils;

import java.io.IOException;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

@Slf4j
public class ConfigUtil {

    private static Properties config = null;

    /**
     * 返回系统config.properties配置信息
     *
     * @param key key值
     * @return value值
     */
    public static String getProperty(String key) {
        if (config == null) {
            synchronized (ConfigUtil.class) {
                if (null == config) {
                    try {
                        Resource resource = new ClassPathResource("application.properties");
                        config = PropertiesLoaderUtils.loadProperties(resource);
                    } catch (IOException e) {
                        log.error("读取配置文件失败！");
                    }
                }
            }
        }
        return config.getProperty(key, "");
    }
}
