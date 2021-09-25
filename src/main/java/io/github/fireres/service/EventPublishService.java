package io.github.fireres.service;

import io.github.fireres.controller.Controller;

public interface EventPublishService {

    void publishControllerLoadedEvent(Controller controller);

}
