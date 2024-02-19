package com.luckytour.server.tasks;

import com.luckytour.server.tasks.monitoruser.UserMonitorCache;
import com.luckytour.server.tasks.monitoruser.UserMonitorCacheUpdatedEvent;
import com.luckytour.server.tasks.monitoruser.UserMonitorTask;
import com.luckytour.server.service.DynamicCheckService;
import com.luckytour.server.service.PushService;
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
public class ScheduledExecutorManager {
	private final UserMonitorCache userMonitorCache;

	private final DynamicCheckService dynamicCheckService;

	private final PushService jiguangPushService;

	private final UserService userService;

	private final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);

	private final Map<String, Future<?>> futures = new ConcurrentHashMap<>();

	@Autowired
	public ScheduledExecutorManager(UserMonitorCache userMonitorCache, DynamicCheckService dynamicCheckService, PushService jiguangPushService, UserService userService) {
		this.userMonitorCache = userMonitorCache;
		this.dynamicCheckService = dynamicCheckService;
		this.jiguangPushService = jiguangPushService;
		this.userService = userService;
	}

	@EventListener
	public void onUserMonitorCacheUpdated(UserMonitorCacheUpdatedEvent event) {
		String uid = event.getUserRealTimeInfo().getUserId();
		Future<?> future = futures.get(uid);
		// 如果任务存在就立即释放一次
		if (future != null) {
			future.cancel(false);
		}
		UserMonitorTask task = new UserMonitorTask(uid, dynamicCheckService, jiguangPushService, userService, userMonitorCache);
		future = executorService.scheduleAtFixedRate(task, 0, 45, TimeUnit.MINUTES);
		task.setFuture((ScheduledFuture<?>) future); // Set the future so the task can cancel itself
		futures.put(uid, future);
	}
}
