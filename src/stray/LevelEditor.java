package stray;

import java.io.File;
import java.util.Iterator;
import java.util.Map.Entry;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import stray.blocks.Block;
import stray.blocks.Blocks;
import stray.entity.Entity;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Array;

public class LevelEditor extends Updateable {

	public LevelEditor(Main m) {
		super(m);
		Iterator it = Blocks.instance().getAllBlocks();
		while (it.hasNext()) {
			blocks.add(((Entry<String, Block>) it.next()).getKey());
		}
		blocks.sort();

		iothread = new Thread() {

			public void run() {
				while (true) {
					try {
						this.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (iothreadtodo != 0) {
						if (iothreadtodo == 1) {
							JFileChooser fileChooser = new JFileChooser();
							if (lastFile != null) {
								fileChooser.setCurrentDirectory(lastFile);
							} else {
								fileChooser.setCurrentDirectory(new File(System
										.getProperty("user.home"), "Desktop"));
							}
							fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							fileChooser.setSelectedFile(new File("a-custom-level.xml"));
							fileChooser.setDialogTitle("Select a directory to save in...");
							int result = fileChooser.showSaveDialog(null);
							if (result == JFileChooser.APPROVE_OPTION) {
								File selectedFile = fileChooser.getSelectedFile();
								lastFile = selectedFile;
								world.save(new FileHandle(selectedFile));
							}
						} else if (iothreadtodo == 2) {
							JFileChooser fileChooser = new JFileChooser();
							if (lastFile != null) {
								fileChooser.setCurrentDirectory(lastFile);
							} else {
								fileChooser.setCurrentDirectory(new File(System
										.getProperty("user.home"), "Desktop"));
							}
							fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
							fileChooser.setDialogTitle("Open an .xml file");
							fileChooser.setFileFilter(new FileNameExtensionFilter(".xml Files",
									"xml"));

							int result = fileChooser.showOpenDialog(null);

							if (result == JFileChooser.APPROVE_OPTION) {
								File selectedFile = fileChooser.getSelectedFile();
								lastFile = selectedFile;
								world.load(new FileHandle(selectedFile));
								if (world.getPlayer() != null) world.camera.forceCenterOn(
										world.getPlayer().x, world.getPlayer().y);
								world.entities.clear();
							}
						}

						iothreadtodo = 0;
					}
				}
			}
		};
		iothread.setDaemon(true);
		iothread.start();
	}

	Array<String> blocks = new Array<String>();

	private File lastFile = null;

	private Thread iothread;
	/**
	 * 0 = nothing, 1 = save, 2 = load
	 */
	private int iothreadtodo = 0;

	int selx = 0;
	int sely = 0;

	int blocksel = 0;
	String defaultmeta = null;

	World world;

	public void resetWorld() {
		if (world == null) {
			world = new World(main);
		}
		world.renderer.showGrid = true;
		world.prepare();
		if(world.getPlayer() != null){
			world.setBlock(Blocks.instance().getBlock("spawnerplayer"), (int) world.getPlayer().x, (int) world.getPlayer().y);
		}
		world.entities.clear();
	}

	@Override
	public void render(float delta) {
		world.renderOnly();

		selx = world.getRoomX(Main.getInputX());
		sely = world.getRoomY(Main.getInputY());

		main.batch.begin();
		main.batch.setColor(1, 1, 1, 0.5f);
		Blocks.instance().getBlock(blocks.get(blocksel)).render(world, selx, sely);
		main.batch.setColor(1, 1, 1, 1);
		main.drawInverse("DEBUG MODE RECOMMENDED - F12", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 5);
		main.drawInverse("ALT+S - SAVE", Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 20);
		main.drawInverse("ALT+O - OPEN", Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 35);
		main.drawInverse("NUMPAD 8462 - change level dimensions (will reset level!)",
				Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 50);
		main.drawInverse("ALT+D - change metadata of selected", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 65);
		main.drawInverse("ALT+SHFT+D - change DEFAULT metadata", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 80);
		main.drawInverse("ALT+T - TEST LEVEL", Settings.DEFAULT_WIDTH - 5,
				Gdx.graphics.getHeight() - 95);
		main.drawInverse("- / + - ADJUST VOID TIME (" + world.voidTime + " s)",
				Settings.DEFAULT_WIDTH - 5, Gdx.graphics.getHeight() - 110);
		main.batch.end();

		world.camera.clamp();
	}

	private void save() {
		for (Entity e : world.entities) {
			if (e == world.getPlayer()) {
				world.entities.removeValue(e, true);
				break;
			}
		}
		if (lastFile != null) {
			world.save(new FileHandle(lastFile));
		} else {
			iothreadtodo = 1;
		}
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
		main.font.draw(main.batch, "selx: " + selx, 5, Main.convertY(starting));
		main.font.draw(main.batch, "sely: " + sely, 5, Main.convertY(starting + 15));
		main.font.draw(main.batch, "block at " + selx + ", " + sely + ": "
				+ Blocks.instance().getKey(world.getBlock(selx, sely)), 5,
				Main.convertY(starting + 30));
		main.font.draw(main.batch, "block selected: " + blocks.get(blocksel), 5,
				Main.convertY(starting + 60));
		main.font.draw(main.batch, "default meta: " + defaultmeta, 5, Main.convertY(starting + 75));
		main.font.draw(main.batch, "block meta: " + world.getMeta(selx, sely), 5,
				Main.convertY(starting + 90));
		main.font.draw(main.batch, "world sizex: " + world.sizex, 5, Main.convertY(starting + 120));
		main.font.draw(main.batch, "world sizey: " + world.sizey, 5, Main.convertY(starting + 135));
		main.font.draw(main.batch, "file location: " + (lastFile == null ? null : lastFile.getName()), 5, Main.convertY(starting + 165));
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
		if (world == null) {
			resetWorld();
		}
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void renderUpdate() {
		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			world.camera.cameray -= (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizey);
			world.camera.clamp();
		} else if ((Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S))
				&& !(Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input
						.isKeyPressed(Keys.ALT_RIGHT))) {
			world.camera.cameray += (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizey);
			world.camera.clamp();
		}
		if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			world.camera.camerax -= (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizex);
			world.camera.clamp();
		} else if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			world.camera.camerax += (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)
					|| Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT) ? 32 : 16)
					* (Gdx.graphics.getDeltaTime() * World.tilesizex);
			world.camera.clamp();
		}

		if (Gdx.input.isButtonPressed(Buttons.LEFT)) {
			world.setBlock(Blocks.instance().getBlock(blocks.get(blocksel)), selx, sely);
			world.setMeta(defaultmeta, selx, sely);
		} else if (Gdx.input.isButtonPressed(Buttons.RIGHT)) {
			world.setBlock(Blocks.instance().getBlock(Blocks.defaultBlock), selx, sely);
			world.setMeta(null, selx, sely);
		} else if (Gdx.input.isButtonPressed(Buttons.MIDDLE)) {
			if (!Blocks.instance().getKey(world.getBlock(selx, sely)).equals(blocks.get(blocksel))) {
				for (int i = 0; i < blocks.size; i++) {
					if (blocks.get(i).equals(Blocks.instance().getKey(world.getBlock(selx, sely)))) {
						blocksel = i;
						break;
					}
				}
			}
		}

		if (Gdx.input.isKeyJustPressed(Keys.ESCAPE)) {
			main.setScreen(Main.MAINMENU);
		}

		if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_8)) {
			world.sizey += 2;
			world.prepare();
			lastFile = null;
		} else if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_2)) {
			if (world.sizey > 12) world.sizey -= 2;
			world.prepare();
			lastFile = null;
		} else if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_4)) {
			if (world.sizex > 20) world.sizex -= 2;
			world.prepare();
			lastFile = null;
		} else if (Gdx.input.isKeyJustPressed(Keys.NUMPAD_6)) {
			world.sizex += 2;
			world.prepare();
			lastFile = null;
		}
		if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
			if (Gdx.input.isKeyJustPressed(Keys.PLUS) || Gdx.input.isKeyJustPressed(Keys.EQUALS)) {
				world.voidTime += 5;
			} else if (Gdx.input.isKeyJustPressed(Keys.MINUS)) {
				world.voidTime -= 5;
				if(world.voidTime < -1) world.voidTime = -1;
			}
		}else{
			if (Gdx.input.isKeyJustPressed(Keys.PLUS) || Gdx.input.isKeyJustPressed(Keys.EQUALS)) {
				world.voidTime++;
			} else if (Gdx.input.isKeyJustPressed(Keys.MINUS)) {
				if (world.voidTime > -1) world.voidTime--;
			}
		}

		if (Gdx.input.isKeyPressed(Keys.ALT_LEFT) || Gdx.input.isKeyPressed(Keys.ALT_RIGHT)) {
			if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT) || Gdx.input.isKeyPressed(Keys.SHIFT_RIGHT)) {
				if (Gdx.input.isKeyJustPressed(Keys.S)) {
					iothreadtodo = 1;
				} else if (Gdx.input.isKeyJustPressed(Keys.D)) {
					String result = (String) JOptionPane.showInputDialog(null,
							"Current default meta: " + defaultmeta, "Editing default meta",
							JOptionPane.PLAIN_MESSAGE, null, null, (defaultmeta == null ? ""
									: defaultmeta));
					if (result != null) {
						defaultmeta = (result.equals("") ? null : result);
					}
				}
			} else {
				if (Gdx.input.isKeyJustPressed(Keys.O)) {
					iothreadtodo = 2;
				} else if (Gdx.input.isKeyJustPressed(Keys.S)) {
					save();
				} else if (Gdx.input.isKeyJustPressed(Keys.D)) {
					int x = selx;
					int y = sely;
					String result = (String) JOptionPane.showInputDialog(null, "Current metadata: "
							+ world.getMeta(x, y),
							"Editing tiledata (" + Blocks.instance().getKey(world.getBlock(x, y))
									+ ") at " + x + ", " + y, JOptionPane.PLAIN_MESSAGE, null,
							null, (world.getMeta(x, y) == null ? "" : world.getMeta(x, y)));
					if (result != null) {
						world.setMeta(result.equals("") ? null : result, x, y);
					}
				} else if (Gdx.input.isKeyJustPressed(Keys.T)) {
					if (lastFile != null) {
						Main.TESTLEVEL.world.load(new FileHandle(lastFile));
						main.setScreen(Main.TESTLEVEL);
					}
				}
			}
		}
	}

	
	public static class EditorGroup{
		
		public static final int TOGGLE = 5;
		public static final int BUTTON = 6;
		public static final int TIMER = 7;
		public static final int SPAWNER = 8;
		
	}
}
