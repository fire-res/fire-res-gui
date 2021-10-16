package io.github.fireres.gui.service.impl;

import io.github.fireres.gui.annotation.FxmlView;
import io.github.fireres.gui.annotation.processor.AnnotationProcessor;
import io.github.fireres.gui.controller.ExtendedComponent;
import io.github.fireres.gui.service.FxmlLoadService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
@Slf4j
public class FxmlLoadServiceImpl implements FxmlLoadService {

    private final List<AnnotationProcessor> annotationProcessors;
    private final ConfigurableApplicationContext context;

    @Autowired
    public FxmlLoadServiceImpl(@Lazy List<AnnotationProcessor> annotationProcessors,
                               ConfigurableApplicationContext context) {
        this.annotationProcessors = annotationProcessors;
        this.context = context;
    }

    @Override
    public <C extends ExtendedComponent<?>> C loadComponent(Class<C> componentClass) {
        return loadComponent(componentClass, null);
    }

    @Override
    public <C extends ExtendedComponent<?>> C loadComponent(Class<C> componentClass, ExtendedComponent<?> parent) {
        return loadComponent(componentClass, parent, component -> {
        });
    }

    @Override
    @SneakyThrows
    public <C extends ExtendedComponent<?>> C loadComponent(Class<C> componentClass,
                                                            ExtendedComponent<?> parent,
                                                            Consumer<C> preConstructAction) {

        if (!componentClass.isAnnotationPresent(FxmlView.class)) {
            throw new IllegalStateException("Component is not annotated with " + FxmlView.class.getSimpleName());
        }

        val component = load(componentClass, buildFxmlReference(componentClass));

        preConstructAction.accept(component);
        initializeComponentHierarchy(component, parent);
        applyAnnotationProcessors(component);
        callPostConstruct(component);

        return component;
    }

    private <C extends ExtendedComponent<?>> void applyAnnotationProcessors(C component) {
        component.getChildren().forEach(this::applyAnnotationProcessors);
        annotationProcessors.forEach(processor -> processor.process(component));
    }

    private <C> C load(Class<C> componentClass, String fxml) {
        val url = componentClass.getResource(fxml);

        try (val fxmlStream = url.openStream()) {
            log.debug("Loading FXML resource at {}", url);
            val loader = new FXMLLoader();
            loader.setLocation(url);
            loader.setControllerFactory(context::getBean);
            loader.load(fxmlStream);

            return loader.getController();
        } catch (IOException e) {
            throw new RuntimeException("Unable to load FXML file " + url, e);
        }
    }

    private <C extends ExtendedComponent<?>> void callPostConstruct(C component) {
        component.getChildren().forEach(this::callPostConstruct);
        component.postConstruct();
    }

    @SuppressWarnings({"rawtypes"})
    @SneakyThrows
    private void initializeComponentHierarchy(ExtendedComponent component, ExtendedComponent parent) {
        initializeFxmlComponentHierarchy(component);
        addAdditionalParent(component, parent);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void addAdditionalParent(ExtendedComponent component, ExtendedComponent parent) {
        if (parent != null) {
            parent.getChildren().add(component);
            component.setParent(parent);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void initializeFxmlComponentHierarchy(ExtendedComponent component) throws IllegalAccessException {
        for (Field field : component.getClass().getDeclaredFields()) {
            if (field.isAnnotationPresent(FXML.class) && field.getName().endsWith("Controller")) {
                field.setAccessible(true);

                val child = (ExtendedComponent<?>) field.get(component);
                child.setParent(component);
                component.getChildren().add(child);
                initializeFxmlComponentHierarchy(child);
            }
        }
    }

    private String buildFxmlReference(Class<?> c) {
        return Optional.ofNullable(c.getAnnotation(FxmlView.class)).map(FxmlView::value)
                .map(s -> s.isEmpty() ? null : s)
                .orElse(c.getSimpleName() + ".fxml");
    }

}
