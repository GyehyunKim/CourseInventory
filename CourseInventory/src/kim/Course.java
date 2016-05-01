package kim;

import java.util.Objects;

public class Course implements Comparable<Course> {
    private String id;
    private String title;
    private int credit;
    private String category;
    
    //Ctor with no argument
    public Course() {
        this("", "", 0, "");
    }
    
    // Ctor with arguments
    public Course(String id, String title, int credit, String category) {
        this.id = id;
        this.title = title;
        this.credit = credit; 
        this.category = category;         
    }
    
    @Override
    public String toString() {
        return "Course ID: " + id + "\nTitle: " + title;
    }
    
    @Override 
    public boolean equals(Object rhs) {
        if (rhs instanceof Course) 
            return this.id == ((Course)rhs).getId();
        else 
            return this == rhs;
    }    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + Objects.hashCode(this.id);
        return hash;
    }
    
    @Override
    public int compareTo(Course rhs) {
        return this.id.compareTo(rhs.getId());
    }
    
    // setters & getters 
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
