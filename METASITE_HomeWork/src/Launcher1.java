import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Launcher1 {

	private String[] inputFile = null;
	private static String tempFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator
			+ "WriteTXT" + File.separator + "ALL_Words").toString();

	private BufferedReader reader = null;
	private BufferedWriter writer1 = null;
	private int wordsCount = 0;

	public int getwordsCount() {
		return wordsCount;
	}

	Launcher1(String[] inputFile) {
		this.inputFile = inputFile;
	}

	Launcher1() {

	}

	public synchronized void readingFile() {

		String[] words = null;
		List<String> someList = new ArrayList<String>();

		try {

			for (int i = 0; i < inputFile.length; i++) {
				reader = new BufferedReader(new FileReader(inputFile[i]));
				String currentLine;

				while ((currentLine = reader.readLine()) != null) {

					words = currentLine.replaceAll("[^a-zA-Z\\s]", "").split("\\s+");

					if (currentLine.equals(""))
						continue;

					for (int k = 0; k < words.length; k++) {
						wordsCount++;
						words[k] = words[k].toUpperCase();
						someList.add(words[k]);
					}
				}
			}

			Collections.sort(someList);
			findingDublicates(someList);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return;
	}

	public synchronized void writingFile() {

		String[] words = null;
		String line;

		try {
			reader = new BufferedReader(new FileReader(tempFile));
			while ((line = reader.readLine()) != null) {
				words = line.split("\n");
				if (line.equals(""))
					continue;

				for (int i = 0; i < words.length; i++) {
					if (seperateWords(words[i]) == 1) {
						writing(words[i], new File(System.getProperty("user.dir") + File.separator + "src"
								+ File.separator + "WriteTXT" + File.separator + "WordsA_G").toString());
					}
					if (seperateWords(words[i]) == 2) {
						writing(words[i], new File(System.getProperty("user.dir") + File.separator + "src"
								+ File.separator + "WriteTXT" + File.separator + "WordsH_N").toString());
					}
					if (seperateWords(words[i]) == 3) {
						writing(words[i], new File(System.getProperty("user.dir") + File.separator + "src"
								+ File.separator + "WriteTXT" + File.separator + "WordsO_U").toString());
					}
					if (seperateWords(words[i]) == 4) {
						writing(words[i], new File(System.getProperty("user.dir") + File.separator + "src"
								+ File.separator + "WriteTXT" + File.separator + "WordsV_Z").toString());
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				reader.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return;
	}

	public void writing(String word, String file) throws IOException {
		writer1 = new BufferedWriter(new FileWriter(file, true));

		try {
			writer1.write(word);
			writer1.newLine();
			int k = 0;
			while (k <= 75) {
				writer1.write("_");
				k++;
			}
			writer1.newLine();
			writer1.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer1 != null)
					writer1.close();

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public static void findingDublicates(List<String> list) {

		Set<String> uniqueWords = new LinkedHashSet<String>(list);
		int num = 0;
		BufferedWriter writer1 = null;

		try {

			for (String words : uniqueWords) {

				String format = "%-25s %-10s %-10s %-10s %-10s";
				num = Collections.frequency(list, words);
				writer1 = new BufferedWriter(new FileWriter(tempFile, true));
				writer1.write(String.format(format, words, "|", Integer.toString(num), "|", " times repeated"));
				writer1.newLine();
				int k = 0;
				while (k <= 75) {
					writer1.write("_");
					k++;
				}
				writer1.newLine();
				writer1.flush();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (writer1 != null)
					writer1.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private static int seperateWords(String words) {
		char c = words.charAt(0);
		c = Character.toLowerCase(c);

		if ((c >= 'a' && c <= 'g') || (c >= 'A' && c <= 'G')) {
			return 1;
		} else if ((c >= 'h' && c <= 'n') || (c >= 'H' && c <= 'N')) {
			return 2;
		} else if ((c >= 'o' && c <= 'u') || (c >= 'O' && c <= 'U')) {
			return 3;
		} else if ((c >= 'v' && c <= 'z') || (c >= 'V' && c <= 'Z')) {
			return 4;
		} else
			return 0;
	}

}
