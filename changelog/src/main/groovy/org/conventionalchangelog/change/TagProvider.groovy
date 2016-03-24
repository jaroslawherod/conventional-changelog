package org.conventionalchangelog.change

import org.conventionalchangelog.domain.Tag
import reactor.core.publisher.Flux

interface TagProvider {

    Flux<Tag> get();
}
