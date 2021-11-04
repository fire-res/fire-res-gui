package io.github.fireres.gui.framework.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.ChoiceBox;

public class FireResChoiceBox<T> extends ChoiceBox<T> {

    private ChangeListener<T> selectionChangeListener;

    private final ObjectProperty<EventHandler<ActionEvent>> onValueSelected = new ObjectPropertyBase<>() {
        @Override
        protected void invalidated() {
            setOnSelected(get());
        }

        @Override
        public Object getBean() {
            return FireResChoiceBox.this;
        }

        @Override
        public String getName() {
            return "onValueSelected";
        }
    };

    private void setOnSelected(EventHandler<ActionEvent> handler) {
        selectionChangeListener = (observableValue, oldValue, newValue) ->
                handler.handle(null);

        getSelectionModel().selectedItemProperty().addListener(selectionChangeListener);
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onValueSelectedProperty() {
        return onValueSelected;
    }

    public final void setOnValueSelected(EventHandler<ActionEvent> value) {
        onValueSelectedProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnValueSelected() {
        return onValueSelectedProperty().get();
    }

    public void setValueWithoutListener(T value) {
        this.getSelectionModel().selectedItemProperty().removeListener(selectionChangeListener);
        this.getSelectionModel().select(value);
        this.getSelectionModel().selectedItemProperty().addListener(selectionChangeListener);
    }

}
