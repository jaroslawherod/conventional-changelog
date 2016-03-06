package org.micold.domain;

public interface Message<T> {
    String getId();

    T getData();

}
