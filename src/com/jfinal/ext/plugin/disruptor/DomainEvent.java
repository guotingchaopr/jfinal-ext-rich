package com.jfinal.ext.plugin.disruptor;

import com.lmax.disruptor.EventFactory;

public class DomainEvent {
	private String name;
	private Object value;

	public String getName() {
		return name;
	}

	public DomainEvent setName(String name) {
		this.name = name;
		return this;
	}

	public Object getValue() {
		return value;
	}

	public DomainEvent setValue(Object value) {
		this.value = value;
		return this;
	}

	public String toString() {
		return "DomainEvent name:" + name + ",value:" + value;
	}

	public final static EventFactory<DomainEvent> EVENT_FACTORY = new EventFactory<DomainEvent>() {
		@Override
		public DomainEvent newInstance() {
			return new DomainEvent();
		}
	};
}