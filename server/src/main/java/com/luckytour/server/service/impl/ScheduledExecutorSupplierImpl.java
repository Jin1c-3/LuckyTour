package com.luckytour.server.service.impl;

import com.luckytour.server.pojo.UserMonitorCache;
import com.luckytour.server.pojo.UserMonitorCacheUpdatedEvent;
import com.luckytour.server.pojo.UserMonitorTask;
import com.luckytour.server.service.DynamicCheckService;
import com.luckytour.server.service.PushService;
import com.luckytour.server.service.ScheduledExecutorSupplier;
import com.luckytour.server.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.*;

/**
 * @author qing
 * @date Created in 2024/2/18 22:44
 */
@Service
@Slf4j
public class ScheduledExecutorSupplierImpl implements ScheduledExecutorSupplier {
	private final UserMonitorCache userMonitorCache;

	private final DynamicCheckService dynamicCheckService;

	private final PushService jiguangPushService;

	private final UserService userService;

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

	private final Map<String, Future<?>> futures = new ConcurrentHashMap<>();


	@Autowired
	public ScheduledExecutorSupplierImpl(UserMonitorCache userMonitorCache, DynamicCheckService dynamicCheckService, PushService jiguangPushService, UserService userService) {
		this.userMonitorCache = userMonitorCache;
		this.dynamicCheckService = dynamicCheckService;
		this.jiguangPushService = jiguangPushService;
		this.userService = userService;
	}

	@EventListener
	public void onUserMonitorCacheUpdated(UserMonitorCacheUpdatedEvent event) {
		String uid = event.getUserMonitor().getUserId();
		Future<?> future = futures.get(uid);
		if (future != null) {
			future.cancel(false);
		}
		futures.put(uid, executorService.scheduleAtFixedRate(new UserMonitorTask(uid, dynamicCheckService, jiguangPushService, userService, userMonitorCache), 0, 45, TimeUnit.MINUTES));

		ScheduledThreadPoolExecutor executor = (ScheduledThreadPoolExecutor) executorService;
		BlockingQueue<Runnable> queue = executor.getQueue();
		log.debug("当前队列中的任务数: {}", queue.size());
	}
}
