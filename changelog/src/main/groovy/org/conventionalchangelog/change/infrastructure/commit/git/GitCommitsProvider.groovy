package org.conventionalchangelog.change.infrastructure.commit.git

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.Immutable
import groovy.util.logging.Slf4j
import org.ajoberstar.grgit.Grgit
import org.conventionalchangelog.change.CommitsProvider
import org.conventionalchangelog.domain.Commit
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

import static reactor.core.publisher.Mono.fromCallable

@Slf4j
@Immutable
public class GitCommitsProvider implements CommitsProvider {

    private Grgit repository;

    private Mono<Commit> head = fromCallable({ asCommit(repository.head()) });

    private Mono<Commit> first = fromCallable({ asCommit(repository.log()[-1]) }).cache();

    @CompileStatic
    Flux<Commit> get(from, till) {
        log.debug("resolve commonts for range {} - {}", from, till)
        def commits = resolveCommits(from, till);
        Flux.fromIterable(commits)
        .startWith(repository.resolve.toCommit(from))
        .map({ asCommit(it) })
    }

    @CompileDynamic
    Mono<Commit> head() {
        head
    }

    @Override
    Mono<Commit> first() {
        first
    }

    private Iterable<org.ajoberstar.grgit.Commit> resolveCommits(from, till) {
        return repository.log {
            range(from, till)
        }
    }


    @CompileStatic
    private Commit asCommit(org.ajoberstar.grgit.Commit commit) {
        return new GitCommit(commit: commit, repository: repository);
    }


}
