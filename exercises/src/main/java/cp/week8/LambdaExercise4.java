package cp.week8;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import cp.week8.LambdaExercise2.BoxFunction;

/**
 * 
 * @author Fabrizio Montesi
 */
public class LambdaExercise4 {
	/*
	 * - Create a list of type ArrayList<String> with some elements of your
	 * preference.
	 * - Create a Box that contains the list.
	 * - Now compute the sum of the lengths of all strings in the list inside of the
	 * box,
	 * by invoking Box::apply with a lambda expression.
	 */

	public static void main(String[] args) {
		ArrayList<String> arr = new ArrayList<>();
		arr.add("Hello");
		arr.add("World!");

		LambdaExercise2.Box<ArrayList<String>> box = new LambdaExercise2.Box<>(arr);

		int result = box.apply((content) -> content.stream().mapToInt(String::length).sum());
		System.out.println(result);
	}
}
