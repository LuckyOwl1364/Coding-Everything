import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import controller.PersonFactory;
import model.Person;

public class Driver {
	public static void main(String[] args) {
		String fileName = "C:/Users/Caitlin/Desktop/Coding Everything/Java/Random Effect Picker/Random Effects v2.txt"; // Name of file used by the program

		String[] data = fillData(fileName);	// instantiate the array of strings (effects) 
		
		new GUIController(data);	// instantiate the TopGUIController
	}	// end main

	
	
	private static String[] fillData(String fileName) {
		try(BufferedReader br = new BufferedReader(new FileReader(fileName))){	// try-catch in case file not found
			int size = Integer.parseInt(br.readLine());	// attempt to parse the size of the array from the file's first line
			String[] data = new String[size];
			
			for (int i = 0; i < size; i++) {
				data[i] = br.readLine();
			}	// end for
			
			return data;
		}	// end try
		catch (FileNotFoundException e){
			System.err.println("Couldn't find file @: " + fileName);
		}	// end catch
		catch (IOException e){
			System.err.println("Something IO broke");
		}	// end try-catch
		return null;
	}	// end fillData
}	// end Driver
