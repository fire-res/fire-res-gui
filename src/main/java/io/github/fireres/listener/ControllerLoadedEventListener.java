package io.github.fireres.listener;

import io.github.fireres.event.ControllerLoadedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ControllerLoadedEventListener extends EventListener<ControllerLoadedEvent> {

    @Override
    protected void handleEvent(ControllerLoadedEvent event) {
        log.info("Loaded controller: {}", event.getController().getClass().getCanonicalName());
    }

}
