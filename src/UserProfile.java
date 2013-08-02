
import java.util.Date;
import java.util.LinkedList;

public class UserProfile {
	public int userId;
	public String name;	
	public Date joinDate;
	public LinkedList<Mantra> mantras;	
	private String password;

	public UserProfile(int id, String name, String pass){
		this.userId = id;
		this.name = name;
		this.password = pass;
		this.joinDate = new Date();
		this.mantras = new LinkedList<Mantra>();
	}

	public Boolean isPassWord(String str) {
		return str.equals(this.password);
	}

	public void setPassword(String old, String pass) {
		if (isPassWord(old)){
			this.password = pass;
		}
	}
}
