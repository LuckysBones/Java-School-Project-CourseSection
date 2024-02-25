package edu.frontrange.csc240.a6;

/**
 * Enum class contains prefix information, takes one 
 * input to output correct 3 letter version
 *
 * @author		Luc Gremillion, S02597411
 * @version		2022-3-6, CSC-240 Assignment 6
 */
public enum Prefix {
/**
 * Computer Information Systems
 */
CIS("Computer Information Systems"),
 /**
 * Computer Networking
 */
CNG("Computer Networking"),
 /**
 *  Computer Science
 */
CSC("Computer Science"),
 /**
 * Computer Web-based
 */
CWB("Computer Web-based");

/**
 * String to hold input from constructor
 */
private final String title;  

/**
 * Constructor
 *
 * @param prefixTitle	       String containing prefix title	
 */
private Prefix(String prefixTitle){
    this.title = prefixTitle;
}

/**
 * getter method to grab current 3 letter prefix
 *		  
 * @return          current title
 */
public String getTitle(){
    return title;
}
}

