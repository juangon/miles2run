package org.milestogo.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by shekhargulati on 17/03/14.
 */
public class CounterServiceTest {

    private CounterService counterService;
    private JedisPool jedisPool;

    @Before
    public void setUp() throws Exception {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        jedisPool.getResource().del(CounterService.COUNTRY_COUNTER);
        jedisPool.getResource().del(CounterService.DEVELOPER_COUNTER);
        jedisPool.getResource().del(CounterService.RUN_COUNTER);
        JedisExecutionService jedisExecutionService = new JedisExecutionService();
        jedisExecutionService.jedisPool = jedisPool;
        counterService = new CounterService();
        counterService.jedisExecutionService = jedisExecutionService;
    }

    @After
    public void tearDown() throws Exception {
        jedisPool.getResource().del(CounterService.COUNTRY_COUNTER);
        jedisPool.getResource().del(CounterService.DEVELOPER_COUNTER);
        jedisPool.getResource().del(CounterService.RUN_COUNTER);
        jedisPool.destroy();
    }

    @Test
    public void testUpdateCountryCounter() throws Exception {
        Long counter = counterService.updateCountryCounter("IN");
        Assert.assertEquals(Long.valueOf(1), counter);
        counter = counterService.updateCountryCounter("USA");
        Assert.assertEquals(Long.valueOf(1), counter);
        counter = counterService.updateCountryCounter("IN");
        Assert.assertEquals(Long.valueOf(0), counter);
    }

    @Test
    public void testGetCountryCounter() throws Exception {
        counterService.updateCountryCounter("IN");
        counterService.updateCountryCounter("IN");
        counterService.updateCountryCounter("PAK");
        counterService.updateCountryCounter("AUS");
        Long countryCounter = counterService.getCountryCounter();
        Assert.assertEquals(Long.valueOf(3), countryCounter);
    }

    @Test
    public void testUpdateRunCounter() throws Exception {
        Long count = counterService.updateRunCounter(1000);
        Assert.assertEquals(Long.valueOf(1000), count);
        count = counterService.updateRunCounter(2000);
        Assert.assertEquals(Long.valueOf(3000), count);
        Long runCounter = counterService.getRunCounter();
        Assert.assertEquals(Long.valueOf(3000), runCounter);
    }
}
