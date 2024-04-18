package cp.week12;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise8
{
	/*
	- As ThreadExercise7, but now use a global counter among all threads instead.
	- Reason about the pros and cons of the two concurrency strategies
	  (write them down).
	*/

	private static void computeOccurrences(String filename, Map<String, Integer> occurrences, AtomicInteger count) {
		try {
			Files.lines(Paths.get(filename))
					.flatMap(Words::extractWords)
					.forEach(s -> {
						synchronized (occurrences) {
							occurrences.merge(s.toLowerCase(), 1, Integer::sum);
						}

						if (s.startsWith("L")) {
							count.incrementAndGet();
						}
					});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		// word -> number of times that it appears over all files
		Map<String, Integer> occurrences = new HashMap<>();

		List<String> filenames = List.of(
				"text1.txt",
				"text2.txt",
				"text3.txt",
				"text4.txt",
				"text5.txt",
				"text6.txt",
				"text7.txt",
				"text8.txt",
				"text9.txt",
				"text10.txt");

		CountDownLatch latch = new CountDownLatch(filenames.size());

		AtomicInteger total = new AtomicInteger(0);

		filenames.stream()
				.map(filename -> new Thread(() -> {
					computeOccurrences(filename, occurrences, total);
					latch.countDown();
				}))
				.forEach(Thread::start);

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
		System.out.println("#l's part 2: " + total);
	}

}
