package com.david.mrdav;

import com.david.framework.Screen;
import com.david.framework.impl.AndroidGame;

public class MrDavGame extends AndroidGame {
	
	@Override
	public Screen getStartScreen(){
		return new LoadingScreen(this);
	}

}
