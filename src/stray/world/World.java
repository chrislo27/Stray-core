package stray.world;

import java.io.IOException;
import java.util.Random;

import stray.Main;
import stray.Settings;
import stray.augment.Augments;
import stray.blocks.Block;
import stray.blocks.BlockCameraMagnet;
import stray.blocks.BlockPlayerSpawner;
import stray.blocks.Blocks;
import stray.blocks.fluid.BlockFluid;
import stray.entity.Entity;
import stray.entity.EntityPlayer;
import stray.pathfinding.Mover;
import stray.pathfinding.TileBasedMap;
import stray.util.AssetMap;
import stray.util.GlobalVariables;
import stray.util.MathHelper;
import stray.util.Particle;
import stray.util.ParticlePool;
import stray.util.Sizeable;
import stray.util.Utils;
import stray.util.render.SmoothCamera;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;
import com.badlogic.gdx.utils.XmlWriter;

public class World implements TileBasedMap {

	public Main main;
	public SpriteBatch batch;

	public int sizex = 32;
	public int sizey = 32;

	public static final int tilesizex = 64;
	public static final int tilesizey = 64;
	public static final float tilepartx = (1f / tilesizex);
	public static final float tileparty = (1f / tilesizey);

	public float gravity = 20f;
	public float drag = 20f;

	public String background = "levelbgcircuit";

	public Block[][] blocks;
	public String[][] meta;

	public Pool<BlockUpdate> buPool = Pools.get(BlockUpdate.class);
	public Array<BlockUpdate> scheduledUpdates = new Array<BlockUpdate>();

	public Array<Entity> entities;

	public Array<Particle> particles;

	public int magicnumber;

	public SmoothCamera camera;
	private float cameramovex = 0, cameramovey = 0;

	/**
	 * number of ticks since creation
	 */
	public long tickTime = -1;
	public int canRespawnIn = 0;
	/**
	 * millisecond time since show() was called
	 */
	public long msTime = 0;
	private long voidMsTime = System.currentTimeMillis();
	public int voidTime = -1;

	public WorldRenderer renderer;

	public Color vignettecolour = new Color(0, 0, 0, 0);

	public GlobalVariables global = new GlobalVariables();

	public String levelfile = null;

	public float checkpointx, checkpointy;

	private float voidTimer = 0;
	private static final float VOID_LENGTH = 6f;
	
	public int currentAugment = 0;
	private boolean augmentActivate = false;

	public World(Main main) {
		this(main, 32, 24, Main.getRandomInst().nextLong());
	}

	public World(Main main, int x, int y, long seed) {
		this.main = main;
		batch = main.batch;
		sizex = x;
		sizey = y;
		camera = new SmoothCamera(this);
		prepare();
		renderer = new WorldRenderer(this);
	}

	public void prepare() {
		msTime = System.currentTimeMillis();
		voidMsTime = System.currentTimeMillis();
		global.clear();
		blocks = new Block[sizex][sizey];
		meta = new String[sizex][sizey];

		for (int j = 0; j < sizex; j++) {
			for (int k = 0; k < sizey; k++) {
				blocks[j][k] = Blocks.instance().getBlock(Blocks.defaultBlock);
				if (k >= sizey - 8) blocks[j][k] = Blocks.instance().getBlock("wall");
				meta[j][k] = null;
			}
		}

		magicnumber = new Random().nextInt();

		entities = new Array<Entity>(32);
		particles = new Array<Particle>();
		addPlayer();
		// camera.forceCenterOn(getPlayer().x, getPlayer().y);
		setCheckpoint();
	}

	public void addPlayer() {
		if (getPlayer() == null) {
			EntityPlayer player = new EntityPlayer(this, 13, sizey - 9);
			player.prepare();
			entities.add(player);
		}
	}

	public float getPan(float x) {
		return Utils.getSoundPan(x, camera.camerax);
	}

	public World getWorld() {
		return this;
	}

	public void generateCircle(int cx, int cy, String id, int rad) {
		for (int x = cx - rad; x < cx + rad + 1; x++) {
			for (int y = cy - rad; y < cy + rad + 1; y++) {
				if (getBlock(x, y) != Blocks.instance().getBlock(id)) {
					if (Math.round(MathHelper.calcDistance(x, y, cx, cy)) <= rad) {
						setBlock(Blocks.instance().getBlock(id), x, y);
					}
				}
			}
		}
	}

	public void setVignette(float r, float g, float b, float alpha) {
		vignettecolour.set(r, g, b, alpha);
	}

	public void setVignette(float alpha) {
		vignettecolour.set(vignettecolour.r, vignettecolour.g, vignettecolour.b, alpha);
	}

