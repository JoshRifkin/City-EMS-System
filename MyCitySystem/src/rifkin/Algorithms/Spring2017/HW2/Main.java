package rifkin.Algorithms.Spring2017.HW2;

import java.io.File;
import java.io.FileNotFoundException;

class Main {
	
	private static File file;

	public static void main(String[] args) throws FileNotFoundException {
		if(args.length == 0) {
			//Try running the program with the sample input file.
			file = new File("SampleInput.txt");
			
		}
		
		else if(args[0].equals(null)){
			exit();
		}
		else {
			file = new File(args[0]);
		}
		
		if(!file.isFile()) exit();
		
		Parser parser = new Parser(file);
		parser.parse();
	}

	private static void exit() {
		System.out.println("You must input a valid file name!");
		System.out.println("Exiting...");
		System.exit(0);
	}
	
}
