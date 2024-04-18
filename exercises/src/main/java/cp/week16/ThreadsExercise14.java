package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise14 {
	/*
	 * Modify Threads/cp/WalkExecutorFuture such that, instead of word occurrences,
	 * it computes a map of type Map< Path, FileInfo >, which maps the Path of each
	 * file found in "data"
	 * to an object of type FileInfo that contains:
	 * - the size of the file;
	 * - the number of lines contained in the file;
	 * - the number of lines starting with the uppercase letter "L".
	 */

	public static record FileInfo(
			long size,
			long nlines,
			long nlinesThatStartWithL) {
	}

	public static void main(String[] args) {
		// word -> number of times it appears over all files
		Map<Path, FileInfo> fileInfos = new HashMap<>();
		ExecutorService executor = Executors.newWorkStealingPool();

		try {
			List<Future<Map<Path, FileInfo>>> futures = Files.walk(Paths.get("data"))
					.filter(Files::isRegularFile)
					.map(filepath -> executor.submit(() -> computeOccurrences(filepath)))
					.collect(Collectors.toList());
			for (Future<Map<Path, FileInfo>> future : futures) {
				Map<Path, FileInfo> fileInfoMap = future.get();
				fileInfoMap.forEach((path, fileinfo) -> {
					fileInfos.put(path, fileinfo);
				});
			}
		} catch (InterruptedException | ExecutionException | IOException e) {
			e.printStackTrace();
		}

		try {
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
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
