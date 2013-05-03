package com.jfinal.ext.plugin.disruptor;

import java.util.concurrent.Executors;

import com.jfinal.plugin.IPlugin;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

public class DisruptorPlugin implements IPlugin {
	private Disruptor<DomainEvent> disruptor;
	private DomainEventHandler handler;

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

	public void add(Class<?> target) {
		handler.addEventMapping(target);
	}
}