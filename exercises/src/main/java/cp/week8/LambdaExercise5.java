package cp.week8;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cp.week8.LambdaExercise2.BoxFunction;

/**
 * 
 * @author Fabrizio Montesi
 */
public class LambdaExercise5 {
	/*
	 * - Write a static method Box::applyToAll that, given
	 * a list of Box(es) with the same type and a BoxFunction with compatible type,
	 * applies the BoxFunction to all the boxes and returns a list
	 * that contains the result of each BoxFunction invocation.
	 */

	public static class Box<T> extends LambdaExercise2.Box<T> {
		public Box(T content) {
			super(content);
		}

		public static <T, O> List<O> applyToAll(List<Box<T>> boxes, BoxFunction<T, O> boxFunction) {
			return boxes.stream().map(box -> box.apply(boxFunction)).toList();
		}
	}

	public static void main(String[] args) {
		BoxFunction<String, Integer> boxFunction = (content) -> {
			return content.length();
		};
		List<Box<String>> boxes = Arrays.asList(
				new Box<>("Hello"),
				new Box<>("Giant"),
				new Box<>("World!"));
		List<Integer> result = Box.applyToAll(boxes, boxFunction);
		System.out.println(result);
	}
}
