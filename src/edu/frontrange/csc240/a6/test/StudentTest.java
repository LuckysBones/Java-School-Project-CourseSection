
package edu.frontrange.csc240.a6.test;

import edu.frontrange.csc240.a6.Degree;
import edu.frontrange.csc240.a6.FRCCDate;
import edu.frontrange.csc240.a6.Student;
import java.time.LocalDate;
import java.util.Random;
import software.haddon.util.ObjectCounter;

import static java.lang.Integer.max;
import static java.lang.System.out;

/**
 * A test program for ensuring that the Student class is functional.
 * This is a very brief test, and does not test in depth.
 *
 * @author		Dr. Bruce K. Haddon, Instructor
 * @version		4.0, 2020-08-22, CSC-240 Assignment 6, per the Assignment
 *				Instructions
 */
public class StudentTest
{
/**
 * Source of random numbers.
 */
private static final Random RANDOM = new Random();

/**
 * A collection of "students" to use in this test program.
 */
private static Student[] TEST_STUDENTS = new Student[]
		{
			new Student("S00001234", "Mdffthp", "Fjspw",
					new FRCCDate(12, 10, randomYear(10)).setISO8601(true), Degree.AASCIS),
			new Student("S00005678", "Mkdwqk", "Fjspw",
					new FRCCDate(2, 15, randomYear(15)), Degree.AS),
			new Student("S00009999", "Mkxtr", "Fjspw",
					new FRCCDate(4, 10, randomYear(12)), Degree.AA)
		};

/**
 * Run the actual tests.
 */
@SuppressWarnings("UnusedAssignment")
public void run()
{
	System.setErr(out);

	out.println("** Testing Student Class");
	out.println();
	out.println("** Test Student Values:");
	out.println();

	for( Student testStudent : TEST_STUDENTS )
	{
		out.println("** \"" +testStudent.getDetails() + "\"");
		out.println(testStudent.toString());
		out.println();
	}

	/* Create a student that is incorrect (the student number is not long
	   enough, and will cause a failure. */
	out.println("** The next student is expected to cause a failure.");
	Student testStudentX = new Student("S0000678", "Jjfbn", "Fjspw");
	out.println("** \""+ testStudentX.getDetails() + "\"");
	out.println();

	out.println("** Count of Student instances should be 4, and is: " +
									ObjectCounter.getCounter(Student.class));

	/* These following statements are purely for test purposes. Calls to System.gc()
	   and System.runFinalization() are not recommended for any ordinary coding
	   style. The use here is to simply show that the Student objects when
	   discarded (the references set to null) may actually be collected. */
	testStudentX = null;
	System.gc();
	System.gc();
	System.runFinalization();
	System.gc();
	out.println("** Count of Student instances should be 3, and is: " +
									ObjectCounter.getCounter(Student.class));

	TEST_STUDENTS = null;
	System.gc();
	System.gc();
	System.runFinalization();
	System.gc();

	out.println("** Count of Student instances should be 0, and is: " +
									ObjectCounter.getCounter(Student.class));
}

/**
 * Program entry point.
 *
 * <p>
 * Execute: </p>
 * <pre>java edu.frontrange.csc240.a11.example1.test.StudentTest</pre>
 *
 * @param args		unused
 */
public static void main(String[] args)
{
	new StudentTest().run();
}

/**
 * Select as year for use as data.
 *
 * @param range			0 if close to current year, otherwise a range 25 year in
 *						the past
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
}

