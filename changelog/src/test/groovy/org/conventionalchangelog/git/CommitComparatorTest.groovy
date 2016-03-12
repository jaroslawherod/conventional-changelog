package org.conventionalchangelog.git

import org.ajoberstar.grgit.Commit
import org.conventionalchangelog.git.CommitComparator
import spock.lang.Specification

class CommitComparatorTest extends Specification {
    def comparator = new CommitComparator()

    def "should compare commits by data"(first, second, result) {
        expect:
        comparator.compare(first, second) == result


        where:
        first                   | second                  | result
        new Commit(time: 10000) | new Commit(time: 11000) | 1
        new Commit(time: 12000) | new Commit(time: 11000) | -1


    }


    def "should compare commits by parent"(first, second, result) {
        expect:
        comparator.compare(first, second) == result


        where:
        first                                      | second                                     | result
        new Commit(time: 10000, parentIds: ["11"]) | new Commit(id: "11", time: 10000)          | 1
        new Commit(id: "11", time: 10000)          | new Commit(time: 10000, parentIds: ["11"]) | -1


    }
}
