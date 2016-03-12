package org.conventionalchangelog.git

import groovy.transform.CompileStatic
import groovy.transform.Immutable
import org.ajoberstar.grgit.Commit
import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.service.ResolveService
import org.conventionalchangelog.CommitsProvider
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

import static java.util.stream.Collectors.toList

@CompileStatic
@Immutable
public class GitCommitsProvider implements CommitsProvider {

    private Grgit repository;

    public Publisher<org.conventionalchangelog.domain.Commit> get() {
        def commits = resolveCommits()
        return Flux.fromIterable(commits)
                .map({ asCommit(it) })
    }

    private Iterable<Commit> resolveCommits() {
        def resolver = repository.getResolve();
        def commits = new LinkedHashSet<String>()
        resolve(repository.head().id, commits, resolver);
        return commits
                .stream()
                .map({ resolver.toCommit(it) })
                .sorted(new CommitComparator())
                .collect(toList())
    }

    private def resolve(String commit, Collection<String> commits, ResolveService resolver) {

        if (!commits.contains(commit)) {
            commits.add(commit);
            def c = resolver.toCommit(commit);
            for (def p : c.parentIds) {
                resolve(p, commits, resolver);
            }
        }
    }


    private org.conventionalchangelog.domain.Commit asCommit(Commit commit) {
        return new GitCommit(commit: commit, repository: repository);
    }

}
