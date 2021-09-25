package io.github.fireres.controller;

import io.github.fireres.service.EventPublishService;
import lombok.RequiredArgsConstructor;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@FxmlView("test-view.fxml")
@RequiredArgsConstructor
public class TestController implements Controller {

    public final EventPublishService eventPublishService;

    @PostConstruct
    public void init() {
         eventPublishService.publishControllerLoadedEvent(this);
    }

}
