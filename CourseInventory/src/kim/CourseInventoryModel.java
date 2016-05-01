package kim;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

public class CourseInventoryModel {
    
    private ArrayList<Course> courses; 
    private ArrayList<String> categories;
    private String selectedCourseId = null; 
    private String newCoursId;
    private String errorMessage = "";
    private boolean changed = false;
    
    public CourseInventoryModel() {
        
        // Create list of Course objects
        courses = new ArrayList<>();    
        
        // List of Categories  
        categories = new ArrayList<>(); 
        Collections.addAll(categories, "DATABASE", 
                "INFORMATION", "MATH", "PROGRAMMING", "SYSTEM");         
    }
    
    public void readCourseFile(File file) {
        
        ArrayList<String> lines = new ArrayList<>(); 
        String line = null; 
        
        // Read file & assign to course  
        try (BufferedReader br = new BufferedReader(new FileReader(file))) { 
            
            // 1. Store the data line by line
            while((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
            return; 
        }
        
        Collections.sort(lines); 
            
        // 2. Convert the lines to list of Courses
        courses.clear();
        for(int i = 0; i < lines.size(); i++) {              
            String[] tokens = lines.get(i).split(";");
            if (tokens.length == 4) {
                //Create Course object with the course info. 
                courses.add(new Course(tokens[0].trim(), 
                                        tokens[1].trim(), 
                                        Integer.parseInt(tokens[2].trim()), 
                                        tokens[3].trim()));
            }   
        }
    }
    
    public void saveCourseFile(File file) {
        
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
        
            ArrayList<String> lines = new ArrayList<>(); 

            // Write line by line 
            for(int i=0; i < courses.size(); i++) {
                Course course = courses.get(i);
                String line = course.getId() + " ; " +
                        course.getTitle() + " ; " + 
                        course.getCredit() + " ; " + 
                        course.getCategory(); 

                bw.write(line); 
                bw.newLine(); 
        }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    
    public ArrayList<String> getCourseIds() {
        
        // Get the list of course ids 
        ArrayList<String> courseIds = new ArrayList<>();
        for (int i=0; i < courses.size(); i++) 
            courseIds.add(courses.get(i).getId());

        return courseIds;  
    }
    
    public ArrayList<String> getCourseIdsByCat(String category) {
        
        // Get the list of Course ids with specified category
        ArrayList<String> courseIdsByCat = new ArrayList<>(); 
        for(int i=0; i < courses.size(); i++) {
            if (courses.get(i).getCategory().equals(category)) 
                courseIdsByCat.add(courses.get(i).getId());
        }
        
        return courseIdsByCat; 
    }
    
    public ArrayList<Course> findCoursesById(String id) {

        //Get list of courses with specified id 
        ArrayList<Course> coursesById = new ArrayList<>(); 
        for(int i=0; i < courses.size(); i++) {
            if (courses.get(i).getId().contains(id)) 
                coursesById.add(courses.get(i));
        }        
        return coursesById;
    }
 
    public ArrayList<Course> findCoursesByTitle(String title) {
        
        //Get list of courses with specified id 
        ArrayList<Course> coursesByTitle = new ArrayList<>(); 
        for(int i=0; i < courses.size(); i++) {
            if (courses.get(i).getTitle().contains(title)) 
                coursesByTitle.add(courses.get(i));
        }        
        return coursesByTitle;
    }
        
    public int getCourseCount() {
        // Get # of courses    
        return courses.size();
    }
    
    public Course getCourse(String id) {
        // Get Course by id   
        int idx = getCourseIdx(id); 
        if (idx < 0)
            return null; 
        return courses.get(idx);
    }
    
    public int getCourseIdx(String id) {
                
        //Search & Get index  
        if (id == null) return -1; 
        Course key = new Course(id, null, 0, null);
        return Collections.binarySearch(courses, key);
    } 
    
    public String getCourseId(int idx) {        
        return courses.get(idx).getId(); 
    }
    
    public int getCategoryCount() {
        return categories.size();
    }
    
    public ArrayList<String> getCategories() {
        return categories; 
    }
    
    public String getCategoryName(int idx) {
        return categories.get(idx);
    }       
    
    public void setSelectedCourseId(String id) {
        this.selectedCourseId = id; 
    }      
    
    public String getSelectedCourseId() {
        return this.selectedCourseId;
    }
    
    public void removeCourse(String id) {   
        
        // Remove Course    
        int idx = getCourseIdx(id); 
        if (idx >= 0)
            courses.remove(getCourse(id));
    } 
    
    public void updateCourse(String id, String title, int credit, String cat) {
        
        // Update course info
        int idx = getCourseIdx(id);
        if (idx >= 0) {
            courses.get(idx).setTitle(title);
            courses.get(idx).setCredit(credit);
            courses.get(idx).setCategory(cat);
        }
    }
    
    public boolean validateCourseId(String id) {
        
        //  Check redundancy
        if (getCourseIdx(id) >= 0) {
            this.errorMessage = "Course ID already exists. Use a unique ID.";
            return false;
        } 
        
        //  Check pattern 
        if (!id.matches("[A-Za-z]{4}\\d{5}")) {
            this.errorMessage = "ID must consist of 4 alphabets followed by 5 digits numeric.";
            return false;
        }
        return true;
    }
    
    public void addCourse(String id, String title, int credit, String category) {
        
        // Add course 
        Course newCourse = new Course(id, title, credit, category);
        setNewCourseId(id);
        courses.add(newCourse);
        Collections.sort(courses);        
    }
    
    public void setNewCourseId(String id) {
        
        this.newCoursId = id; 
    }
    
    public String getNewCourseId() {

        return this.newCoursId; 
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }

    public boolean isChanged() {
        return changed;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }
}
