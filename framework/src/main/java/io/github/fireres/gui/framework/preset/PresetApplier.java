package io.github.fireres.gui.framework.preset;

public interface PresetApplier<T> {

    void apply(T object, Preset preset);

}
