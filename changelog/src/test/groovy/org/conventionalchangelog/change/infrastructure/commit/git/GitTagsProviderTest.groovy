package org.conventionalchangelog.change.infrastructure.commit.git

import org.conventionalchangelog.domain.Commit
import org.reactivestreams.Subscriber
import spock.lang.Specification

class GitTagsProviderTest extends Specification {
    def repository = new GitRepository();
    def tags = new GitTagProvider(repository: repository.repository);

    def cleanup() {
        repository.dispose();
    }


    def "should get  tags from repository"() {
        given:
        repository
                .chore("build", "build script update")
                .tag("1.0.0")
                .feat("Feature.java", "new feature added to project")
                .feat("Feature1.java", "second feature file")
                .tag("1.1.0");
        when:
        def t = tags.get().toList().get();
        then:
        t[0].name == "1.0.0"
        t[1].name == "1.1.0"

    }

    def "should skip non semantic verion tags "() {
        given:
        repository
                .chore("build", "build script update")
                .tag("1.0.0")
                .feat("Feature.java", "new feature added to project")
                .tag("my-version")
                .feat("Feature1.java", "second feature file")
                .tag("1.1.0");
        when:
        def tag = tags.get().toList().get();

        then:
        tag.size() == 2
        tag[0].name == "1.0.0"
        tag[1].name == "1.1.0"


    }
}
