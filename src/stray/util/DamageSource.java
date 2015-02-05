package stray.util;


public class DamageSource {

	public static final DamageSource yourMother = new DamageSource("yourMother");
	public static final DamageSource fire = new DamageSource("fire");
	public static final DamageSource generic = new DamageSource("generic");
	public static final DamageSource spikes = new DamageSource("spikes");
	
	public String name = "";
	
	public DamageSource(String n){
		name = n;
	}
}
