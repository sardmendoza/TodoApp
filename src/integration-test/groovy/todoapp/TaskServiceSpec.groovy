package todoapp

import grails.testing.mixin.integration.Integration
import grails.gorm.transactions.Rollback
import grails.validation.ValidationException
import spock.lang.Specification
import org.hibernate.SessionFactory

@Integration
@Rollback
class TaskServiceSpec extends Specification {

    TaskService taskService
    SessionFactory sessionFactory

    private Long setupData() {
        new Task(
                title: "Test Task 1",
                description: "Test Task 1 Description",
                category: "Work Related",
                completed: false)
                .save(flush: true, failOnError: true)
        new Task(
                title: "Test Task 2",
                description: "Test Task 2 Description",
                category: "Leisure",
                completed: false)
                .save(flush: true, failOnError: true)
        new Task(
                title: "Test Task 3",
                description: "Test Task 3 Description",
                category: "Leisure",
                completed: false)
                .save(flush: true, failOnError: true)
        Task task = new Task(
                title: "Test Task 4",
                description: "Test Task 4 Description",
                category: "Leisure",
                completed: false)
                .save(flush: true, failOnError: true)
        return task.id
    }

    void "should return a record"() {
        setup:
        Long taskId = setupData()

        expect:
        taskService.get(taskId) != null
    }

    void "should have correct list"() {
        setup:
        setupData()

        when:
        List<Task> taskList = taskService.list(max: 2, offset: 2)

        then:
        taskList.size() == 2

        expect:
        taskList.get(0).title == "Test Task 3"
        taskList.get(1).title == "Test Task 4"
    }

    void "should have correct record count"() {
        setup:
        setupData()

        expect:
        taskService.count() == 4
    }

    void "should have one less record after delete"() {
        setup:
        Long taskId = setupData()

        expect:
        taskService.count() == 4

        when:
        taskService.delete(taskId)
        sessionFactory.currentSession.flush()

        then:
        taskService.count() == 3
    }

    void "should not be null after adding new task"() {
        when:
        Task task = new Task(
                title: "Test Task 4",
                description: "Test Task 5 Description",
                category: "Leisure",
                completed: false)
        taskService.save(task)

        then:
        task.id != null
    }

    void "should throw ValidationException if category is invalid"() {
        when:
        Task task = new Task(
                title: "Test Task 5",
                description: "Test Task 5 Description",
                category: "ASD",
                completed: false)
        taskService.save(task)

        then:
        thrown(ValidationException)
    }

    void "should throw ValidationException if title is not provided"() {
        when:
        Task task = new Task(
                description: "Test Task Description",
                category: "Leisure",
                completed: false)
        taskService.save(task)

        then:
        thrown(ValidationException)
    }

    void "should throw ValidationException if title characters is less than specified"() {
        when:
        Task task = new Task(
                title: "A",
                description: "Test Task Description",
                category: "Leisure",
                completed: false)
        taskService.save(task)

        then:
        thrown(ValidationException)
    }
}
