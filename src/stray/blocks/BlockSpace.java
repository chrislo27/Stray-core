package stray.blocks;

import stray.Main;
import stray.util.AssetMap;
import stray.world.World;

import com.badlogic.gdx.graphics.Texture;

public class BlockSpace extends Block {

	public BlockSpace(String path) {
		super(path);
	}

	public void renderPlain(Main main, float camerax, float cameray, int x, int y, int magic) {
		if (animationlink != null) {
			main.batch.draw(main.animations.get(animationlink).getCurrentFrame(), x
					* World.tilesizex - camerax, y * World.tilesizey - cameray, World.tilesizex,
					World.tilesizey);
			return;
		}

		if (usingMissingTex) {
			main.batch.draw(main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class), x
					* World.tilesizex - camerax, y * World.tilesizey - cameray, World.tilesizex,
					World.tilesizey);
			return;
		}

		if (path == null) return;

		if (!connectedTextures) {
			if (!variants) {
				drawAt(main.batch, main.manager.get(sprites.get("defaulttex"), Texture.class), x
						* World.tilesizex - camerax, y * World.tilesizey - cameray);
			} else {
				drawAt(main.batch, main.manager.get(sprites.get("defaulttex"
						+ ((variantNum(magic, x, y)) & (varianttypes - 1))), Texture.class), x
						* World.tilesizex - camerax, y * World.tilesizey - cameray);
			}
		} else {
			drawAt(main.batch, main.manager.get(sprites.get("full"), Texture.class), x
					* World.tilesizex - camerax, y * World.tilesizey - cameray);

		}
	}

}
