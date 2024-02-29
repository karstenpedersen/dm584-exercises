package cp.week8;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Fabrizio Montesi
 */
public class LambdaExercise3 {
	/*
	 * NOTE: When I write Class::methodName, I don't mean to use a method reference
	 * (lambda expression), I'm simply
	 * talking about a particular method.
	 */

	/*
	 * - Create a Box that contains an ArrayList<String> with some elements of your
	 * preference.
	 * - Now compute a sorted version of your list by invoking Box::apply, passing a
	 * lambda expression that uses List::sort.
	 */

	public static void main(String[] args) {
		ArrayList<String> arr = new ArrayList<>(List.of("World!", "Hello"));

		var box = new LambdaExercise2.Box<>(arr);
		box.apply((content) -> {
			content.sort(String::compareTo);
			return content;
		});
		System.out.println(box.content());
	}
}
