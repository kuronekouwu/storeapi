package dev.mottolab.storeapi.events;

import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderQueueListener implements ApplicationListener<OrderQueueEvent> {
    @Override
    public void onApplicationEvent(OrderQueueEvent event) {
        System.out.println("Not implemented yet");
    }
}
