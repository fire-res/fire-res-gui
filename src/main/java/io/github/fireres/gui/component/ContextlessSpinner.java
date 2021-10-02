package io.github.fireres.gui.component;

import javafx.beans.NamedArg;
import javafx.event.Event;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;

import static javafx.scene.input.ContextMenuEvent.CONTEXT_MENU_REQUESTED;

public class ContextlessSpinner<N extends Number> extends Spinner<N> {

    public ContextlessSpinner(@NamedArg("min") int min,
                              @NamedArg("max") int max,
                              @NamedArg("initialValue") int initialValue) {
        super(min, max, initialValue);

        initialize();
    }

    public ContextlessSpinner(@NamedArg("min") int min,
                              @NamedArg("max") int max,
                              @NamedArg("initialValue") int initialValue,
                              @NamedArg("amountToStepBy") int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);

        initialize();
    }

    public ContextlessSpinner(@NamedArg("min") double min,
                              @NamedArg("max") double max,
                              @NamedArg("initialValue") double initialValue) {
        super(min, max, initialValue);

        initialize();
    }

    public ContextlessSpinner(@NamedArg("min") double min,
                              @NamedArg("max") double max,
                              @NamedArg("initialValue") double initialValue,
                              @NamedArg("amountToStepBy") double amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);

        this.addEventFilter(CONTEXT_MENU_REQUESTED, Event::consume);
    }

    public ContextlessSpinner() {
        super();

        initialize();
    }

    private void initialize() {
        this.addEventFilter(CONTEXT_MENU_REQUESTED, Event::consume);
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.getParent().requestFocus();
            }
        });
    }
}
