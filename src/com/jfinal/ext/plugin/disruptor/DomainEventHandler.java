package com.jfinal.ext.plugin.disruptor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.reflect.TypeToken;
import com.jfinal.log.Logger;
import com.lmax.disruptor.EventHandler;

public class DomainEventHandler implements EventHandler<DomainEvent> {
	protected static final Logger log = Logger.getLogger(DomainEventHandler.class);
	private static final Map<String, EventObject> map = Maps.newHashMap();

	public void addEventMapping(Class<?> clazz) {
		Set<? extends Class<?>> supers = TypeToken.of(clazz).getTypes().rawTypes();
		for (Method method : clazz.getMethods()) {
			for (Class<?> c : supers) {
				try {
					Method m = c.getMethod(method.getName(), method.getParameterTypes());
					if (m.isAnnotationPresent(Subscribe.class)) {
						Class<?>[] parameterTypes = method.getParameterTypes();
						if (parameterTypes.length != 1) {
							log.debug("Method " + method + " has @Subscribe annotation, but requires " + parameterTypes.length + " arguments.  Event handler methods must require a single argument.");
							continue;
						}
						String name = m.getAnnotation(Subscribe.class).value();
						if (map.containsKey(name)) {
							log.debug("Method " + method + " has @Subscribe anootation, buy key " + name + " is already used .");
							continue;
						}
						Object target = clazz.newInstance();
						map.put(name, new EventObject(target, m));
						log.debug("add one Subscribe : name=" + name + ",target:" + target);
					}
				} catch (Exception ignored) {
				}
			}
		}
	}

	@Override
	public void onEvent(DomainEvent event, long sequence, boolean endOfBatch) throws Exception {
		try {
			EventObject eo = map.get(event.getName());
			eo.method.invoke(eo.target, new Object[]{event.getValue()});
		} catch (Exception e) {
			log.error("", e);
		}
	}

	class EventObject {
		private final Object target;
		private final Method method;

		public EventObject(Object target, Method method) {
			this.target = target;
			this.method = method;
		}
	}
}