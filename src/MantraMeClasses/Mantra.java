package MantraMeClasses;


import java.util.Date;

public class Mantra {
	public String man_str;
	public Date creationDate;
	public String creatorName;
	public int viewCount;
	public double avg_grade;
	public int gradersNumber;
	public int mantra_id;

	public Mantra(){
		this.man_str = "";
		this.creationDate = new Date();
		this.creatorName = "";
		this.viewCount = 1;
		this.avg_grade = 1;
		this.gradersNumber = 0;
		this.mantra_id = -1;
	}
	
	public Mantra(String mantra, String name, int id){
		this.man_str = mantra;
		this.creationDate = new Date();
		this.creatorName = name;
		this.viewCount = 1;
		this.avg_grade = 1;
		this.gradersNumber = 0;
		this.mantra_id = id;
	}

	public void grade(int grade){
		if (grade<0 || grade > 5) return;
		double sum = this.gradersNumber * this.avg_grade;
		this.gradersNumber++;
		this.avg_grade = (sum + grade) / this.gradersNumber;
	}

	public void addViewer(int n){
		this.viewCount += n;
	}

	public String toString(){
		String ans = "\n[Mantra id : " + this.mantra_id+ " ,";
		ans += "man_str : " + this.man_str + " ,";
		ans += "Mantra creation Date : " + this.creationDate.toString() + " ,";
		ans += "Mantra creator Name : " + this.creatorName + " ,";
		ans += "Mantra view Count : " + this.viewCount + " ,";
		ans += "Mantra avg grade : " + this.avg_grade + " ,";
		ans += "Mantra Number of graders : " + this.gradersNumber + " ]\n";
		return ans;			
	}
}