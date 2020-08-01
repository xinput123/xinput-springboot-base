package com.bootbase.util;

import com.bootbase.consts.RedisConsts;
import org.slf4j.Logger;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisCommands;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.util.Pool;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author <a href="mailto:fivesmallq@gmail.com">fivesmallq</a>
 * @version Revision: 1.0
 * @date 16/8/13 下午4:36
 */
public class RedisUtils {

    private static final Logger logger = Logs.get();

    private static Pool<Jedis> pool = null;
    private static SimpleProperties prop = SimpleProperties.readConfiguration(RedisConsts.CONF_FILE);
    // jedis集群
    private static JedisCluster jedisCluster = null;
    private static Set<HostAndPort> jedisClusterNodes = new HashSet();
    private static String mode = prop.getStringProperty(RedisConsts.REDIS_MODE);

    /**
     * 将key 的值设为value ，当且仅当key不存在，等效于 SETNX
     */
    public static final String NX = "NX";

    /**
     * 以秒为单位设置key的过期时间
     */
    public static final String EX = "EX";

    /**
     * 调用set后的返回值
     */
    public static final String OK = "OK";

    private static void init() {
        Integer timeout = prop.getIntProperty(RedisConsts.REDIS_TIMEOUT);
        String password = prop.getStringProperty(RedisConsts.REDIS_PWD);
        Integer port = prop.getIntProperty(RedisConsts.REDIS_PORT);
        JedisPoolConfig poolConfig = getPoolConfig();
        if (mode.equals(RedisConsts.MODE_SHARDED)) {
            logger.error(RedisConsts.MODE_SHARDED + " Not implemented");
        } else if (mode.equals(RedisConsts.MODE_SENTINEL)) {
            String master = prop.getStringProperty(RedisConsts.REDIS_MASTER);
            String[] stnArray = prop.getStringArrayProperty(RedisConsts.REDIS_SENTINELS);
            Set<String> stns = new HashSet<>();
            for (String stn : stnArray) {
                stns.add(stn);
            }
            pool = new JedisSentinelPool(master, stns, poolConfig, timeout, password);
        } else if (mode.equals(RedisConsts.MODE_CLUSTER)) {
            String[] ipsArray = prop.getStringArrayProperty("redis.ips");
            for (String ip : ipsArray) {
                jedisClusterNodes.add(new HostAndPort(ip, port));
            }
            jedisCluster = new JedisCluster(jedisClusterNodes, timeout, poolConfig);
        } else {
            String ip = prop.getStringProperty(RedisConsts.REDIS_IP);
            pool = new JedisPool(poolConfig, ip, port, timeout, password);
        }
    }

    public static JedisPoolConfig getPoolConfig() {
        JedisPoolConfig jedisCfg = new JedisPoolConfig();
        Properties poolConfig = prop.getPropertyGroup(RedisConsts.REDIS_POOL, true);
        ReflectUtils reflect = ReflectUtils.on(jedisCfg);
        Method[] methods = JedisPoolConfig.class.getMethods();
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            String methodName = method.getName();
            if (methodName.startsWith("set")) {
                String prop = methodName.replaceFirst("set", "");
                prop = Character.toLowerCase(prop.charAt(0)) + prop.substring(1);
                Object value = poolConfig.get(prop);
                if (value != null) {
                    logger.info("set prop success:{} --> {}", prop, value);
                    ReflectUtils.on(jedisCfg)
                            .call(methodName, TypeUtils.cast(value, method.getParameterTypes()[0]));
                }
            }
        }
        return jedisCfg;
    }

    private static Jedis getJedis() {
        return getPool().getResource();
    }

    private static Jedis getJedis(int index) {
        Jedis jedis = getJedis();
        jedis.select(index);
        return jedis;
    }

    public static void execute(Consumer<JedisCommands> consumer) {
        execute(consumer, 0);
    }

    public static void execute(Consumer<JedisCommands> consumer, int index) {
        Jedis jedis = null;
        try {
            if (mode.equals(RedisConsts.MODE_CLUSTER)) {
                JedisCluster jedisCluster = getJedisCluster();
                consumer.accept(jedisCluster);
            } else {
                jedis = getJedis(index);
                consumer.accept(jedis);
            }
        } catch (Exception e) {
            logger.error("consumer accept error! consumer:{} index:{}", consumer, index, e);
        } finally {
            if (!mode.equals(RedisConsts.MODE_CLUSTER)) {
                closeJedis(jedis);
            }
        }
    }

    public static <R> R executeResult(Function<JedisCommands, R> function) {
        return executeResult(function, 0);
    }

    public static <R> R executeResult(Function<JedisCommands, R> function, int index) {
        R apply = null;
        Jedis jedis = null;
        try {
            if (mode.equals(RedisConsts.MODE_CLUSTER)) {
                JedisCluster jedisCluster = getJedisCluster();
                apply = function.apply(jedisCluster);
            } else {
                jedis = getJedis(index);
                apply = function.apply(jedis);
            }
        } catch (Exception e) {
            logger.error("function apply error! consumer:{} index:{}", function, index, e);
        } finally {
            if (!mode.equals(RedisConsts.MODE_CLUSTER)) {
                closeJedis(jedis);
            }
        }
        return apply;
    }

    private static void closeJedis(Jedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    public static Pool<Jedis> getPool() {
        if (pool == null) {
            init();
        }
        return pool;
    }

    public static JedisCluster getJedisCluster() {
        if (jedisCluster == null) {
            init();
        }
        return jedisCluster;
    }
}
