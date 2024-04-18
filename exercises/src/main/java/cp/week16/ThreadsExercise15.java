package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise15 {
	/*
	 * Adapt your program from ThreadsExercise14 to use an
	 * ExecutorCompletionService, as in Threads/cp/WalkCompletionService.
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
		ExecutorCompletionService<Map<Path, FileInfo>> completionService = new ExecutorCompletionService<>(executor);

		try {
			long pendingTasks = Files.walk(Paths.get("data"))
					.filter(Files::isRegularFile)
					.map(filepath -> completionService.submit(() -> computeOccurrences(filepath)))
					.count();

			while (pendingTasks > 0) {
				Map<Path, FileInfo> fileInfoMap = completionService.take().get();
				fileInfoMap.forEach((path, fileinfo) -> {
					fileInfos.put(path, fileinfo);
				});
				pendingTasks--;
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
