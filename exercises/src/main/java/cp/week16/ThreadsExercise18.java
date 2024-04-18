package cp.week16;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author Fabrizio Montesi <fmontesi@imada.sdu.dk>
 */
public class ThreadsExercise18 {
	/*
	 * Adapt your program from ThreadsExercise16 to stop as soon as a task that has
	 * processed a file with more than 10 lines is completed.
	 * 
	 * Hint: use CompletableFuture.anyOf
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
					
			CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(futures);

			FileInfo result = (FileInfo)anyOfFuture.get();

			if (result.nlines > 10) {
				
			}

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
