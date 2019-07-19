package com.mycompany.app;

import java.sql.SQLException;

/**
 * Runs the app
 * @author Christoffer
 *
 */
public class App {

	/**
	 * Initiates the classes and runs the app
	 * @param args
	 * @throws SQLException
	 * @throws InterruptedException
	 */
	public static void main( String[] args ) throws SQLException, InterruptedException
	{
		DatabaseHandler db = new DatabaseHandler();
		db.initiateDatabase();
		Game game = new Game();
		db.initiateUser(game);
		Gui gui = new Gui(db);
		App app = new App();
		app.run(gui, game, db);
	}

	/**
	 * Run method for the app
	 * @param gui
	 * @param game
	 * @param db
	 * @throws InterruptedException
	 * @throws SQLException
	 */
	public void run(Gui gui, Game game, DatabaseHandler db) throws InterruptedException, SQLException{
		while(!gui.getPlayGame()) {
			Thread.sleep(100);
		}
		if(db.getPlayerFreeRounds()==0 && db.getPlayerCoins()<10) {
			gui.notEnoughFounds();
		}else {
			game.playNormal(db);
			gui.updateResults(game.getGameId(), game.getWin(), game.getFreeRound(),db);
		}
		gui.setPlayGame(false);
		run(gui, game, db);
	}
}