package es.upsa.ssi.gateway.coches.infraestructure.cdi.producers;

import es.upsa.ssi.gateway.coches.infraestructure.cdi.qualifiers.RedisCacheDefaults;
import io.quarkus.redis.datasource.RedisDataSource;
import io.quarkus.redis.datasource.hash.HashCommands;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@ApplicationScoped
public class RedisCacheProducer
{
    @Inject
    RedisDataSource redisDataSource;

    @Produces
    @Dependent
    @RedisCacheDefaults(name = "")
    public <K, V> RedisCache<K, V> produceRedisCache(InjectionPoint ip)
    {
        RedisCacheDefaults redisCacheDefaults = ip.getQualifiers()
                                                  .stream()
                                                  .filter(RedisCacheDefaults.class::isInstance)
                                                  .findFirst()
                                                  .map(RedisCacheDefaults.class::cast)
                                                  .get();
        ParameterizedType parameterizedType = (ParameterizedType) ip.getType();
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Class<K> keyClass = (Class<K>) actualTypeArguments[0];
        Class<V> valueClass = (Class<V>) actualTypeArguments[1];
        HashCommands<String, K, V> hashCommands = redisDataSource.hash(String.class, keyClass, valueClass);
        RedisCache<K, V> redisCache = RedisCache.of(redisCacheDefaults.name(), hashCommands);
        return redisCache;
    }
}
