package stray.augment;

import stray.world.World;

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
		public String getName() {
			return "augment.name.default";
		}

		@Override
		public void onActivateStart(World world) {
		}

		@Override
		public void onActivate(World world) {
		}

		@Override
		public void onActivateEnd(World world) {
		}

		@Override
		public long getUseTime() {
			return 0;
		}

		@Override
		public boolean canUse(World world) {
			return false;
		}

		@Override
		public void onActivateTick(World world) {
		}

		@Override
		public boolean isInUse(World world) {
			return false;
		}
		
	};
	
	private void loadResources() {
		list.add(new FLUDDAugment());
		list.add(new FireAugment());
		list.add(new TelekinesisAugment());
	}
	
	public static Array<Augment> getList(){
		return instance().list;
	}
	
	public static Augment getAugment(int i){
		if(i >= getList().size || i < 0) return instance().def;
		return getList().get(i);
	}
	
}
