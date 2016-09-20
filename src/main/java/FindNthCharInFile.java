import java.io.*;

public class FindNthCharInFile {
	public static String getEveryNthChar(String pathToFile, int n) {
		String fileContents = getFileContents(pathToFile);

		StringBuilder result = new StringBuilder();
		for (int i = 0; i < fileContents.length(); i+=n) {
			result.append(fileContents.charAt(i));
		}

		return result.toString();
	}

	private static String getFileContents(String pathToFile) {
		try(BufferedReader in = new BufferedReader(new FileReader(pathToFile))) {
			StringBuilder sb = new StringBuilder();
			String lineIn = in.readLine();
			while(lineIn != null) {
				sb.append(lineIn).append('\n');
				lineIn = in.readLine();
			}
			return sb.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
