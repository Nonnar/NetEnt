package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Handles the database connections and SQL queries
 * @author Christoffer
 *
 */
public class DatabaseHandler {
	private Connection conn;
	private Statement s;
	private ResultSet rs;
	private String findGame;
	private String playerName;
	private int playerCoins;
	private int playerFreeRounds;

	/**
	 * Establishes a connection to the database
	 * @throws SQLException
	 */
	private void connect() throws SQLException {
		conn = null;
		try {
			Class.forName("org.sqlite.JDBC");
			conn = DriverManager.getConnection("jdbc:sqlite:app.db");
		} catch (Exception e) {
			System.err.println("Cannot connect to database server");
			System.exit(1);
		}
	}

	/**
	 * Initiate database if it doesn't exist
	 * @throws SQLException
	 */
	public void initiateDatabase() throws SQLException {
		//System.out.println("Start Initiate database");
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Players (PlayerID INTEGER PRIMARY KEY AUTOINCREMENT, PlayerName TEXT NOT NULL, Coins INTEGER, FreeRounds INTEGER); ");
			s.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("Cannot create Players databse");
			System.exit(1);
		}
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("INSERT INTO Players(PlayerName, Coins, FreeRounds) SELECT 'Player', "+100+", "+0+" WHERE NOT EXISTS (SELECT * FROM Players); ");
			s.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("Cannot update Players databse");
			System.exit(1);
		}
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("CREATE TABLE IF NOT EXISTS Games (GameID INTEGER PRIMARY KEY AUTOINCREMENT, CoinWins INTEGER, FreeRound INTEGER); "); 
			s.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("Cannot create Games databse");
			System.exit(1);
		}
	}

	/**
	 * Add user to database
	 * @param playerName
	 * @throws SQLException
	 */
	public void addUser(String playerName) throws SQLException {
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("INSERT INTO Players(PlayerName, Coins, FreeRounds) SELECT '"+playerName+"', "+0+", "+0+"; ");
			s.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("Cannot uppdate Players database");
			System.exit(1);
		}
	}

	/**
	 * Add game to database
	 * @param freeRound 
	 * @param win 
	 * @return 
	 * @throws SQLException
	 */
	public int addGame(int win, int freeRound) throws SQLException {
		int gameId = 0;
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("INSERT INTO Games(CoinWins, FreeRound) SELECT "+win+", "+freeRound+";");
			s.close();
			conn.close();
		} catch (Exception e) {
			System.err.println("Cannot add game to database server");
			System.exit(1);
		}
		try {
			connect();
			s = conn.createStatement();
			rs = s.executeQuery("SELECT GameID FROM Games ORDER BY GameID DESC;");
			gameId=rs.getInt("GameID");
			s.close();
			rs.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot find GameID in database server");
			System.exit(1);
		}
		return gameId;
	}

	/**
	 * Returns the result of game id
	 * @param gameId
	 * @return
	 * @throws SQLException
	 */
	public String findGame(int gameId) throws SQLException {
		findGame="";
		try {
			connect();
			s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM Games WHERE GameID="+gameId);
			findGame="Found game\r\nGame id: "+rs.getInt("GameID")+"\r\nCoins won: "+rs.getInt("CoinWins")+"\r\nFree rounds won: "+rs.getInt("FreeRound");
			s.close();
			rs.close();
			conn.close();
		}
		catch (Exception e) {
			findGame="Cannot find game id";
		}
		return findGame;
	}

	/**
	 * Getters and setters for player name, free rounds and current coins
	 */
	public String getPlayerName(){
		return playerName;
	}
	public void setPlayerName(String playerName){
		this.playerName=playerName;
	}

	public int getPlayerCoins(){
		return playerCoins;
	}
	public void setPlayerCoins(int i){
		this.playerCoins=i;
	}
	public int getPlayerFreeRounds() {
		return playerFreeRounds;
	}
	private void setPlayerFreeRound(int playerFreeRounds) {
		this.playerFreeRounds=playerFreeRounds;	
	}

	/**
	 * Load the user to display name, coins and free rounds
	 * @param game
	 * @throws SQLException
	 */
	public void initiateUser(Game game) throws SQLException {
		try {
			connect();
			s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM Players WHERE PlayerID="+1);
			setPlayerName(rs.getString(2));
			setPlayerCoins(rs.getInt(3));
			game.setFreeRound(rs.getInt(4));
			setPlayerFreeRound(rs.getInt(4));
			s.close();
			rs.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot connect to database server");
			System.exit(1);
		}
	}

	/**
	 * Adds a free round to the database
	 * @throws SQLException
	 */
	public void addFreeRound() throws SQLException {
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("UPDATE Players SET FreeRounds=FreeRounds+1 WHERE PlayerID=1");
			s.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot add a free round to database server");
			System.exit(1);
		}
	}

	/**
	 * Removes a free round from the database
	 * @throws SQLException
	 */
	public void useFreeRound() throws SQLException {
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("UPDATE Players SET FreeRounds=FreeRounds-1 WHERE PlayerID=1");
			s.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot connect to database server");
			System.exit(1);
		}
	}

	/**
	 * Adds coins to the database
	 * @param coins
	 * @throws SQLException
	 */
	public void addCoins(int coins) throws SQLException {
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("UPDATE Players SET Coins=Coins+"+coins+" WHERE PlayerID=1");
			s.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot add coins to database server");
			System.exit(1);
		}
		try {
			connect();
			s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM Players WHERE PlayerID="+1);
			setPlayerCoins(rs.getInt(3));
			s.close();
			rs.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot find coins in database server");
			System.exit(1);
		}
	}

	/**
	 * Removes coins from the database
	 * @param coins
	 * @throws SQLException
	 */
	public void useCoins(int coins) throws SQLException {
		try {
			connect();
			s = conn.createStatement();
			s.executeUpdate("UPDATE Players SET Coins=Coins-"+coins+" WHERE PlayerID=1");
			s.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot remove coins from database server");
			System.exit(1);
		}
		try {
			connect();
			s = conn.createStatement();
			rs = s.executeQuery("SELECT * FROM Players WHERE PlayerID="+1);
			setPlayerCoins(rs.getInt(3));
			s.close();
			rs.close();
			conn.close();
		}
		catch (Exception e) {
			System.err.println("Cannot find coins in database server");
			System.exit(1);
		}
	}
}
