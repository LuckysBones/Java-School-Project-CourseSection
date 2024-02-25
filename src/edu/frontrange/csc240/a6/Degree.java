
package edu.frontrange.csc240.a6;

/**
 * Each degree has a unique mnemonic (which forms the values of the enumeration)
 * and for each mnemonic there is a corresponding full title for the degree.
 *
 * @author		Dr. Bruce K. Haddon, Instructor
 * @version		4.0, 2020-08-22, CSC-240 Assignment 6, per the Assignment
 *				Instructions
 */
public enum Degree
{
/**
 * Associate of Arts degree.
 */
AA("Associate of Arts"),

/**
 * Associate of Science degree.
 */
AS("Associate of Science"),

/**
 * Associate of Science - Computer Science Transfer degree.
 */
ASCS("Associate of Science - Computer Science Transfer"),

/**
 * Associate of Science - Business Transfer degree.
 */
ASBUS("Associate of Science - Business Transfer"),

/**
 * Associate of Applied Science degree.
 */
AAS("Associate of Applied Science"),

/**
 * Associate of Applied Science/Computer Information Systems degree.
 */
AASCIS("Associate of Applied Science/Computer Information Systems");

/**
 * Instance variable for holding the title of the degree from the constructor.
 */
private final String degreeTitle;

/**
 * Constructor.
 *
 * @param degreeTitle	the full title of the degree
 */
private Degree(String degreeTitle)
{
	this.degreeTitle = degreeTitle;
}

/**
 * Access the given full title of the degree corresponding to the name.
 *
 * @return				the full title of the degree
 */
public String getTitle()
{
	return degreeTitle;
}

///**
// * Main method to conduct some tests on the degree class.
// *
// * @param args		not used.
// */
//public static void main(String[] args)
//{
//	for( Degree degree : Degree.values() )
//		System.out.printf("%8s  %s%n", degree.name(), degree.getTitle());
//}
}