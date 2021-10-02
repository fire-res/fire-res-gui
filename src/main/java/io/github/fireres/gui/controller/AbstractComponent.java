package io.github.fireres.gui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Slf4j
public abstract class AbstractComponent<N> implements ExtendedComponent<N> {

    @FXML
    private Object component;

    @Getter
    @Setter
    private ExtendedComponent<?> parent;

    @Getter
    private final List<ExtendedComponent<?>> children = new ArrayList<>();

    @SneakyThrows
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("Initializing: {}", this.getClass().getSimpleName());
        initialize();
    }

    protected void initialize() {
        //do nothing by default
    }

    @SuppressWarnings("unchecked")
    @Override
    public N getComponent() {
        return (N) component;
    }

    /**
     * c&p from Spinner
     * https://stackoverflow.com/questions/32340476/manually-typing-in-text-in-javafx-spinner-is-not-updating-the-value-unless-user/32349847
     */
    protected <T> void commitSpinner(Spinner<T> spinner) {
        log.info("Commit {} changes", spinner.getId());
        if (!spinner.isEditable()) {
            return;
        }

        val text = spinner.getEditor().getText();
        val valueFactory = spinner.getValueFactory();

        if (valueFactory != null) {
            val converter = valueFactory.getConverter();

            if (converter != null) {
                val value = converter.fromString(text);
                valueFactory.setValue(value);
            }
        }
    }

    protected void handleSpinnerLostFocus(Boolean focusValue, Spinner<?> spinner, Runnable action) {
        if (!focusValue) {
            log.info("Spinner {} lost focus, new value: {}", spinner.getId(), spinner.getValue());

            commitSpinner(spinner);
            action.run();
        }
    }

    protected Optional<ExtendedComponent<?>> findFirstComponent(Class<? extends ExtendedComponent<?>> componentClass) {
        val components = findComponents(componentClass);

        if (components.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(components.get(0));
        }
    }

    protected <C extends ExtendedComponent<?>> List<C> findComponents(Class<C> componentClass) {
        val root = lookupRoot();
        val components = new ArrayList<C>();

        lookupComponents(root, componentClass, components);

        return components;
    }

    @SuppressWarnings("unchecked")
    private <C extends ExtendedComponent<?>> void lookupComponents(ExtendedComponent<?> component,
                                                                   Class<C> componentToLookup,
                                                                   List<C> list) {

        if (componentToLookup.equals(component.getClass())) {
            list.add((C) component);
        }

        component.getChildren().forEach(child ->
                lookupComponents(child, componentToLookup, list));
    }

    private ExtendedComponent<?> lookupRoot() {
        var current = (ExtendedComponent<?>) this;

        while (current.getParent() != null) {
            current = current.getParent();
        }

        return current;
    }

    @Override
    public <C extends ExtendedComponent<?>> List<C> getChildren(Class<C> childClass) {
        return children.stream()
                .filter(child -> child.getClass().equals(childClass))
                .map(child -> (C) child)
                .collect(Collectors.toList());
    }
}
