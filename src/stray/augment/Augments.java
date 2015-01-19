package stray.augment;

import com.badlogic.gdx.utils.Array;


public class Augments {
	
	private static Augments instance;

	private Augments() {
	}

	public static Augments instance() {
		if (instance == null) {
			instance = new Augments();
			instance.loadResources();
		}
		return instance;
	}

	private Array<Augment> list = new Array<Augment>();
	private Augment def = new Augment(){

		@Override
		public void onActivateStart() {
		}

		@Override
		public void onActivate() {
		}

		@Override
		public void onActivateEnd() {
		}

		@Override
		public String getName() {
			return "augment.default.name";
		}
		
	};
	
	private void loadResources() {
		
	}
	
	public static Array<Augment> getList(){
		return instance().list;
	}
	
	public static Augment getAugment(int i){
		if(i >= getList().size || i < 0) return instance().def;
		return getList().get(i);
	}
	
}
