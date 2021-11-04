package io.github.fireres.gui.framework.controller;

import java.util.List;

public interface ExtendedComponent<N> extends Controller {

    N getComponent();

    ExtendedComponent<?> getParent();

    void setParent(ExtendedComponent<?> parentComponent);

    List<ExtendedComponent<?>> getChildren();

    <C extends ExtendedComponent<?>> List<C> getChildren(Class<C> childClass);

    <C extends ExtendedComponent<?>> void removeChildren(Class<C> childClass);

    ComponentMetaData getMetaData();

}
