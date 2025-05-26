package ru.ir.visualiser.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


/** config singleton.
 * contains opt and workPath
 *
 */
@Configuration
@Getter
public class LocalConfig {
    @Value("${app.opt-path}")
    private String optPath;
    @Value("${app.work-space}")
    private String workPath;
}