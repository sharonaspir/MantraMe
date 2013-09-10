package MantraMeClasses;

import java.util.Date;

public class Mantra {
	public String Description;
	public Date CreationDate;
	public String Author;
	public String Id;
	public int ReleventSport;
	public int ReleventEducation;
	public int ReleventNewAge;
	public int ReleventHealth;

	public Mantra(){
		this.Description = "";
		this.CreationDate = new Date();
		this.Author = "";
		this.Id = "-1";
		ReleventSport = 50;
		ReleventEducation = 50;
		ReleventNewAge = 50;
		ReleventHealth = 50;
	}

	public Mantra(String mantra, String author, String id){
		this.Description = mantra;
		this.CreationDate = new Date();
		this.Author = author;
		this.Id = id;
		ReleventSport = 50;
		ReleventEducation = 50;
		ReleventNewAge = 50;
		ReleventHealth = 50;
	}
	
	public void SetCreationDate(Date date){
		this.CreationDate = date;		
	}

	public void SetRelevents(int sport, int education, int newAge, int health){
		ReleventSport = sport;
		ReleventEducation = education;
		ReleventNewAge = newAge;
		ReleventHealth = health;	
	}

	public String toString(){
		String ans = "\n[Mantra id : " + this.Id+ " ,";
		ans += "man_str : " + this.Description + " ,";
		ans += "Mantra creation Date : " + this.CreationDate.toString() + " ,";
		ans += "Mantra Name : " + this.Author + " ,";
		ans += "ReleventSport : " + this.ReleventSport + " ,";
		ans += "ReleventEducation : " + this.ReleventEducation + " ,";
		ans += "ReleventNewAge : " + this.ReleventNewAge + " ,";
		ans += "ReleventHealth : " + this.ReleventHealth + " ,";

		return ans;			
	}
	
}