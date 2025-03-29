package com.study.commerce.commerce.common.util;

import java.util.Set;

import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisUtil {

	private final RedisTemplate<String, Object> redisTemplate;

	public void setValues(String key, String data, int duration) {
		try {
			redisTemplate.opsForZSet().add(key, data, duration);
		} catch (Exception e) {
			throw new RuntimeException("Redis save fail..");
		}
	}

	public Long getSizeForKey(String key) {
		try {
			return redisTemplate.opsForZSet().size(key);
		} catch (Exception e) {
			throw new RuntimeException("Redis getSize fail..");
		}
	}

	public Set<Object> getAllForKeyOfRange(String key, Long start, Long end) {
		try {
			return redisTemplate.opsForZSet().range(key, start, end);
		} catch (Exception e) {
			throw new RuntimeException("Redis getAllForKeyOfRange fail..");
		}
	}

	public Long getRankForData(String key, Object data) {
		try {
			return redisTemplate.opsForZSet().rank(key, data);
		} catch (Exception e) {
			throw new RuntimeException("Redis getRankForData fail..");
		}
	}

	public void remove(String key, Object data) {
		try {
			redisTemplate.opsForZSet().remove(key, data);
		} catch (Exception e) {
			throw new RuntimeException("Redis getRankForData fail..");
		}
	}

	public void clear() {
		log.info("Redis clear!!!!");
		redisTemplate.execute((RedisCallback<String>)connection -> {
			connection.serverCommands().flushDb();  // 현재 DB의 모든 데이터 삭제
			return null;
		});
	}
}
