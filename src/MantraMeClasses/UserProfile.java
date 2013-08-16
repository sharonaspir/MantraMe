
package MantraMeClasses;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public class UserProfile {
	
	public static UserProfile userProfileUsed = null;
	
	public String userId;
	public String name;	
	public String email;	
	public Date joinDate;
	public LinkedList<Mantra> localMantras;	

	@Override
	public String toString() {
		return "name: " + name + ". email " + email; 
	}

	public int intrestEducation;
	public int intrestNewAge;
	public int intrestSport;
	public int intrestHealth; 

	public UserProfile(String name, String email){
		String uuid = UUID.randomUUID().toString();
		this.userId = uuid;
		this.name = name;
		this.email = email;
		this.joinDate = new Date();
		this.localMantras = new LinkedList<Mantra>();		
	}

	public void SetInterst(int education, int newAge,int sport,int health){
		intrestEducation = education;
		intrestNewAge = newAge;
		intrestSport= sport;
		intrestHealth = health; 
	}
}
