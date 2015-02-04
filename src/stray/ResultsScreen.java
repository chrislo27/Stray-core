package stray;

import stray.ui.LevelSelectButton;
import stray.ui.NextLevelButton;
import stray.ui.RetryLevelButton;
import stray.util.Utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

public class ResultsScreen extends Updateable {

	public ResultsScreen(Main m) {
		super(m);
		
		container.elements.add(new LevelSelectButton(Gdx.graphics.getWidth() / 2 - 24 - 8 - 48, Gdx.graphics.getHeight() / 4){

			@Override
			public boolean onLeftClick() {
				main.setScreen(Main.LEVELSELECT);
				
				return true;
			}
			
		});
		
		container.elements.add(new RetryLevelButton(Gdx.graphics.getWidth() / 2 - 24, Gdx.graphics.getHeight() / 4){

			@Override
			public boolean onLeftClick() {
				Main.LEVELSELECT.goToLevel(levelname);
				
				return true;
			}
			
		});
		
		container.elements.add(new NextLevelButton(Gdx.graphics.getWidth() / 2 - 24 + 8 + 48, Gdx.graphics.getHeight() / 4){

			@Override
			public boolean onLeftClick() {
				Main.LEVELSELECT.goToLevel(Math.round(Main.LEVELSELECT.wanted));
				
				return true;
			}
			
		});
		
	}

	private String levelfile = null;
	private int levelname = 0;
	private boolean voidPresent = false;
	private int resultsPick = MathUtils.random(0, 3);
	private int deaths = 0;

	public ResultsScreen setData(String levelf, int levelid, boolean voidChasing, int deaths) {
		levelfile = levelf;
		levelname = levelid;
		voidPresent = voidChasing;
		this.deaths = deaths;
		
		int old = resultsPick;
		while(resultsPick != old){
			resultsPick = MathUtils.random(0, 3);
		}
		
		return this;
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		main.batch.begin();
		if (levelfile != null) {
			main.font.setColor(1, 1, 1, 1);
			main.font.setScale(2);
			main.drawCentered(
					Levels.getLevelName(levelname),
					Gdx.graphics.getWidth() / 2, Main.convertY(225));
			main.font.setScale(1);
			main.drawCentered(
					Translator.getMsg("menu.results.latesttime")
							+ Utils.formatMs(main.progress.getLong(levelfile + "-latesttime")),
					Gdx.graphics.getWidth() / 2, Main.convertY(275));
			main.drawCentered(
					Translator.getMsg("menu.results.besttime")
							+ Utils.formatMs(main.progress.getLong(levelfile + "-latesttime")),
					Gdx.graphics.getWidth() / 2, Main.convertY(300));
			main.drawCentered(
					Translator.getMsg("menu.results.deaths")
							+ deaths,
					Gdx.graphics.getWidth() / 2, Main.convertY(325));
			
			main.drawCentered(
					Translator.getMsg("menu.results.verdict"),
					Gdx.graphics.getWidth() / 2, Main.convertY(380));
			if(voidPresent){
				if(main.progress.getLong(levelfile + "-latesttime") <= Levels.instance().levels.get(levelname).bestTime){
					main.drawCentered(
							"\"" + Translator.getMsg("menu.results.good" + resultsPick) + "\"",
							Gdx.graphics.getWidth() / 2, Main.convertY(400));
				}else{
					main.drawCentered(
							"\"" + Translator.getMsg("menu.results.bad" + resultsPick) + "\"",
							Gdx.graphics.getWidth() / 2, Main.convertY(400));
				}
			}else{
				main.drawCentered(
						"\"" + Translator.getMsg("menu.results.good" + resultsPick) + "\"",
						Gdx.graphics.getWidth() / 2, Main.convertY(400));
			}
		}
		
		container.render(main);
		main.batch.end();
	}

	@Override
	public void renderUpdate() {
	}

	@Override
	public void tickUpdate() {
	}

	@Override
	public void renderDebug(int starting) {
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void show() {
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

}
