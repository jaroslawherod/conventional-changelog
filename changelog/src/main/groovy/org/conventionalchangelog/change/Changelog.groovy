package org.conventionalchangelog.change

import com.github.zafarkhaja.semver.Version
import groovy.transform.CompileStatic
import org.conventionalchangelog.domain.Change
import org.conventionalchangelog.domain.ReleasedVersion
import org.conventionalchangelog.domain.Tag
import org.reactivestreams.Publisher
import reactor.core.publisher.Flux

@CompileStatic
class Changelog {
    private TagProvider tags;
    private CommitsProvider commits;

    Publisher<ReleasedVersion> get() {
        def t = Flux.from(tags.get());
        if (t.count().get() < 2) {
            t.startWith(new Tag() {
                @Override
                String getId() {
                    return commits.first().get().getId()
                }

                @Override
                String getName() {
                    return null
                }
            })
        }
        t.zipWith(t.skip(1l))
                .map({
            new ReleasedVersion(version: Version.valueOf(it.t2.name), changes: Flux.from(commits.get(it.t1.id, it.t2.id))
                    .map({ Change.from(it) }).filter({ it.isPresent() }).map({ it.get() }))
        })

    }

}