	public void setVignette(Color c, float alpha) {
		vignettecolour.set(c);
		setVignette(alpha);
	}

	public void inputUpdate() {
		if (main.getConv() != null) return;
		if (getPlayer() == null) return;
		if (getPlayer().health > 0) {

			if (Gdx.input.isKeyPressed(Keys.SPACE)) {
				getPlayer().jump();
			} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))) {
				
			} else if ((Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W))) {
				
			}

			if ((Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A))) {
				getPlayer().moveLeft();
				
			} else if ((Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D))) {
				getPlayer().moveRight();
				
			} else {

			}
			
			if(Gdx.input.isKeyJustPressed(Keys.E)){
				Augments.getAugment(currentAugment).onActivateStart();
				augmentActivate = true;
			}else if(Gdx.input.isKeyJustPressed(Keys.Q)){
				if(Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)){
					currentAugment--;
					if(currentAugment < 0) currentAugment = Augments.getList().size - 1;
				}else{
					currentAugment++;
					if(currentAugment >= Augments.getList().size) currentAugment = 0;
				}
			}
			if(Gdx.input.isKeyPressed(Keys.E)){
				Augments.getAugment(currentAugment).onActivate();
				augmentActivate = true;
			}
			
			if(!Gdx.input.isKeyJustPressed(Keys.E) && augmentActivate){
				augmentActivate = false;
				Augments.getAugment(currentAugment).onActivateEnd();
			}
			
		}

		if (Gdx.input.isKeyPressed(Keys.I)) {
			cameramovey = -World.tilesizey * 1.5f;
		} else if (Gdx.input.isKeyPressed(Keys.K)) {
			cameramovey = World.tilesizey * 1.5f;
		} else {
			cameramovey = 0;
		}
		if (Gdx.input.isKeyPressed(Keys.J)) {
			cameramovex = -World.tilesizex * 1.5f;
		} else if (Gdx.input.isKeyPressed(Keys.L)) {
			cameramovex = World.tilesizex * 1.5f;
		} else cameramovex = 0;

		if (Settings.debug) {
			if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)) {
				if (Gdx.input.isKeyJustPressed(Keys.T)) {
					getPlayer().damage(0.999f);
				}
			}
		}

	}

	public void renderUpdate() {
		inputUpdate();
		for (Entity e : entities) {
			e.renderUpdate();
		}
	}

	public EntityPlayer getPlayer() {
		for (int i = 0; i < entities.size; i++) {
			if (entities.get(i) instanceof EntityPlayer) {
				return (EntityPlayer) entities.get(i);
			}
		}

		return null;
	}

	private void centerCamera() {
		EntityPlayer p = getPlayer();
		if (p == null) {
			return;
		}
		if (p.health <= 0) return;

		camera.centerOn(((p.x + (p.sizex / 2f)) * tilesizex + cameramovex), ((p.y + (p.sizey / 2f))
				* tilesizey + cameramovey)
				- (tilesizey * 3));
		for (int x = (int) (p.x - ((Gdx.graphics.getWidth() / 2) / tilesizex)); x < (int) (p.x + ((Gdx.graphics
				.getWidth() / 2) / tilesizex)); x++) {
			for (int y = (int) (p.y - ((Gdx.graphics.getHeight() / 2) / tilesizey)) - 3; y < (int) (p.y + ((Gdx.graphics
					.getHeight() / 2) / tilesizex)) + 3; y++) {
				if (getBlock(x, y) instanceof BlockCameraMagnet) {
					camera.centerOn((x + 0.5f) * tilesizex, (y - 0.5f) * tilesizex);
					camera.clamp();
					main.camera.update();
					return;
				}
			}
		}
		camera.clamp();
		main.camera.update();
	}

	public void renderOnly() {
		main.buffer.begin();
		batch.begin();
		renderer.renderBackground();
		batch.flush();

		renderer.renderBlocks();
		// entities
		renderer.renderEntities();

		// particles
		if (particles.size > 0) {
			for (Particle p : particles) {
				p.render(this, main);
			}
		}
		batch.end();
		main.buffer.end();

		batch.begin();
		
		renderer.renderBuffer();

		batch.end();
	}

	public void render() {

		if (vignettecolour.a > 0) {
			vignettecolour.a -= Gdx.graphics.getRawDeltaTime();
			if (vignettecolour.a < 0) vignettecolour.a = 0;
		}

		renderOnly();

		batch.begin();
		if (voidTime > 0 && getVoidDistance() > 0f
				&& (camera.camerax / World.tilesizex) - getVoidDistance() < 8) {
			renderer.renderVoid();
			if ((camera.camerax / World.tilesizex) - getVoidDistance() < 4) {
				if ((voidTimer += Gdx.graphics.getRawDeltaTime()) >= VOID_LENGTH) {
					main.manager.get(AssetMap.get("voidambient"), Sound.class).play(Settings.getSoundVolume(), 1f,
							getPan(getVoidDistance()));
					voidTimer -= VOID_LENGTH;
				}
			} else {
				if (voidTimer != 0) {
					main.manager.get(AssetMap.get("voidambient"), Sound.class).stop();
					voidTimer = 0;
				}
			}
		}
		renderer.renderUi();
		batch.end();

		Particle item;
		for (int i = particles.size; --i >= 0;) {
			item = particles.get(i);
			if (item.lifetime <= 0) {
				particles.removeIndex(i);
				ParticlePool.instance().getPool().free(item);
			}
		}

	}

	private Color setAlphaColor(float a, Color c) {
		c.a = a;
		return c;
	}

	public void setCheckpoint() {
		checkpointx = getPlayer().x;
		checkpointy = getPlayer().y;
	}

	public void setCheckpoint(float x, float y) {
		checkpointx = x;
		checkpointy = y;
	}

	public void attemptCheckpoint() {

	}

	public void tickUpdate() {
		++tickTime;

		renderer.tickUpdate();

		for (int y = sizey - 1; y >= 0; y--) {
			for (int x = (BlockFluid.movementDirection() ? 0 : sizex - 1); (BlockFluid
					.movementDirection() ? x++ < sizex : --x >= 0);) {
				executeBlockUpdates();
				if (getBlock(x, y).getTickRate() < 1) continue;
				if (getBlock(x, y).getTickRate() > 1) if (tickTime % getBlock(x, y).getTickRate() != 0) continue;

				getBlock(x, y).tickUpdate(this, x, y);
			}
		}

		executeBlockUpdates();

		if (canRespawnIn > 0) {
			canRespawnIn--;
			if (canRespawnIn == 0) {
				getPlayer().x = checkpointx;
				getPlayer().y = checkpointy;
				getPlayer().health = getPlayer().maxhealth;
				getPlayer().invincibility = 1;
				voidMsTime = System.currentTimeMillis();
			}
		}

		for (int i = 0; i < entities.size; i++) {
			entities.get(i).tickUpdate();
		}
		for (int i = entities.size - 1; i > -1; i--) {
			if (entities.get(i).isDead()) {
				if (!entities.get(i).onDeath()) entities.removeIndex(i);
			}
		}

		centerCamera();
		camera.update();

		if (getPlayer() != null) {
			if (getVoidDistance() > (getPlayer().x + getPlayer().sizex)) {
				getPlayer().damage(9001);
			}
		}
	}

	/**
	 * 
	 * @return -1 if void time is 0 or less, the distance in blocks of the void
	 *         otherwise
	 */
	public float getVoidDistance() {
		if (voidTime <= 0) return -1;
		return MathUtils.clamp(
				((((System.currentTimeMillis() - voidMsTime) / 1000f) / voidTime) * sizex), -1,
				sizex);
	}

	public void executeBlockUpdates() {
		BlockUpdate b;
		for (int i = scheduledUpdates.size; --i >= 0;) {
			b = scheduledUpdates.get(i);
			setBlock(b.block, b.x, b.y);
			setMeta(b.meta, b.x, b.y);
			scheduledUpdates.removeIndex(i);
			buPool.free(b);
		}
	}

	private int getEntitiesType(Class<? extends Entity> cls) {
		int num = 0;
		for (Entity e : entities) {
			if (cls.isInstance(e)) {
				num++;
			}
		}
		return num;
	}

	public boolean canSpawn(Sizeable s, int x, int y) {
		if (x >= 0 && y >= 0 && x < sizex && y < sizey) {
			int width = ((int) s.getWidth()) + 1;
			int height = ((int) s.getHeight()) + 1;

			for (int checkx = x; checkx < x + width; checkx++) {
				for (int checky = y; checky < y + height; checky++) {
					if (getBlock(checkx, checky).isSolid(this, checkx, checky)) {
						return false;
					}
				}
			}
			return true;
		}

		return false;
	}

	public void show() {
		msTime = System.currentTimeMillis();
	}

	public void hide() {
		main.manager.get(AssetMap.get("voidambient"), Sound.class).stop();
		voidTimer = 0;
	}

	public void dispose() {
		voidTimer = 0;
	}

	/**
	 * get room position based on mouse coords
	 * 
	 * @param x
	 * @return
	 */
	public int getRoomX(float x) {
		return (int) ((x + camera.camerax) / tilesizex);
	}

	/**
	 * @see getRoomX
	 * @param y
	 * @return
	 */
	public int getRoomY(float y) {
		return (int) ((y + camera.cameray) / tilesizey);
	}

	public Block getBlock(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return Blocks.defaultBlock();
		if (blocks[x][y] == null) return Blocks.defaultBlock();
		return blocks[x][y];
	}

	public String getMeta(int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return null;
		return meta[x][y];
	}

	public void setBlock(Block r, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		blocks[x][y] = r;

	}

	public void setMeta(String m, int x, int y) {
		if (x < 0 || y < 0 || x >= sizex || y >= sizey) return;
		meta[x][y] = m;
	}

	public boolean doesBlockExist(Array<Block> blocks) {
		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				if (blocks.contains(getBlock(x, y), true)) return true;
			}
		}

		return false;
	}

	@Override
	public int getWidthInTiles() {
		return sizex;
	}

	@Override
	public int getHeightInTiles() {
		return sizey;
	}

	@Override
	public void pathFinderVisited(int x, int y) {
	}

	@Override
	public boolean blocked(Mover mover, int tx, int ty) {
		return getBlock(tx, ty).isSolid(this, tx, ty);
	}

	@Override
	public float getCost(Mover mover, int sx, int sy, int tx, int ty) {

		return 1;
	}

	public void save(FileHandle file) {
		if (!file.exists()) try {
			file.file().createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		XmlWriter w = new XmlWriter(file.writer(false));

		try {
			XmlWriter writer = w.element("level");

			XmlWriter essentials = writer.element("essentials");

			essentials.attribute("sizex", "" + sizex);
			essentials.attribute("sizey", "" + sizey);
			essentials.attribute("bg", background);
			essentials.attribute("voidTime", "" + voidTime);
			essentials.pop();

			XmlWriter tiles = writer.element("tiles");
			for (int x = 0; x < sizex; x++) {
				for (int y = 0; y < sizey; y++) {
					XmlWriter tile = tiles.element("tile");
					tile.attribute("x", "" + x);
					tile.attribute("y", "" + y);
					tile.attribute("block", Blocks.instance().getKey(getBlock(x, y)));
					tile.attribute("meta", getMeta(x, y) == null ? "" : getMeta(x, y));
					tile.pop();
				}
			}
			tiles.pop();

			writer.pop();
			w.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void load(FileHandle file) {
		if (!file.exists()) {
			Main.logger.warn("level + \"" + file.path() + "\" does not exist!");
			return;
		}

		levelfile = file.nameWithoutExtension();

		prepare();
		entities.clear();
		ParticlePool.instance().getPool().freeAll(particles);

		XmlReader parser = new XmlReader();
		Element root = null;
		try {
			root = parser.parse(file);
		} catch (IOException e2) {
			e2.printStackTrace();
		}
		Element essentials = root.getChildByName("essentials");
		sizex = Integer.parseInt(essentials.getAttribute("sizex"));
		sizey = Integer.parseInt(essentials.getAttribute("sizey"));
		voidTime = Integer.parseInt(essentials.getAttribute("voidTime", "-1"));
		tickTime = -1;
		background = essentials.getAttribute("bg", "levelbgcircuit");

		prepare();

		Array<Element> elements = root.getChildByName("tiles").getChildrenByName("tile");

		for (Element tile : elements) {
			setBlock(Blocks.instance().getBlock(tile.getAttribute("block")),
					Integer.parseInt(tile.getAttribute("x")),
					Integer.parseInt(tile.getAttribute("y")));
			String meta = tile.getAttribute("meta");
			setMeta(meta.equals("") ? null : meta, Integer.parseInt(tile.getAttribute("x")),
					Integer.parseInt(tile.getAttribute("y")));
		}

		entities.clear();

		if (getPlayer() == null) {
			this.addPlayer();
			getPlayer().x = -8;
			getPlayer().y = -8;
		}

		for (int x = 0; x < sizex; x++) {
			for (int y = 0; y < sizey; y++) {
				if (getBlock(x, y) instanceof BlockPlayerSpawner) {
					camera.forceCenterOn((x + 0.5f) * tilesizex, (y + 0.5f) * tilesizey
							- (tilesizey * 3));
				}
			}
		}
		System.gc();
	}

	@Override
	@Deprecated
	public boolean canMoveDirectly(Mover mover, int sx, int sy, int tx, int ty) {
		if (blocked(mover, tx, ty)) return false;
		return true;
	}

	public Array<Entity> getEntities() {
		return entities;
	}

}
