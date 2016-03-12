package org.conventionalchangelog.domain;

import static java.util.Arrays.asList;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class Change {
    ChangeType type;

    String message;

    public static Optional<Change> from(Commit commit) {
        final String message = commit.getMessage();
        if (isEmpty(message)) {
            return Optional.empty();
        } else {
            String[] parts = message.split(":");
            if (hasPrefix(parts)) {
                return Optional.empty();
            }
            final String prefix = parts[0];
            return ChangeType.from(prefix).map(asChange(parts));
        }
    }

    private static Function<ChangeType, Change> asChange(String[] parts) {
        return type -> builder().type(type).message(asList(parts).stream().skip(1).collect(Collectors.joining(":")).trim()).build();
    }

    private static boolean hasPrefix(String[] parts) {
        return parts.length < 2;
    }

    private static boolean isEmpty(String message) {
        return message == null || message.isEmpty();
    }

}
