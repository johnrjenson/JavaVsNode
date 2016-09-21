import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

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
		try(RandomAccessFile aFile = new RandomAccessFile(pathToFile, "r"); FileChannel inChannel = aFile.getChannel()) {
			long fileSize = inChannel.size();
			ByteBuffer buffer = ByteBuffer.allocate((int) fileSize);
			inChannel.read(buffer);
			buffer.flip();
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < fileSize; i++) {
				sb.append((char) buffer.get());
			}
			return sb.toString();
		} catch (IOException e) {
			throw e;
		}
	}
}
