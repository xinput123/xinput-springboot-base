package com.xinput.bootbase.config;

import com.xinput.bleach.util.Logs;
import com.xinput.bleach.util.SimpleProperties;
import com.xinput.bootbase.consts.BootBaseConsts;
import org.slf4j.Logger;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.InputStream;

/**
 * 初始化一些操作，现在用来验证system.properties 文件是否存在
 *
 * @author xinput
 * @date 2020-06-19 14:12
 */
@Component
public class BootBaseInit implements CommandLineRunner {

    private static final Logger logger = Logs.get();

    @Override
    public void run(String... args) throws Exception {
        try (InputStream is = SimpleProperties.getResourceAsStream(BootBaseConsts.DEFAULT_SYSTEM_FILE)) {

        } catch (Exception e) {
            logger.warn("file:[{}] not exists.", BootBaseConsts.DEFAULT_SYSTEM_FILE);
            System.err.println("Cannot read " + BootBaseConsts.DEFAULT_SYSTEM_FILE);
        }
    }
}
