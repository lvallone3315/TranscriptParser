package transcriptparser;

import java.util.ArrayList;

/**
 * Student class - each instance = one speaker (aka student) <br>
 *   externally - keyed on student name, each unique name will construct a new student instance<br>
 *   maintains a list of comments (Strings) for the speaker<br>
 *     implemented as an ArrayList - no limit on # comments<br>
 * <P>
 * Supports:<br>
 *   Retrieving student name (specified when instance created)<br>
 *   Storing comments attributed to the student
 *   Retrieving the # of comments attributed to the student (integer)
 *   Printing all comments to the console
 * 
 * ToDo: move Student class to separate file
 *   
 * @author lvall
 */
public class Student {
    String studentName;
    ArrayList<String> comments = new ArrayList<String>();
    
    /**
     * Student() constructor
     * @param name - name of student (required)
     */
    Student(String name) {
        studentName = name;
    }
    
    /**
     * getStudentName
     * @return student name stored in instance
     */
    public String getStudentName() {
        return studentName;
    }
    
    /**
     * getNumComments()
     * @return # of lines in transcript attributable to student
     */
    public int getNumComments() {
        return(comments.size());
    }
    
    /**
     * appendComment - saves comment for this student
     * @param comment - comment to store in memory
     */
    void appendComment(String comment) {
        comments.add(comment);
    }
    
    
    /**
     * printComments() - print to console all comments for this student instance
     * Note: for now, assume no new lines embedded (ie use println)
     * ToDo: instead of printing, return string (a really big one) to caller
     *   Reason for hesitating: string might be huge - need to check if any limits
     */
    void printComments() {
        System.out.println(studentName);
        for (String printString:comments) {
            System.out.println(printString);
        }
    }
}  // end Student Class
