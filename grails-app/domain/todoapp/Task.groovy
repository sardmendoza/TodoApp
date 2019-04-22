package todoapp

class Task {

    String title
    String description
    String category
    Boolean completed = false

    static constraints = {
        title (blank: false, nullable: false, size:3..30)
        description (blank: true, nullable: true, size:0..80)
        category(inList:["Work Related", "Family", "Leisure", "Others"])
    }

}
