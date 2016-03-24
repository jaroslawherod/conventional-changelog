package org.conventionalchangelog.domain

public enum ChangeType {
    FEATURE("feat"), CHORE, REFACTOR, BUGFIX("fix");

    private final String prefix;

    ChangeType() {
        prefix = name();
    }

    ChangeType(String prefix) {
        this.prefix = prefix;
    }

    private boolean matches(String type) {
        return prefix.equalsIgnoreCase(type);
    }

    public static Optional<ChangeType> from(String type) {
        Objects.requireNonNull(type);
        for (ChangeType changeType : values()) {
            if (changeType.matches(type)) {
                return Optional.of(changeType);
            }
        }
        return Optional.empty();

    }
}
