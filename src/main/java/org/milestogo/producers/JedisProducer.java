package org.milestogo.producers;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

/**
 * Created by shekhargulati on 17/03/14.
 */
@ApplicationScoped
public class JedisProducer {

    @Produces
    public JedisPool jedisPool() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxActive(50);
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(1);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnReturn(true);
        poolConfig.setTestWhileIdle(true);
        return new JedisPool(poolConfig, "localhost");
    }

    @PreDestroy
    public void close() {
        jedisPool().destroy();
    }
}
