package stray.suspicion;

import java.util.HashMap;

import stray.Main;
import stray.conversation.Conversation;
import stray.conversation.Speech;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;


public class Offenses {
	
	private static Offenses instance;

	private Offenses() {
	}

	public static Offenses instance() {
		if (instance == null) {
			instance = new Offenses();
			instance.loadResources();
		}
		return instance;
	}
	
	private HashMap<String, Offense> offenseList = new HashMap<String, Offense>();

	private void loadResources() {
		FileHandle h = Gdx.files.internal("offenses/offenses.xml");
		XmlReader reader = new XmlReader();
		Element root = reader.parse(h.readString());
		Array<Element> elements = root.getChildrenByName("offense");
		
		for (final Element e : elements) { // for each offense
			offenseList.put(e.getAttribute("id", "failed-" + e.hashCode()),
					new Offense(){
				@Override
				public String getUnlocalizedName(){
					return e.getAttribute("name", defaultName);
				}
				
				@Override
				public int getOffenseLevel(){
					return e.getIntAttribute("level", 1);
				}
			});
		}
	}
	
}
