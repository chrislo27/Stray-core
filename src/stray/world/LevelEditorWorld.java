package stray.world;

import stray.Main;


public class LevelEditorWorld extends World{

	public LevelEditorWorld(Main main, int x, int y, long seed) {
		super(main, x, y, seed);
	}
	
	public LevelEditorWorld(Main main){
		super(main);
	}
	
	@Override
	public int getMeta(int x, int y){
		if(x == -1337 && y == -1337){
			return Main.LEVELEDITOR.defaultmeta;
		}
		return super.getMeta(x, y);
	}

}
