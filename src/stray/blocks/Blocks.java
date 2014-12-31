package stray.blocks;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import stray.Main;
import stray.blocks.fluid.BlockFluid;
import stray.entity.Entity;
import stray.entity.EntityZaborinox;
import stray.world.World;

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

	private void loadResources() {
		blocks = new HashMap<String, Block>();
		reverse = new HashMap<Block, String>();

		put("space", new Block("images/blocks/old/space/space").hasVariants(8));
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
		
		put("switchred", new BlockSwitch("images/blocks/switch/red", "red").solidify());
		put("switchgreen", new BlockSwitch("images/blocks/switch/green", "green").solidify());
		put("switchblue", new BlockSwitch("images/blocks/switch/blue", "blue").solidify());
		put("switchpurple", new BlockSwitch("images/blocks/switch/purple", "purple").solidify());
		put("switchorange", new BlockSwitch("images/blocks/switch/orange", "orange").solidify());
 		
		put("togglered", new BlockToggle("images/blocks/toggle/red", "red").solidify());
		put("togglegreen", new BlockToggle("images/blocks/toggle/green", "green").solidify());
		put("toggleblue", new BlockToggle("images/blocks/toggle/blue", "blue").solidify());
		put("togglepurple", new BlockToggle("images/blocks/toggle/purple", "purple").solidify());
		put("toggleorange", new BlockToggle("images/blocks/toggle/orange", "orange").solidify());
		
		put("timerred", new BlockTimer("images/blocks/timer/red", "red"));
		put("timergreen", new BlockTimer("images/blocks/timer/green", "green"));
		put("timerblue", new BlockTimer("images/blocks/timer/blue", "blue"));
		put("timerpurple", new BlockTimer("images/blocks/timer/purple", "purple"));
		put("timerorange", new BlockTimer("images/blocks/timer/orange", "orange"));
		
		put("exitportal", new BlockExitPortal(null).setAnimation("portal"));
		put("platform", new BlockPlatform("images/blocks/platform/platform").useConTextures());
		put("cameramagnet", new BlockCameraMagnet("images/blocks/magnet"));
		put("electrode", new BlockElectrode("images/blocks/electrode/electrode"));
		
		// spawners
		put("spawnerplayer", new BlockPlayerSpawner("images/entity/player/player"));
		put("spawnerzaborinox", new BlockSpawner("images/entity/zaborinox"){
			public Entity getEntity(World world, int x, int y){
				return new EntityZaborinox(world, x, y);
			}
		});
		
		// fluids
		put("fluidtest", new BlockFluid("aodkawfoa/aowa"){
			@Override
			public int getGravityDirection(){
				return 1;
			}
			
			@Override
			public int getTickRate(){
				return 5;
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
