package com.hjj.www;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryOneTime;

import java.time.LocalDateTime;

/**
 * @author haojunjie
 * @create 2022-08-10 22:23
 */
public class LockMain {
    public static void main(String[] args) {
        //重试策略,定义初试时间3s,重试3次
        ExponentialBackoffRetry exponentialBackoffRetry = new ExponentialBackoffRetry(3000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("192.168.10.104:2181", 3000, 3000, exponentialBackoffRetry);
        client.start();

        InterProcessMutex lock = new InterProcessMutex(client, "/hjjlock/lock_test");

        try {
            lock.acquire();
            System.out.println(LocalDateTime.now() +  " 获取锁成功");

            Thread.sleep(30_000);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                lock.release();
                System.out.println(LocalDateTime.now() +  " 释放锁成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
