package dev.mottolab.storeapi.events;

import org.springframework.context.ApplicationEvent;

public class OrderQueueEvent extends ApplicationEvent {
    public OrderQueueEvent(Object source) {
        super(source);
    }
}
