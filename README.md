# xinput-springboot-base
xinput-springboot-base 是一个对springboot的简单封装，主要是参考play框架的做法去封装的

## 使用
```$xslt
<dependency>
    <groupId>com.github.xinput123</groupId>
    <artifactId>xinput-springboot-base</artifactId>
    <version>0.0.5</version>
</dependency>
```

## 在resource目录下创建 system.properties 文件
| 属性 | 描述 |
| :--- | :--- |
| api.cookie.token      | 自定义Cookie中token对应的key值，默认是jwt
| api.cookie.enable     | 自定义开启Cookie验证，默认关闭
| api.secret.key        | 自定义生成Token时的key,默认为xinput-boot
| api.secure.enable     | 自定义是否开启全局Token验证，dev环境不验证
| token.exp             | token过期时间,默认360024
| refresh.token.exp     | token刷新时间，默认3600247
| mock.userId           | 默认用户Id
| mock.userName         | 默认用户名称
| bucket.access.key     | 对象存储ak
| bucket.access.secret  | 对象存储sk
| wechat.appid          | 微信小程序appid
| wechat.secret         | 微信小程序sk
| limit.default         | 默认一次取多少条数据，默认 10
| limit.max             | 自定义一次最多取多少条数据，默认 50
| offset.max            | 自定义最大偏移量，默认 1000000

## 在resource目录下创建 redis.properties 配置文件
```
# redis config
redis.mode=single
redis.master=redis
redis.sentinels=localhost:6379
redis.timeout=5000
redis.ip=localhost
redis.port=6379
redis.pool.maxActive=200
redis.pool.maxIdle=60
redis.pool.minIdle=10
redis.pool.maxWait=10000
redis.pool.testOnBorrow=true
redis.pool.testOnReturn=true
redis.pool.testWhileIdle=true
redis.pool.whenExhaustedAction=WHEN_EXHAUSTED_GROW
redis.pool.timeBetweenEvictionRunsMillis=60000
redis.pool.numTestsPerEvictionRun=3
redis.pool.minEvictableIdleTimeMillis=60000
redis.pool.lifo=false
redis.ips=localhost
```