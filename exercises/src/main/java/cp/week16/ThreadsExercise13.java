package cp.week16;

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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import cp.week12.Words;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise13 {
	/*
	 * Modify ThreadsExercise9 to use executors.
	 * Try different kinds of executor (cached thread pool or fixed thread pool) and
	 * different fixed pool sizes.
	 * Which executor runs faster?
	 * Can you explain why?
	 */
	public static void main(String[] args) {
		// word -> number of times that it appears over all files
		Map<Character, Set<String>> occurrences = new ConcurrentHashMap<>();

		Path path = Paths.get("data/");
		List<Path> filePaths = new ArrayList<>();
		try {
			filePaths = Files.walk(path).filter(Files::isRegularFile).toList();
		} catch (IOException e) {
			e.printStackTrace();
		}

		ExecutorService executor = Executors.newWorkStealingPool();

		// CountDownLatch latch = new CountDownLatch(filePaths.size());
		// filePaths.stream()
		// 		.map(filePath -> new Thread(() -> {
		// 			System.out.println(filePath.toAbsolutePath());
		// 			computeOccurrences(filePath, occurrences);
		// 			latch.countDown();
		// 		}))
		// 		.forEach(Thread::start);
		// try {
		// 	latch.await();
		// } catch (InterruptedException e) {
		// 	e.printStackTrace();
		// }

		filePaths.forEach(filepath -> {
			executor.submit(() -> {
				computeOccurrences(filepath, occurrences);
			});
		});

		try {
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch(InterruptedException e) {
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
