package StudentCourse;

public class Course {

    private String id;
    private String name;
    private String room;

    public Course(String id, String name, String room) {
        this.id = id;
        this.name = name;
        this.room = room;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
