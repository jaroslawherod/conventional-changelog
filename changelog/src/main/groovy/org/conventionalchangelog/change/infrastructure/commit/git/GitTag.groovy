package org.conventionalchangelog.change.infrastructure.commit.git

import groovy.transform.CompileStatic
import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.Tag

@CompileStatic
class GitTag implements org.conventionalchangelog.domain.Tag {
    private Tag tag;
    private Grgit repository;

    @Override
    String getName() {
        return tag.getName()
    }

    @Override
    String getId() {
        return tag.commit.id
    }
}
