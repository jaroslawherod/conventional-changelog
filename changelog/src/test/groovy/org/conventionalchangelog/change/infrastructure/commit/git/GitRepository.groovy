package org.conventionalchangelog.change.infrastructure.commit.git

import org.ajoberstar.grgit.Grgit
import org.ajoberstar.grgit.operation.BranchListOp

class GitRepository {
    File directory;
    Grgit repository;

    GitRepository() {
        directory = File.createTempDir()
        directory.deleteOnExit();
        repository = Grgit.init(dir: directory)
    }

    def head() { repository.head() }

    def tail() {
        repository.log().last()
    }

    def change(String file, String message) {
        new File(directory, file) << generator(50);
        repository.add(update: true, patterns: ["*"])
        repository.commit(message: message);
        return this;
    }

    def chore(String file, String message) {
        new File(directory, file) << generator(50);
        repository.add(update: true, patterns: ["*"])
        repository.commit(message: "chore: " + message);
        return this;
    }

    def feat(String file, String message) {
        new File(directory, file) << generator(50);
        repository.add(update: true, patterns: ["*"])
        repository.commit(message: "feat: " + message);
        return this;
    }

    def branch(branch) {
        def branches = repository.branch.list(mode: BranchListOp.Mode.LOCAL)
        repository.branch.add(branch);
        return this
    }

    def tag(String name) {
        repository.tag.add(name: name);
        return this;
    }

    private def generator = { String alphabet, int n ->
        new Random().with {
            (1..n).collect { alphabet[nextInt(alphabet.length())] }.join()
        }
    }.curry((('A'..'Z') + ('0'..'9') + ('a'..'z')).join());

    def dispose() {
        repository.close();
        directory.deleteDir();
    }

}
