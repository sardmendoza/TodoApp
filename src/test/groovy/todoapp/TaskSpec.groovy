package todoapp

import grails.testing.gorm.DomainUnitTest
import spock.lang.Specification

class TaskSpec extends Specification implements DomainUnitTest<Task> {

    void 'test title cannot be null'() {
        when:
        domain.title = null

        then:
        !domain.validate(['title'])
        domain.errors['title'].code == 'nullable'
    }

    void 'test title cannot be blank'() {
        when:
        domain.title = ''

        then:
        !domain.validate(['title'])
    }

    void 'test category value should be in list'() {
        when:
        domain.category = "Hello"

        then:
        !domain.validate(['category'])
        domain.errors['category'].code == 'not.inList'
    }
}
