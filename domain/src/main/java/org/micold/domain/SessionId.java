package org.micold.domain;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class SessionId {

    private final UUID id;

    public SessionId() {
        this.id = UUID.randomUUID();
    }
}
