package MantraMeClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MantraGetter {

	UserProfile userProfile;
	
	List<Mantra>  allMantras = new ArrayList<Mantra>();
	
	public MantraGetter(UserProfile user){		
		userProfile = user;
		
		allMantras.add(new Mantra("“Love is the only miracle there is.” – Osho", "Osho", 1));
		allMantras.add(new Mantra("“Be the change you wish to see in the world.” – Gandhi", "Gandhi", 2));
		allMantras.add(new Mantra("“Every day in every way I’m getting better and better.” – Laura Silva", "Laura Silva", 3));
		allMantras.add(new Mantra("“I change my thoughts, I change my world.” – Norman Vincent Peale", "Norman Vincent Peale", 4));		
		allMantras.add(new Mantra("“Eat Well Travel Often” – Unknown", "Eat Well", 5));		
		
	
		
	}
	
	public Mantra GetNewMantra(){
		Random r = new Random();
		int indexRandom = r.nextInt(allMantras.size());
		
		return allMantras.get(indexRandom);
	}
}
