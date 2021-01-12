/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transcriptparser;

import java.util.ArrayList;

/**
 *
 * @author lvall
 */
public class TranscriptParser {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String s1 = "a:Now is the time";
        String s2 = "b: for all good men";
        String s3 = "To come: to the aid of their party\n";
        Student student1 = new Student("student1");
        student1.appendComment(parseTranscriptLine(s1)[1]);
        student1.appendComment(parseTranscriptLine(s2)[1]);
        student1.appendComment(parseTranscriptLine(s3)[1]);
        student1.printComments();
        
        // Logic to parse lines, create or append to student instances
        String[] s = new String[] {"abc: abc comment", "def: def comment", "abc: abc comment2",
        "abc", "adf:", "adf: Hello World!", ":adf", "xyz abc", "uvw:"};
        
        // list of student instances already created
        ArrayList<Student> students = new ArrayList<Student>();
        
        String[] transcriptLine;
        for (String line:s) {  // will replace this with read from file - while not EOF
            transcriptLine = parseTranscriptLine(line);
            // If a valid transcript line (<name>:<text>)
            // If instance already created, append text
            // else create instance & append text
            if (transcriptLine.length == 2) {
                Boolean foundStudentName = false;
                for (Student studentPointer:students) {
                    if (studentPointer.getStudentName().equals(transcriptLine[0])) {
                        studentPointer.appendComment(transcriptLine[1]);
                        foundStudentName = true;
                        break;
                    }
                }
                if (!foundStudentName) { // create new student instance
                    Student tempStudentPointer = new Student(transcriptLine[0]);
                    students.add(tempStudentPointer);
                    tempStudentPointer.appendComment(transcriptLine[1]);
                }
            }      
        }
        
        // print results
        for (Student studentPointer:students) {
            studentPointer.printComments();
            System.out.println();
        }
    }
    
    // Helper function to parse input lines
    static String[] parseTranscriptLine(String line) {
        // syntax is <speaker name>:<comment> - don't worry about multiple :
        String[] parsedLine = line.split(":",2);
        return parsedLine;
    }
    
}

class Student {
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
    String getStudentName() {
        return studentName;
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
     */
    void printComments() {
        System.out.println(studentName);
        for (String printString:comments) {
            System.out.println(printString);
        }
    }
}
