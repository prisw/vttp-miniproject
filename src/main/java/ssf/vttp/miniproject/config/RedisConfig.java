package ssf.vttp.miniproject.config;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;


@Configuration
public class RedisConfig {

    private Logger logger = Logger.getLogger(RedisConfig.class.getName());

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private Integer redisPort;

    @Value("${spring.redis.username}")
    private String redisUsername;

    @Value("${spring.redis.password}")
    private String redisPassword;

    @Value("${spring.redis.database}")
    private Integer redisDatabase;
    
    @Bean("REDIS")
    public RedisTemplate<String, Object> createRedisConnection() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();

        config.setHostName(redisHost);
        config.setPort(redisPort);
        config.setDatabase(redisDatabase);
        config.setUsername(redisUsername);
        config.setPassword(redisPassword); 


        logger.log(Level.INFO,"Using Redis database %d".formatted(redisPort));
        logger.log(Level.INFO,"Using Redis password is set: %b".formatted(redisPassword !="NOT_SET"));


        JedisClientConfiguration jedisClient = JedisClientConfiguration.builder().build();
        JedisConnectionFactory jedisFactory = new JedisConnectionFactory(config,jedisClient);
        jedisFactory.afterPropertiesSet();

        RedisTemplate<String,Object> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisFactory);

        // template.setKeySerializer(new StringRedisSerializer());
        // template.setValueSerializer(new StringRedisSerializer());
        // template.setHashKeySerializer(new StringRedisSerializer());
        // template.setHashValueSerializer(new StringRedisSerializer());

        template.setKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        template.afterPropertiesSet();

        return template;
    }
    
}
