import java.io.*;
import java.util.*;

public class Processor {

	public static void main(String[] args) {
		preProcess();
		writeSamplesToFile(sortSamples());
	}

	public static void preProcess() {
		BufferedReader reader;
		try {

			// file input, defines headers

			reader = new BufferedReader(new FileReader("src/data.csv"));
			String line = reader.readLine();

			// gets main headers (metabolite names), collects to ArrayList mainHeaders

			String[] lineDelim = line.split(",");
			List<String> mainHeaders = new ArrayList<String>();
			for (int i = 1; i < lineDelim.length; i = i + 6) {
				mainHeaders.add(lineDelim[i]);
			}

			// Initiates BufferedWriter class, then dumps all items in mainHeaders ArrayList
			// to processed data file, with one empty space
			// between each metabolite (",,,"). Can adjust to have multiple empty spaces,
			// depending on output requirement

			BufferedWriter writer = new BufferedWriter(new FileWriter("src/processed_data.csv"));

			writer.write(",");

			for (int i = 0; i < mainHeaders.size(); i++) {
				writer.write(mainHeaders.get(i) + ",,,");
			}

			writer.newLine();

			List<String> subHeaders = new ArrayList<String>();

			// gets subheaders (isotope numbers), collects to ArrayList subHeaders - take
			// note of index when dealing with other sheets,
			// additionally, groups each subheader into groups of 6 (i = i + 6).

			writer.write(",");

			for (int i = 1; i < lineDelim.length; i = i + 6) {
				subHeaders.add(lineDelim[i + 2]);
				subHeaders.add(lineDelim[i + 4] + ",");
			}

			// writes subheaders into processed data file

			for (int i = 0; i < subHeaders.size(); i++) {
				writer.write(subHeaders.get(i) + ",");
			}

			writer.newLine();

			writer.write(lineDelim[0].substring(lineDelim[0].indexOf("_") + 1) + ",");

			for (int i = 1; i < lineDelim.length; i = i + 6) {
				writer.write(lineDelim[i + 3] + ",");
				writer.write(lineDelim[i + 5] + ",,");
			}
			writer.newLine();

			// reads row after first row

			line = reader.readLine();

			// processes all the rest of the data

			while (line != null) {
				String[] nextLineDelim = line.split(",");

				// removes date off of sample name to satisfy getSortOrder() requirements
				writer.write(nextLineDelim[0].substring(nextLineDelim[0].indexOf("_") + 1) + ",");

				for (int i = 1; i < nextLineDelim.length - 3; i = i + 6) {
					writer.write(nextLineDelim[i + 3] + ",");
					writer.write(nextLineDelim[i + 5] + ",,");
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

	public static ArrayList<Sample> sortSamples() {
		BufferedReader reader;
		ArrayList<Sample> samples = new ArrayList<Sample>();
		try {

			// reads first line and removes empty elements
			reader = new BufferedReader(new FileReader("src/processed_data.csv"));
			String line = reader.readLine();
			ArrayList<String> metaboliteNames = new ArrayList<String>(Arrays.asList(line.split(",")));
			metaboliteNames.removeIf(n -> (n.isBlank()));

			// reads second line and removes empty elements
			line = reader.readLine();
			ArrayList<String> peaks = new ArrayList<String>(Arrays.asList(line.split(",")));
			peaks.removeIf(n -> (n.isBlank()));

			// creates an arraylist of metabolites, then assigns each metabolite a name and
			// 2 peaks
			ArrayList<Metabolite> metaboliteList = new ArrayList<Metabolite>();
			for (int i = 0; i < metaboliteNames.size(); i++) {
				Metabolite m = new Metabolite(metaboliteNames.get(i));
				m.setPeak1(peaks.get(2 * i));
				m.setPeak2(peaks.get(2 * i + 1));
				metaboliteList.add(m);
			}

			line = reader.readLine();

			// reads all the rest of the data and creates a list of samples, assigning each
			// peak data to the correct metabolite in the sample - DEEP COPY EXAMPLE (Line
			// 134)
			while (line != null) {
				ArrayList<String> sampleData = new ArrayList<String>(Arrays.asList(line.split(",")));
				sampleData.removeIf(n -> (n.isBlank()));
				Sample s = new Sample(sampleData.get(0));
				ArrayList<Metabolite> mList = new ArrayList<Metabolite>();
				for (int i = 0; i < metaboliteList.size(); i++) {
					Metabolite m = new Metabolite(metaboliteList.get(i));
					m.setPeak1Data(Double.parseDouble(sampleData.get(2 * i + 1)));
					m.setPeak2Data(Double.parseDouble(sampleData.get(2 * i + 2)));
					mList.add(m);
				}
				s.setMetaboliteList(mList);
				samples.add(s);
				line = reader.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		Collections.sort(samples, new SampleComparator());
		return samples;
	}

	public static ArrayList<Double> getAverage(ArrayList<Sample> samples) {
					double sum = 0;
					ArrayList<Double> averageRatios = new ArrayList<Double>();
					int numberOfMetabolites = samples.get(0).getMetabolites().size();
					for (int i = 0; i < numberOfMetabolites; i++) {
						for (int j = 0; j < samples.size(); j++) {
							Sample s = samples.get(j);
							Metabolite m = s.getMetabolites().get(i);
							sum += m.getRatio();
						}
						averageRatios.add(sum/samples.size());
					}
					return averageRatios;
				}

	public static void writeSamplesToFile(ArrayList<Sample> samples) {
		try {

			// writes first row, just the metabolite names
			BufferedWriter writer = new BufferedWriter(new FileWriter("src/organized_data.csv"));

			writer.write(",");
			List<Metabolite> m = samples.get(0).getMetabolites();
			for (int i = 0; i < m.size(); i++) {
				writer.write(m.get(i).getName() + ",,,,");
			}

			writer.newLine();

			// writes second row, metabolite peaks
			writer.write(",");
			for (int i = 0; i < m.size(); i++) {
				writer.write(m.get(i).getPeak1() + ",");
				writer.write(m.get(i).getPeak2() + ",");
				writer.write("Ratio,,");
				// ADD EXTRA TERMS HERE
			}

			// assigns each sample a type
			for (int i = 0; i < samples.size(); i++) {
				samples.get(i).setSampleType(samples.get(i).getName());
			}

			ArrayList<Sample> lfSamples = new ArrayList<Sample>();
			ArrayList<Sample> lbSamples = new ArrayList<Sample>();
			ArrayList<Sample> hfSamples = new ArrayList<Sample>();
			ArrayList<Sample> hbSamples = new ArrayList<Sample>();
			ArrayList<Sample> uncategorized = new ArrayList<Sample>();

			for (int i = 0; i < samples.size(); i++) {
				Sample s = samples.get(i);
				if (s.getSampleType() == "LF") {
					lfSamples.add(s);
				} else if (s.getSampleType() == "LB") {
					lbSamples.add(s);
				} else if (s.getSampleType() == "HF") {
					hfSamples.add(s);
				} else if (s.getSampleType() == "HB") {
					hbSamples.add(s);
				} else {
					uncategorized.add(s);
				}
			}

			writer.newLine();

			// writes all lfSamples to the file

			for (int i = 0; i < lfSamples.size(); i++) {
				Sample s = lfSamples.get(i);
				List<Metabolite> sampleMetabolites = s.getMetabolites();
				writer.write(s.getName() + ",");
				for (int j = 0; j < sampleMetabolites.size(); j++) {
					writer.write(sampleMetabolites.get(j).getPeak1Data() + ",");
					writer.write(sampleMetabolites.get(j).getPeak2Data() + ",");
					writer.write(sampleMetabolites.get(j).getRatio() + ",,");
					// ADD EXTRA TERMS HERE
				}
				writer.newLine();
			}

			writer.newLine();
			ArrayList<Double> lfAverages = getAverage(lfSamples);
			for (int i = 0; i < lfAverages.size(); i++) {
				writer.write(",,," + lfAverages.get(i) + ",");
			}
			writer.newLine();
			writer.newLine();

			for (int i = 0; i < lbSamples.size(); i++) {
				Sample s = lbSamples.get(i);
				List<Metabolite> sampleMetabolites = s.getMetabolites();
				writer.write(s.getName() + ",");
				for (int j = 0; j < sampleMetabolites.size(); j++) {
					writer.write(sampleMetabolites.get(j).getPeak1Data() + ",");
					writer.write(sampleMetabolites.get(j).getPeak2Data() + ",");
					writer.write(sampleMetabolites.get(j).getRatio() + ",,");
				}
				writer.newLine();
			}
			writer.newLine();
			ArrayList<Double> lbAverages = getAverage(lfSamples);
			for (int i = 0; i < lbAverages.size(); i++) {
				writer.write(",,," + lbAverages.get(i) + ",");
			}
			writer.newLine();
			writer.newLine();

			for (int i = 0; i < hfSamples.size(); i++) {
				Sample s = hfSamples.get(i);
				List<Metabolite> sampleMetabolites = s.getMetabolites();
				writer.write(s.getName() + ",");
				for (int j = 0; j < sampleMetabolites.size(); j++) {
					writer.write(sampleMetabolites.get(j).getPeak1Data() + ",");
					writer.write(sampleMetabolites.get(j).getPeak2Data() + ",");
					writer.write(sampleMetabolites.get(j).getRatio() + ",,");
				}
				writer.newLine();
			}

			writer.newLine();
			ArrayList<Double> hfAverages = getAverage(lfSamples);
			for (int i = 0; i < hfAverages.size(); i++) {
				writer.write(",,," + hfAverages.get(i) + ",");
			}
			writer.newLine();
			writer.newLine();

			for (int i = 0; i < hbSamples.size(); i++) {
				Sample s = hbSamples.get(i);
				List<Metabolite> sampleMetabolites = s.getMetabolites();
				writer.write(s.getName() + ",");
				for (int j = 0; j < sampleMetabolites.size(); j++) {
					writer.write(sampleMetabolites.get(j).getPeak1Data() + ",");
					writer.write(sampleMetabolites.get(j).getPeak2Data() + ",");
					writer.write(sampleMetabolites.get(j).getRatio() + ",,");
				}
				writer.newLine();
			}

			writer.newLine();
			ArrayList<Double> hbAverages = getAverage(lfSamples);
			for (int i = 0; i < hbAverages.size(); i++) {
				writer.write(",,," + hbAverages.get(i) + ",");
			}
			writer.newLine();
			writer.newLine();

			for (int i = 0; i < uncategorized.size(); i++) {
				Sample s = uncategorized.get(i);
				List<Metabolite> sampleMetabolites = s.getMetabolites();
				writer.write(s.getName() + ",");
				for (int j = 0; j < sampleMetabolites.size(); j++) {
					writer.write(sampleMetabolites.get(j).getPeak1Data() + ",");
					writer.write(sampleMetabolites.get(j).getPeak2Data() + ",");
					writer.write(sampleMetabolites.get(j).getRatio() + ",,");
				}
				writer.newLine();
			}

			writer.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
