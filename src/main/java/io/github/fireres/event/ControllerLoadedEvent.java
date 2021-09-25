package io.github.fireres.event;

import io.github.fireres.controller.Controller;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

public class ControllerLoadedEvent extends ApplicationEvent {

    @Getter
    private final Controller controller;

    public ControllerLoadedEvent(Object source, Controller controller) {
        super(source);
        this.controller = controller;
    }

}
