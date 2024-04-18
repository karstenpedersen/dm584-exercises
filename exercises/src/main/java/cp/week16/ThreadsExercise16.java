package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise16 {
	/*
	 * Adapt your program from ThreadsExercise15 to use CompletableFuture, as in
	 * Threads/cp/WalkCompletableFuture.
	 */

	public static record FileInfo(
			long size,
			long nlines,
			long nlinesThatStartWithL) {
	}

	public static void main(String[] args) {
		Map<Path, FileInfo> fileInfos = new ConcurrentHashMap<>();

		try {
			CompletableFuture<Void>[] futures = Files.walk(Paths.get("data")).filter(Files::isRegularFile)
					.map(filepath -> CompletableFuture.supplyAsync(() -> computeOccurrences(filepath))
							.thenAccept(fileOccurences -> fileOccurences.forEach((path, fileinfo) -> fileInfos.put(path, fileinfo))))
					.toList().toArray(new CompletableFuture[0]);
			CompletableFuture.allOf(futures).join();
		} catch (IOException e) {
			e.printStackTrace();
		}

		fileInfos.forEach((word, n) -> System.out.println(word + ": " + n));
	}

	private static Map<Path, FileInfo> computeOccurrences(Path textFile) {
		try {
			long size = Files.size(textFile);
			long nlines = Files.lines(textFile).count();
			long linesThatStartsWithL = Files.lines(textFile).filter(line -> line.startsWith("L")).toList().size();

			FileInfo fileinfo = new FileInfo(size, nlines, linesThatStartsWithL);
			Map<Path, FileInfo> map = new HashMap<>() {
				{
					put(textFile, fileinfo);
				}
			};
			return map;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}
}
