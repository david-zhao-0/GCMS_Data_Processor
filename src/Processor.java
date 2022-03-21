import java.io.*;
import java.util.*;

public class Processor { 

	public static void main(String[] args) {
		BufferedReader reader;
		try {
			
			// file input, defines headers
			
			reader = new BufferedReader(new FileReader("src/data.csv"));
			String line = reader.readLine();
			
			// gets main headers (metabolite names), collects to ArrayList mainHeaders
			
			String[] headers = line.split(",");
			List<String> mainHeaders = new ArrayList<String>();	
			for (int i = 1; i < headers.length; i = i + 6) {
				mainHeaders.add(headers[i]);
			}
			
			// Initiates BufferedWriter class, then dumps all items in mainHeaders ArrayList to processed data file, with one empty space
			// between each metabolite (", ,"). Can adjust to have multiple empty spaces, depending on output requirement
			
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/processed_data.csv"));		
			
			writer.write(",");
			
			for (int i = 0; i < mainHeaders.size(); i++) {
				writer.write(mainHeaders.get(i) + ", , ,");
			}
			
			writer.newLine();
			
			List<String> subHeaders = new ArrayList<String>();
			
			// gets subheaders (isotope numbers), collects to ArrayList subHeaders - take note of index when dealing with other sheets,
			// additionally, groups each subheader into groups of 6 (i = i + 6). 
			
			writer.write(",");
			
			for (int i = 1; i < headers.length; i = i + 6) {
				subHeaders.add(headers[i + 2]);
				subHeaders.add(headers[i + 4] + ",");
			}
			
			// writes subheaders into processed data file
			
			for (int i = 0; i < subHeaders.size(); i++) {
				writer.write(subHeaders.get(i) + ",");
			}
			
			writer.newLine();			
			
			// attempt at differentiating between different sample types LF/LB/HF/HB
//			List<String> lfSamples = new ArrayList<String>();
//			List<String> lbSamples = new ArrayList<String>();
//			List<String> hfSamples = new ArrayList<String>();
//			List<String> hbSamples = new ArrayList<String>();
			
			// gets and writes first line of data to processed data file, as well as sample name
			// first set of data is LF, followed by LB, then HF, then HB
			
			writer.write(headers[0].substring(headers[0].indexOf("_") + 1) + ",");
			
			for (int i = 1; i < headers.length; i = i + 6) {
				writer.write(headers[i + 3] + ",");
				writer.write(headers[i + 5] + ", ,");
			}
			writer.newLine();
			
			// reads row after first row
			
			line = reader.readLine();	
			
			// processes all the rest of the data
			
			while (line != null) {
				String[] data = line.split(",");
//				if (data[0].contains("LF")) {
//					lfSamples.add(line); 
//				} else if (data[0].contains("LB")) {
//					lbSamples.add(line);
//				} else if (data[0].contains("HF")) {
//					hfSamples.add(line);
//				} else {
//					hbSamples.add(line);
//				}
				
				writer.write(data[0].substring(data[0].indexOf("_") + 1) + ",");
				
				for (int i = 1; i < data.length - 3; i = i + 6) {
					writer.write(data[i + 3] + ",");
					writer.write(data[i + 5] + ", ,");
				}
				writer.newLine();
				line = reader.readLine();	
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
