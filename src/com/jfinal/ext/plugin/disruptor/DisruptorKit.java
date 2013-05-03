package com.jfinal.ext.plugin.disruptor;

import com.lmax.disruptor.RingBuffer;

public class DisruptorKit {
	private static volatile RingBuffer<DomainEvent> ringBuffer;

	static void init(RingBuffer<DomainEvent> ringBuffer) {
		DisruptorKit.ringBuffer = ringBuffer;
	}

	public static void post(String name, Object value) {
		long sequence = ringBuffer.next();
		ringBuffer.getPreallocated(sequence).setName(name).setValue(value);
		ringBuffer.publish(sequence);
	}
}