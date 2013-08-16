package MantraMeClasses;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import android.content.Context;

public class MantraGetter {

	private UserProfile userProfile;	
	private DataBaseManager dbManager;

	public MantraGetter(UserProfile user, Context context){		
		userProfile = user;
		List<Mantra> allMantras = CreateMantaList();			
		dbManager = new DataBaseManager(context);
		dbManager.AddMantra(allMantras);		
	}

	private List<Mantra> CreateMantaList() {
		List<Mantra>  allMantras = new ArrayList<Mantra>();

		allMantras.add(new Mantra("Love is the only miracle there is. – Osho", 
				"Osho", java.util.UUID.randomUUID().toString()));
		allMantras.add(new Mantra("Be the change you wish to see in the world. – Gandhi", 
				"Gandhi", java.util.UUID.randomUUID().toString()));
		allMantras.add(new Mantra("Every day in every way I’m getting better and better. – Laura Silva", 
				"Laura Silva", java.util.UUID.randomUUID().toString()));
		allMantras.add(new Mantra("I change my thoughts, I change my world. – Norman Vincent Peale", 
				"Norman Vincent Peale", java.util.UUID.randomUUID().toString()));		
		allMantras.add(new Mantra("Eat Well Travel Often – Unknown", 
				"Eat Well", java.util.UUID.randomUUID().toString()));
		return allMantras;
	}

	public Mantra GetNewMantra(){		

		List<Mantra>  allMantras = dbManager.GetAllMantra();
		Random r = new Random();
		int indexRandom = r.nextInt(allMantras.size());

		return allMantras.get(indexRandom);		
	}
}
