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
