package MantraMeClasses;


import java.util.Date;

public class Mantra {
	public String man_str;
	public Date creationDate;
	public String mantraName;
	public String mantra_id;
	public int ReleventSport;
	public int ReleventEducation;
	public int ReleventNewAge;
	public int ReleventHealth;

	public Mantra(){
		this.man_str = "";
		this.creationDate = new Date();
		this.mantraName = "";
		this.mantra_id = "-1";
		ReleventSport = 50;
		ReleventEducation = 50;
		ReleventNewAge = 50;
		ReleventHealth = 50;
	}

	public Mantra(String mantra, String name, String id){
		this.man_str = mantra;
		this.creationDate = new Date();
		this.mantraName = name;
		this.mantra_id = id;
		ReleventSport = 50;
		ReleventEducation = 50;
		ReleventNewAge = 50;
		ReleventHealth = 50;
	}

	public void SetRelevents(int sport, int education, int newAge, int health){
		ReleventSport = sport;
		ReleventEducation = education;
		ReleventNewAge = newAge;
		ReleventHealth = health;	
	}

	public String toString(){
		String ans = "\n[Mantra id : " + this.mantra_id+ " ,";
		ans += "man_str : " + this.man_str + " ,";
		ans += "Mantra creation Date : " + this.creationDate.toString() + " ,";
		ans += "Mantra Name : " + this.mantraName + " ,";

		ans += "ReleventSport : " + this.ReleventSport + " ,";
		ans += "ReleventEducation : " + this.ReleventEducation + " ,";
		ans += "ReleventNewAge : " + this.ReleventNewAge + " ,";
		ans += "ReleventHealth : " + this.ReleventHealth + " ,";

		return ans;			
	}
}