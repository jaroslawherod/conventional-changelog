package org.conventionalchangelog.application.commit

import org.conventionalchangelog.domain.Commit
import org.reactivestreams.Publisher

interface CommitsProvider {
    Publisher<Commit> get()
}