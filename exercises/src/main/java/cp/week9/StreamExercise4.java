package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StreamExercise4 {
	/*
	 * - Create a stream of lines for the file created in StreamExercise1.
	 * - Use Stream::mapToInt and IntStream::sum to count how many times
	 * the letter "C" occurs in the entire file.
	 */

	public static void main(String[] args) {
		final Path path = Paths.get("src/main/java/cp/week9/test2.txt");
		try (Stream<String> lines = Files.lines(path)) {
			// long num = lines.mapToLong(line -> line.chars().filter(c -> c ==
			// 'C').count()).sum();
			// int num2 = lines.mapToInt(line -> line.chars().filter(c -> c ==
			// 'C').toArray().length).sum();
			long num = lines.flatMapToInt(String::chars).filter(c -> c == 'C').count();
			// System.out.println(num2);
			System.out.println(num);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
