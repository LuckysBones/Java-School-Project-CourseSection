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
 * (c) 2010-2020 Dr. Bruce K. Haddon
 */

package software.haddon.util;

import java.lang.ref.Cleaner;
import java.util.HashMap;
import java.util.Map;

import static java.lang.ref.Cleaner.create;

/**
 * An instance of this class may be introduced as the value of a field in any class
 * in order to be able to count the number of instances of that class that exist at
 * any given time.
 *
 * @author		Dr. Bruce K. Haddon
 * @version		2.0, 2019-11-26
 */
public class ObjectCounter
{
/**
 * Create a cleaner (and its thread) for decrementing the counters.
 */
private static final Cleaner CLEANER = create();

/**
 * A map to keep the association between a given class and a count of the instances
 * of that class.
 */
private static final Map<Class<?>, Integer> COUNTERS = new HashMap<>();

/**
 * Constructor: this ObjectCounter is used by creating an instance of this counter
 * in each instance of the class whose instances are to be counted, and by passing a
 * reference to the instance. So the argument value should be {@code this}.
 *
 * @param object		the object of whose instances are to be counted
 */
public ObjectCounter(Object object)
{
	if( object == null )
		throw new IllegalArgumentException("Argument is null");

	/* Get the class of the object. */
	Class<?> clazz = object.getClass();
	/* Protect the COUNTERS Map. */
	synchronized( COUNTERS )
	{
		/* Initialize the entry in the map if needed. */
		if( !COUNTERS.containsKey(clazz) ) COUNTERS.put(clazz, 0);
		/* Increment the count of instances of the given class. */
		COUNTERS.put(clazz, COUNTERS.get(clazz) + 1);
	}

	/* Register the object that is to have its class count reduced when the
	   object becomes Phantom-reachable. */
	CLEANER.register(object, new Decrementer(clazz));
}

/**
 * Get the count of instances of the requested class. The count returned will be
 * the count of the known instances of that class (0 if the class is not known).
 *
 * @param clazz			the desired class
 * @return				the count of known instances of that class
 */
public static int getCounter(Class<?> clazz)
{
	int result = 0;
	synchronized( COUNTERS )
	{
		if( COUNTERS.containsKey(clazz) )
			result = COUNTERS.get(clazz);
	}
	return result;
}

/**
 * The class to be registered, to do the counter decrementation.
 */
private static class Decrementer implements Runnable
{
/**
 * Remember the class whose count is to be decremented.
 */
private final Class<?> clazz;

/**
 * Constructor:
 *
 * @param clazz			the class whose count is to be decremented
 */
Decrementer(Class<?> clazz)
{
	/* Remember the class whose count is to be decremented. */
	this.clazz = clazz;
}

/**
 * The method that does the decrementation.
 *
 * @throws NullPointerException if the class object is not present in the map. If
 *						this exception is thrown, the Cleaner will ignore it, but no
 *						further action will be taken.
 */
@Override
public void run() throws NullPointerException
{
	/* Protect the COUNTERS Map. */
	synchronized( COUNTERS )
	{
		COUNTERS.put(this.clazz, COUNTERS.get(clazz) - 1);
	}
}
}
}
