package com.mycompany.app;

import java.sql.SQLException;
import java.util.Random;

/**
 * Handles the logic of the game
 * @author Christoffer
 *
 */
public class Game {
	private int win;
	private int freeRound;
	private int gameId;

	/**
	 * Runs the game and updates the database with the results
	 * @param db
	 * @throws SQLException
	 */
	public void playNormal(DatabaseHandler db) throws SQLException {
		Random r = new Random();
		float percent = r.nextFloat();
		if(getFreeRound()==1) {
			db.useFreeRound();
		}else {
			db.useCoins(10);
		}
		setWin(0);
		setFreeRound(0);	
		if (percent >= 0.7f) {
			win = 20;
			db.addCoins(win);
		}
		percent = r.nextFloat();
		if (percent >= 0.9f) {
			freeRound = 1;
			db.addFreeRound();
		}
		gameId=db.addGame(win, freeRound);
		setWin(win);
		setFreeRound(freeRound);
		setGameId(gameId);
		return;
	}

	/**
	 * Getters and setters for GameId, Coins and Free rounds won
	 */
	public void setWin(int win){
		this.win=win;
	}

	public int getWin(){
		return win;
	}
	public void setFreeRound(int freeRound){
		this.freeRound=freeRound;
	}

	public int getFreeRound(){
		return freeRound;
	}
	public void setGameId(int gameId){
		this.gameId=gameId;
	}

	public int getGameId(){
		return gameId;
	}

}
