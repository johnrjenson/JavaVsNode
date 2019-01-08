import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FindNthCharInFile {
	public static String getEveryNthChar(String pathToFile, int n) throws IOException {
		String fileContents = getFileContents(pathToFile);

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < fileContents.length(); i+=n) {
			result.append(fileContents.charAt(i));
		}

		return result.toString();
	}

	private static String getFileContents(String pathToFile) throws IOException {
		byte[] bytes = Files.readAllBytes(Paths.get(pathToFile));

		return new String(bytes, "US-ASCII");
	}
}
