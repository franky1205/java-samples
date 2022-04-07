package org.example;

import org.redisson.Redisson;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @author Frankie Chao
 */
public class RedisDataReplicator {

    public static void main(String[] args) {
        final String tenantId = "dqtdc";
        RedissonClient source = createRedisClient(Optional.ofNullable(System.getProperty("redis.source.ip"))
                .orElseThrow(() -> new IllegalArgumentException("Cannot find source redis ip with system property: [redis.source.ip]")));
        RedissonClient destination = createRedisClient("127.0.0.1");

        try {
            copyByBucket(source, destination, "APPLICATIONS", tenantId);
            copyByBucket(source, destination, "CONNECTORS_ENABLED", tenantId);
            copyByBucket(source, destination, "CONNECTOR_CONFIGS", tenantId);
            copyByBucket(source, destination, "PLATFORM_PARAMETERS", tenantId);
            copyByBucket(source, destination, "TRUSTED_CA_CONFIG", tenantId);
            copyByBucket(source, destination, "EMBEDDED_PDP_CONFIG", tenantId);
            copyByBucket(source, destination, "PROXY_CONFIG", tenantId);
            copyByBucket(source, destination, "ZTNA_AUTH", tenantId);
            copyByBucket(source, destination, "SDP_APPLICATIONS", tenantId);
            copyByBucket(source, destination, "SDP_CONNECTORS", tenantId);
            copyByBucket(source, destination, "SDP_ROUTING", tenantId);
            copyByBucket(source, destination, "DEVICES", tenantId);
            copyByBucket(source, destination, "EMAILGATEWAY_CONFIG", tenantId);
            copyByBucket(source, destination, "EDLPAGENT_CONFIG", tenantId);
            copyByBucket(source, destination, "SIEMAGENT_CONFIG", tenantId);
            copyByBucket(source, destination, "LOGAGENT_CONFIG", tenantId);
            copyByBucket(source, destination, "DPAAS_CONFIG", tenantId);
            copyByBucket(source, destination, "GENPROXY_CONFIG", tenantId);
            copyByBucket(source, destination, "REVPROXY_CONFIG", tenantId);
            copyByBucket(source, destination, "FWDPROXY_CONFIG", tenantId);
            copyByBucket(source, destination, "TENANT_SERVICE_SETTINGS", tenantId);
        } finally {
            source.shutdown(10, 10, TimeUnit.SECONDS);
            destination.shutdown(10, 10, TimeUnit.SECONDS);
        }
    }

    static RedissonClient createRedisClient(String hostname) {
        Config config = new Config();
        config.useSingleServer()
                .setDatabase(0)
                .setAddress(String.format("redis://%s:6379", hostname));
        return Redisson.create(config);
    }

    static RBucket<byte[]> getRedisBucket(RedissonClient redissonClient, String keyPrefix, String tenantId) {
        return redissonClient.getBucket(String.format("%s_%s", keyPrefix, tenantId));
    }

    private static void copyByBucket(RedissonClient sourceClient, RedissonClient destClient, String keyPrefix, String tenantId) {
        RBucket<byte[]> sourceDataBucket = getRedisBucket(sourceClient, keyPrefix, tenantId);
        RBucket<byte[]> destDataBucket = getRedisBucket(destClient, keyPrefix, tenantId);
        if (sourceDataBucket.get() != null) {
            destDataBucket.set(sourceDataBucket.get());
        }
    }
}
