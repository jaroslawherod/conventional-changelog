package org.conventionalchangelog.git

import org.conventionalchangelog.domain.Commit
import org.reactivestreams.Subscriber
import reactor.core.publisher.Flux
import spock.lang.Specification

class GitChangesProviderTest extends Specification {

    def repository = new GitRepository();
    def changes = new GitCommitsProvider(repository: repository.repository);

    def cleanup() {
        repository.dispose();
    }

    def "should load single commit from repository"() {
        given:
        repository.chore("chore.txt", "new build file")
        Subscriber<Commit> subscriber = Mock();
        when:
        def s = changes.get();
        s.subscribe(subscriber);
        then:
        subscriber.onNext({ it.fullMessage == "chore: new build file" })
    }


    def "should load multiple commits"() {
        given:
        repository
                .chore("build", "build script update")
                .feat("Feature.java", "new feature added to project")
                .feat("Feature1.java", "second feature file");
        Subscriber<Commit> subscriber = Mock();
        when:
        def s = changes.get();
        s.subscribe(subscriber)

        then:
        subscriber.onNext({ it.fullMessage == "chore: build script update" })
        subscriber.onNext({ it.fullMessage == "feat: second feature file" })
        subscriber.onNext({ it.fullMessage == "feat: new feature added to project" })
    }

    def "should load multiple commits in correct order"() {
        given:
        repository
                .chore("build", "build script update")
                .feat("Feature.java", "new feature added to project")
                .feat("Feature1.java", "second feature file");
        when:
        def commits = Flux.from(changes.get());

        then:
        commits.elementAt(0).get().message == "chore: build script update";
        commits.elementAt(1).get().message == "feat: new feature added to project";
        commits.elementAt(2).get().message == "feat: second feature file";
    }

}
