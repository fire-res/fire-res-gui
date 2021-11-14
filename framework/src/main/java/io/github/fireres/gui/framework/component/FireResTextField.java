package io.github.fireres.gui.framework.component;

import javafx.event.Event;
import javafx.scene.control.TextField;

import static javafx.scene.input.ContextMenuEvent.CONTEXT_MENU_REQUESTED;

public class FireResTextField extends TextField {

    public FireResTextField() {
        this.addEventFilter(CONTEXT_MENU_REQUESTED, Event::consume);
    }

}
