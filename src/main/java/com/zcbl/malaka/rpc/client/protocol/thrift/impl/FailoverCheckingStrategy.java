/**
 * 
 */
package com.zcbl.malaka.rpc.client.protocol.thrift.impl;

import static com.google.common.cache.CacheBuilder.newBuilder;
import static com.google.common.collect.EvictingQueue.create;
import static java.lang.Boolean.TRUE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.MINUTES;
import static org.slf4j.LoggerFactory.getLogger;

import java.util.Set;
import java.util.concurrent.ExecutionException;

import org.slf4j.Logger;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.EvictingQueue;

/**
 * @author jys 2016年8月30日
 * @param <T>
 */
public class FailoverCheckingStrategy<T> {

	private static final int DEFAULT_FAIL_COUNT = 10;
	private static final long DEFAULT_FAIL_DURATION = MINUTES.toMillis(1);
	private static final long DEFAULT_RECOVERY_DURATION = MINUTES.toMillis(1);
	private final Logger logger = getLogger(getClass());
	private final long failDuration;

	private final Cache<T, Boolean> failedList;

	private final LoadingCache<T, EvictingQueue<Long>> failCountMap;

	public FailoverCheckingStrategy() {
		this(DEFAULT_FAIL_COUNT, DEFAULT_FAIL_DURATION, DEFAULT_RECOVERY_DURATION);
	}

	public FailoverCheckingStrategy(int failCount, long failDuration, long recoveryDuration) {
		this.failDuration = failDuration;
		this.failedList = newBuilder().weakKeys().expireAfterWrite(recoveryDuration, MILLISECONDS).build();
		this.failCountMap = newBuilder().weakKeys().build(new CacheLoader<T, EvictingQueue<Long>>() {

			@Override
			public EvictingQueue<Long> load(T key) throws Exception {
				return create(failCount);
			}
		});
	}

	public Set<T> getFailed() {
		return failedList.asMap().keySet();
	}

	public void fail(T object) {
		logger.trace("server {} failed.", object);
		boolean addToFail = false;
		try {
			EvictingQueue<Long> evictingQueue = failCountMap.get(object);
			synchronized (evictingQueue) {
				evictingQueue.add(System.currentTimeMillis());
				if (evictingQueue.remainingCapacity() == 0
						&& evictingQueue.element() >= System.currentTimeMillis() - failDuration) {
					addToFail = true;
				}
			}
		} catch (ExecutionException e) {
			logger.error("Ops.", e);
		}
		if (addToFail) {
			failedList.put(object, TRUE);
			logger.info("server {} failed. add to fail list.", object);
		}
	}
}
