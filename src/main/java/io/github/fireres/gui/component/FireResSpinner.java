package io.github.fireres.gui.component;

import javafx.beans.NamedArg;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.Spinner;
import javafx.scene.input.KeyCode;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import static javafx.scene.input.ContextMenuEvent.CONTEXT_MENU_REQUESTED;

@Slf4j
public class FireResSpinner<N extends Number> extends Spinner<N> {

    private final ObjectProperty<EventHandler<ActionEvent>> onLostFocus = new ObjectPropertyBase<>() {
        @Override
        protected void invalidated() {
            setOnLostFocusAction(get());
        }

        @Override
        public Object getBean() {
            return FireResSpinner.this;
        }

        @Override
        public String getName() {
            return "onLostFocus";
        }
    };

    public FireResSpinner(@NamedArg("min") int min,
                          @NamedArg("max") int max,
                          @NamedArg("initialValue") int initialValue) {
        super(min, max, initialValue);

        initialize();
    }

    public FireResSpinner(@NamedArg("min") int min,
                          @NamedArg("max") int max,
                          @NamedArg("initialValue") int initialValue,
                          @NamedArg("amountToStepBy") int amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);

        initialize();
    }

    public FireResSpinner(@NamedArg("min") double min,
                          @NamedArg("max") double max,
                          @NamedArg("initialValue") double initialValue) {
        super(min, max, initialValue);

        initialize();
    }

    public FireResSpinner(@NamedArg("min") double min,
                          @NamedArg("max") double max,
                          @NamedArg("initialValue") double initialValue,
                          @NamedArg("amountToStepBy") double amountToStepBy) {
        super(min, max, initialValue, amountToStepBy);

        this.addEventFilter(CONTEXT_MENU_REQUESTED, Event::consume);
    }

    public FireResSpinner() {
        super();

        initialize();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onLostFocusProperty() {
        return onLostFocus;
    }

    public final void setOnLostFocus(EventHandler<ActionEvent> value) {
        onLostFocusProperty().set(value);

    }
    public final EventHandler<ActionEvent> getOnLostFocus() {
        return onLostFocusProperty().get();
    }

    private void initialize() {
        this.addEventFilter(CONTEXT_MENU_REQUESTED, Event::consume);
        this.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                this.getParent().requestFocus();
            }
        });
    }

    private void setOnLostFocusAction(EventHandler<ActionEvent> handler) {
        this.focusedProperty().addListener((observable, oldValue, newValue) ->
                handleSpinnerLostFocus(newValue, () -> handler.handle(null)));
    }

    /**
     * c&p from Spinner
     * https://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user/32349847
     */
    private <T> void commitSpinner() {
        log.info("Commit {} changes", getId());
        if (!isEditable()) {
            return;
        }

        val text = getEditor().getText();
        val valueFactory = getValueFactory();

        if (valueFactory != null) {
            val converter = valueFactory.getConverter();

            if (converter != null) {
                val value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

    private void handleSpinnerLostFocus(Boolean focusValue, Runnable action) {
        if (!focusValue) {
            log.info("Spinner {} lost focus, new value: {}", getId(), getValue());

            commitSpinner();
            action.run();
        }
    }
}
