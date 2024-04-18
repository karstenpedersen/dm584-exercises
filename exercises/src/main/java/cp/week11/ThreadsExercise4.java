package cp.week11;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise4 {
	/*
	 * - Write the example from Listing 4.2 in the book.
	 * - Add a method that returns a reference to the internal field mySet.
	 * - Use the new method from concurrent threads to create unsafe access to
	 * mySet.
	 */

	public static class Person {
		// Person
	}

	// @ThreadSafe
	public static class PersonSet {
		// @GuardedBy("this")
		private final Set<Person> mySet = new HashSet<Person>();

		public synchronized void addPerson(Person p) {
			mySet.add(p);
		}

		public synchronized boolean containsPerson(Person p) {
			return mySet.contains(p);
		}

		public Set<Person> mySet() {
			return this.mySet;
		}
	}

	public static class MyThread extends Thread {
		@Override
		public void run() {
			
		}
	}

	public static void main(String[] args) {
		var set = new PersonSet();
		set.addPerson(new Person());
		set.addPerson(new Person());
		set.addPerson(new Person());
	}
}
