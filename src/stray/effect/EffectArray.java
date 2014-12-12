package stray.effect;

import com.badlogic.gdx.utils.Array;

public class EffectArray extends Array<Effect> {

	public boolean containsEffect(Class<? extends Effect> cls) {
		for (int i = 0; i < this.size; i++) {
			if (cls.isInstance(this.get(i))) {
				return true;
			}
		}
		return false;
	}

	public int getEffectAt(Class<? extends Effect> cls) {
		for (int i = 0; i < this.size; i++) {
			if (cls.isInstance(this.get(i))) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void add(Effect e) {
		if (containsEffect(e.getClass())) {
			this.removeIndex(getEffectAt(e.getClass()));
			super.add(e);
		} else {
			super.add(e);
		}
	}
}
