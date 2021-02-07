/**
 * Zoom Transcript Parser
 *    Groups comments from a Zoom transcript file by speaker
 * 
 * Command line argument: <optional> - transcript filename
 *    adding a comment line
 */

package transcriptparser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author lvall
 */
public class TranscriptParser {
    
    final static String VERSION = "Zoom Transcript Parser v0.4";
    
    final static int MAX_FIELDS = 2;  // when splitting input, divide into MAX strings
    final static String DEFAULT_INPUT_FILENAME = "transcript.vtt";  // transcript raw data
    final static int SNAME = 0;   // student name - index into parsed string
    final static int COMMENT = 1;   // student comment - index into parsed string

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {

        // input filename
        String inputFilename = DEFAULT_INPUT_FILENAME;
        // list of student instances - dynamically created & added to arraylist
        ArrayList<Student> students = new ArrayList<Student>();
        // stores each line of text read from file
        String line;
        // stores parsed comment
        String[] transcriptLine;

        System.out.println(VERSION);
        
        // args[0] if present is input filename, if not use default
        if (args.length > 0) {
            inputFilename = args[0];
        }
        // Default path in top directory of project - TranscriptParser
        File file = new File(inputFilename);
        String path = file.getAbsolutePath();
        System.out.printf("Transcript File: %s\n\n", path);
        BufferedReader br = null;   // assigned null to quiet compiler warning
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TranscriptParser.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        
        // for (String line:s) {  // will replace this with read from file - while not EOF
        while ((line = br.readLine()) != null) {
            transcriptLine = parseTranscriptLine(line);
            

            // If a valid transcript line (<name>:<text>)
            // If instance already created, append text
            // else create instance & append text
            if (transcriptLine.length == MAX_FIELDS) {
                    // if > found in comment, then Zoom date string, ignore it
                if (transcriptLine[COMMENT].indexOf('>') != -1)
                    continue;
                
                Boolean foundStudentName = false;
                for (Student studentPointer:students) {
                    if (studentPointer.getStudentName().equals(transcriptLine[SNAME])) {
                        studentPointer.appendComment(transcriptLine[COMMENT]);
                        foundStudentName = true;
                        break;
                    }
                }
                if (!foundStudentName) { // create new student instance
                    Student tempStudentPointer = new Student(transcriptLine[SNAME]);
                    students.add(tempStudentPointer);
                    tempStudentPointer.appendComment(transcriptLine[COMMENT]);
                }
            }      
        }
        
        // print results
        for (Student studentPointer:students) {
            studentPointer.printComments();
            System.out.println();
        }
        
        // print summary of # lines of comments per student
        System.out.println("Comment Line Summaries");
        for (Student studentPointer:students) {
            System.out.printf ("%25s:\t%d\n", studentPointer.getStudentName(),
                    studentPointer.getNumComments());
        }
    }
    
    // Helper function to parse input lines
    static String[] parseTranscriptLine(String line) {
        // syntax is <speaker name>:<comment> - don't worry about multiple :
        String[] parsedLine = line.split(":",MAX_FIELDS);
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
     */
    void printComments() {
        System.out.println(studentName);
        for (String printString:comments) {
            System.out.println(printString);
        }
    }
}
