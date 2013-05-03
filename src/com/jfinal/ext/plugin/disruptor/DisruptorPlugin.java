package com.jfinal.ext.plugin.disruptor;

import java.util.concurrent.Executors;

import com.jfinal.plugin.IPlugin;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorPlugin implements IPlugin {
	private final Disruptor<DomainEvent> disruptor;
	private final DomainEventHandler handler;

	@SuppressWarnings("unchecked")
	public DisruptorPlugin(int bufferSize) {
		disruptor = new Disruptor<DomainEvent>(DomainEvent.EVENT_FACTORY, bufferSize, Executors.newCachedThreadPool(), ProducerType.SINGLE, new BlockingWaitStrategy());
		handler = new DomainEventHandler();
		disruptor.handleEventsWith(handler);
	}

	@Override
	public boolean start() {
		DisruptorKit.init(disruptor.start());
		return true;
	}

	@Override
	public boolean stop() {
		disruptor.shutdown();
		return true;
	}

	public void register(Class<?> linstner) {
		handler.addEventMapping(linstner);
	}

	public void unregister(Class<?> linstner) {
		handler.removeEventMapping(linstner);
	}
}