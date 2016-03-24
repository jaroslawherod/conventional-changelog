package org.conventionalchangelog.change.infrastructure.commit.git

import org.conventionalchangelog.domain.Commit
import org.reactivestreams.Publisher
import org.reactivestreams.Subscriber
import reactor.core.publisher.Flux
import spock.lang.Specification

class GitCommitsProviderTest extends Specification {

    def repository = new GitRepository();
    def changes = new GitCommitsProvider(repository: repository.repository);

    def cleanup() {
        repository.dispose();
    }

    def "should load single commit from repository"() {
        given:
        repository.chore("chore.txt", "new build file")
        when:
        def commits = Flux.from(getCommits()).toList().get();
        then:
        commits.size() == 1
        commits[0].message == "chore: new build file"
    }

    private Publisher<Commit> getCommits() {
        changes.get(repository.tail(), repository.head())
    }


    def "should load multiple commits"() {
        given:
        repository
                .chore("build", "build script update")
                .feat("Feature.java", "new feature added to project")
                .feat("Feature1.java", "second feature file");
        when:
        def commits = Flux.from(getCommits()).toList().get();

        then:
        commits.size() == 3
        commits[0].message == "chore: build script update"
        commits[1].message == "feat: second feature file"
        commits[2].message == "feat: new feature added to project"
    }

    def "should head commit"() {
        given:
        repository
                .chore("build", "build script update")
                .feat("Feature1.java", "second feature file");
        when:
        def head = changes.head().get()

        then:
        head.message == "feat: second feature file"
    }

    def "should first commit"() {
        given:
        repository
                .chore("build", "build script update")
                .feat("Feature1.java", "second feature file");
        when:
        def first = changes.first().get()

        then:
        first.message == "chore: build script update"
    }


}
