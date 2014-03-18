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
//        poolConfig.setMaxActive(50);
//        poolConfig.setMaxIdle(10);
//        poolConfig.setMinIdle(1);
//        poolConfig.setTestOnBorrow(true);
//        poolConfig.setTestOnReturn(true);
//        poolConfig.setTestWhileIdle(true);
        String host = System.getenv("OPENSHIFT_REDIS_HOST");
        int port = Integer.valueOf(System.getenv("OPENSHIFT_REDIS_PORT"));
        String password = System.getenv("REDIS_PASSWORD");
        System.out.print(String.format("Redis configuration : Host %s Port %d Password %s", host, port, password));
        if (host == null) {
            System.out.print("Localhost Redis Configuration");
            return new JedisPool(poolConfig, "localhost");
        }
        JedisPool jedisPool = new JedisPool(poolConfig, host, port, 2000, password);
        return jedisPool;
    }

    @PreDestroy
    public void close() {
        jedisPool().destroy();
    }
}
