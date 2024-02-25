package edu.frontrange.csc240.a6;

import java.util.ArrayList;
import java.util.List;
import software.haddon.util.ObjectCounter;

/**
 * grabs the section number and applies it on top of the prefix/course string
 *
 * @author		Luc Gremillion, S02597411
 * @version		2022-3-6, CSC-240 Assignment 6
 */
public class Section {
    
/**
 * Default max students allowed in the class at one time
 */
public static int MAXIMUM_STUDENTS_PER_SECTION = 32;
/**
 * Default section number length
 */
public static int SECTION_NUMBER_LENGTH = 3;
/**
 * Object counter counts how many time the class has been called
 */
private final ObjectCounter counter = new ObjectCounter(this);
/**
 * newline formatter, so a newline doesn't need to be hard coded for each line
 */
private static final String LINE_SEPARATOR = System.getProperty("line.separator");
/**
 * String to hold information for section number
 */
private String sectionNumber;
/**
 * Course variable for course
 */
private final Course course;
/**
 * FRCCDate variable startDate
 */
private FRCCDate startDate;
/**
 * FRCCDate variable endDate
 */
private FRCCDate endDate;
/**
 * FRCCDate variable startTime
 */
private FRCCTime startTime;
/**
 * FRCCDate variable endTime
 */
private FRCCTime endTime;
/**
 * List hold each students information name + student number
 */
private final List<Student> students = new ArrayList<>();
/**
 * Default section number in replacement if variable is valid
 */
private final String SECTION_NUMBER = "";


/**
 * Section Constructor
 *
 * @param course                    current course that is selected    
 * @param sectionNumber             current section number that is selected
 */
public Section(Course course, String sectionNumber){
    this.course = course;
    this.sectionNumber = SECTION_NUMBER;
    if(valSectionNum(sectionNumber))
        this.sectionNumber = sectionNumber;
}


/**
 * Adds a student to the course
 * 
 * @param thisStudent           adds student to students list, to store for later use
 */
public void addStudent(Student thisStudent){
    if( students.size() != MAXIMUM_STUDENTS_PER_SECTION && students != null )
                    students.add(thisStudent);
}


/**
 * gets details of course prefix-course-section
 *		
 * @return              detailed output of information about student
 */
public String getDetails(){ 
    return String.join(LINE_SEPARATOR, "Section: " + toString(), "Course: " + course.getDetails(),
                    "Dates:  " + startDate + " to " + endDate, "Times:  " + startTime + " to " + endTime,
                    "Enrollment: " + students.size());
}


/**
 * gets the current roster of students
 *		
 * @return     roster of students
 */
public String getRoster(){
    String temp = "";
    
    for(int x =0;x<students.size();x++){
        //temp = temp.isEmpty() ? "" : LINE_SEPARATOR;
            temp += (temp.isEmpty() ? "" : LINE_SEPARATOR) + students.get(x);
    }

    return temp;
}

/**
 * gets the size of the roster - students list
 *		
 * @return    size of student list
 */
public int getRosterCount(){
    return students.size();
}  

/**
 * gets the current section number
 *		
 * @return                  String section number
 */
public String getSectionNumber(){
    return sectionNumber;
}
/**
 * tells the user whether or not if the section number is valid
 *
 * @param sectionNumber                   the sections number value
 * @return				true or false
 */
private boolean valSectionNum(String sectionNumber){
    return sectionNumber != null && sectionNumber.length() == SECTION_NUMBER_LENGTH;
}

/**
 * matches students to degrees and will return the ones that match
 *		
 * @param degree                current degree that is selected
 * @return                          matches to the degree
 */
public boolean isDegree(Degree degree){
    //Student student
    return students.stream().anyMatch(student -> ( student.getDegree() == degree ));
}

/**
 * sets the dates for the current selected class
 *		
 * @param startDate         sets the start date for class
 * @param endDate           sets the end date for class
 */
public void setDates(FRCCDate startDate, FRCCDate endDate){
    this.startDate = startDate;
    this.endDate = endDate;
}

/**
 * sets the times for the current selected class
 *
 * @param startTime                 sets the start time for class
 * @param endTime                   sets the end time for class
 */
public void setTimes(FRCCTime startTime, FRCCTime endTime){
    this.startTime = startTime;
    this.endTime = endTime;
}

/**
 * combines variables into a string format 
 *
 * @return	      formats 3 variables toString(), course and sectionNumber			
 */
@Override
public String toString(){ 
    return super.toString() + course.toString() + "-" + sectionNumber;
}
}
