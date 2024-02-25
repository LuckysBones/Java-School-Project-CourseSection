package edu.frontrange.csc240.a6.test;

import edu.frontrange.csc240.a6.Degree;
import edu.frontrange.csc240.a6.FRCCDate;
import edu.frontrange.csc240.a6.FRCCTime;
import edu.frontrange.csc240.a6.Student;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Set;
import software.haddon.util.ObjectCounter;

import static java.lang.Integer.max;
import static java.lang.Math.multiplyExact;
import static java.lang.System.exit;
import static java.lang.System.gc;
import static java.lang.System.out;
import static java.lang.System.runFinalization;
import static java.util.Objects.isNull;

/**
 * Test program for the features of the college system which is the subject of
 * the Assignment. This test program tests (very mildly) some of the features of
 * the Prefix, Course, and Section classes.
 * <p>
 * If the variable ACTIVATE_EXCEPTION_PROCESSING is set true, then testing for
 * the throwing of Exceptions is activated. Otherwise not.
 *
 * @author		Dr. Bruce K. Haddon, Instructor
 * @version		4.1, 2021-07-20, CSC-240 Assignment 6, per the Assignment
 *				Instructions
 */
@SuppressWarnings({ "UnusedAssignment", "NestedAssignment", "ProtectedInnerClass" })
public class Assignment6Test
{
/**
 * The CSC-241 Advanced Java Course
 */
private static final String ADVANCED = "\u0032\u0034\u0031";

/**
 * Computer Information Systems department/
 */
private static final String COMPUTER_INFORMATION_SYSTEMS = "\u0043\u0049\u0053";

/**
 * The Computer Networking department.
 */
private static final String COMPUTER_NETWORKING = "\u0043\u004E\u0047";

/**
 * The Computer Science department.
 */
private static final String COMPUTER_SCIENCE = "\u0043\u0053\u0043";

/**
 * The Computer Web-Based department.
 */
private static final String COMPUTER_WEB_BASED = "\u0043\u0057\u0042";

/**
 * Letters to use for student names.
 */
private static final String LETTERS = "bcdfghjklmnpqrstvwx";

/**
 * Generator for random numbers (fixed seed).
 */
private static final Random RANDOM = new Random(-1);

/**
 * The CSC-240 Java Course
 */
private static final String STANDARD = "\u0032\u0034\u0030";

/**
 * Count of error messages produced.
 */
private static int errorCount;

/**
 * The package for this assignment.
 */
private static String packageName;

/**
 * Activate the checking of expected exceptions.
 */
private final boolean ACTIVATE_EXCEPTION_PROCESSING = getClass().getSimpleName().contains("7");

//----------------------------- The actual tests -------------------------------
/**
 * Run the actual tests. The output is both compared visually to the expected
 * output, as well as the test checking anything it can.
 */
public void runTest()
{
	System.setErr(System.out);

	/* Error count is incremented with every error message produced. */
	errorCount = 0;

	/* Variable used for year calculations. */
	int year;

	/* Deduce the name of the package containing the classes to be tested.
	   Start by finding the Student class. All the other classes should be in the
	   same package. */
	String nameOfStudentClass = Student.class.getCanonicalName();
	packageName = Student.class.getCanonicalName().
							substring(0, nameOfStudentClass.length() -
										Student.class.getSimpleName().length());
	/* Package name if empty will not end with ".", otherwise it will. */

	/* Start by assuming no exceptions are expected. */
	Class<?> illegalStateException = null;
	Class<?> assertionError = null;
	Class<?> illegalArgumentException = null;
	Class<?> validationException = null;
	Class<?> courseException = null;
	Class<?> sectionException= null;

	List<String> errorList = new LinkedList<>();
	/* Set up the exceptions that may be expected in some implementations. */
	if( ACTIVATE_EXCEPTION_PROCESSING )
	{
		illegalStateException = IllegalStateException.class;
		assertionError = AssertionError.class;
		illegalArgumentException = IllegalArgumentException.class;
		validationException = Classes.VALIDATION_EXCEPTION.classClass(false);
		if( validationException == null )
			errorList.add("ValidationException missing/not implemented");

		courseException = Classes.COURSE_EXCEPTION.classClass(false);
		if( courseException == null )
			errorList.add("CourseException missing/not implemented");

		sectionException= Classes.SECTION_EXCEPTION.classClass(false);
		if( sectionException == null )
			errorList.add("SectionException missing/not implemented");
	}

// --------------------- Test program for the assignment. ----------------------
	String testName = this.getClass().getSimpleName();
	testName = testName.replace("Test", "");
	testName = testName.substring(0, testName.length() - 1) + " " +
										testName.substring(testName.length() - 1);
	markline();
	message("Test Program for " + testName);
	markline();

// ----------------------- Test setting up the prefixes. -----------------------
	message("-- Check the Prefix Enumeration class --");
	Object coursePrefix = null;
	ArrayList<String> valueStrings = new ArrayList<>();

	/* Make a list of names of the expected Prefixes, and collect the
	   objects. */
	valueStrings.addAll(Arrays.asList(COMPUTER_INFORMATION_SYSTEMS,
			COMPUTER_SCIENCE, COMPUTER_NETWORKING, COMPUTER_WEB_BASED));
	Object[] objects = Methods.VALUES.objectArrayInvoke(null, null);
	if( objects != null )
		/* For each of the collected objects, check the name. If the name is
		   recognized, remove it from the list (keeping track of the computer
		   science prefix) and printing the actual title. */
		for( var object : objects )
		{
			String stringName = Methods.NAME.stringInvoke(object, null);
			/* If the name is not recognized, inform the user. */
			if( !valueStrings.contains(stringName) )
				errorList.add("Prefix value " + stringName + " not recognized.");
			else
			{
				/* Otherwise, list the class and its name and the corresponding
				   title of the course. */
				if( stringName.equals(COMPUTER_SCIENCE) )
					coursePrefix = object;
				valueStrings.remove(stringName);
				message(stringName + " " +
						Methods.GET_TITLE.stringInvoke(object, null), 0, "values");
			}
		}
	/* Note any prefix values that were not defined. */
	valueStrings.forEach((remains) ->
			{	errorList.add("Prefix value " + remains + " not found."); } );
	if( errorList.isEmpty() )
		message("Prefixes OK");
	else
	{
		errorList.add("Classes (Section, Course, Exception, and/or Prefix values) not found");
		errorList.forEach((errorMessage) -> error(errorMessage));
		message("Test program terminated");
		exit(0);
	}
	line();

// -------------------------- Check the Course class ---------------------------
	message("-- Checking the Course class --");
	/* Prepare to create two correctly formed courses. */
	int credits;
	Object firstCourse = null;
	Object secondCourse = null;
	if( coursePrefix == null ) error("Prefix value CSC not defined");
	else
	{
		/* First course construction, using constructor(2). */
		String name = "Java Programming";
		credits = 3;
		message("Create first course");
		/* First parameter not null for expected exception. */
		firstCourse = Constructors.COURSE4.instance(null,
								coursePrefix, STANDARD, name, credits);
		String nameAsis = Fields.COURSE_NAME.stringAccess(firstCourse);
		if( nameAsis == null )
			error("Name of course has not been set, should be \"" + name + "\"");
		else if( !nameAsis.equals(name) )
			error("Name of course is incorrect: found \"" +
					nameAsis + "\", should be \"" + name + "\"");
		checkCredits(firstCourse, credits, validationException, false);

		/* Second course construction, using constructor(1). */
		name = "Advanced Java Programming";
		message("Create second course");
		/* First parameter not null for expected exception. */
		secondCourse = Constructors.COURSE3.instance(null,
								coursePrefix, ADVANCED, name);
		nameAsis = Fields.COURSE_NAME.stringAccess(secondCourse);
		if( nameAsis == null )
			error("Name of course has not been set, should be \"" + name + "\"");
		else if( !nameAsis.equals(name) )
			error("Name of course is incorrect: found \"" +
					nameAsis + "\", should be \"" + name + "\"");
		credits = Fields.MISSING_CREDITS_VALUE.intAccess(secondCourse);
		checkCredits(secondCourse, credits, validationException, false);
	}
	/* Check number of Course objects now present. */
	printObjectCount(Classes.COURSE.classClass(true), false, 2, "OK");

	/* Check the details of the first course. */
	if( firstCourse != null )
	{
		message("First Course: ");
		copy(Methods.COURSE_GET_DETAILS.stringInvoke(firstCourse, null), 0, "getDetails");
	} else
		error("First course not constructed");

	/* Check the required fields of the Course class. */
	checkFields(firstCourse);

	/* Check the details of the second course. */
	if( secondCourse != null )
	{
		message("Second Course: ");
		copy(Methods.COURSE_GET_DETAILS.stringInvoke(secondCourse, null), 0, "getDetails");
	} else
		error("Second course not constructed");
	/* Check the required fields of the Course class. */
	checkFields(secondCourse);

	markline();

	/* Checking the set and get credits methods. */
	message("-- Checking the set and get credits methods --");
	/* Check that setting credits works correctly. */
	credits = Fields.MISSING_CREDITS_VALUE.intAccess(secondCourse);
	checkCredits(secondCourse, credits, validationException, true);
	line();
	message("-- Checking the value returned by \"toString\" --");
	String result = Methods.TO_STRING.stringInvoke(firstCourse, null);
	String intended1 = coursePrefix + "\u002D" + STANDARD;
	String intended2 = coursePrefix + "\u2010" + STANDARD;
	if( !intended1.equals(result) && !intended2.equals(result) )
		error("Incorrect value \"" + result + "\" for Course \"toString\"");
	result = Methods.TO_STRING.stringInvoke(secondCourse, null);
	intended1 = coursePrefix + "\u002D" + ADVANCED;
	intended2 = coursePrefix + "\u2010" + ADVANCED;
	if( !intended1.equals(result) && !intended2.equals(result) )
		error("Incorrect value \"" + result + "\" for Course \"toString\"");
	line();

// -------------------------- Check the Section class --------------------------
	message("-- Checking the Section class --");
	/* Create a first section. */
	Object firstSection = null;
	/* First parameter not null for expected exception. */
	firstSection = Constructors.SECTION2.instance(null, firstCourse, "001");
	if( firstSection == null )
		error("First section not constructed.");
	else
	{
		/* The construction worked. */
		message("First section constructed.");
		String sectionNumberAsis =
				Methods.GET_SECTION_NUMBER.stringInvoke(firstSection, null);
		if( sectionNumberAsis == null )
			error("Section number of Section has not been set, should be \"" + "001" + "\"");
		else if( !sectionNumberAsis.equals("001" ) )
			error("Section number for section should be \"001\"");

		/* Check the required fields of the Section class. */
		checkFields(firstSection);

		year = randomYear(0);
		Methods.SET_DATES.voidInvoke(firstSection, null,
				new FRCCDate(8, 21, year), new FRCCDate(12, 10, year));
		Methods.SET_TIMES.voidInvoke(firstSection, null,
				new FRCCTime(10, 0, 0), new FRCCTime(11, 15, 0));
		int studentCount = 5;
		/* Add some students to the section. */
		Student[] sectionStudents = getStudents(studentCount);
		checkStudents(sectionStudents, sectionStudents.length, firstSection, "First");
	}

	/* Create a second section. */
	Object secondSection = null;
		/* First parameter not null for expected exception. */
	secondSection = Constructors.SECTION2.instance(null, firstCourse, "002");
	if( secondSection == null )
		error("Second section not constructed.");
	else
	{
		/* The construction worked. */
		message("Second section constructed.");
		if( !Methods.GET_SECTION_NUMBER.stringInvoke(secondSection, null).equals("002") )
			error("Section number for section should be \"002\"");

		/* Check the required fields of the Section class. */
		checkFields(secondSection);

		year = randomYear(0);
		Methods.SET_DATES.voidInvoke(secondSection, null,
				new FRCCDate(8, 21, year), new FRCCDate(12, 10, year));
		Methods.SET_TIMES.voidInvoke(secondSection, null,
				new FRCCTime(10, 0, 0), new FRCCTime(11, 15, 0));

		/* Add some students to the section. */
		Student[] sectionStudents =
		{
			studentGenerator(new FRCCDate(10, 12, randomYear(10))),
			studentGenerator(new FRCCDate(11, 1, randomYear(8))),
			studentGenerator(new FRCCDate(5, 24, randomYear(12)))
		};
		checkStudents(sectionStudents, sectionStudents.length, secondSection, "Second");
	}

	/* Create a third section. */
	Object thirdSection = null;
	/* First parameter not null for expected exception. */
	thirdSection = Constructors.SECTION2.instance(null, secondCourse, "001");
	if( thirdSection == null )
		error("Third section not constructed.");
	else
	{
		/* The construction worked. */
		message("Third section constructed.");
		if( !Methods.GET_SECTION_NUMBER.stringInvoke(thirdSection, null).equals("001") )
			error("Section number for section should be \"001\"");

		/* Check the required fields of the Section class. */
		checkFields(thirdSection);

		year = randomYear(0);
		Methods.SET_DATES.voidInvoke(thirdSection, null,
				new FRCCDate(8, 21, year), new FRCCDate(12, 10, year));
		Methods.SET_TIMES.voidInvoke(thirdSection, null,
				new FRCCTime(10, 0, 0), new FRCCTime(11, 15, 0));

		/* Add some students to the section. */
		Student[] sectionStudents =
		{
			studentGenerator(new FRCCDate(8, 13, randomYear(10)), Degree.AS),
			studentGenerator(new FRCCDate(5, 24, randomYear(12)), Degree.ASBUS)
		};
		checkStudents(sectionStudents, sectionStudents.length, thirdSection, "Third");

		printObjectCount(Classes.SECTION.classClass(true), false, 3, "OK");
	}
	line();

	/* The fourth Section is created with an invalid section number. */
	message("Attempt to construct fourth Section, but with an invalid section number");
	String invalidSectionNumber = "987654";
	Object fourthSection = null;
	fourthSection = Constructors.SECTION2.instance(
							sectionException, secondCourse, invalidSectionNumber);
	if( fourthSection != null )
	{
		if( sectionException == null )
		{
			String actualSectionNumber = Methods.GET_SECTION_NUMBER.stringInvoke(fourthSection, null);
			if( actualSectionNumber == null || !actualSectionNumber.isEmpty() )
				error("Invalid section number must be replaced by empty String");
		} else
			error("Section construction must not complete with invalid Section number");
	}
	line();

	/* Do the fourth section again, this time with a valid section number. */
	message("Attempt again creating the fourth Section, but with a valid section number");
	fourthSection = null;
	fourthSection = Constructors.SECTION2.instance(null, secondCourse, "002");
	if( fourthSection == null )
		error("Fourth section not constructed.");
	else
	{
		/* The section was created; check that the valid section number was
		   noticed. */
		message("Fourth section constructed with valid section number");
		if( !Methods.GET_SECTION_NUMBER.stringInvoke(fourthSection, null).equals("002") )
			error("Section number for section should be \"002\"");

		year = randomYear(0);
		Methods.SET_DATES.voidInvoke(fourthSection, null,
				new FRCCDate(8, 21, year), new FRCCDate(12, 10, year));
		Methods.SET_TIMES.voidInvoke(fourthSection, null,
				new FRCCTime(10, 0, 0), new FRCCTime(11, 15, 0));

// ------------------ Check on adding students to a section. -------------------
		/* Firstly, add a small number of students. */
		Student[] sectionStudents =
			{
				studentGenerator(new FRCCDate(11, 18, randomYear(7)), Degree.AS)
			};
		checkStudents(sectionStudents, sectionStudents.length, fourthSection, "Fourth");

		/* Try adding a  null student. */
		int currentCount = Methods.GET_ROSTER_COUNT.intInvoke(fourthSection, null);
		if( currentCount != sectionStudents.length )
			error("Method \"rosterCount\" of Section class returned incorrect value");
		else
		{
			message("Adding a null student to the roster");
			Methods.ADD_STUDENT.voidInvoke(fourthSection, illegalArgumentException, (Object) null);

			/* The student count should be unchanged. */
			if( currentCount == sectionStudents.length )
				message("Null student not added. OK");
			else
				error("Null student should not be added to the roster");
			line();
		}

		/* Now fill the section to the maximum number of students. */
		message("Adding enough students to fill the fourth Section");
		int studentCount = Fields.MAXIMUM_STUDENTS_PER_SECTION.intAccess(fourthSection);
		List<Student> additionalSectionStudents = new ArrayList<>();
		additionalSectionStudents.addAll(Arrays.asList(sectionStudents));
		additionalSectionStudents.addAll(Arrays.asList(getStudents(studentCount - 1)));
		sectionStudents = additionalSectionStudents.toArray(sectionStudents);
		checkStudents(sectionStudents, sectionStudents.length, fourthSection, "Fourth", 1);

		/* Attempt to add 10 more students. */
		message("Adding additional students to the Section");
		for( Student student : getStudents(10) )
			Methods.ADD_STUDENT.voidInvoke(fourthSection, illegalStateException, student);
		try
		{
			int getCount = Methods.GET_ROSTER_COUNT.intInvoke(fourthSection, null);
			if( getCount != sectionStudents.length )
				error("Fourth" + " section student count " + getCount +
					" incorrect; " + "should be " + (sectionStudents.length));
			else
				message("Number of students in Section still properly the maximum");
		} catch( Throwable th)
		{
			error("Unexpected "+ th.getClass().getSimpleName() +
					" in " + Methods.GET_ROSTER_COUNT.getMethodName() +
							" of Section");
		}
	}
	markline();
// ------------------------ Check setting dates and times. -------------------------
	message("-- Testing setting dates and times --");
	/* Check the assertions on the setDates and setTimes methods. */

	year = randomYear(0);
	message("Set date with null first date");
	Methods.SET_DATES.voidInvoke(fourthSection, assertionError,
			null, new FRCCDate(12, 10, year));
	message("Set date with null second date");
	Methods.SET_DATES.voidInvoke(fourthSection, assertionError,
			new FRCCDate(8, 21, year), null);
	message("Set time with null first time");
	Methods.SET_TIMES.voidInvoke(thirdSection, assertionError,
			null, new FRCCTime(11, 15, 0));
	message("Set time with null second time");
	Methods.SET_TIMES.voidInvoke(thirdSection, assertionError,
			new FRCCTime(10, 0, 0), null);
	markline();

// ------------- Check garbage collection and remaining instances --------------

	/* The code below uses System.gc() and System.runFinalization() (in the
	   ObjectCounter class. System.gc() should only ever be used in a testing and
	   debugging situation, it should never be part of a normal program except in
	   the most unusual circumstance. Calling System.gc() does not guarantee that
	   a garbage collection has been run, only that a "best effort" has been
	   expended. However, if it does run, a full garbage collection will be done.

	   Similarly, System.runFinalization()should only ever be used in a testing
	   and debugging situation, it should rarely be part of a normal program
	   except in the most unusual circumstance. Part of the problem is that it
	   can only be fully effective if a gc has just completed, but calling
	   System.gc() does not guarantee that a garbage collection has been run.
	   Hence, System.runFinalization() can also only say that a "best effort"
	   has been expended. */
	message("-- Testing counting Students, Courses and Sections --");
	message("Current values:");

	/* Print the state of affairs as it currently stands. */
	printObjectCount(Student.class, false, -1);
	printObjectCount(Classes.SECTION.classClass(true), false, -1);
	printObjectCount(Classes.COURSE.classClass(true), false, -1);

	message("Set first, second and fourth Sections to null then garbage collect");
	firstSection = null;
	secondSection = null;
	fourthSection = null;

	printObjectCount(Classes.SECTION.classClass(true), true, 1, "Should be 1");
						// Section count should now be 1, for  all
						// Sections except the third have been deleted, and
						// a garbage collection has happened.
	/* This ensures that there is still a remaining reference to a course.  */
	message("Set first Course to null then garbage collect ");
	firstCourse = null;
	printObjectCount(Classes.COURSE.classClass(true), true, 1, "Should be 1");

	message("Set second Course to null then garbage collect " +
			"(Count of Course objects should still be 1)");
	secondCourse = null;
	printObjectCount(Classes.COURSE.classClass(true), true,1, "Should still be 1");
						// Course count should still be 1, as the third Section
						// still holds a reference to the second course.

	/* A couple of students should remain referenced by a section. */
	message("What has happened to the student count with these collections?");
	printObjectCount(Student.class, true, 2, "Should be 2");
	markline();

// ----------------- Check on constructing incorrect courses. ------------------
	message("-- Testing operations with some invalid values --");

	/* Test of Course construction (3) with invalid course number. */
	message("Attempt Course construction with invalid course number");
	String courseNumber = "22A1";
	Object another = Constructors.COURSE3.instance(courseException,
								coursePrefix, courseNumber, "No such course");
	if( another == null )
	{
		if( courseException ==  null )
			error("Course with course number " +
								courseNumber + " not constructed.");
	}
	else
	{
		/* The course was created; check that the invalid course number was
		   noticed. */
		message("Course constructed with invalid course number.");
		String toStringAsis = Methods.TO_STRING.stringInvoke(another, null);
		if( toStringAsis == null )
			error("toString produces a null value");
		else if(toStringAsis.endsWith(courseNumber) )
			error("Course number for course should be empty");
	}

	/* Test of Course construction (3) with null course number. */
	courseNumber = null;
	another = Constructors.COURSE3.instance(courseException,
								coursePrefix, courseNumber, "No such course");
	if( another == null )
	{
		if( courseException ==  null )
			error("Course with course number " +
								courseNumber + " not constructed.");
	}
	else
	{
		/* The course was created; check that the invalid course number was
		   noticed. */
		message("Course constructed with null course number.");
		try
		{
			String actualCourseNumber = Fields.COURSE_NUM.stringAccess(another);
			if( actualCourseNumber == null || !actualCourseNumber.isEmpty())
				error("Course number for course should be empty");
		} catch (NullPointerException ex)
		{
			error("Null course number not recognized in Course constructor");
		}
	}

	/* Test of Course construction (3) with null course name. */
	String courseName = null;
	another = Constructors.COURSE3.instance(courseException,
								coursePrefix, ADVANCED, courseName);
	if( another == null )
	{
		if( courseException ==  null )
			error("Course with null course name not constructed.");
	}
	else
	{
		/* The course was created; check that the invalid course name was
		   noticed. */
		message("Course constructed with invalid course name.");
		if( Methods.COURSE_GET_DETAILS.stringInvoke(another, null).contains("null") )
			error("Course name for course should be empty.");
	}

	/* Test of course constructor with invalid number of credits, */
	another = Constructors.COURSE4.instance(courseException,
								coursePrefix, ADVANCED, "Invalid credits", -6);
	if( another == null )
	{
		if( courseException ==  null )
			error("Course with invalid number of credits not constructed");
	}
	else
	{
		/* The course was created; check that the invalid credits were
		   noticed. */
		credits = Fields.MISSING_CREDITS_VALUE.intAccess(another);
		message("Course constructed with invalid number of credits.");
		checkCredits(another, credits, validationException, true);
	}

	/* Create another section with an invalid section number. */

	String sectionNumber = "WXYZ";
	Object anotherCourse = Constructors.COURSE4.instance(null,
								coursePrefix, STANDARD, "Test", 3);
	if( anotherCourse != null )
	{
		another = Constructors.SECTION2.instance(sectionException,
				anotherCourse, sectionNumber);
		if( another == null )
		{
			if( sectionException == null )
				error("Section with empty section number not constructed.");
		} else
		{
			/* Give this section some dates. */
			year = randomYear(0);
			Methods.SET_DATES.voidInvoke(another, assertionError,
				new FRCCDate(8, 21, year), new FRCCDate(12, 10, year));
			Methods.SET_TIMES.voidInvoke(another, assertionError,
				new FRCCTime(10, 0, 0), new FRCCTime(11, 15, 0));
			/* The section was created; check that the invalid course number was
			   noticed. */
			message("Section constructed with invalid section number.");
			try
			{
				String details = Methods.SECTION_GET_DETAILS.stringInvoke(another, null);
				if( result == null )
					error( "Method \"getDetails\" of class \"SECTION\" could not be accessed");
				else if( details.contains("null") )
					error("Section number for course should be empty.");
			} catch( Throwable th )
			{
				error("Unexpected "+ th.getClass().getSimpleName() +
					" in " + Methods.GET_SECTION_NUMBER.getMethodName() +
							" of Section");
			}
		}
	}

	/* Create another section with an null section number. */
	sectionNumber = null;
	if( anotherCourse != null )
	{
		another = Constructors.SECTION2.instance(sectionException,
				anotherCourse, sectionNumber);
		if( another == null )
		{
			if( sectionException == null )
				error("Section with null section number not constructed.");
		} else
		{
			/* Give this section some dates. */
			year = randomYear(0);
			Methods.SET_DATES.voidInvoke(another, assertionError,
				new FRCCDate(8, 21, year), new FRCCDate(12, 10, year));
			Methods.SET_TIMES.voidInvoke(another, assertionError,
				new FRCCTime(10, 0, 0), new FRCCTime(11, 15, 0));
			/* The section was created; check that the invalid course number was
			   noticed. */
			message("Section constructed with null section number.");
			try
			{
				String details = Methods.SECTION_GET_DETAILS.stringInvoke(another, null);
				if( result == null )
					error( "Method \"getDetails\" of class \"SECTION\" could not be accessed");
				else if( details.contains("null") )
					error("Section number for course should be empty.");
			} catch( Throwable th )
			{
				error("Unexpected "+ th.getClass().getSimpleName() +
					" in " + Methods.GET_SECTION_NUMBER.getMethodName() +
							" of Section");
			}
		}
	}

	out.println();
	if( errorCount != 0 )
		error(errorCount + " error message" +
							(errorCount == 1 ? "" : "s") + " reported" );
	else
		message("No error messages reported");
}

/**
 * Check the operation of credits via constructor, and by set and get Credits.
 *
 * @param course		the Course instance to be tested
 * @param expected		the currently expected value from the Course instance
 * @param validationException if an exception is expected, is should be this one
 * @param all			true if all values are to be checked, else just the expected
 */
private void checkCredits(Object course, int expected, Class<?> validationException, boolean all)
{
	int maxCredits = Fields.MAXIMUM_CREDITS.intAccess(course);
	int minCredits = Fields.MINIMUM_CREDITS.intAccess(course);
	int missingCredits = Fields.MISSING_CREDITS_VALUE.intAccess(course);

	Integer[] checkValues = {null, maxCredits, minCredits, maxCredits + 1, minCredits - 1, missingCredits};
	Integer[] results = {expected, maxCredits, minCredits, missingCredits, missingCredits, missingCredits};

	Class<?> needException = null;
	if( checkValues.length != results.length )
		throw new AssertionError("checkValues and results not same length;");

	for( int i = 0; i != checkValues.length; ++i )
	{
		if( checkValues[i] != null )
		{
			needException =  results[i] == missingCredits ? validationException : null;
			Methods.SET_CREDITS.voidInvoke(course, needException , checkValues[i]);
		}
		if( results[i] != null )
		{
			int valueFound = Fields.CREDITS.intAccess(course);
			int valueGet = Methods.GET_CREDITS.intInvoke(course, null);
			if( valueFound != results[i] )
			{
				error("Credits for course is incorrect: found \"" +
					valueFound + "\", should be \"" + results[i] + "\"");
				break;
			}
			if( checkValues[i] != null )
				if( valueGet != results[i] )
				{
					error("setCredits/getCredits not the same for course: \"" +
						valueGet + "\", should be \"" + checkValues[i] + "\"");
					break;
				}
		}
		if( !all ) return;
	}
	needException =  expected == missingCredits ? validationException : null;
	Methods.SET_CREDITS.voidInvoke(course, needException, expected);
}

/**
 * Add the given students to a section, and check that they were correctly
 * added.
 *
 * @param sectionStudents	an array of students to be added
 * @param expected			number of students expected in roster
 * @param section			the section to which to add them
 * @param reference			the way to refer to this section
 * @param skip				optional count of students to skip adding (to account
 *							for students already added to the Section)
 */
private void checkStudents(Student[] sectionStudents, int expected, Object section,
		String reference, int... skip)
{
	/* Set up the skip count. Default is zero. */
	int skipCount = 0;
	if( skip != null & skip.length > 0 )
		skipCount = skip[0];

	Degree degree = Degree.AS;//randomDegree();
	boolean degreePresent = false;
	if( sectionStudents != null )
		for( Student student : sectionStudents )
		{
			if( student != null )
			{
				degreePresent |= student.getDegree() == degree;
				if( skipCount == 0 )
					Methods.ADD_STUDENT.voidInvoke(section, null, student);
				else
				--skipCount;
			}
		}
//	System.out.println("degreePresent = " + degreePresent);
	int getCount = Methods.GET_ROSTER_COUNT.intInvoke(section, null);
	if( getCount != expected )
		error(reference + " section student count " + getCount +
				" incorrect; should be " + expected);
	message("Details of the " +
			reference.toLowerCase(Locale.getDefault()) + " section");
	copy(Methods.SECTION_GET_DETAILS.stringInvoke(section, null), 0, "getDetails");
	message("Roster of the " +
			reference.toLowerCase(Locale.getDefault()) + " section");
	String roster1 = Methods.GET_ROSTER.stringInvoke(section, null);
	copy(roster1, getCount, "getRoster");
	String roster2 = Methods.GET_ROSTER.stringInvoke(section, null);
	if( !roster1.equals(roster2) )
		error("A second copy of the roster of the " +
				reference.toLowerCase(Locale.getDefault()) +
						" section not the same");
	boolean	isPresentResult = Methods.IS_DEGREE.booleanInvoke(section, null, degree);
	if( isPresentResult ^ degreePresent )
	{
		if( !degreePresent )
			error("isDegree reports Degree " + degree.name() + " (" +
					degree.getTitle() + ") " + "present in roster, but it should not be");
		else
			error("isDegree reports Degree " + degree.name() + " (" + degree.getTitle() + ") " +
					"not present in roster, but it should be");
	}
	line();
}

/**
 * Create a given number of Student objects.
 *
 * @param count	the given number
 * @return	array of that many students
 */
private Student[] getStudents(int count)
{
	Student[] result = new Student[count];
	for( int i = 0; i != count; ++i ) result[i] = studentGenerator();
	return result;
}

/**
 * Print the number of objects known of the indicated class
 *
 * @param clazz		the indicated class
 * @param gc		if true do a garbage collection before the actual count is taken
 * @param count		expected count (negative if to be ignored)
 * @param additionalMessage message to be added if actual count is good
 */
private void printObjectCount(Class<?> clazz, boolean gc,
							int count, String... additionalMessage)
{
	try
	{
		if( gc )
		{
			gc();gc();gc();		//	A gc "forces" items to be finalized to be
								//	placed on the finalization queue.
			runFinalization();	// This "forces" the finalization queue to be
								// processed
			message("Garbage collection executed");
		}
		int actual = ObjectCounter.getCounter(clazz);
		if( count < 0 || actual == count )
			message("Count of " + clazz.getSimpleName() +
				" objects = " + actual + " " +
					((additionalMessage != null && additionalMessage.length == 1) ?
							"(" + additionalMessage[0] + ")" : ""));
		else
			error("Count of " + clazz.getSimpleName() +
							" objects is " + actual + "; should be " + count);
	} catch( NullPointerException ex )
	{
		error("Class " + clazz.getSimpleName() + " has no ObjectCounter declared");
	}
}

/**
 * Factory method for creating what may be reasonable "students." This method
 * uses one of the three available Student constructors, depending upon the
 * information supplied to the method. The values supplied may be any of
 * (1) none, meaning the Student is instantiated without either a date of birth
 * or a declared major; (2) with a date of birth, or (3) a date of birth and a
 * declared major. The given parameters are checked to be of the correct type.
 *
 *
 * @param dateDegree	an array, which may be empty, contain a date of birth, or
 *						contain a date of birth and a declared major.
 * @return				an instantiated Student object.
 */
@SuppressWarnings({"UseSpecificCatch", "BroadCatchBlock", "TooBroadCatch"})
private Student studentGenerator(Object... dateDegree)
{
	/*  Random number for student number. Get a different set of student
	    names each half-year. */
	LocalDate now = LocalDate.now();
	int seed = now.getYear() * 2;
	if( now.getDayOfYear() > 182 ) ++seed;
	int rnumber;
	boolean again = false;
	String studentNumber = "";
	do
		try
		{
			rnumber = multiplyExact((rnumber = RANDOM.nextInt(seed)), rnumber);
			String snumber = String.format("%030d", rnumber);
			int slength = snumber.length();
			studentNumber = "S" + snumber.substring(slength - 8, slength);
		} catch( ArithmeticException ex )
		{
			again = true;
		}
	while( again );

	/* Four to eight random letters for a family name. */
	rnumber = RANDOM.nextInt(5) + 4;
	StringBuilder familyName = new StringBuilder(4);
	for( int j = 0; j != rnumber; ++j )
		familyName.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
	familyName.setCharAt(0, Character.toUpperCase(familyName.charAt(0)));

	/* Three to seven random letters for a given name. */
	rnumber = RANDOM.nextInt(5) + 3;
	StringBuilder givenName = new StringBuilder(3);
	for( int j = 0; j != rnumber; ++j )
		givenName.append(LETTERS.charAt(RANDOM.nextInt(LETTERS.length())));
	givenName.setCharAt(0, Character.toUpperCase(givenName.charAt(0)));

	/* Instantiate a Student using one of the three constructors. */
	Degree degree = randomDegree();
	try
	{
		return switch( dateDegree.length )
		{
		case 0 ->
		{
			Student student = new Student(studentNumber,
						givenName.toString(), familyName.toString());
			if( RANDOM.nextInt(3) != 0 )
				student.setBirthDate(new FRCCDate( RANDOM.nextInt(12) + 1,
								RANDOM.nextInt(28) + 1, randomYear(0)));
			if( RANDOM.nextFloat() < 0.3F )
				student.setDegree(degree);
			yield student;
		}
		case 1 ->
				dateDegree[0] instanceof FRCCDate ?
					new Student(studentNumber,
							givenName.toString(), familyName.toString(),
								(FRCCDate) dateDegree[0]) :
					new Student(studentNumber,
							givenName.toString(), familyName.toString());
		case 2 ->
				dateDegree[0] instanceof FRCCDate && dateDegree[1] instanceof Degree ?
					new Student(studentNumber, givenName.toString(), familyName.toString(),
								(FRCCDate) dateDegree[0], (Degree) dateDegree[1]) :
					new Student(studentNumber, givenName.toString(), familyName.toString(),
								(FRCCDate) dateDegree[0], degree);
		default ->
				throw new AssertionError("Error in studentGenerator");
		};
	} catch( Exception ex )
	{
		error("Error: dateDegree.length = " + dateDegree.length +
				" studentNumber = " + studentNumber +
				" names = " + givenName.toString() + ", " + familyName.toString() +
					 ex.toString());
		return null;
	}
}

/**
 * Program entry point.
 * <p>
 * Execute:</p>
 * <pre> java edu.frontrange.csc240.a6.Assignment6Test</pre>
 *
 * @param args	unused.
 */
public static void main(String... args)
{
	try
	{
		/* Run the actual tests. */
		new Assignment6Test().runTest();
	} catch( Throwable th )
	{
		/* Some exception other than those expected has happened. */
		error("Unexpected exception: " + th.toString());
		throw th;
	}
}

//--------------------- Message and other output methods -----------------------
/**
 * Used for printing copies of messages from the object being tested.Prints the
 * message surrounded by quotes.
 *
 * @param copy		the String to copy to the output
 * @param count		expected entries in the copy String
 * @param method	name of the method used for the string
 */
protected static void copy(String copy, int count, String method)
{
	if( copy == null )
		error("Output string is null");
	else
	{
		checkStringForEndline(copy, count, method);
		out.println("\"" + copy + "\"");
	}
}

/**
 * Check the string for an endline character at the end.If so, produce an error
 * saying that the designated method produced a result (a String) that has an
 * endline (where it should not).
 *
 * @param string		the String to be checked
 * @param count			expected number of line in string (if not 0)
 * @param method		the method to note if the endline is present
 */
protected static void checkStringForEndline(String string, int count, String method)
{
	if( method != null && !method.isBlank() & string.length() > 2 )
	{
		String end = string.substring(string.length() - 1, string.length());
		if( end.equals("\n") || end.equals("\r") )
		{
			error("Result of " + method + " has an extra line.separator");
		}
	}
	/* If count is not zero, check number of entries in the string variable. */
	if( count != 0 )
	{
		/* Count number of lines in the String string by ensuring a single \n
		   after each line. */
		String counterString = string.replace("\r", "\n");
		counterString += "\n";
		counterString = counterString.replaceAll("\\R{2,}", "\n");

		/* Index + 1 is the next starting position. */
		int index = -1;
		int localCount = 0;
		while( (index = counterString.indexOf('\n', index + 1)) >= 0 )
			++localCount;
		if( count != localCount )
			error("Unexpected number of lines: " +
					"found = " + localCount + " expected = " + count);
		else
			message("Entries counted = " + localCount);
	}
}

/**
 * Print an error message--looks like a regular message, but with two question
 * marks.
 *
 * @param message	the message to the user.
 */
protected static void error(String message)
{
	++errorCount;
	out.println("??*  " + message);
}

/**
 * Print an empty line.
 */
protected static void line()
{
	out.println();
}

/**
 * Print a row of asterisks to mark off different parts of the output.
 */
protected static void markline()
{
	out.println("***********************************************");
}

/**
 * Output a message with the test marker prepended.
 *
 * @param message		the message
 */
protected static void message(String message)
{
	message(message, 0, null);
}

/**
 * Output a message with the test marker prepended.If method is present, the
 * message will be checked for an endline character at the end--if so, an extra
 * error message will be produced.
 *
 * @param message		the message
 * @param count			expected entries in the message (or 0 if it does not matter)
 * @param methodName	name of the method if appropriate
 */
protected static void message(String message, int count, String methodName)
{
	if( message == null)
		error("Message to be printed in null");
	else
	{
		checkStringForEndline(message, count, methodName);
		out.println(" **  " + message);
	}
}

/**
 * Select a degree for use as data.
 *
 * @return				the selected year
 */
private static Degree randomDegree()
{
	List<Degree> degrees = new LinkedList<>();
	degrees.addAll(Arrays.asList(Degree.values()));
	int random = RANDOM.nextInt(degrees.size());
	return degrees.get(random);
}

/**
 * Select a year for use as data.
 *
 * @param range			0 if result is to be close to current year, otherwise a
 *						range around 25 year in	the past
 * @return				the selected year
 */
private static int randomYear(int range)
{
	LocalDate now = LocalDate.now();
	int medianAge = range == 0 ? 0 : 25;
	range = max(1, range);
	LocalDate random = now.minusYears(medianAge + RANDOM.nextInt(range) - range/2 + 1);
	return random.getYear();
}

//------------------------ Reflective calling support --------------------------

/**
 * Enumeration of the different classes involved in the exercise
 */
@SuppressWarnings("ProtectedInnerClass")
protected static enum Classes
{
/**
 * The class Prefix indicating department.
 */
PREFIX("Prefix"),

/**
 * The class Course, indicating the course.
 */
COURSE("Course"),

/**
 * The class Section, being a section of a Course.
 */
SECTION("Section"),

/**
 * Students, who are registered with Sections.
 */
STUDENT("Student"),

/**
 * Indicating a date (for a Section)
 */
DATE("FRCCDate"),

/**
 * Exception for a validation failure.
 */
VALIDATION_EXCEPTION("ValidationException"),

/**
 * Exception for failure to construct a Course.
 */
COURSE_EXCEPTION("CourseException"),

/**
 * Exception for failure to construct a Section.
 */
SECTION_EXCEPTION("SectionException");

/**
 * The class of the class.
 */
private Class<?> clazz;

/**
 * The class loader for this application.
 */
private final ClassLoader classLoader;

/**
 * The name of the class.
 */
private final String name;

/**
 * What has been reported
 */
private final Set<String> reported = new HashSet<>();

/**
 * Constructor: private to prevent external instantiation.
 *
 * @param name	the name of the class
 */
private Classes(String name)
{
	/* Remember the name, and identify the class loader. */
	this.name = name;
	this.classLoader = this.getClass().getClassLoader();
}

/**
 * Access the actual class of the Classes enumeration object.
 *
 * @param report	if absence of the class is to be reported
 * @return the actual class of the Classes enumeration object
 */
public Class<?> classClass(boolean report)
{
	/* If this class has not yet been loaded, then load the class. */
	if( clazz == null )
		try
		{
			clazz = classLoader.loadClass(packageName  + name);
		} catch( ClassNotFoundException ex )
		{
			/* Report the class cannot be found if report is desired, and it
			   has not yet been reported. */
			if( report & !reported.contains(name) )
			{
				error(ex.toString());
				reported.add(name);
			}
		}
	return clazz;
}
	}

/**
 * Enumerate and define the constructors for the classes.
 */
protected static enum Constructors
{
/**
 * {@code Course} constructor with three parameters.
 */
COURSE3(Classes.COURSE, Classes.PREFIX.classClass(true),
					String.class, String.class),

/**
 * {@code Course} constructor with four parameters.
 */
COURSE4(Classes.COURSE, Classes.PREFIX.classClass(true),
					String.class, String.class, int.class),

/**
 * {@code Section} constructor with two parameters.
 */
SECTION2(Classes.SECTION, Classes.COURSE.classClass(true),
					String.class),

/**
 * {@code Student} constructor with three parameters.
 */
STUDENT3(Classes.STUDENT, String.class,
					String.class, String.class),

/**
 * {@code Student} constructor with four parameters.
 */
STUDENT4(Classes.STUDENT, String.class,
					String.class, String.class, Classes.DATE.classClass(true));

/**
 * The enumeration value designating the class.
 */
private final Classes classic;

/**
 * The actual constructor for the enumeration value.
 */
private Constructor<?> constructor;

/**
 * The types of the parameters needed for this constructor.
 */
private final Class<?>[] types;

/**
 * Constructor: private to prevent external instantiation.
 *
 * @param classic	the enumeration value for the class
 * @param types	the types of the parameters to the constructor
 */
private Constructors(Classes classic, Class<?>... types)
{
	this.classic = classic;
	this.types = types;
}

/**
 * Obtain an instance of the class.
 *
 * @param arguments		the arguments to the constructor for this instance
 * @param exception		if not null, the class of an expected exception
 *
 *
 * @return			the instance
 */
public Object instance(Class<?> exception, Object... arguments)
{
	Object result = null;
	/* If the constructor has not yet been found ... */
	if( constructor == null )
		try
		{
			Class<?> classClass = classic.classClass(true);
			if( classClass == null )
				throw new InstantiationException(
						classic.name() + ": constructor not found");
			constructor = classClass.getConstructor(types);
			if( constructor == null )
				throw new InstantiationException(
						classic.name() + ": no available constructor");
			constructor.setAccessible(true);
		} catch( NoSuchMethodException | SecurityException |
				InstantiationException ex )
		{
			error("Error: " + ex.toString());
		}
	/* If the constructor is successfully found, then create an instance of
	   that class.  */
	if( constructor != null)
		try
		{
			result = constructor.newInstance(arguments);
			if( exception != null )
				error( exception.getSimpleName() +
											" expected but did not happen.");
		} catch( InvocationTargetException ex)
		{
			if( ex.getCause() == null ) error(ex.toString());
			else if( ex.getCause().getClass() == exception )
			{
					message("OK, expected: " + ex.getCause().toString());

					Throwable furtherCause = ex.getCause().getCause();
					if( furtherCause != null )
					{
						message("Cause of the exception is:");
						message("   " +	furtherCause.toString());
					}
			}
			else
				error("Unexpected exception: " + ex.getCause().toString());
		} catch( InstantiationException | IllegalAccessException |
				IllegalArgumentException | 	NullPointerException ex )
		{
			error("Error: " + ex.toString());
		}
	return result;
}
}

/**
 * Enumeration of the methods used in the tests.
 */
protected static enum Methods
{
/**
 * {@code addStudent} method of the class {@code Section}.
 */
ADD_STUDENT(Classes.SECTION, "addStudent", Student.class),

/**
 * {@code getDetails} method of the class {@code Course}.
 */
COURSE_GET_DETAILS(Classes.COURSE, "getDetails"),

/**
 * {@code getCredits} method of the class {@code Course}.
 */
GET_CREDITS(Classes.COURSE, "getCredits"),

/**
 * {@code getRoster} method of the class {@code Section}.
 */
GET_ROSTER(Classes.SECTION, "getRoster"),

/**
 * {@code getSectionNumber} method of the class {@code Section}.
 */
GET_SECTION_NUMBER(Classes.SECTION, "getSectionNumber"),

/**
 * {@code getRosterCount} method of the class {@code Section}.
 */
GET_ROSTER_COUNT(Classes.SECTION, "getRosterCount"),

/**
 * {@code getTitle} method of the class {@code Prefix}.
 */
GET_TITLE(Classes.PREFIX, "getTitle"),

/**
 * {@code isDegree} method of the class {@code Section}.
 */
IS_DEGREE(Classes.SECTION, "isDegree", Degree.class),

/**
 * {@code name} method of the class {@code Prefix}.
 */
NAME(Classes.PREFIX, "name"),

/**
 * {@code getDetails} method of the class {@code Section}.
 */
SECTION_GET_DETAILS(Classes.SECTION, "getDetails"),

/**
 * {@code setCredits} method of the class {@code Course}.
 */
SET_CREDITS(Classes.COURSE, "setCredits", int.class),

/**
 * {@code setDates} method of the class {@code Section}.
 */
SET_DATES(Classes.SECTION, "setDates", FRCCDate.class, FRCCDate.class),

/**
 * {@code setTimes} method of the class {@code Section}.
 */
SET_TIMES(Classes.SECTION, "setTimes", FRCCTime.class, FRCCTime.class),

/**
 * {@code toString} method of the class {@code Course}.
 */
TO_STRING(Classes.COURSE, "toString"),

/**
 * {@code values} method of the class {@code Prefix}.
 */
VALUES(Classes.PREFIX, "values");

/**
 * The actual class containing the method.
 */
private final Class<?> clazz;

/**
 * Reflective access to the method.
 */
private Method method = null;

/**
 * The known name of the method.
 */
private final String methodName;

/**
 * The array of the types of the parameters to this method.
 */
private final Class<?>[] parameters;

/**
 * Constructor: private for use only within the class.
 *
 * @param classo		class (or Classes enum) containing the method
 * @param name			name of the method
 * @param parameters	the types of each of the parameters to the method
 */
private Methods(Object classo, String name, Class<?>... parameters)
{
	this.methodName = name;
	this.parameters = parameters;

	clazz = classo instanceof Class ? (Class<?>) classo :
			classo instanceof Classes ? ((Classes) classo).classClass(true) : null;

}

/**
 * Gets the actual name (as a String) of the method.
 *
 * @return				the actual name of the method
 */
public String getMethodName()
{
	return methodName;
}

/**
 * Call a method returning a boolean value.
 *
 * @param instance		the instance of the class
 * @param exception		if not null, the class of an expected exception
 * @param parameters	parameters of the method
 * @return				the value returned by the method call
 */
@SuppressWarnings("CallToPrintStackTrace")
public boolean booleanInvoke(Object instance, Class<?> exception, Object... parameters)
{
	boolean result = false;
	if( setMethod() )
		try
		{
			result = (boolean) method.invoke(instance, parameters);
		if( exception != null )
				error( exception.getSimpleName() +
											" expected but did not happen.");
		} catch( InvocationTargetException ex)
		{
			if( ex.getCause() == null ) error(ex.toString());
			else if( ex.getCause().getClass() == exception )
					message("OK, expected: " + ex.getCause().toString());
			else
			{
				error("Unexpected exception: " + ex.getCause().toString());
				ex.getCause().printStackTrace();
			}
		} catch( SecurityException | IllegalAccessException |
				IllegalArgumentException | 	NullPointerException ex )
		{
			error("Error: " + ex.toString());
		}
	return result;
}

/**
 * Call a method returning an int value.
 *
 * @param instance		the instance of the class
 * @param exception		if not null, the class of an expected exception
 * @return				the value returned by the method call
 */
@SuppressWarnings("CallToPrintStackTrace")
public int intInvoke(Object instance, Class<?> exception)
{
	int result = 0;
	if( setMethod() )
		try
		{
			result = (int) method.invoke(instance);
		if( exception != null )
				error( exception.getSimpleName() +
											" expected but did not happen.");
		} catch( InvocationTargetException ex)
		{
			if( ex.getCause() == null ) error(ex.toString());
			else if( ex.getCause().getClass() == exception )
					message("OK, expected: " + ex.getCause().toString());
			else
			{
				error("Unexpected exception: " + ex.getCause().toString());
				ex.getCause().printStackTrace();
			}
		} catch( SecurityException | IllegalAccessException |
				IllegalArgumentException | 	NullPointerException ex )
		{
			error("Error: " + ex.toString());
		}
	return result;
}

/**
 * Call a method returning an Object value.
 *
 * @param instance		the instance of the class
 * @param exception		if not null, the class of an expected exception
 * @return				the value returned by the method call
 */
@SuppressWarnings("CallToPrintStackTrace")
public Object[] objectArrayInvoke(Object instance, Class<?> exception)
{
	Object[] result = new Object[0];
	if( setMethod() )
		try
		{
			result = (Object[]) method.invoke(instance);
			if( exception != null )
				error( exception.getSimpleName() +
											" expected but did not happen.");
		} catch( InvocationTargetException ex)
		{
			if( ex.getCause() == null ) error(ex.toString());
			else if( ex.getCause().getClass() == exception )
					message("Expected: " + ex.getCause().toString());
			else
			{
				error("Unexpected exception: " + ex.getCause().toString());
				ex.getCause().printStackTrace();
			}
		} catch( SecurityException | IllegalAccessException |
				IllegalArgumentException | 	NullPointerException ex )
		{
			error("Error: " + ex.toString());
		}
	return result;
}

/**
 * Call a method returning a String value.
 *
 * @param instance		the instance of the class
 * @param exception		if not null, the class of an expected exception
 * @return				the value returned by the method call
 */
@SuppressWarnings("CallToPrintStackTrace")
public String stringInvoke(Object instance, Class<?> exception)
{
	String result = null;
	if( setMethod() )
		try
		{
			result =  (String) method.invoke(instance);
			if( exception != null )
				error( exception.getSimpleName() +
											" expected but did not happen.");
		} catch( InvocationTargetException ex)
		{
			if( ex.getCause() == null ) error(ex.toString());
			else if( ex.getCause().getClass() == exception )
					message("Expected: " + ex.getCause().toString());
			else
			{
				error("Unexpected exception: " + ex.getCause().toString());
				ex.getCause().printStackTrace();
			}
		} catch( SecurityException | IllegalAccessException |
				IllegalArgumentException | 	NullPointerException ex )
		{
			error("Error: " + ex.toString());
		}
	return result;
}

/**
 * Call a method returning no value.
 *
 * @param instance		the instance of the class
 * @param exception		if not null, the class of an expected exception
 * @param parameters	the values for the parameters of the call
 */
@SuppressWarnings("CallToPrintStackTrace")
public void voidInvoke(Object instance, Class<?> exception, Object... parameters)
{
	if( setMethod() )
		try
		{
			method.invoke(instance, parameters);
			if( exception != null )
				error( exception.getSimpleName() +
											" expected but did not happen.");
		} catch( InvocationTargetException ex)
		{
			if( ex.getCause() == null ) error(ex.toString());
			else if( ex.getCause().getClass() == exception )
					message("Expected: " + ex.getCause().toString());
			else
			{
				error("Unexpected exception: " + ex.getCause().toString());
				ex.getCause().printStackTrace();
			}
		} catch( SecurityException | IllegalAccessException |
				IllegalArgumentException | 	NullPointerException ex )
		{
			error("Error: " + ex.toString());
		}
}

/**
 * Delayed setting of the method variable
 *
 * @return	true if the method variable is successfully set
 */
private boolean setMethod()
{
	if( isNull(clazz) ) return false;
	/* If the variable is not already set... */
	if( isNull(method) )
		try
		{
			/* Get the Method object by reflection on the class. */
			method = clazz.getMethod(methodName, parameters);
		} catch( NoSuchMethodException | SecurityException |
				IllegalArgumentException | NullPointerException ex )
		{
			/* The reflection did not succeed in finding the method. */
			error("Method \"" + methodName + "\" of class \"" +
								clazz.getSimpleName() + "\" not found.");
			return false;
		}
	/* Method was already set, or was found on this call. */
	return true;
}
}

/**
 * Check that the public and private fields needed for the tests are present,
 * and, if needed, have the correct values.
 *
 * @param object		the Course object to check
 */
private void checkFields(Object object)
{
	/* Get a list of the expected fields. */
	Fields[] fields = Fields.values();
	if( object != null )
	{
		/* For each of the Fields objects, the status, and if needed, the
		   value. */
		for( Fields field : fields )
		{
			/* Ensure that the field is meant to be a field of the object being
			   checked. */
			if( object.getClass() == field.getClassOfField() )
				try
				{
					field.setField();
					int modifiers = field.getField().getModifiers();
					/* Otherwise, the field has been recognized */
					if( field.isAccess() )
					{
						/* The field is public, so should also be static, and
						   may have an initial value. */
						if( !Modifier.isPublic(modifiers) )
							error("Field \"" + field.getVariableName() +
												"\" is not public");
						if( !Modifier.isStatic(modifiers) )
							error("Field \"" + field.getVariableName() +
												"\" is not static");

						/* If there is a value specified, check it. */
						if( field.getValue() != null )
						{
							int intendedValue = field.getValue();
							/* The actual value from the field. */
							int fieldValue = field.intAccess(object);
							if( fieldValue != intendedValue )
								error("Field \"" + field.getVariableName() +
												"\" does not have correct value");
						}
					} else
					{
						/* The field shoud be private. */
						if( !Modifier.isPrivate(modifiers) )
							error("Field \"" + field.getVariableName() +
												"\" is not private");
					}
				}
				catch( Exception ex)
				{
					error("\"" + field.name() + "\" not accessible or does " +
							"not exist");
				}
		}
	}
}

/**
 * Enumeration of the fields used in the tests.
 */
protected static enum Fields
{
/**
 * The {@code courseName} field of the class {@code Course}.
 */
COURSE_NAME(Classes.COURSE, "courseName", false, null),

/**
 * The {@code courseNum} field of the class {@code Course}.
 */
COURSE_NUM(Classes.COURSE, "courseNum", false, null),

/**
 * The {@code credits} field of the class {@code Course}.
 */
CREDITS(Classes.COURSE, "credits", false, -1),

/**
 * The {@code MISSING_CREDITS_VALUE} field of the class {@code Course}.
 */
MISSING_CREDITS_VALUE(Classes.COURSE, "MISSING_CREDITS_VALUE", true, null),

/**
 * The {@code MAXIMUM_CREDITS} field of the class {@code Course}.
 */
MAXIMUM_CREDITS(Classes.COURSE, "MAXIMUM_CREDITS", true, 5),

/**
 * The {@code MINIMUM_CREDITS} field of the class {@code Course}.
 */
MINIMUM_CREDITS(Classes.COURSE, "MINIMUM_CREDITS", true, 0),

/**
 * The {@code COURSE_NUMBER_LENGTH} field of the class {@code Course}.
 */
COURSE_NUMBER_LENGTH(Classes.COURSE, "COURSE_NUMBER_LENGTH", true, 3),

/**
 * The {@code MAXIMUM_STUDENTS_PER_SECTION} field of the class {@code Section}.
 */
MAXIMUM_STUDENTS_PER_SECTION(Classes.SECTION, "MAXIMUM_STUDENTS_PER_SECTION", true, 32),
/**
 * The {@code SECTION_NUMBER_LENGTH} field of the class {@code Section}.
 */
SECTION_NUMBER_LENGTH(Classes.SECTION, "SECTION_NUMBER_LENGTH", true, 3);

/**
 * The actual class containing the field.
 */
private final Class<?> clazz;

/**
 * Reflective access to the field.
 */
private Field field = null;

/**
 * True if access to this field is to be public, otherwise private.
 */
private final boolean access;

/**
 * If public access to the field, then should also be static, and have an initial
 * value.
 */
private final Integer value;

/**
 * The known name of the field.
 */
private final String name;

/**
 * Constructor: private for use only within the class.
 *
 * @param classo		class (or Classes enum) containing the method
 * @param name			name of the field
 * @param access		access to the field (true if required to be public,
 *						otherwise private
 * @param value			the expected value of the field
 */
private Fields(Object classo, String name, boolean access, Integer value)
{
	this.name = name;
	clazz = classo instanceof Class ? (Class<?>) classo :
			classo instanceof Classes ?
								((Classes) classo).classClass(true) : null;
	this.access = access;
	this.value = value;
}

/**
 * @return the value
 */
public Integer getValue()
{
	if( !access ) throw new AssertionError("No value available for private field");
	return value;
}

/**
 * Access an int field.
 *
 * @param instance	the instance of the class
 * @return			the value accessed
 */
public int intAccess(Object instance)
{
	if( setField() )
		try
		{
			return (int) field.get(instance);
		} catch( SecurityException | IllegalAccessException |
				NullPointerException ex )
		{
			error(ex.toString());
		}
	return 0;
}

/**
 * Access a String field.
 *
 * @param instance	the instance of the class
 * @return			the value accessed
 */
public String stringAccess(Object instance)
{
	if( setField() )
		try
		{
			return (String) field.get(instance);
		} catch( SecurityException | IllegalAccessException |
				NullPointerException ex )
		{
			error(ex.toString());
		}
	return null;
}

/**
 * Delayed setting of the field variable
 *
 * @return				true if the field variable is successfully set
 */
private boolean setField()
{
	/* If the variable is not already set... */
	if( field == null )
		try
		{
			/* Get the Method object by reflection on the class. */
			field = clazz.getDeclaredField(name);
			field.setAccessible(true);
		} catch( NoSuchFieldException | SecurityException |
				IllegalArgumentException | NullPointerException ex )
		{
			/* The reflection did not succeed in finding the method. */
			error("Field \"" + name + "\" in class \"" + clazz + "\" not found.");
			return false;
		}
	/* Field was already set, or was found on this call. */
	return true;
}

/**
 * Return true if access to this field is both public and static. Otherwise, it
 * should be private.
 *
 * @return				the access
 */
public boolean isAccess()
{
	return access;
}

/**
 * Return the actual Field object associated with this Fields constant.
 *
 * @return				the field
 */
public Field getField()
{
	return field;
}

/**
 * Return the class associated with this Fields constant.
 *
 * @return				the class
 */
public Class<?> getClassOfField()
{
	return clazz;
}

/**
 * Return the name of the actual Course variable that is this field.
 *
 * @return				the name of the actual Course variable that is this field
 */
public String getVariableName()
{
   return name;
}
}
}
