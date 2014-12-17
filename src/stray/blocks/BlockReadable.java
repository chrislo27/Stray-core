package stray.blocks;

import stray.Main;
import stray.Translator;
import stray.conversation.Conversations;
import stray.entity.EntityPlayer;
import stray.world.World;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;

public class BlockReadable extends Block {

	public BlockReadable(String path) {
		super(path);
	}

	@Override
	public void render(World world, int x, int y) {
		super.render(world, x, y);
		if (Block.entityIntersects(world, x, y, world.getPlayer())) {
			world.main.font.setColor(Color.WHITE);
			world.main.drawCentered(Translator.getMsg("block.readable"), x * world.tilesizex
					- world.camera.camerax + (World.tilesizex / 2),
					Main.convertY((y * world.tilesizey - world.camera.cameray) - 15));
			if (Gdx.input.isKeyJustPressed(Keys.UP) || Gdx.input.isKeyJustPressed(Keys.W)) {
				if(world.main.getConv() == null){
					if(world.getMeta(x, y) != null){
						if(Conversations.instance().convs.containsKey(world.getMeta(x, y))){
							onRead(world, x, y);
							world.main.setConv(Conversations.instance().convs.get(world.getMeta(x, y)));
						}
					}
				}
			}
		}
	}
	
	public void onRead(World world, int x, int y){
		
	}

}
