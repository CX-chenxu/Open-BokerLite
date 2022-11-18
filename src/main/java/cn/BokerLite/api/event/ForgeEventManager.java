package cn.BokerLite.api.event;

import com.google.common.reflect.TypeToken;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ForgeEventManager { // L Forge

	public static final ForgeEventManager EVENT_BUS = new ForgeEventManager(MinecraftForge.EVENT_BUS);

	private final ConcurrentHashMap<Object, ArrayList<IEventListener>> listeners;

	final EventBus eventBus;

	final int busID;

	@SuppressWarnings("unchecked")
	private ForgeEventManager(EventBus eventBus) {
		this.eventBus = eventBus;
		Class<EventBus> eventBusClass = (Class<EventBus>) eventBus.getClass();
		try {
			Field listenersField = eventBusClass.getDeclaredField("listeners");
			listenersField.setAccessible(true);
			listeners = (ConcurrentHashMap<Object, ArrayList<IEventListener>>) listenersField.get(eventBus);
			Field busIDField = eventBusClass.getDeclaredField("busID");
			busIDField.setAccessible(true);
			busID = busIDField.getInt(eventBus);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
	}

	public void register(Object target) {
		if (listeners.containsKey(target)) {
			return;
		}
		Set<? extends Class<?>> supers = TypeToken.of(target.getClass()).getTypes().rawTypes();
		for (Method method : target.getClass().getMethods()) {
			for (Class<?> cls : supers) {
				try {
					Method real = cls.getDeclaredMethod(method.getName(), method.getParameterTypes());
					if (real.isAnnotationPresent(SubscribeEvent.class)) {
						Class<?>[] parameterTypes = method.getParameterTypes();
						if (parameterTypes.length != 1) {
							throw new IllegalArgumentException("Method " + method
									+ " has @SubscribeEvent annotation, but requires " + parameterTypes.length
									+ " arguments.  Event handler methods must require a single argument.");
						}

						Class<?> eventType = parameterTypes[0];

						if (!Event.class.isAssignableFrom(eventType)) {
							throw new IllegalArgumentException("Method " + method
									+ " has @SubscribeEvent annotation, but takes a argument that is not an Event "
									+ eventType);
						}
						register(eventType, target, real);
						break;
					}
				} catch (NoSuchMethodException | IllegalArgumentException ignored) {}
			}
		}
	}
	
	private void register(Class<?> eventType, Object target, Method method) {
		try {
			Constructor<?> ctr = eventType.getConstructor();
			ctr.setAccessible(true);
			Event event = (Event) ctr.newInstance();
			IEventListener listener = new ReflectionEventListener(target, method);
			event.getListenerList().register(busID, method.getAnnotation(SubscribeEvent.class).priority(),
					listener);
			ArrayList<IEventListener> others = listeners.computeIfAbsent(target, k -> new ArrayList<>());
			others.add(listener);
		} catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public int hashCode() {
		return eventBus.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		return eventBus.equals(obj);
	}

	public void unregister(Object object) {
		eventBus.unregister(object);
	}

	public boolean post(Event event) {
		return eventBus.post(event);
	}

	public String toString() {
		return eventBus.toString();
	}

}
