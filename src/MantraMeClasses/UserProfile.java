
package MantraMeClasses;

import java.util.Date;
import java.util.LinkedList;
import java.util.UUID;

public class UserProfile {
	
	private static UserProfile userProfileUsed = null;
	
	private String password;
	public String userId;
	public String name;	
	public String email;	
	public Date joinDate;
	public LinkedList<Mantra> localMantras;	

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
	
	public UserProfile(String name, String email, String id){		
		this.userId = id;
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
	
	public void SetPassWord(String pass){		
		password = pass;
	}
	
	public String GetPassWord(){		
		return password;
	}

	@Override
	public String toString() {
		return "name: " + name + ". email " + email + " . pass " + password; 
	}
	
	public static UserProfile getUser(){
		return userProfileUsed;
	}
	
	public static void setUser(UserProfile user){
		userProfileUsed = user;
	}
}
