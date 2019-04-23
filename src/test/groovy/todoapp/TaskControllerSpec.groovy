package todoapp

import grails.testing.gorm.DataTest
import grails.testing.web.controllers.ControllerUnitTest
import spock.lang.Specification

class TaskControllerSpec extends Specification implements ControllerUnitTest<TaskController>, DataTest {

    Class[] getDomainClassesToMock() {
        [Task]
    }

    def 'should return correct model when calling index action'() {
        given:
        List<Task> testTasks = [
                new Task(
                        title: "Test Task 1",
                        description: "Test Task 1 Description",
                        category: "Work Related",
                        completed: false),
                new Task(
                        title: "Test Task 2",
                        description: "Test Task 2 Description",
                        category: "Leisure",
                        completed: false)]

        controller.taskService = Stub(TaskService) {
            list(_) >> testTasks
            count() >> testTasks.size()
        }

        when:
        controller.index()

        then:
        model.taskList
        model.taskList.size() == 2
        model.taskList.find { it.title == "Test Task 1" && it.category == "Work Related" }
        model.taskCount == 2
    }

    def 'if title is included, success'() {
        given:
        String title = "Test Task 1"
        controller.taskService = Stub(TaskService) {
            save(_, _) >> new Task(
                    title: title,
                    description: "Test Task 1 Description",
                    category: "Work Related",
                    completed: false)
            read(_) >> new Task(
                    title: title,
                    description: "Test Task 1 Description",
                    category: "Work Related",
                    completed: false)
        }

        when:
        request.method = 'POST'
        request.format = 'form'
        request.contentType = FORM_CONTENT_TYPE
        params['title'] =  title
        controller.save()

        then: 'a message indicating that the user has been saved is placed'
        flash.message

        then: 'the user is redirected to show the task'
        response.redirectedUrl.startsWith('/task/show')

        and: 'a found response code is used'
        response.status == 302

    }

}
