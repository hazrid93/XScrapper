package com.jrp.pma.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = AppProperties.CONFIGURATION_PROPERTY_PREFIX,
                         ignoreUnknownFields = false)
public class AppProperties {
    // configuration properties https://tuhrig.de/using-configurationproperties-to-separate-service-and-configuration/
    // //https://stackoverflow.com/questions/49880453/what-difference-does-enableconfigurationproperties-make-if-a-bean-is-already-an/49888642
    static final String CONFIGURATION_PROPERTY_PREFIX = "application";
    private final Async async = new Async();

    public Async getAsync() {
        return async;
    }

    public static class Async {

        private Integer corePoolSize;
        private Integer maxPoolSize = Integer.MAX_VALUE;
        private Integer queueCapacity = Integer.MAX_VALUE;
        private Integer forkThreadSize = 10;

        public Integer getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(final Integer corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public Integer getMaxPoolSize() {
            return maxPoolSize;
        }

        public void setMaxPoolSize(final Integer maxPoolSize) {
            this.maxPoolSize = maxPoolSize;
        }

        public Integer getQueueCapacity() {
            return queueCapacity;
        }

        public void setQueueCapacity(final Integer queueCapacity) {
            this.queueCapacity = queueCapacity;
        }

        public Integer getForkThreadSize() {
            return forkThreadSize;
        }

        public void setForkThreadSize(Integer forkThreadSize) {
            this.forkThreadSize = forkThreadSize;
        }
    }
}
