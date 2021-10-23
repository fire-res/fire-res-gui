package io.github.fireres.gui.preset;

public interface PresetApplier<T> {

    void apply(T object, Preset preset);

}
