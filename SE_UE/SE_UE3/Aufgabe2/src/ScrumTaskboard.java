import java.io.*;
import java.util.*;

public class ScrumTaskboard {

	public static void main (String[] args) throws FileNotFoundException {
		String file = args[0];
		System.setOut(new PrintStream(new FileOutputStream("ScrumTaskboard-output.txt")));
		Scanner scanner = new Scanner(file);
		Parser parser = new Parser(scanner);
		parser.Parse();
		System.out.println(parser.errors.count + " errors detected");

	}
}