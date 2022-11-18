package cn.BokerLite.api.event;

import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ReflectionEventListener implements IEventListener {
	
	final Object object;
	final Method targetMethod;
	
	public ReflectionEventListener(Object object, Method targetMethod) {
		this.object = object;
		this.targetMethod = targetMethod;
	}

	@Override
	public void invoke(Event event) {
		try {
			targetMethod.invoke(object, event);
		} catch (IllegalAccessException | IllegalArgumentException e) {
			//e.printStackTrace();
		} catch (InvocationTargetException e) {
		//	throw new RuntimeException(e.getTargetException());
		}
	}

}
