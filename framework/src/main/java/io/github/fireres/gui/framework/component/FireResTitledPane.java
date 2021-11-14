package io.github.fireres.gui.framework.component;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ObjectPropertyBase;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TitledPane;

public class FireResTitledPane extends TitledPane {

    private final ObjectProperty<EventHandler<ActionEvent>> onExpandChanged = new ObjectPropertyBase<>() {
        @Override
        protected void invalidated() {
            setExpandChangeEventListener(get());
        }

        @Override
        public Object getBean() {
            return FireResTitledPane.this;
        }

        @Override
        public String getName() {
            return "onExpandChanged";
        }
    };

    private void setExpandChangeEventListener(EventHandler<ActionEvent> handler) {
        this.expandedProperty().addListener((observableValue, oldValue, newValue) -> handler.handle(null));
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onExpandChangedProperty() {
        return onExpandChanged;
    }

    public final void setOnExpandChanged(EventHandler<ActionEvent> value) {
        onExpandChangedProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnExpandChanged() {
        return onExpandChangedProperty().get();
    }

}
