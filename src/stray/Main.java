package stray;

import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import stray.achievements.Achievement;
import stray.achievements.Achievements;
import stray.achievements.Appearance;
import stray.achievements.CompletedAchievements;
import stray.animation.SynchedAnimation;
import stray.blocks.Blocks;
import stray.conversation.Conversation;
import stray.conversation.Conversations;
import stray.suspicion.Offenses;
import stray.transition.Transition;
import stray.transition.TransitionScreen;
import stray.util.AssetMap;
import stray.util.CaptureStream;
import stray.util.CaptureStream.Consumer;
import stray.util.ElectricityRenderer;
import stray.util.Gears;
import stray.util.Logger;
import stray.util.MathHelper;
import stray.util.MemoryUtils;
import stray.util.Shaders;
import stray.util.Splashes;
import stray.util.Utils;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Array;

/**
 * 
 * Main class, think of it like slick's Main class
 *
 */
public class Main extends Game implements Consumer {

	public OrthographicCamera camera;
	public SpriteBatch batch;
	public SpriteBatch maskRenderer;
	public SpriteBatch blueprintrenderer;
	public BitmapFont font;
	public BitmapFont arial;

	private static Color rainbow = new Color();
	private static Color inverseRainbow = new Color();

	CompletedAchievements achievements = new CompletedAchievements();
	Array<Appearance> toShow = new Array<Appearance>();

	Matrix4 normalProjection;

	public static boolean showFPS = true;
	public static boolean debug = false;

	public static final String version = "alpha 1.3";

	public AssetManager manager;

	public static AssetLoadingScreen ASSETLOADING = null;
	public static MainMenuScreen MAINMENU = null;
	public static HelpScreen HELP = null;
	public static GameScreen GAME = null;
	public static TransitionScreen TRANSITION = null;
	public static ColourScreen COLOUR = null;
	public static CutsceneScreen CUTSCENE = null;
	public static MiscLoadingScreen MISCLOADING = null;
	public static LevelSelectScreen LEVELSELECT = null;
	public static LevelEditor LEVELEDITOR = null;
	public static TestLevel TESTLEVEL = null;
	public static NewGame NEWGAME = null;
	public static Backstory BACKSTORY = null;

	public static Texture filltex;

	private Conversation currentConvo = null;

	public ShaderProgram maskshader;
	public ShaderProgram blueprintshader;
	public ShaderProgram toonshader;
	public ShaderProgram greyshader;

	public HashMap<String, SynchedAnimation> animations = new HashMap<String, SynchedAnimation>();
	public HashMap<String, Texture> textures = new HashMap<String, Texture>();

	private CaptureStream output;
	private PrintStream printstrm;
	private JFrame consolewindow;
	private JTextArea consoletext;
	private JScrollPane conscrollPane;

	/**
	 * used for storing progress, level data etc
	 */
	public Preferences progress;

	public static final int TICKS = 30;
	public static final int MAX_FPS = 60;
	private int[] lastFPS = new int[5];
	private static int framesUntilTick = 0;

	public static Gears gears;

	/**
	 * use this rather than Gdx.app.log
	 */
	public static Logger logger;

	public Main(Logger l) {
		super();
		logger = l;
	}

