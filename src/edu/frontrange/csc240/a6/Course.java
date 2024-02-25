package edu.frontrange.csc240.a6;

import software.haddon.util.ObjectCounter;

/**
 * grabs course information then formats the information in a form of a string.
 *
 * @author		Luc Gremillion, S02597411
 * @version		2022-3-6, CSC-240 Assignment 6
 */
public class Course {
/**
 * Default course number length, so user can check to see if
 * the course number length is larger than 3 or less than3
 */    
public static int COURSE_NUMBER_LENGTH = 3;
/**
 * Default maximum amount of credits user can get from a single class
 */
public static int MAXIMUM_CREDITS = 5;
/**
 * Default minimum amount of credits user can get from a single class
 */
public static int MINIMUM_CREDITS = 0;
/**
 * Default missing credit value to set base value
 */
public static int MISSING_CREDITS_VALUE = 0;
/**
 * object counter counts how many time the class has been called
 */
private final ObjectCounter counter = new ObjectCounter(this);
/**
 * input from another class, sets the course name
 */
private String courseName;
/**
 * input from another class, sets the course number
 */
private String courseNum;
/**
 * input from another class, sets/adds up the current credits the student has
 */
private int credits = 0;
/**
 * input from another class, sets the course prefix value
 */
private final Prefix coursePrefix;
/**
 * Default course number, used in replacement of invalid or initializes variable
 */
private final String COURSE_NUM = "";
/**
 * Default course name, used in replacement of invalid or initializes variable
 */
private final String COURSE_NAME = " ";

/**
 * Course Constructor
 *
 * @param coursePrefix          Comes from Prefix java file holds the prefix value
 * @param courseNum		String containing a 3 numbered value
 * @param courseName            String contains the courses name
 */
public Course(Prefix coursePrefix, String courseNum, String courseName){
    this.coursePrefix = coursePrefix;

    this.courseNum = COURSE_NUM;
    if(valCourseNum(courseNum))
        this.courseNum = courseNum;

    this.courseName = COURSE_NAME;
    if(valCourseName(courseName))
        this.courseName = courseName;

}

/**
 * Course Constructor
 *
 * @param coursePrefix          Comes from Prefix java file holds the prefix value
 * @param courseNum		String containing a 3 numbered value
 * @param courseName            String contains the courses name	
 * @param credits               grabs the amount of credits the class is worth
 */
public Course(Prefix coursePrefix, String courseNum, String courseName, int credits){
    this.coursePrefix = coursePrefix;
    this.courseNum = COURSE_NUM;
    if(valCourseNum(courseNum))
        this.courseNum = courseNum;
    this.courseName = COURSE_NAME;
    if(valCourseName(courseName))
        this.courseName = courseName;
    this.credits = 0;
    if(valCredits(credits))
        this.credits = credits;
}

/**
 * tells the user whether or not if the course number is valid
 *
 * @param courseNum                  the course numbers value
 * @return				true or false
 */
private boolean valCourseNum(String courseNum){
    return courseNum != null && courseNum.length() == COURSE_NUMBER_LENGTH;
}

/**
 * tells the user whether or not if the course name is valid
 *
 * @param courseName                   the course names value
 * @return				true or false
 */
private boolean valCourseName(String courseName){
    return courseName != null && !courseName.isEmpty();
}

/**
 * sets the amounts of credits the class is worth
 *		
 * @param numCredits            amount of credits
 */
public void setCredits(int numCredits) {
    this.credits = 0;
    if(valCredits(numCredits))
        this.credits = numCredits;
}

/**
 * tells the user whether or not if the number of credits is valid
 *
 * @param numCredits                   the credits value
 * @return				true or false
 */
private boolean valCredits(int numCredits){
    return numCredits <= MAXIMUM_CREDITS && numCredits >= MINIMUM_CREDITS;
}


/**
 * gets current amount of credits
 *		
 * @return      amount of credits
 */
public int getCredits() {
    return credits;
}

/**
 * gets details about the course information
 *		
 * @return                  course information in String format
 */
public String getDetails(){
    return String.join(toString(), " ", courseName, String.valueOf(credits), "Credits");
}

/**
 * combines variables into a string format 
 * 
 * @return   String builder containing toString(), coursePrefix and courseNum		
 */
@Override
public String toString(){
    return super.toString() + coursePrefix.getTitle() + "-" + courseNum;
}
}
