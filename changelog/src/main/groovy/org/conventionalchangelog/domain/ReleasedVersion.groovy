package org.conventionalchangelog.domain

import com.github.zafarkhaja.semver.Version
import reactor.core.publisher.Flux

class ReleasedVersion {

    Version version

    Flux<Change> changes;

}
