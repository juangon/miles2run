package org.miles2run.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by shekhargulati on 26/03/14.
 */
public class NotificationServiceTest {

    private NotificationService notificationService;
    private JedisPool jedisPool;

    @Before
    public void setUp() throws Exception {
        jedisPool = new JedisPool(new JedisPoolConfig(), "localhost");
        JedisExecutionService jedisExecutionService = new JedisExecutionService();
        jedisExecutionService.jedisPool = jedisPool;
        notificationService = new NotificationService();
        notificationService.jedisExecutionService = jedisExecutionService;
    }

    @After
    public void tearDown() throws Exception {
        jedisPool.getResource().del("notifications:test0");
        jedisPool.destroy();
    }

    @Test
    public void testAddNotification() throws Exception {
        Long notification = notificationService.addNotification(new Notification("test0", "test1", Action.FOLLOW, new Date().getTime()));
        Assert.assertEquals(1L, notification.longValue());
    }

    @Test
    public void testGetNotifications() throws Exception {
        notificationService.addNotification(new Notification("test0", "test1", Action.FOLLOW, new Date().getTime()));
        notificationService.addNotification(new Notification("test0", "test2", Action.FOLLOW, new Date().getTime()));
        notificationService.addNotification(new Notification("test0", "test3", Action.FOLLOW, new Date().getTime()));

        Set<Notification> notifications = notificationService.notifications("test0");
        Assert.assertEquals(3, notifications.size());
        Iterator<Notification> iterator = notifications.iterator();
        Assert.assertEquals("test3", iterator.next().getUserTookAction());
        Assert.assertEquals("test2", iterator.next().getUserTookAction());
        Assert.assertEquals("test1", iterator.next().getUserTookAction());
    }

    @Test
    public void testShouldGiveZeroNotificationsWhenNoNotificationExist() throws Exception {
        Set<Notification> notifications = notificationService.notifications("test0");
        Assert.assertEquals(0, notifications.size());
    }
}
