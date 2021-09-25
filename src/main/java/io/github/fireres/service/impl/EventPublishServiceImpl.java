package io.github.fireres.service.impl;

import io.github.fireres.controller.Controller;
import io.github.fireres.event.ControllerLoadedEvent;
import io.github.fireres.service.EventPublishService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventPublishServiceImpl implements EventPublishService {

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void publishControllerLoadedEvent(Controller controller) {
        val event = new ControllerLoadedEvent(this, controller);

        eventPublisher.publishEvent(event);
    }

}
