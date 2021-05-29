package lk.todolist.tm;

public class ToDo {
    private String id;
    private String description;
    private String user_Id;

    public ToDo ( ) {
    }

    public ToDo ( String id, String description, String user_Id ) {
        this.id = id;
        this.description = description;
        this.user_Id = user_Id;
    }

    public String getId ( ) {
        return id;
    }

    public void setId ( String id ) {
        this.id = id;
    }

    public String getDescription ( ) {
        return description;
    }

    public void setDescription ( String description ) {
        this.description = description;
    }

    public String getUser_Id ( ) {
        return user_Id;
    }

    public void setUser_Id ( String user_Id ) {
        this.user_Id = user_Id;
    }

    @Override
    public String toString ( ) {
        return description;
    }
}
