package stray.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import stray.Main;
import stray.entity.Entity;
import stray.entity.EntityZaborinox;
import stray.world.World;

import com.badlogic.gdx.graphics.Color;

public class Blocks {

	private static Blocks instance;

	private Blocks() {
	}

	public static Blocks instance() {
		if (instance == null) {
			instance = new Blocks();
			instance.loadResources();
		}
		return instance;
	}

	private HashMap<String, Block> blocks;
	private HashMap<Block, String> reverse;
	
	public static final Color RED = new Color(1, 0, 0, 1);
	public static final Color GREEN = new Color(16 / 255f, 164 / 255f, 43 / 255f, 1);
	public static final Color BLUE = new Color(0, 145 / 255f, 1, 1);
	public static final Color PURPLE = new Color(178 / 255f, 0, 1, 1);
	public static final Color ORANGE = new Color(1, 106 / 255f, 0, 1);

	private void loadResources() {
		blocks = new HashMap<String, Block>();
		reverse = new HashMap<Block, String>();

		put("space", new BlockSpace("images/blocks/old/space/space").hasVariants(8));
		put("wall", new Block("images/blocks/old/dungeonwall/wall").useConTextures().solidify());
		put("empty", new BlockEmpty());
		put("spike", new BlockSpike("images/blocks/spike").solidify());
		put("sign", new BlockReadable("images/blocks/sign/sign"){
			@Override
			public void onRead(World world, int x, int y){
				if(world.getMeta(x, y) != null){
					if(world.getMeta(x, y).equalsIgnoreCase("secretexit")){
						world.main.awardAchievement("secretexit");
					}
				}
			}
		}.hasVariants(4));
		
		put("switchred", new BlockSwitch(RED, "red").solidify());
		put("switchgreen", new BlockSwitch(GREEN, "green").solidify());
		put("switchblue", new BlockSwitch(BLUE, "blue").solidify());
		put("switchpurple", new BlockSwitch(PURPLE, "purple").solidify());
		put("switchorange", new BlockSwitch(ORANGE, "orange").solidify());
 		
		put("togglered", new BlockToggle(RED, "red").solidify());
		put("togglegreen", new BlockToggle(GREEN, "green").solidify());
		put("toggleblue", new BlockToggle(BLUE, "blue").solidify());
		put("togglepurple", new BlockToggle(PURPLE, "purple").solidify());
		put("toggleorange", new BlockToggle(ORANGE, "orange").solidify());
		
		put("timerred", new BlockTimer(RED, "red"));
		put("timergreen", new BlockTimer(GREEN, "green"));
		put("timerblue", new BlockTimer(BLUE, "blue"));
		put("timerpurple", new BlockTimer(PURPLE, "purple"));
		put("timerorange", new BlockTimer(ORANGE, "orange"));
		
		put("exitportal", new BlockExitPortal());
		put("platform", new BlockPlatform("images/blocks/platform/platform").useConTextures());
		put("cameramagnet", new BlockCameraMagnet("images/blocks/magnet"));
		put("electrode", new BlockElectrode("images/blocks/electrode/electrode"));
		put("fire", new BlockFire(null).setAnimation("fire"));
		
		// spawners
		put("spawnerplayer", new BlockPlayerSpawner("images/entity/player/player"));
		put("spawnerzaborinox", new BlockSpawner("images/entity/zaborinox"){
			public Entity getEntity(World world, int x, int y){
				return new EntityZaborinox(world, x, y);
			}
		});
		
		

	}

	private void put(String key, Block value) {
		blocks.put(key, value);
		reverse.put(value, key);
	}

	public Block getBlock(String key) {
		if (key == null) return defaultBlock();
		return blocks.get(key);
	}

	public String getKey(Block block) {
		if (block == null) return defaultBlock;
		return reverse.get(block);
	}

	public Iterator getAllBlocks() {
		return blocks.entrySet().iterator();
	}

	public void dispose() {
		Iterator it = getAllBlocks();
		while (it.hasNext()) {
			Entry pairs = (Entry) it.next();
			Block block = (Block) pairs.getValue();
			block.dispose();
		}
	}

	/**
	 * must be called manually
	 * 
	 * @param main
	 */
	public void addBlockTextures(Main main) {
		Iterator it = getAllBlocks();
		while (it.hasNext()) {
			Entry pairs = (Entry) it.next();
			Block block = (Block) pairs.getValue();
			block.addTextures(main);
		}
	}

	public static Block defaultBlock() {
		return instance().getBlock(defaultBlock);
	}

	public static final String defaultBlock = "empty";
}
