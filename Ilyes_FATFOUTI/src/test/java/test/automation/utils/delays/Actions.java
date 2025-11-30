package test.automation.utils.delays;

import java.util.Optional;

public interface Actions<T> {
    default String getUniqueId() {
        return this.getClass().getCanonicalName();
    }

    String getDescription();

    Optional<T> perform();
}
