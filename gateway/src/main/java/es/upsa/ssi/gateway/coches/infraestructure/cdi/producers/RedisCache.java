package es.upsa.ssi.gateway.coches.infraestructure.cdi.producers;

import io.quarkus.redis.datasource.hash.HashCommands;

public interface RedisCache<K,V>
{
    V get(K key);
    boolean set(K key, V value);
    int del(K... keys);

    static <K,V> RedisCache<K,V> of(final String hashName, final HashCommands<String, K, V> hashCommands)
    {
        return new RedisCache<K, V>()
               {
                    @Override
                    public V get(K key)
                    {
                        return hashCommands.hget(hashName, key);
                    }

                    @Override
                    public boolean set(K key, V value)
                    {
                        return hashCommands.hset(hashName, key, value);
                    }

                    @Override
                    public int del(K... keys)
                    {
                        return hashCommands.hdel(hashName, keys);
                    }
               };
    }

}
