package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamExercise5 {
	/*
	 * ! (Exercises marked with ! are more difficult.)
	 * 
	 * - Create a stream of lines for the file created in StreamExercise1.
	 * - Use Stream::map to map each line to a HashMap<String, Integer> that
	 * stores how many times each character appears in the line.
	 * For example, for the line "abbc" you would produce a map with entries:
	 * a -> 1
	 * b -> 2
	 * c -> 1
	 * - Use Stream::reduce(T identity, BinaryOperator<T> accumulator)
	 * to produce a single HashMap<String, Integer> that stores
	 * the results for the entire file.
	 */

	public static void main(String[] args) {
		final Path path = Paths.get("src/main/java/cp/week9/test2.txt");
		try (Stream<String> lines = Files.lines(path)) {
			var counts = lines
					.flatMap(line -> Stream.of(line.split("")))
					.collect(Collectors.groupingBy(c -> c, Collectors.counting()));

			counts.forEach((key, value) -> System.out.println("'" + key + "' -> " +
					value));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
