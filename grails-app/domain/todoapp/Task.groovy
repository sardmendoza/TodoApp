package todoapp

class Task {

    String title
    String description
    Boolean completed

    static constraints = {
        title (blank: false, nullable: false, size:3..30)
        description (blank: true, nullable: true, size:3..80)

    }
}
