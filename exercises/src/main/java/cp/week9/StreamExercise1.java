package cp.week9;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class StreamExercise1 {
	/*
	 * - Create a file with many (>100) lines of text.
	 * For example, you can use this website: loremipsum.io
	 * - Use Files.lines to get a stream of the lines contained within the file.
	 * - Use Stream::filter and Stream::forEach to print on screen each line that
	 * ends with a dot.
	 */

	public static void main(String[] args) {
		final Path path = Paths.get("src/main/java/cp/week9/test2.txt");
		try (Stream<String> lines = Files.lines(path)) {
			lines.filter(line -> line.endsWith(".")).forEach(System.out::println);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
