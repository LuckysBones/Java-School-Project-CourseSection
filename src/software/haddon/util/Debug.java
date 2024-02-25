/*-
 * This software is the work of Dr. Bruce K. Haddon (hereafter, "the Owner"), and
 * all rights and intellectual property remain the property of that person.
 *
 * Rights to view or use this software as source code, or for execution, are only
 * granted via one or more licences at the discretion the Owner. In any event, the
 * grant to the "Licensee" shall be for a non-exclusive, non-transferable license to
 * view or use this software version (hereafter, "the Software") according to the
 * terms of the licence and contract executed between the Licensee and the Owner.
 * Licensee agrees that the copyright notice and this statement will appear on all
 * copies of the Software, packaging, and documentation or portions thereof made
 * under the terms of the license and contract.
 *
 * Please refer to the your license and contract for further important copyright and
 * licensing information. If you are reading this, and you do not have a signed,
 * current license or confidentiality agreement executed with the Owner, it is
 * because someone has violated the terms of an agreement, an act to which you may
 * be held to be a party.
 *
 * The Owner makes no representations or warranties about the suitability of the
 * Software, either express or implied, including but not limited to the implied
 * warranties of merchantability, fitness for particular purposes, or
 * non-infringement, other than those contained in the Licensee's license and
 * contract documents. The Owner shall not be liable for any damages suffered by
 * the Licensee as a result of using, modifying or distributing this software or
 * its derivatives.
 *
 * Irrespective of the conditions above, permission is granted to the students and
 * academic and instructional staff of the Front Range Community College, Colorado,
 * to read and use this class, but not to make amendments or to distribute the
 * the source or compiled class to other parties.
 *
 * (c) 2010-2021 Dr. Bruce K. Haddon
 */

package software.haddon.util;

import java.io.PrintStream;

/**
 * This class has some of the functionality of PrintStream.
 * It prints to a given OutputStream, making the output conditional upon the state
 * of the variable "debug". By setting this false, all debug output can be suppressed.
 * <p>
 * These methods are used by writing the form: </p>
 * <blockquote><pre><code>
 * Debug.printf( ... )
 * </code></pre></blockquote>
 * <p>
 * or similarly, <code>Debug.println.</code></p>
 * <p>
 * The debugging output is intended to be a line at a time. If {@code printf} is
 * used, and the format does not end with a "%n" code, a line.separator will be
 * added. </p>
 * <p>
 * The debug state defaults to {@code true}, and the output stream defaults to
 * {@code System.out}.
 *
 * @author		Dr. Bruce K. Haddon
 * @version		2.0, 2020-12-03
 */
public class Debug
{
/**
 * Debug indicator.
 */
private static final String INDICATOR = "*db ";

/**
 * The state of the "debug" flag.
 */
private static boolean debug = true;

/**
 * The PrintStream to which to send the output.
 */
private static PrintStream outputStream;

/**
 * Constructor - private to prevent instantiation
 */
private Debug(){}

/**
 * Get the current state of the debug flag.
 *
 * @return	the current state
 */
public static boolean debug()
{
	return debug;
}

/**
 * Set the current state of the debug flag.
 *
 * @param debug			the desired debug state
 * @return				the previous value (so it may be restored if needed)
 */
public static boolean debug(boolean debug)
{
	boolean wasBefore = Debug.debug;
	Debug.debug = debug;
	return wasBefore;
}

/**
 * Delegate to print a formatted output.
 *
 * @param format		the formatter String
 * @param args			the arguments to the formatter String
 * @return				the PrintStream used
 */
public static PrintStream printf(String format, Object... args)
{
	if( debug )
	{
		output().print(INDICATOR);
		output().printf(format, args);
		if( !format.trim().endsWith("%n") ) output().println();
		output().flush();
	}
	return output();
}

/**
 * Delegate to print an end-of-line.
 */
public static void println()
{
	if( debug )
	{
		output().println(INDICATOR);
		output().flush();
	}
}

/**
 * Delegate to print a string followed by an end-of-line.
 *
 * @param string		the string to print on this line
 */
public static void println(String string)
{
	if( debug )
	{
		output().println(INDICATOR + string);
		output().flush();
	}
}

/**
 * Change the PrintStream.
 *
 * @param outputStream	the desired PrintStream to use
 * @return				the previous value (so it may be restored if needed)
 */
public static PrintStream setPrintStream(PrintStream outputStream)
{
	PrintStream wasBefore = Debug.outputStream;
	Debug.outputStream = outputStream;
	return wasBefore;
}

/**
 * This method is here in case the value of System.out is changed during the use of
 * the debug methods. This method ensures the System.out is evaluated whenever it is
 * used.
 *
 * @return				the current value of the stream to use for debug output
 */
private static PrintStream output()
{
	return outputStream == null ? System.out : outputStream;
}
}
