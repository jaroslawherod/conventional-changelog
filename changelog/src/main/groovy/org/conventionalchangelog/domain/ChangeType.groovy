package org.conventionalchangelog.domain;

import java.util.Objects;
import java.util.Optional;

public enum ChangeType {
    FEATURE("feat"), CHORE, REFACTOR;

    private final String prefix;

    ChangeType() {
        prefix = name();
    }

    ChangeType(String prefix) {
        this.prefix = prefix;
    }

    private boolean matches(String type){
        return prefix.equalsIgnoreCase(type);
    }

    public static Optional<ChangeType> from(String type) {
        Objects.requireNonNull(type);
        for ( ChangeType changeType : values()){
            if(changeType.matches(type)){
                return Optional.of(changeType);
            }
        }
        return Optional.empty();

    }
}
