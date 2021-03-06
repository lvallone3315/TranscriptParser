/**
 * Zoom Transcript Parser <br>
 *    Group comments from a Zoom transcript file by speaker
 * <P>
 * Command line argument: <optional> - transcript filename
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
 * Zoom Transcript Parser <br>
 *    Groups comments from a Zoom transcript file by speaker
 * <P>
 * Command line argument: [optional] - transcript filename
 *
 * @author lvall
 */
public class TranscriptParser {
    
    final static String VERSION = "Zoom Transcript Parser v0.5";
    
    final static int MAX_FIELDS = 2;  // when splitting input, divide into MAX strings
    final static String DEFAULT_INPUT_FILENAME = "transcript.vtt";  // transcript raw data
    final static int SNAME = 0;   // student name - index into parsed string
    final static int COMMENT = 1;   // student comment - index into parsed string
    final static String COMMENT_DELIMITER = ":";

    /**
     * main() for transcript parser<br>
     *    reads a zoom transcript file, parses the file to identify comments for each speaker <br>
     *    (transcript file is plain text with .vtt extension type, <br>
     *     though any text file can be specified, as long as it follows the Zoom format) <br>
     *    on transcript End of File <br>
     *    output to console: <br>
     *      for each speaker, all comments from that speaker and <br>
     *      summary of speakers and for each, number of comments <br>
     * 
     * @param args [optional] Zoom transcript file name (relative or absolute path) <br>
     *    if no argument specified, uses default filename (v0.4 = transcript.vtt)
     * 
     * @throws IOException if input file not accessible
     */
    public static void main(String[] args) throws IOException {

        // input filename, if no input file specified, use default
        String inputFilename = DEFAULT_INPUT_FILENAME;
        // list of speaker instances - dynamically created and added to arraylist
        ArrayList<Student> students = new ArrayList<Student>();
        
        // temporary storage of each line of text read from file, to be parsed
        String line;
        // parsed comment as two Strings [0]=speaker, [1]=speaker's comment
        //   delimiter is hardcoded as ":"  -- ToDo: parameterize delimiters
        String[] transcriptLine;

        System.out.println(VERSION);
        
        // args[0] if present is input filename, if not use default
        //   default filename set in inputFilename declaration
        //   ToDo - add else to set to default, more robust if enhanced to loop
        //      and parse multiple transcript files
        if (args.length > 0) {
            inputFilename = args[0];
        }
        // Default path in top directory of project - TranscriptParser
        //   variable path (absolute path) used for debugging/user info 
        //    to clearly identify specified file
        File file = new File(inputFilename);
        String path = file.getAbsolutePath();
        System.out.printf("Transcript File: %s\n\n", path);
        
        // Open specified file for reading
        BufferedReader br = null;   // assigned null to quiet compiler warning
        try {
            br = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(TranscriptParser.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(-1);
        }
        
        // Main read and parse loop
        // While not at the End of File
        //   read a transcript comment (format - speaker:comment)
        //   parse - break into two strings based on delimiter (":")
        //   store comment to the appropriate speaker (aka student)
        while ((line = br.readLine()) != null) {
            transcriptLine = parseTranscriptLine(line);
            

            // If a valid transcript line (<name>:<text>)
            // If speaker instance already created, append text
            // else create a new speaker instance and append text
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
        
        // print results on console output for each speaker (student)
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
        String[] parsedLine = line.split(COMMENT_DELIMITER,MAX_FIELDS);
        return parsedLine;
    }
    
}  // end class - TranscriptParser
