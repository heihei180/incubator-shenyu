/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.springboot.starter.plugin.alibaba.dubbo;

import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.plugin.api.ShenyuPlugin;
import org.apache.shenyu.plugin.base.handler.PluginDataHandler;
import org.apache.shenyu.sync.data.api.MetaDataSubscriber;
import org.junit.Before;
import org.junit.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.context.annotation.Configuration;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

/**
 * Test case for {@link AlibabaDubboPluginConfiguration}.
 */
@Configuration
@EnableConfigurationProperties
public class AlibabaDubboPluginConfigurationTest {

    private ApplicationContextRunner applicationContextRunner;

    @Before
    public void before() {
        applicationContextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(AlibabaDubboPluginConfiguration.class))
            .withBean(AlibabaDubboPluginConfigurationTest.class)
            .withPropertyValues("debug=true");
    }

    @Test
    public void testAlibabaDubboPlugin() {
        applicationContextRunner.run(context -> {
                ShenyuPlugin plugin = context.getBean("alibabaDubboPlugin", ShenyuPlugin.class);
                assertNotNull(plugin);
                assertThat(plugin.getOrder(), is(PluginEnum.DUBBO.getCode()));
                assertThat(plugin.named(), is(PluginEnum.DUBBO.getName()));
            }
        );
    }

    @Test
    public void testAlibabaAbstractDubboPluginDataHandler() {
        applicationContextRunner.run(context -> {
                PluginDataHandler handler = context.getBean("alibabaDubboPluginDataHandler", PluginDataHandler.class);
                assertNotNull(handler);
                assertThat(handler.pluginNamed(), is(PluginEnum.DUBBO.getName()));
            }
        );
    }

    @Test
    public void testAlibabaDubboMetaDataSubscriber() {
        applicationContextRunner.run(context -> {
                MetaDataSubscriber subscriber = context.getBean("dubboMetaDataSubscriber", MetaDataSubscriber.class);
                assertNotNull(subscriber);
            }
        );
    }
}
