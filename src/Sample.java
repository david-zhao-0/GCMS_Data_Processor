
public class Sample {
	private String name;
	private int sortOrder;
	private String metabolite;
	private String peak1;
	private String peak2;
	private int peak1Data;
	private int peak2Data;
	
	// Name Getter
	public String getName() {
		return name;
	}
	
	// Name Setter
	public void setName(String newName) {
		this.name = newName;
	}
	
	// sortOrder getter
	// returns the sort order number based on the name of the sample, with order LF<LB<HF<HB (uses cases) -> note that the switch condition
	// requires for the first 2 character indexes to be LF/LB/HF/HB, or it will be at the top (return -1)
	
	public int getSortOrder() {
		switch (this.name.substring(0, 2)) {
		case "LF": 
			return 0;
		case "LB":
			return 1;
		case "HF":
			return 2;
		case "HB":
			return 3;
		default: 
			return -1;
		}
	}
	
	// Metabolite Name Getter
	public String getMetabolite() {
		return metabolite;
	}
	
	// Metabolite Name Setter
	public void setMetabolite(String newMetabolite) {
		this.metabolite = newMetabolite;
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
		this.name = peak2;
	}
	
	// peak1Data Getter
	public int getPeak1Data() {
		return peak1Data;
	}
	
	// peak1Data Setter
	public void setPeak1Data(int peak1Data) {
		this.peak1Data = peak1Data;
	}
	
	// peak2Data Getter
	public int getPeak2Data() {
		return peak2Data;
	}
	
	// peak2Data Setter
	public void setPeak2Data(int peak2Data) {
		this.peak2Data = peak2Data;
	}
	
}
