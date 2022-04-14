package org.example;

import org.apache.commons.lang3.tuple.Pair;
import org.example.utils.ExiUtil;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
                    .filter(rBucketPair -> rBucketPair.getRight().get() != null)
                    .map(rBucketPair -> Pair.of(rBucketPair.getLeft(), rBucketPair.getRight().get()))
                    .map(RedisDataReader::convertToXmlMessage)
                    .forEach(messagePair -> logger.info("Get keyType: [{}] with XML message: [{}]",
                            messagePair.getLeft(), messagePair.getRight()));
        } finally {
            dataSourceClient.shutdown(5, 5, TimeUnit.SECONDS);
        }
    }

    private static Pair<String, String> convertToXmlMessage(Pair<String, byte[]> bytesPair) {
        String xmlMessage = ExiUtil.decompressEXI(bytesPair.getRight());
        return Pair.of(bytesPair.getLeft(), xmlMessage == null ? "--NULL--" : xmlMessage);
    }

}
