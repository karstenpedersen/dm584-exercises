package cp.week14;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import cp.week12.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise10 {
	/*
	 * Modify ThreadsExercise9 to use Files.walk over the data directory in the
	 * Threads project, such
	 * that you recursively visit all files in that directory instead of using a
	 * fixed list of filenames.
	 */

	public static void main(String[] args) {
		// word -> number of times that it appears over all files
		Map<Character, Set<String>> occurrences = new ConcurrentHashMap<>();

		Path path = Paths.get("data/");
		System.out.println(path.toAbsolutePath());

		List<Path> filePaths = new ArrayList<>();
		try {
			filePaths = Files.walk(path).filter(Files::isRegularFile).toList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		CountDownLatch latch = new CountDownLatch(filePaths.size());

		filePaths.stream()
				.map(filePath -> new Thread(() -> {
					System.out.println(filePath.toAbsolutePath());
					computeOccurrences(filePath, occurrences);

					latch.countDown();
				}))
				.forEach(Thread::start);

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		occurrences.forEach((word, n) -> System.out.println(word + ": " + n));
	}

	private static void computeOccurrences(Path filePath, Map<Character, Set<String>> occurrences) {
		try {
			Files.lines(filePath)
					.flatMap(Words::extractWords)
					.forEach(s -> {
						Set<String> set = new HashSet<>();
						set.add(s);
						occurrences.merge(s.charAt(0), set, (current, next) -> {
							current.addAll(next);
							return current;
						});
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
