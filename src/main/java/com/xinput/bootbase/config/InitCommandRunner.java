package com.xinput.bootbase.config;

import com.xinput.bootbase.consts.DefaultConsts;
import com.xinput.bootbase.util.Logs;
import com.xinput.bootbase.util.SimpleProperties;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 初始化一些操作，现在用来验证system.properties 文件是否存在
 *
 * @Author: xinput
 * @Date: 2020-06-19 14:12
 */
@Component
public class InitCommandRunner implements CommandLineRunner {

    private static final Logger logger = Logs.get();

    @Override
    public void run(String... args) throws Exception {
        try (InputStream is = SimpleProperties.getResourceAsStream(DefaultConsts.DEFAULT_SYSTEM_FILE)) {

        } catch (Exception e) {
            logger.warn("file:[{}] not exists.", DefaultConsts.DEFAULT_SYSTEM_FILE);
            System.err.println("Cannot read " + DefaultConsts.DEFAULT_SYSTEM_FILE);
        }
    }
}
