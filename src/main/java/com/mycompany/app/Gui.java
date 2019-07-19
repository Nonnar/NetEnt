package com.mycompany.app;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JSpinner;

/**
 * Handles the Gui
 * @author Christoffer
 *
 */
public class Gui {
	private JButton btnAddCoins;
	private JFrame frame;
	private JButton btnPlay;
	private JTextArea textAreaResult;
	private JLabel lblPlayerName;
	private JLabel lblCoins;
	private JLabel lblCostToPlay;
	private JSpinner spinnerFindGame;
	private JButton btnFindGame;
	private String findGame;
	private boolean playGame=false;

	/**
	 * Creates the interface.
	 * @param db 
	 */
	public Gui(DatabaseHandler db) {
		initialize(db);
		frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 * @param db 
	 */
	private void initialize(final DatabaseHandler db) {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		btnPlay = new JButton("Play!");
		btnPlay.setBounds(134, 177, 207, 32);
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPlayGame(true);
			}
		});
		frame.getContentPane().setLayout(null);
		frame.getContentPane().add(btnPlay);

		textAreaResult = new JTextArea();
		textAreaResult.setBounds(134, 80, 207, 86);
		textAreaResult.setLineWrap(true);
		textAreaResult.setText("Results of the game will be \r\ndisplayed here");
		frame.getContentPane().add(textAreaResult);

		lblPlayerName = new JLabel("PlayerName");
		lblPlayerName.setBounds(29, 11, 120, 32);
		frame.getContentPane().add(lblPlayerName);
		lblPlayerName.setText(db.getPlayerName());

		lblCoins = new JLabel("Coins: 0");
		lblCoins.setBounds(311, 14, 66, 27);
		frame.getContentPane().add(lblCoins);
		lblCoins.setText("Coins: "+db.getPlayerCoins());


		lblCostToPlay = new JLabel("10 Coins to play you have 0 free rounds");
		lblCostToPlay.setBounds(127, 220, 317, 31);
		frame.getContentPane().add(lblCostToPlay);
		if(db.getPlayerFreeRounds()>=1) {
			lblCostToPlay.setText("0 Coins to play you have 1 free round");
		}else {
			lblCostToPlay.setText("10 Coins to play you have 0 free rounds");
		}

		spinnerFindGame = new JSpinner();
		spinnerFindGame.setBounds(40, 103, 41, 27);
		frame.getContentPane().add(spinnerFindGame);

		btnFindGame = new JButton("Find game");
		btnFindGame.setBounds(10, 143, 104, 23);
		btnFindGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if((int) spinnerFindGame.getValue()>0) {
					try {
						findGame=db.findGame((int) spinnerFindGame.getValue());
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
					textAreaResult.setText(findGame);
				}
			}
		});
		frame.getContentPane().add(btnFindGame);

		btnAddCoins = new JButton("Add coins");
		btnAddCoins.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					db.addCoins(100);
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				lblCoins.setText("Coins: "+db.getPlayerCoins());
			}
		});
		btnAddCoins.setBounds(298, 46, 104, 23);
		frame.getContentPane().add(btnAddCoins);
	}

	/**
	 * Getter and setter for boolean playGame
	 * @return
	 */
	public boolean getPlayGame(){
		return playGame;
	}

	public void setPlayGame(boolean b){
		this.playGame=b;
	}

	/**
	 * Updates the UI with the latest game result
	 * @param gameId
	 * @param win
	 * @param freeRound
	 * @param db 
	 */
	public void updateResults(int gameId, int win, int freeRound, DatabaseHandler db) {
		textAreaResult.setText("GameID: "+gameId+"\r\nResult:\r\nYou won "+win+" Coins\r\nYou won "+freeRound+" Free rounds");
		lblCoins.setText("Coins: "+db.getPlayerCoins());
		if(freeRound == 1) {
			lblCostToPlay.setText("0 Coins to play you have 1 free round");
		}else {
			lblCostToPlay.setText("10 Coins to play you have 0 free rounds");
		}

	}

	/**
	 * Displays message if the player does not have enough coins or a free round
	 */
	public void notEnoughFounds() {
		textAreaResult.setText("You do not have enough coins\r\nor free rounds to play.\r\nAdd more coins to play");

	}
}
