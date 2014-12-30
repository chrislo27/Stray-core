package stray.blocks.fluid;

import com.badlogic.gdx.graphics.Texture;

import stray.Main;
import stray.blocks.Block;
import stray.util.AssetMap;
import stray.world.World;

public class BlockFluid extends Block {

	public BlockFluid(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		if (usingMissingTex) {
			world.batch.draw(
					world.main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class),
					x,
					y,
					World.tilesizex,
					World.tilesizey,
					0,
					0,
					world.main.manager.get(AssetMap.get("blockmissingtexture"), Texture.class)
							.getWidth(),
					(int) (world.main.manager.get(AssetMap.get("blockmissingtexture"),
							Texture.class).getHeight() / 1f), false, false);
			return;
		}
		if (animationlink != null) {
			world.batch.draw(world.main.animations.get(animationlink).getCurrentFrame(), x
					* world.tilesizex - world.camera.camerax,
					Main.convertY((y * world.tilesizey - world.camera.cameray) + World.tilesizey),
					World.tilesizex, (World.tilesizey / 1f));
			return;
		}
		if (path == null) return;

		if (!connectedTextures) {
			if (!variants) {
				world.batch
						.draw(world.main.manager.get(sprites.get("defaulttex"), Texture.class),
								x,
								y,
								World.tilesizex,
								World.tilesizey,
								0,
								0,
								world.main.manager.get(sprites.get("defaulttex"), Texture.class)
										.getWidth(),
								(int) (world.main.manager.get(sprites.get("defaulttex"),
										Texture.class).getHeight() / 1f), false, false);
			} else {
				world.batch.draw(
						world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class),
						x,
						y,
						World.tilesizex,
						World.tilesizey,
						0,
						0,
						world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class).getWidth(),
						(int) (world.main.manager.get(
								sprites.get("defaulttex"
										+ ((variantNum(world, x, y)) & (varianttypes - 1))),
								Texture.class).getHeight() / 1f), false, false);
			}
		} else {
			drawConnectedTexture(world, x, y,
					world.main.manager.get(sprites.get("corner"), Texture.class),
					world.main.manager.get(sprites.get("full"), Texture.class),
					world.main.manager.get(sprites.get("edgever"), Texture.class),
					world.main.manager.get(sprites.get("edgehor"), Texture.class));
		}
	}

}
