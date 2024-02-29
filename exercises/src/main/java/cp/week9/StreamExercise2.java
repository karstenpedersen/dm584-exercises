package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

public class StreamExercise2 {
	/*
	 * - Create a stream of lines for the file created in StreamExercise1.
	 * - Use Stream::filter and Stream::collector (the one with three parameters)
	 * to create an ArrayList of all lines that start with a "C".
	 * - Suggestion: look at
	 * https://docs.oracle.com/javase/8/docs/api/java/util/stream/Stream.html#
	 * collect-java.util.function.Supplier-java.util.function.BiConsumer-java.util.
	 * function.BiConsumer-
	 */

	public static void main(String[] args) {
		final Path path = Paths.get("src/main/java/cp/week9/test2.txt");
		try (Stream<String> lines = Files.lines(path)) {
			var filtered = lines
					.filter(line -> line.startsWith("C"))
					.collect(ArrayList<String>::new, ArrayList::add, ArrayList::addAll); // Use toList, but this was
																							// just to show
			System.out.println(filtered);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
