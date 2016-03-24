package org.conventionalchangelog.change

import org.conventionalchangelog.domain.Commit
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface CommitsProvider {

    Flux<Commit> get(from, till)

    Mono<Commit> head();

    Mono<Commit> first();
}