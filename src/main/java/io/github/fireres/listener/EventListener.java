package io.github.fireres.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

@Slf4j
public abstract class EventListener<E extends ApplicationEvent> implements ApplicationListener<E> {

    @Override
    public void onApplicationEvent(E event) {
        log.info("Event received: {}", event);

        try {
            this.handleEvent(event);
        } catch (Exception var3) {
            log.warn("Exception while processing event {}", event.getClass(), var3);
        }
    }

    protected abstract void handleEvent(E payload);

}
