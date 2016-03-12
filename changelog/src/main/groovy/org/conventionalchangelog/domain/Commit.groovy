package org.conventionalchangelog.domain;

import java.time.LocalDateTime;

public interface Commit {
    String getId();

    String getMessage();

    LocalDateTime getDate();

    Iterable<Tag> getTags();

}