	@Override
	public void create() {
		progress = getPref("progress");
		resetTitle();
		redirectSysOut();

		for (int i = 0; i < lastFPS.length; i++) {
			lastFPS[i] = 0;
		}

		ShaderProgram.pedantic = false;
		camera = new OrthographicCamera();
		camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		batch = new SpriteBatch();
		batch.enableBlending();
		maskRenderer = new SpriteBatch();
		maskRenderer.enableBlending();
		blueprintrenderer = new SpriteBatch();
		manager = new AssetManager();
		font = new BitmapFont(Gdx.files.internal("fonts/couriernewbold.fnt"),
				Gdx.files.internal("fonts/couriernewbold.png"), false);
		font.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		arial = new BitmapFont();
		arial.getRegion().getTexture().setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),
				Gdx.graphics.getHeight());

		Pixmap pix = new Pixmap(1, 1, Format.RGBA8888);
		pix.setColor(Color.WHITE);
		pix.fill();
		filltex = new Texture(pix);
		pix.dispose();

		maskshader = new ShaderProgram(Shaders.VERTBAKE, Shaders.FRAGBAKE);
		maskshader.begin();
		maskshader.setUniformi("u_texture1", 1);
		maskshader.setUniformi("u_mask", 2);
		maskshader.end();
		maskRenderer.setShader(maskshader);

		blueprintshader = new ShaderProgram(Shaders.VERTBAKE, Shaders.FRAGBLUEPRINT);
		blueprintshader.begin();
		blueprintshader.end();
		blueprintrenderer.setShader(blueprintshader);

		toonshader = new ShaderProgram(Shaders.VERTTOON, Shaders.FRAGTOON);

		greyshader = new ShaderProgram(Shaders.VERTGREY, Shaders.FRAGGREY);

		loadUnmanagedAssets();
		loadAssets();

		Gdx.input.setInputProcessor(getDefaultInput());

		prepareStates();

		this.setScreen(ASSETLOADING);

		achievements.load("achievement", getPref("achievements"));

		Thread gcer = new Thread("Stray-the daemon garbage nomer from hell") {

			public void run() {
				final int gctiming = 60000;
				while (true) {
					try {
						this.sleep(50);
						if (showgc) showgc = false;
						this.sleep(gctiming - 50);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					System.gc();
					showgc = true;
				}
			}
		};
		gcer.setPriority(Thread.MIN_PRIORITY);
		gcer.setDaemon(true);
		gcer.start();

	}

	private void prepareStates() {
		ASSETLOADING = new AssetLoadingScreen(this);
		MAINMENU = new MainMenuScreen(this);
		HELP = new HelpScreen(this);
		GAME = new GameScreen(this);
		COLOUR = new ColourScreen(this);
		TRANSITION = new TransitionScreen(this);
		CUTSCENE = new CutsceneScreen(this);
		MISCLOADING = new MiscLoadingScreen(this);
		LEVELSELECT = new LevelSelectScreen(this);
		LEVELEDITOR = new LevelEditor(this);
		TESTLEVEL = new TestLevel(this);
		NEWGAME = new NewGame(this);
		BACKSTORY = new Backstory(this);
	}

	@Override
	public void dispose() {
		batch.dispose();
		manager.dispose();
		font.dispose();
		arial.dispose();
		Blocks.instance().dispose();
		maskshader.dispose();
		blueprintshader.dispose();
		toonshader.dispose();
		maskRenderer.dispose();
		blueprintrenderer.dispose();

		Iterator it = animations.entrySet().iterator();
		while (it.hasNext()) {
			((SynchedAnimation) ((Entry) it.next()).getValue()).dispose();
		}

		it = textures.entrySet().iterator();
		while (it.hasNext()) {
			((Texture) ((Entry) it.next()).getValue()).dispose();
		}

		// dispose screens
		ASSETLOADING.dispose();
		MAINMENU.dispose();
		HELP.dispose();
		GAME.dispose();
		COLOUR.dispose();
		TRANSITION.dispose();
		CUTSCENE.dispose();
		MISCLOADING.dispose();
		LEVELSELECT.dispose();
		LEVELEDITOR.dispose();
		TESTLEVEL.dispose();
		BACKSTORY.dispose();

	}

	/**
	 * sets title of window to "GAMENAME VERSION - RANDOM_SPLASH"
	 */
	public static void resetTitle() {
		Gdx.graphics.setTitle(Translator.getMsg("gamename") + " " + Main.version + " - "
				+ Splashes.getRandomSplash());
	}

	public void awardAchievement(String id) {
		Achievement a = Achievements.instance().achievements
				.get(Achievements.instance().achievementId.get(id));
		if (achievements.getAll().get(a) == false) {
			toShow.add(new Appearance(a));
			achievements.complete(a);
			achievements.save("achievement", getPref("achievements"));

		}
	}
	
	public void redirectSysOut(){
		PrintStream ps = System.out;
		output = new CaptureStream(this, ps);
		printstrm = new PrintStream(output);
		resetConsole();
		System.setOut(printstrm);
	}

	public Conversation getConv() {
		return currentConvo;
	}

	public void resetConsole() {
		consolewindow = new JFrame();
		consolewindow.setTitle("Console for " + Translator.getMsg("gamename") + " " + Main.version);
		consolewindow.setVisible(false);
		consoletext = new JTextArea(40, 60);
		consoletext.setEditable(false);
		conscrollPane = new JScrollPane(consoletext);
		consolewindow.add(conscrollPane, null);
		consolewindow.pack();
	}

	public void resetSystemOut() {
		System.setOut(new PrintStream(new FileOutputStream(FileDescriptor.out)));
	}


	@Override
	public void appendText(final String text) {
		consoletext.append(text);
		consoletext.setCaretPosition(consoletext.getText().length());
	}

	public void setConv(Conversation c) {
		if (currentConvo != null) {
			currentConvo.reset();
		}
		currentConvo = c;
		if (currentConvo != null) {
			currentConvo.talk(this, 1 / 3f);
		}
		currentConvText = "";
	}

	public void transition(Transition from, Transition to, Screen next) {
		TRANSITION.prepare(this.getScreen(), from, to, next);
		setScreen(TRANSITION);
	}

	public static Color getRainbow() {
		return getRainbow(1);
	}

	public static Color getInverseRainbow() {
		return getRainbow(1);
	}

	public static Color getRainbow(float s) {
		return rainbow.set(
				Utils.HSBtoRGBA8888(MathHelper.getNumberFromTime(System.currentTimeMillis(), s),
						1f, 0.75f)).clamp();
	}

	public static Color getInverseRainbow(float s) {
		return inverseRainbow.set(
				Utils.HSBtoRGBA8888(
						1.0f - MathHelper.getNumberFromTime(System.currentTimeMillis(), 1f), 1f,
						0.75f)).clamp();
	}

	public InputMultiplexer getDefaultInput() {
		InputMultiplexer plexer = new InputMultiplexer();
		plexer.addProcessor(new MainInputProcessor(this));
		return plexer;
	}

	private static Random random = new Random();

	public static Random getRandomInst() {
		return random;
	}

	public void fillRect(float x, float y, float width, float height) {
		batch.draw(filltex, x, y, width, height);
	}

	public static String getSpecialChar(String type) {
		if (type.equals("power") || type.equals("electricity")) {
			return Character.toString((char) 221);
		} else if (type.equals("zacharie")) {
			return Character.toString((char) 189);
		}

		return type;
	}

	/**
	 * Directions to use
	 * 
	 * 1) Draw original texture (the base tex) on main batch - this renders the
	 * actual sprite
	 * 
	 * 2) end main batch, start itemRenderer
	 * 
	 * 3) call this method (base tex, mask) - prepares mask
	 * 
	 * 4) re-draw original texture (base tex) - draws the stencil (which uses
	 * base tex)
	 * 
	 * 5) end itemRenderer, begin main batch
	 * 
	 * 
	 * 
	 * @param mask
	 *            mask itself (generally base tex as well)
	 * @param tostencil
	 *            texture to cake on
	 */
	public static void useMask(Texture mask, Texture tostencil) {
		// bind mask to glActiveTexture(GL_TEXTURE2)
		mask.bind(2);

		// bind sprite to glActiveTexture(GL_TEXTURE1)
		tostencil.bind(1);

		// now we need to reset glActiveTexture to zero!!!! since sprite batch
		// does not do this for us
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
	}

	private static final char specialDelimiter = '`';

	/**
	 * call sparingly
	 * 
	 * @param data
	 * @return
	 */
	public static String convertStringToSpecial(String data) {
		boolean found = false;
		String ret = data.toString();
		StringBuilder copy = new StringBuilder();
		char[] arr = data.toCharArray();
		for (int i = 0; i < arr.length; i++) {
			char c = arr[i];
			if (found) { // inside
				if (c == specialDelimiter) {
					ret = ret.replace(
							Character.toString(specialDelimiter) + copy
									+ Character.toString(specialDelimiter),
							getSpecialChar(copy.toString()));
					found = false;
					copy = new StringBuilder();
				} else {
					copy.append(c);
				}
			} else {
				if (c == specialDelimiter) {
					found = true;
				}
			}

		}

		return ret;
	}

	public static int random(int x, int y) {
		return random(x, y, random);
	}

	public static int random(int x, int y, Random rand) {
		if (x == y) {
			return x;
		}
		if (x < 0) {
			return (random(1, (y * 2) + 1) - (y + 1));
		}
		return rand.nextInt((y - x) + 1) + x;
	}
	
	public static float random(float x, float y){
		return random(x, y, random);
	}
	
	public static float random(float x, float y, Random rand) {
		if (x == y) {
			return x;
		}
		return rand.nextFloat() * (x - y) + x;
	}

	/**
	 * converts y-down to y-up
	 * 
	 * @param f
	 *            the num. of px down from the top of the screen
	 * @return the y-down conversion of input
	 */
	public static int convertY(float f) {
		return Math.round(Gdx.graphics.getHeight() - f);
	}

	public void drawInverse(String s, float x, float y) {
		font.draw(batch, s, x - font.getBounds(s).width, y);
	}

	public void drawCentered(String s, float x, float y) {
		font.draw(batch, s, x - (font.getBounds(s).width / 2), y);
	}

	@Override
	public void render() {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearDepthf(1f);
		Gdx.gl.glClear(GL20.GL_DEPTH_BUFFER_BIT);

		try {
			if (getScreen() != null && framesUntilTick >= (MAX_FPS / TICKS)) {
				framesUntilTick = 0;
				((Updateable) getScreen()).tickUpdate();
				tickUpdate();
			}
			if (getScreen() != null) {
				((Updateable) getScreen()).renderUpdate();
			}
			preRender();
			super.render();
			postRender();
		} catch (Exception e) {
			e.printStackTrace();

			Gdx.files.local("crash/").file().mkdir();
			String date = new SimpleDateFormat("yyyy-MM-dd hh-mm-ss").format(new Date());
			date.trim();
			FileHandle handle = Gdx.files.local("crash/crash-log_" + date + ".txt");
			handle.writeString(output.toString(), false);

			consoletext.setText(output.toString());
			resetSystemOut();
			System.out.println("\n\nThe game crashed. There is an error log at " + handle.path()
					+ " ; please send it to the game developer!\n");
			try {
				Thread.sleep(5);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();

			Gdx.app.exit();
			System.exit(1);
		}
		++framesUntilTick;
	}

	public void tickUpdate() {
		if (!(toShow.size == 0)) {
			if (toShow.first().time == Appearance.startingTime) {
				manager.get(AssetMap.get("questcomplete"), Sound.class).play(0.5f);
			}
			if (toShow.first().time > 0) {
				toShow.first().time--;
			} else if (toShow.first().time <= 0 && toShow.first().y <= 0) {
				toShow.removeIndex(0);
			}
		}
	}

	public void renderAchievements() {
		// achievement -- 0 is fully up, 64 is fully down

		if (!(toShow.size == 0)) {
			Appearance ap = toShow.first();
			if (ap.time > 0) {
				if (ap.y < 64) {
					ap.y += 192 * Gdx.graphics.getDeltaTime();
					if (ap.y > 64) ap.y = 64;
				}
			} else { // retract
				if (ap.y > 0) {
					ap.y -= 192 * Gdx.graphics.getDeltaTime();
					if (ap.y < 0) ap.y = 0;
				}
			}
			if (ap.a.special) {
				batch.setColor(Main.getInverseRainbow());
			}
			batch.draw(manager.get(AssetMap.get("achievementui"), Texture.class),
					Gdx.graphics.getWidth() - 256, Main.convertY(ap.y));
			font.setColor(Main.getRainbow());
			drawCentered(Translator.getMsg("achievementget"), Gdx.graphics.getWidth() - 128,
					Main.convertY(ap.y - 60));
			font.setColor(Color.WHITE);
			font.setScale(0.75f);
			font.drawWrapped(batch, Translator.getMsg("achievement." + ap.a.data + ".name") + " - "
					+ Translator.getMsg("achievement." + ap.a.data + ".desc"),
					Gdx.graphics.getWidth() - 250, Main.convertY(ap.y - 45), 246);
			font.setScale(1f);
			batch.setColor(Color.WHITE);
		}
	}

	private void preRender() {
		camera.update();
		gears.update(1);
		if (Gdx.input.isKeyJustPressed(Keys.F12)) {
			debug = !debug;
		}
		if (debug) { // console things -> alt + key
			if (((Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)))) {
				if (Gdx.input.isKeyJustPressed(Keys.C)) {
					if (consolewindow.isVisible()) {
						consolewindow.setVisible(false);
					} else {
						consolewindow.setVisible(true);
						conscrollPane.getVerticalScrollBar().setValue(
								conscrollPane.getVerticalScrollBar().getMaximum());
					}
				} else if (Gdx.input.isKeyJustPressed(Keys.Q)) {
					throw new GameException(
							"This is a forced crash caused by pressing ALT+Q while in debug mode.");
				} else if (Gdx.input.isKeyJustPressed(Keys.L)) {
					LEVELEDITOR.resetWorld();
					setScreen(LEVELEDITOR);
				} else if (Gdx.input.isKeyJustPressed(Keys.A)) {
					toShow.add(new Appearance(Achievements.instance().achievements.get(Achievements
							.instance().achievementId.get("test"))));
				} else if (Gdx.input.isKeyJustPressed(Keys.G)) {
					gears.reset();
				}

			}
		}
	}

	public String currentConvText = "";
	public static final int convTextIncrement = 2;

	private int totalavgFPS = 0;
	private float fpstimer = 0;

	public float getAvgFPS() {
		totalavgFPS = 0;
		for (int i = 0; i < lastFPS.length; i++) {
			totalavgFPS += lastFPS[i];
		}

		return ((totalavgFPS) / (lastFPS.length * 1f));
	}
	
	public void drawTextBg(String text, float x, float y){
		batch.setColor(0, 0, 0, 0.6f);
		fillRect(x, y, font.getBounds(text).width + 2, 17);
		font.draw(batch, text, x + 1, y + 15);
		batch.setColor(1, 1, 1, 1);
	}

	private void postRender() {
		batch.begin();

		font.setColor(Color.WHITE);

		font.draw(
				batch,
				"FPS: "
						+ Gdx.graphics.getFramesPerSecond()
						+ (Main.debug ? " (avg of " + lastFPS.length + " sec: "
								+ String.format("%.1f", getAvgFPS()) + ") "
								+ Arrays.toString(lastFPS) : ""), 5, Gdx.graphics.getHeight() - 5);

		renderAchievements();

		if (currentConvo != null) {
			batch.setColor(0, 0, 0, 0.5f);
			fillRect(0, 0, Gdx.graphics.getWidth(), 128);
			batch.setColor(Color.LIGHT_GRAY);
			int width = 3;
			fillRect(0, 0, width, 128);
			fillRect(0, 126, Gdx.graphics.getWidth(), width);
			fillRect(0, 0, Gdx.graphics.getWidth(), width);
			fillRect(Gdx.graphics.getWidth() - width, 0, width, 128);

			font.setColor(Color.WHITE);
			if (currentConvo.getCurrent().speaker != null) font.draw(batch,
					Translator.getMsg("conv.name." + currentConvo.getCurrent().speaker) + ": ", 10,
					120);
			font.drawWrapped(batch, currentConvText, 10, 100, Gdx.graphics.getWidth() - 20);
			drawInverse(Translator.getMsg("conversation.next"), Gdx.graphics.getWidth() - 8, 20);
			batch.setColor(Color.WHITE);
			if (!currentConvText.equals(Translator.getMsg(currentConvo.getCurrent().text))) {
				currentConvText = Translator.getMsg(currentConvo.getCurrent().text).substring(
						0,
						MathUtils.clamp(currentConvText.length() + convTextIncrement, 0, Translator
								.getMsg(currentConvo.getCurrent().text).length()));
			}
		}

		if (this.getScreen() != null) {
			if (debug) ((Updateable) this.getScreen()).renderDebug(this.renderDebug());
		}
		batch.end();

		fpstimer += Gdx.graphics.getDeltaTime();
		if (fpstimer >= 1) {
			fpstimer--;
			int[] temp = lastFPS.clone();
			for (int i = 1; i < lastFPS.length; i++) {
				lastFPS[i] = temp[i - 1];
			}
			lastFPS[0] = Gdx.graphics.getFramesPerSecond();
		}
	}

	public int getMostMemory = MemoryUtils.getUsedMemory();

	private boolean showgc = false;

	private int renderDebug() {
		int offset = 0;
		if (getScreen() != null) offset = ((Updateable) getScreen()).getDebugOffset();
		if (MemoryUtils.getUsedMemory() > getMostMemory) getMostMemory = MemoryUtils
				.getUsedMemory();
		font.setColor(Color.WHITE);
		font.draw(batch, "version: " + Main.version, 5, Main.convertY(30 + offset));
		font.draw(batch, "Memory: "
				+ NumberFormat.getInstance().format(MemoryUtils.getUsedMemory()) + " KB / "
				+ NumberFormat.getInstance().format(MemoryUtils.getMaxMemory()) + " KB (max "
				+ NumberFormat.getInstance().format(getMostMemory) + " KB) "
				+ (showgc ? "gc'd" : ""), 5, Main.convertY(45 + offset));
		font.draw(batch, "Available cores: " + MemoryUtils.getCores(), 5,
				Main.convertY(60 + offset));
		font.draw(batch, "OS: " + System.getProperty("os.name"), 5, Main.convertY(75 + offset));
		if (getScreen() != null) {
			font.draw(batch, "state: " + getScreen().getClass().getSimpleName(), 5,
					Main.convertY(90 + offset));
		} else {
			font.draw(batch, "state: null", 5, Main.convertY(90 + offset));
		}

		return 75 + 30 + offset;
	}

	public static Preferences getPref(String ref) {
		return Gdx.app.getPreferences("stray-" + ref);
	}

	public void setClearColor(int r, int g, int b) {
		Gdx.gl20.glClearColor(r / 255f, g / 255f, b / 255f, 1f);
	}

	

	private void loadAssets() {
		AssetMap.instance(); // load asset map namer thing
		Achievements.instance();
		Translator.instance();
		Conversations.instance();
		Offenses.instance();
		Blocks.instance().addBlockTextures(this);
		manager.load(AssetMap.add("blockmissingtexture", "images/blocks/missing/missing.png"),
				Texture.class);

		// missing
		manager.load(AssetMap.add("missingtexture", "images/missing.png"), Texture.class);

		// forme ui
		manager.load(AssetMap.add("formeuiturtle", "images/formeui/turtle.png"), Texture.class);
		manager.load(AssetMap.add("formeuiguillotine", "images/formeui/guillotine.png"), Texture.class);
		manager.load(AssetMap.add("formeuihoop", "images/formeui/hoop.png"), Texture.class);
		
		// ui
		manager.load(AssetMap.add("spacekraken", "images/ui/misc.png"), Texture.class);
		manager.load(AssetMap.add("guilanguage", "images/ui/button/language.png"), Texture.class);
		manager.load(AssetMap.add("guibg", "images/ui/button/bg.png"), Texture.class);
		manager.load(AssetMap.add("guiexit", "images/ui/button/exit.png"), Texture.class);
		manager.load(AssetMap.add("guibgfalse", "images/ui/button/bgfalse.png"), Texture.class);
		manager.load(AssetMap.add("guibgtrue", "images/ui/button/bgtrue.png"), Texture.class);

		// particle
		manager.load(AssetMap.add("money", "images/particle/money.png"), Texture.class);
		manager.load(AssetMap.add("checkpoint", "images/particle/checkpoint.png"), Texture.class);
		manager.load(AssetMap.add("poof", "images/particle/poof.png"), Texture.class);
		manager.load(AssetMap.add("sparkle", "images/particle/sparkle.png"), Texture.class);
		manager.load(AssetMap.add("arrowup", "images/particle/arrow/up.png"), Texture.class);
		manager.load(AssetMap.add("arrowdown", "images/particle/arrow/down.png"), Texture.class);
		manager.load(AssetMap.add("arrowleft", "images/particle/arrow/left.png"), Texture.class);
		manager.load(AssetMap.add("arrowright", "images/particle/arrow/right.png"), Texture.class);
		manager.load(AssetMap.add("arrowcentre", "images/particle/arrow/centre.png"), Texture.class);
		manager.load(AssetMap.add("lasercube", "images/particle/lasercube.png"), Texture.class);
		manager.load(AssetMap.add("particlepower", "images/particle/power.png"), Texture.class);
		manager.load(AssetMap.add("magnetglow", "images/particle/magnetglow.png"), Texture.class);
		manager.load(AssetMap.add("airwhoosh", "images/particle/airwhoosh.png"), Texture.class);
		manager.load(AssetMap.add("particlecircle", "images/particle/circle.png"), Texture.class);

		// cutscene
		manager.load("images/cutscene/stunning.png", Texture.class);
		manager.load("images/cutscene/stunning2.png", Texture.class);
		manager.load("images/cutscene/controls0.png", Texture.class);
		manager.load("images/cutscene/controls1.png", Texture.class);
		manager.load("images/cutscene/controls2.png", Texture.class);
		manager.load("images/cutscene/rep.png", Texture.class);

		// effects
		manager.load(AssetMap.add("effecticonblindness", "images/ui/effect/icon/blindness.png"),
				Texture.class);
		manager.load(AssetMap.add("effectoverlayblindness", "images/ui/effect/blindness.png"),
				Texture.class);

		// entities
		manager.load(AssetMap.add("player", "images/entity/player/player.png"), Texture.class);
		manager.load(AssetMap.add("smallasteroid", "images/entity/smallasteroid.png"),
				Texture.class);
		manager.load(AssetMap.add("entityzaborinox", "images/entity/zaborinox.png"), Texture.class);

		// misc
		manager.load(AssetMap.add("vignette", "images/ui/vignette.png"), Texture.class);
		manager.load(AssetMap.add("entityshield", "images/entity/shield.png"), Texture.class);
		manager.load(AssetMap.add("levelselectbg", "images/levelselectbg.png"), Texture.class);
		manager.load(AssetMap.add("levelselectdot", "images/levelselectdot.png"), Texture.class);
		manager.load(AssetMap.add("levelselected", "images/levelselected.png"), Texture.class);
		manager.load(AssetMap.add("glintsquare", "images/item/glintsquare.png"), Texture.class);

		// level backgrounds
		manager.load(AssetMap.add("levelbgcity", "images/levelbg/city.png"), Texture.class);
		manager.load(AssetMap.add("levelbgcircuit", "images/levelbg/circuit.png"), Texture.class);

		// colour
		manager.load(AssetMap.add("colourblue", "images/colour/blue.png"), Texture.class);
		manager.load(AssetMap.add("colourred", "images/colour/red.png"), Texture.class);
		manager.load(AssetMap.add("colourorange", "images/colour/orange.png"), Texture.class);
		manager.load(AssetMap.add("colourgreen", "images/colour/green.png"), Texture.class);
		manager.load(AssetMap.add("colourpurple", "images/colour/purple.png"), Texture.class);
		manager.load(AssetMap.add("colouryellow", "images/colour/yellow.png"), Texture.class);
		manager.load(AssetMap.add("colourmetal", "images/colour/metal_back.jpg"), Texture.class);
		manager.load(AssetMap.add("colourpointer1", "images/colour/pointer1.png"), Texture.class);
		manager.load(AssetMap.add("colourpointer2", "images/colour/pointer2.png"), Texture.class);
		manager.load(AssetMap.add("colournuclear", "images/colour/radioactiveFull.png"),
				Texture.class);

		// sfx
		manager.load(AssetMap.add("questcomplete", "sounds/questcomplete.ogg"), Sound.class);
		manager.load(AssetMap.add("switchsfx", "sounds/switch.ogg"), Sound.class);
		
		// voice (assetmap -> "voice-<name>")
		//manager.load(AssetMap.add("voice-child0", "sounds/voice/child/child0.ogg"), Sound.class);
		//manager.load(AssetMap.add("voice-child1", "sounds/voice/child/child1.ogg"), Sound.class);
		//manager.load(AssetMap.add("voice-child2", "sounds/voice/child/child2.ogg"), Sound.class);

		// colour
		manager.load(AssetMap.add("colour200pts", "sounds/colour/200pts.ogg"), Sound.class);
		manager.load(AssetMap.add("colourswap", "sounds/colour/apocalypseSwap.ogg"), Sound.class);
		manager.load(AssetMap.add("colourcoverup", "sounds/colour/coverUpAndLand.ogg"), Sound.class);
		manager.load(AssetMap.add("colourincorrect", "sounds/colour/incorrect.ogg"), Sound.class);

		// music
	}

	private void loadUnmanagedAssets() {
		// misc

		// unmanaged textures
		textures.put("gear", new Texture("images/gear.png"));
		gears = new Gears(this);

		// animations
		animations.put("shine", new SynchedAnimation(0.1f, 20, "images/item/shine/shine", ".png",
				false));
		animations.put("portal", new SynchedAnimation(0.05f, 32, "images/blocks/portal/portal",
				".png", true).setRegionTile(32, 32));

		// load animations
		Iterator it = animations.entrySet().iterator();
		while (it.hasNext()) {
			((SynchedAnimation) ((Entry) it.next()).getValue()).load();
		}

	}

	public Texture getCurrentShine() {
		return animations.get("shine").getCurrentFrame().getTexture();
	}

}
