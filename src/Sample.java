import java.util.*;

public class Sample {
	private String name;
	private int sortOrder;
	private ArrayList<Metabolite> metabolites;
	
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
	
	// metabolites getter
	public ArrayList<Metabolite> getMetabolites() {
		return this.metabolites;
	}
	
	// metabolites setter
	public void addMetabolite(Metabolite metabolite) {
		this.metabolites.add(metabolite);
	}
	
}
