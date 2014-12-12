package stray.util;

import com.badlogic.gdx.utils.Array;

public class Splashes {

	public static String getRandomSplash() {
		if (instance().splashes.size == 0) {
			return "WARNING MISSING SPLASHES";
		}
		return instance().splashes.random();
	}

	private static Splashes instance;

	private Splashes() {
	}

	public static Splashes instance() {
		if (instance == null) {
			instance = new Splashes();
			instance.loadResources();
		}
		return instance;
	}

	Array<String> splashes = new Array<String>(128);

	private void loadResources() {
		splashes.add("The Conductor!");
		splashes.add("This is all your fault Gaary!");
		splashes.add("Also try Cook, Serve, Delicious!");
		splashes.add("Uses libGDX!");
		splashes.add("Music by Kevin MacLeod!");
		splashes.add("Buckle your pants!");
		splashes.add(":D");
		splashes.add("The invention of the air conditioner was pretty cool...");
		splashes.add("Interactive!");
		splashes.add("Also try Kerbal Space Program!");
		splashes.add("Alrighty guys!");
		splashes.add("Don't you want a snack-arie?");
		splashes.add("Remember to blink!");
		splashes.add("You can't swallow your own tongue!");
		splashes.add("Mind the Gap!");
		splashes.add("Is the cake really a lie?");
		splashes.add("When all else fails, use missiles.");
		splashes.add("Breathing is mandatory.");
		splashes.add("I'm a potato...");
		splashes.add("Come to daddy!");
		splashes.add("Suddenly, pineapples!");
		splashes.add("All your base are belong to us!");
		splashes.add("Firing lazors since 1453!");
		splashes.add("Let's play poker!");
		splashes.add("Spaaaaaaaaaace!");
		splashes.add("Is that supposed to look like that?");
		splashes.add("Cheers!");
		splashes.add("Jarate!");
		splashes.add("You just can't get enough of it!");
		splashes.add("What are you waiting for?!");
		splashes.add("You may be subject to addiction...");
		splashes.add("It's ka-boom time!");
		splashes.add("Rise and shine, Mr. Freeman. Rise and shine.");
		splashes.add("Only because I'm special!");
		splashes.add("There is someone behind you...");
		splashes.add("Priceless!");
		splashes.add("Hilarious!");
		splashes.add("Snails are slow...");
		splashes.add("To be continued...");
		splashes.add("Je parle plusieurs langues!");
		splashes.add("Clever, very clever, and foolish!");
		splashes.add("Do a barrel roll!");
		splashes.add("C'mere, cupcake!");
		splashes.add("I will saw through your bones!");
		splashes.add("The Grass is a scaaaary place!");
		splashes.add("Also listen to Pepper Steak by Alias Coldwood!");
		splashes.add("Brought to you by the letter "
				+ new Character(Utils.getRandomLetter()).toString() + "!");
		splashes.add("EEEEheEheEEEHeeeEEheE!");
		splashes.add("Is there a problem?");
		splashes.add("This text is not green.");
		splashes.add("Why are you reading this?");
		splashes.add("NOTICE: Do not notice");
		splashes.add("Read this text upside down to look stupid.");
		splashes.add("Potato magic!");
		splashes.add("Do the things, win the points!");
		splashes.add("x = 2; (x)spooky(x2)me");
		splashes.add("Fluffy biscuits!");
		splashes.add("Do wolves love donuts?!");
		splashes.add("ZELDA IS PRINCESS");

		// splashes.add("");

		splashes.shuffle();
	}

}
