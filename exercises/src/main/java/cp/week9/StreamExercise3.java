package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StreamExercise3 {
	/*
	 * - Create a stream of lines for the file created in StreamExercise1.
	 * - Use Stream::filter and Stream::count to count how many lines
	 * contain the letter "L".
	 * 
	 */

	public static void main(String[] args) {
		final Path path = Paths.get("src/main/java/cp/week9/test2.txt");
		try (Stream<String> lines = Files.lines(path)) {
			long lineCount = lines.filter(line -> line.contains("L")).count();
			System.out.println(lineCount);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
