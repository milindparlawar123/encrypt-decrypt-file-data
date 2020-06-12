import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Random;

public class Mono {
	private static Random ran = new Random(1234567890);

	public static void main(String args[]) {
		if (args.length != 3) {
			System.out.println("provide all 3 arguments");
			System.exit(0);
		}
		FileProcessor fileProcInput = new FileProcessor();

		// code start: below code is of random number generation and to generate unique
		// cypher characters for all 26 alphabets to use for decryption and encryption

		char keys[] = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
				't', 'u', 'v', 'w', 'x', 'y', 'z' };
		HashMap<Character, Character> keyValues = new HashMap<Character, Character>();

		char values[] = new char[26];

		for (int i = 0; keyValues.size() != 26;) {
			int temp = 97 + getRandomNumber(0, 25);

			int key = (temp < 123 ? temp : temp - 26);
			if (keyValues.get((char) key) == null) {
				keyValues.put((char) key, keys[i]);
				values[i] = (char) (temp < 123 ? temp : temp - 26);
				i++;
			}
		}

		// :code end
		
		//start: print mappings
		for(int i=0; i<keys.length; i++) {
			System.out.print(keys[i]+"-"+values[i]+", ");
			}
		System.out.println("");
		//:end

		try {
			Integer.parseInt(args[2]);
		} catch (Exception e) {
			System.out.println("incorrect input, please provide 1 for encryption and 0 for decryption");
			System.exit(0);
		}
		int input = Integer.parseInt(args[2]);
		// for encryption
		if (input == 1) {
			String data = readInputFile(args[0]);
			String cypher = "";
			for (char a : data.toCharArray()) {
				cypher += values[a - 97];
			}

			fileProcInput.writeToFile(cypher, args[1]);

		} else if (input == 0) { // for decryption
			String deCypher = "";
			String inputCypher = readInputFile(args[0]);

			for (char a : inputCypher.toCharArray()) {
				deCypher += keyValues.get(a);
			}
			fileProcInput.writeToFile(deCypher, args[1]);
		} else {
			System.out.println("incorrect input, please provide 1 for encryption and 0 for decryption");
			System.exit(0);
		}
	}

	private static String readInputFile(String inputFileName) {
		FileProcessor fileProcInput = null;
		String numberStr = null;

		try {
			fileProcInput = new FileProcessor(inputFileName);
			numberStr = fileProcInput.poll();
			fileProcInput.close();
		} catch (InvalidPathException | SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return numberStr;
	}

	private static int getRandomNumber(int min, int max) {

		int intValue = ran.ints(min, (max + 1)).findFirst().getAsInt();
		return intValue;
	}
}

//below class is for reading from the file and writing into the file
class FileProcessor {
	private BufferedReader reader;
	private String line;
	private File file;
	private BufferedWriter fileWriter;

	public FileProcessor() {

	}

	public FileProcessor(String inputFilePath)
			throws InvalidPathException, SecurityException, FileNotFoundException, IOException {

		if (!Files.exists(Paths.get(inputFilePath))) {
			throw new FileNotFoundException("fild not found");
		}

		reader = new BufferedReader(new FileReader(new File(inputFilePath)));
		line = reader.readLine();
		if (line == null) {
			System.out.println(inputFilePath + "empty file");
			System.exit(0);
		}
	}

	public String poll() throws IOException {
		if (null == line)
			return null;

		line = line.trim();
		return line;
	}

	public void writeToFile(String str, String inputFileName) {
		file = new File(inputFileName);
		try {
			this.fileWriter = new BufferedWriter(new FileWriter(file));
			this.fileWriter.write(str);
			this.fileWriter.close();
		} catch (IOException e) {
			System.err.println("error in writing file");
			e.printStackTrace();
			System.exit(0);
		} finally {
		}

	}

	public void close() throws IOException {
		try {
			reader.close();
		} catch (IOException e) {
			throw new IOException("error while closing file", e);
		} finally {

		}
	}
}
