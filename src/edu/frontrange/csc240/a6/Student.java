
package edu.frontrange.csc240.a6;

import software.haddon.util.ObjectCounter;

/**
 * Keep details of each student. The detail include the student ID, given and
 * family name, date-of-birth, and the target degree for the student.
 *
 * @author		Dr. Bruce K. Haddon, Instructor
 * @version		4.0, 2020-08-22, CSC-240 Class
 */
public class Student extends Object
{
/**
 * Value to be used if no or invalid first name provided.
 */
private static final String DEFAULT_FIRST_NAME = null;

/**
 * Value to be used if no or invalid last name provided.
 */
private static final String DEFAULT_LAST_NAME = null;

/**
 * String to identify the first (given) name in messages.
 */
private static final String FIRST_NAME_LABEL = "first name";

/**
 * Constant across all Student instances, for the length of the student ID.
 * This value must be 1 or greater.
 */
private static final int ID_LENGTH = 9;

/**
 * String to use if the first name is not validly set.
 */
private static final String INVALID_FIRST_NAME =
									"<Invalid " + FIRST_NAME_LABEL + ">";
/**
 * String to identify the last (family) name in messages.
 */
private static final String LAST_NAME_LABEL = "last name";

/**
 * String to use if the last name is not validly set.
 */
private static final String INVALID_LAST_NAME =
									"<Invalid " + LAST_NAME_LABEL + ">";
/**
 * The system-specific character sequence used to separate lines.
 */
private static final String LINE_SEPARATOR = System.getProperty("line.separator");

/**
 * The student's date of birth (DOB), or null.
 */
private FRCCDate birthDate;

/**
 * Object counter field to enable the counting of Student instances,
 * @see ObjectCounter
 */
final ObjectCounter counter = new ObjectCounter(this);

/**
 * The student's given name.
 */
private String firstName;

/**
 * The student's family name.
 */
private String lastName;

/**
 * Student ID for this student. The length must be ID_LENGTH.
 */
private String studentID;

/**
 * The student's current objective degree, or null.
 */
private Degree targetDegree;

/**
 * This first constructor validates the studentID, the firstName, and the
 * lastName, and leaves the birthDate and targetDegree with default values.
 *
 * @param studentID		student ID for this student. The length must be ID_LENGTH.
 * @param firstName		the student's given name.
 * @param lastName		the student's family name.
 */
public Student(String studentID, String firstName, String lastName)
{
	/* If no valid studentID, leave the value null. */
	if( isValidStudentID(studentID) )
		this.studentID = studentID;

	this.firstName = DEFAULT_FIRST_NAME;
	if( isValidFirstName(firstName) )
		this.firstName = firstName;

	this.lastName = DEFAULT_LAST_NAME;
	if( isValidLastName(lastName) )
		this.lastName = lastName;
}

/**
 * This second constructor validates the studentID, the firstName, and the
 * lastName (by means of the first constructor), sets the birthDate, and leaves
 * the targetDegree with its default value.
 *
 * @param studentID		student ID for this student. The length must be ID_LENGTH.
 * @param firstName		the student's given name.
 * @param lastName		the student's family name.
 * @param birthDate		the student's date-of-birth (precondition: not null)
 */
public Student(String studentID, String firstName, String lastName,
									FRCCDate birthDate)
{
	this(studentID, firstName, lastName);
	setBirthDate(birthDate);
}

/**
 * This third constructor validates the studentID, the firstName, the
 * lastName, sets the birthDate (all using the second constructor) and the
 * targetDegree.
 *
 * @param studentID		student ID for this student. The length must be ID_LENGTH.
 * @param firstName		the student's given name.
 * @param lastName		the student's family name.
 * @param birthDate		the student's date-of-birth (precondition: not null)
 * @param targetDegree	the student's degree program (precondition: not null)
 */
public Student(String studentID, String firstName, String lastName,
					FRCCDate birthDate,	Degree targetDegree)
{
	this(studentID, firstName, lastName, birthDate);
	setDegree(targetDegree);
}

/**
 * Get the Student's data-of-birth.
 *
 * @return		student's date-of-birth (null if not supplied)
 */
public FRCCDate getBirthDate()
{
	return birthDate;
}

/**
 * Set the birth date of the Student.
 * <p>
 * This method is "final" so that an overridable method will not be called from
 * a constructor.
 *
 * @param birthDate		the birth date of the Student (precondition: not null)
 */
public final void setBirthDate(FRCCDate birthDate)
{
	this.birthDate = birthDate;
}

/**
 * Get the target degree towards which the Student is working.
 *
 * @return		student's currently targeted degree (null if not supplied)
 */
public Degree getDegree()
{
	return targetDegree;
}

/**
 * Set the target degree towards which this student is working.
 * <p>
 * This method is "final" so that an overridable method will not be called from
 * a constructor.
 *
 * @param targetDegree	the target degree (precondition: not null)
 */
public final void setDegree(Degree targetDegree)
{
	this.targetDegree = targetDegree;
}

/**
 * Create a String detailing the Student's name, student ID, date of birth,
 * and the degree towards which the Student is working. Each item is
 * separated from the next by the system designated line.separator.
 *
 * @return		all details of student (name, ID, DOB, and degree).
 */
public String getDetails()
{
	/* The literal strings in this output occur only in this method. */
	return String.join(LINE_SEPARATOR,
		"Student: " + getFullName(),
		"Student ID: " + (studentID == null ?
				"<No valid StudentID supplied>" : studentID),
		"Birth Date: " + (birthDate == null ?
				"<Birth date not provided>" : birthDate),
		"Degree: " + (targetDegree == null ?
				"<No Degree specified)>" : targetDegree));
}

/**
 * Get the Student's first (given) name.
 *
 * @return				student's given name (null if no valid name supplied)
 */
public String getFirstName()
{
	return firstName;
}

/**
 * Set the first (given) name of the Student.
 *
 * @param firstName		the given name of the Student
 */
public void setFirstName(String firstName)
{
	this.firstName = DEFAULT_FIRST_NAME;
	if( isValidFirstName(firstName) )
		this.firstName = firstName;
}

/**
 * Get the full name of the student. If parts of the name have not been
 * validly supplied, this will be noted in the returned String.
 *
 * @return				full name (given name followed by family name).
 */
@SuppressWarnings("StringEquality")
public String getFullName()
{
	/* The test of the value of the firstName and the lastName depend for
	   correctness on the use of the given unique constant to fill out invalid
	   positions. */
	return (firstName == DEFAULT_FIRST_NAME ? INVALID_FIRST_NAME : firstName)
			+ " " +	(lastName == DEFAULT_LAST_NAME ? INVALID_LAST_NAME : lastName);
}

/**
 * Get the Student's last (family) name.
 *
 * @return				student's family name (null if no valid name supplied)
 */
public String getLastName()
{
	return lastName;
}

/**
 * Set the last (family) name of the Student.
 *
 * @param lastName		the family name of the Student
 */
public void setLastName(String lastName)
{
	this.lastName = DEFAULT_LAST_NAME;
	if( isValidLastName(lastName) )
		this.lastName = lastName;
}

/**
 * Get the student ID of the Student.
 *
 * @return				student ID (S and eight digits).
 */
public String getStudentID()
{
	return studentID;
}

/**
 * Student ID and name.
 *
 * @return				student ID and name
 */
@Override
public String toString()
{
	return getStudentID() + " " + getFullName();
}

/**
 * Validate a first name. The name must not be null or empty.
 *
 * @param name			the name to validate
 * @return				true if the name if valid
 */
private static boolean isValidFirstName(String name)
{
	return name != null && !name.isEmpty();
}

/**
 * Validate a last name. The name must not be null or empty.
 *
 * @param name			the name to validate
 * @return				true if the name if valid
 */
private static boolean isValidLastName(String name)
{
	return name != null && !name.isEmpty();
}

/**
 * Validate a student ID. It must consist of the letter 'S' followed by
 * exactly the number of digits to make up the total length.
 *
 * @param studentID		the student ID to validate
 * @return				true if the student ID if valid
 */
private static boolean isValidStudentID(final String studentID)
{
	/* If the ID is of the correct length, and starts with the letter 'S',
	   then check that the remaining characters are digits. This is done by
	   checking that all the remaining characters satisfy the isDigit
	   condition. */
	if( studentID.length() == ID_LENGTH && studentID.charAt(0) == 'S' )
	{
		int i;
		for( i = 1; i != ID_LENGTH; ++i )
			if( !Character.isDigit(studentID.charAt(i)) ) break;
		/* All the remaining characters were digits. */
		if( i == ID_LENGTH ) return true;
	}
	return false;
}
}

