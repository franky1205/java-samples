package org.example;

import io.micrometer.core.instrument.util.StringUtils;
import org.example.utils.ExiUtil;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.util.Pair;

import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.example.RedisDataReplicator.createRedisClient;
import static org.example.RedisDataReplicator.getRedisBucket;

/**
 * @author Frankie Chao
 */
public class RedisDataReader {

    private static final Logger logger = LoggerFactory.getLogger(RedisDataReader.class);

    public static void main(String[] args) {
        final Set<String> keyPrefixSet = Set.of("APPLICATIONS", "CONNECTORS_ENABLED",
                "CONNECTOR_CONFIGS", "DEVICES", "DPAAS_CONFIG", "EMAILGATEWAY_CONFIG");
        final String tenantId = "dqtdc";
        RedissonClient dataSourceClient = createRedisClient("127.0.0.1");
        try {
            keyPrefixSet.stream()
                    .map(keyPrefix -> Pair.of(keyPrefix, getRedisBucket(dataSourceClient, keyPrefix, tenantId)))
                    .filter(rBucketPair -> rBucketPair.getSecond().get() != null)
                    .map(rBucketPair -> Pair.of(rBucketPair.getFirst(), rBucketPair.getSecond().get()))
                    .map(RedisDataReader::convertToXmlMessage)
                    .forEach(messagePair -> logger.info("Get keyType: [{}] with XML message: [{}]", messagePair.getFirst(), messagePair.getSecond()));
        } finally {
            dataSourceClient.shutdown(5, 5, TimeUnit.SECONDS);
        }
    }

    private static Pair<String, String> convertToXmlMessage(Pair<String, byte[]> bytesPair) {
        String xmlMessage = ExiUtil.decompressEXI(bytesPair.getSecond());
        return Pair.of(bytesPair.getFirst(), xmlMessage == null ? "--NULL--" : xmlMessage);
    }

}
