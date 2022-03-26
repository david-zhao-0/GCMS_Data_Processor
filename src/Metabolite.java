import java.util.*;

public class Metabolite {
	// Creates variables for each metabolite to be analyzed
	// name - name of metabolite
	// peak 1 - isotope number 1 for metabolite of interest
	// peak 2 - isotope number 2 for metabolite of interest
	// peak1Data - int representing mass spec peak of data
	// peak2Data - int representing mass spec peak of data
	private String name;
	private String peak1;
	private String peak2;
	private double peak1Data;
	private double peak2Data;
	private double ratio;
	
	// Metabolite constructor
	public Metabolite(String name) {
		this.name = name;
	}
	
	// Copy constructor
	public Metabolite(Metabolite metabolite) {
		this.name = metabolite.name;
		this.peak1 = metabolite.peak1;
		this.peak2 = metabolite.peak2;
		this.peak1Data = metabolite.peak1Data;
		this.peak2Data = metabolite.peak2Data;
		this.ratio = metabolite.ratio;
	}
	
	// Name Getter
	public String getName() {
		return name;
	}
	
	// Name Setter
	public void setName(String newName) {
		this.name = newName;
	}
	
	
	// peak1 Getter
	public String getPeak1() {
		return peak1;
	}
	
	// peak1 Setter
	public void setPeak1(String peak1) {
		this.peak1 = peak1;
	}
	
	// peak2 Getter
	public String getPeak2() {
		return peak2;
	}
	
	// peak2 Setter
	public void setPeak2(String peak2) {
		this.peak2 = peak2;
	}
	
	// peak1Data Getter
	public double getPeak1Data() {
		return peak1Data;
	}
	
	// peak1Data Setter
	public void setPeak1Data(double peak1Data) {
		this.peak1Data = peak1Data;
	}
	
	// peak2Data Getter
	public double getPeak2Data() {
		return peak2Data;
	}
	
	// peak2Data Setter
	public void setPeak2Data(double peak2Data) {
		this.peak2Data = peak2Data;
	}
	
	// ratio Getter
	public double getRatio() {
		return this.peak1Data/this.peak2Data;
	}
}
