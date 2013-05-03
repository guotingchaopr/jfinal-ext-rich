package com.jfinal.ext.ioc;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import org.joor.Reflect;

import com.jfinal.aop.Interceptor;
import com.jfinal.core.ActionInvocation;
import com.jfinal.core.Controller;

public class InjectInterceptor implements Interceptor {
	private static final Map<String, Object> map = new HashMap<String, Object>();

	public void intercept(ActionInvocation ai) {
		Controller controller = ai.getController();
		Field[] fields = controller.getClass().getDeclaredFields();
		for (Field field : fields) {
			Object bean = null;
			if (field.isAnnotationPresent(Inject.class)) {
				bean = map.get(field.getName());
				if (bean == null) {
					bean = Reflect.on(field.getType()).create().get();
					map.put(field.getName(), bean);
				}
			} else {
				continue;
			}

			try {
				if (bean != null) {
					field.setAccessible(true);
					field.set(controller, bean);
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		ai.invoke();
	}
}