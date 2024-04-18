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
public class ThreadsExercise7 {
	/*
	 * - Modify Threads/cp/threads/SynchronizedMap such that:
	 * Each threads also counts the total number of times that any word
	 * starting with the letter "L" appears.
	 * Each thread should have its own total (no shared global counter).
	 * The sum of all totals is printed at the end.
	 */

	private static int computeOccurrences(String filename, Map<String, Integer> occurrences) {
		try {
			// final int[] total = {0};
			AtomicInteger total = new AtomicInteger(0);
			Files.lines(Paths.get(filename))
					.flatMap(Words::extractWords)
					.forEach(s -> {
						synchronized (occurrences) {
							occurrences.merge(s.toLowerCase(), 1, Integer::sum);
						}

						if (s.startsWith("L")) {
							// total[0]++;
							total.incrementAndGet();
						}
					});
			// return total[0];
			return total.get();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return 0;
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
					int c = computeOccurrences(filename, occurrences);
					total.addAndGet(c);	
					latch.countDown();
				}))
				.forEach(Thread::start);

		try {
			latch.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		occurrences.forEach( (word, n) -> System.out.println( word + ": " + n ) );
		System.out.println("#l's: " + total);
	}

}
