package com.david.mrdav;

import java.util.List;

import com.david.framework.Game;
import com.david.framework.Graphics;
import com.david.framework.Screen;
import com.david.framework.Input.TouchEvent;

public class HelpScreen3 extends Screen {

	public HelpScreen3(Game game) {
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		int len = touchEvents.size();
		
		for (int i =0;i < len; i++){
			TouchEvent event = touchEvents.get(i);
			if(event.type == TouchEvent.TOUCH_UP){
				if(event.x > 256 && event.y >416){
					game.setScreen(new MainMenuScreen(game));
					if(Settings.soundEnabled)
						Assets.click.play(1);
					return;
				}
			
			}
		}
	}

	@Override
	public void present(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawPixmap(Assets.background, 0, 0);
		g.drawPixmap(Assets.help1,64, 100);
		g.drawPixmap(Assets.buttons, 256, 416,0,64,64,64);

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
