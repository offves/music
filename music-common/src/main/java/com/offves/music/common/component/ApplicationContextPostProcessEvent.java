package com.offves.music.common.component;

import com.offves.music.common.util.SpringContextUtils;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.Nonnull;

/**
 * ApplicationContext后置处理事件 ps: 应仅用于获取上下文
 * @author liaozan
 * @since 2019-10-13
 **/
public class ApplicationContextPostProcessEvent implements ApplicationListener<ApplicationPreparedEvent> {

    @Override
    public void onApplicationEvent(@Nonnull ApplicationPreparedEvent event) {
        SpringContextUtils.setApplicationContextIfNone(event.getApplicationContext());
    }

}
