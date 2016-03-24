package org.conventionalchangelog.change.infrastructure.commit.git

import com.github.zafarkhaja.semver.Version
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.ajoberstar.grgit.Grgit
import org.conventionalchangelog.change.TagProvider
import org.conventionalchangelog.domain.Tag
import reactor.core.publisher.Flux

@CompileStatic
class GitTagProvider implements TagProvider {

    private Grgit repository;

    @Override
    Flux<Tag> get() {
        return Flux.fromIterable(tags())
                .map(this.&asTag)
                .filter(this.&isVersionTag);

    }

    @CompileDynamic
    private List<org.ajoberstar.grgit.Tag> tags() {
        repository.tag.list()
    }

    private boolean isVersionTag(Tag tag) {
        try {
            Version.valueOf(tag.name)
            return true;
        } catch (ex) {
            return false
        }
    }

    private Tag asTag(org.ajoberstar.grgit.Tag tag) {
        new GitTag(repository: repository, tag: tag);
    }
}
