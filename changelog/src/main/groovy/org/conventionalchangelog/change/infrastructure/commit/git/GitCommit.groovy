package org.conventionalchangelog.change.infrastructure.commit.git

import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import org.ajoberstar.grgit.Commit
import org.ajoberstar.grgit.Grgit
import org.conventionalchangelog.domain.Tag

import java.time.LocalDateTime

@CompileStatic
@EqualsAndHashCode(excludes = "repository")
class GitCommit implements org.conventionalchangelog.domain.Commit {
    private Grgit repository;
    private Commit commit;

    @Override
    String getId() {
        return commit.getId()
    }

    @Override
    String getMessage() {
        return commit.getFullMessage()
    }

    @Override
    LocalDateTime getDate() {
        return LocalDateTime.from(commit.getDate().toInstant());
    }

    @Override
    @CompileDynamic
    Iterable<Tag> getTags() {
        repository.tag.list().findAll({ it.commit == commit }).collect { new GitTag(tag: it, repository: repository) }
    }


    @Override
    public String toString() {
        return "GitCommit{" +
                "commit=" + commit +
                '}';
    }
}
