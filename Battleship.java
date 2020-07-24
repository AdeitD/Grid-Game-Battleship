/**
 * File: Battleship.java
 * Author: Adeit Dalal
 * Date: June 14, 2018
 * Description: This program is an interective and 
 * exciting grid-based GUI game that will be used
 * as exemplars to interest players into taking the
 * java programming course offered by Robo-Geek.
 * 
 * Note: The Music Volume feature was planned on being added, however, due to time restrictions the final version was not
 * able to be implemented without a massive amount of errors. Thus, you can see the intended music volume, but cannot
 * change the actual music level. Another thing that should've been fixed was my replay, which works, but resizes all of my screens
 * weirdly. Due to time restraints, I was also unable to finish being able to tell the user that a ship was 'sunk' in time.
 * 
 * Also, the theme in this game was slightly hard to show, but, the best way to emphasize it was to simply add Pirates of the 
 * Caribbean music and have Jack Sparrow giving out all the info from JOptionPanes.
 * 
 * Photos Source:
 * https://www.amazon.co.uk/ACTIVISION-Battleship-Nintendo-3DS/dp/B007EMRBSQ
 * http://clickwallpapers.com/pirate-ship-battle-wallpaper-hd-resolution/
 * https://www.gamingonlinux.com/uploads/articles/tagline_images/1279157998id7908gol.jpg
 */


import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.applet.Applet;
import javax.sound.sampled.*;


public class Battleship extends Applet implements ActionListener {
	public Battleship() {
	}
	int boardP[][] = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
	int opBoard[][] = {{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, {1, 1, 1, 1, 1, 1, 1, 1, 1, 1}, 
			{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}};
	//All the arrays for grids, which will basically be updated whenever a boat is placed down or there are hits/misses.


	int row = 10;
	int col = 10;
	int boatsPlaced = 0;
	
	int gameStage = 1;
	//Used to allow for buttons on different grids to have the same action commands, by first checking the 
	//game stage and then checking for action commands.
	int pHit = 17;
	int pMiss = 0;
	int eHit = 0;
	//Stats for the hit/miss within the game, used to calculate final score and who's won.
	
	char alignment = 'v';
	char gameMode;  
	
	int volChange = 100;
	int highScore = 0;
	int hardAccuracy = 3;
	//Declaration of all global variables. These global variables were chosen becuase they were required to change across multiple
	//methods and the method it was called it was called in actionPerformed, where declaring up ints and chars would not allow them to save.
	
	JSlider slider;
	JLabel shipDirec, pUpdate, eUpdate, pHitMiss, highScore1;
	JLabel j1[]; 
	JLabel j2[];
	CardLayout cdLayout = new CardLayout ();
	Panel scrAll, mainS, instruc, modeS, placeB, confirm, gameSc1, gameSc2;
	JButton donePlace, rotate, reset, nextP, playHard, switchTurn, ready;
	JButton b1[], o1[];
	//Declaration of widget global variables that used mutators in actionPerformed or are part of the CardLayout series of panels.

	int hardModeUnlock = 0;
	//Set the value of this integer to 3000 in order to automatically unlock Hard Mode!
	JProgressBar prog;
	
	public void init () {  
		setBackground (new Color (77, 32, 224));
		setSize (710, 710);
		//Sets the background and size of the entire applet

		playFile ("Pirates of The Caribbean Theme - 1 hour version");
		//Plays the music
		
		scrAll = new Panel();
		scrAll.setLayout (cdLayout);
		scrAll.setSize(610, 610);

		mainS();
		instructions();
		modes();
		placeBoats();
		confirm();
		gameScr1();
		gameScr2();
		
		ImageIcon jackS = createImageIcon ("jackSparrow.png");
		JOptionPane.showMessageDialog(null, "Ahoy there! It seems my crew and I have been cornered by members of \nthe East India Trading Company! \nHelp us and we'll give you gold for your work!", "Jack Sparrow", 
				JOptionPane.INFORMATION_MESSAGE, jackS);

		add ("Center", scrAll);
		//Sets up the cardlayout and calls each of the methods that hold a card.

	}

	public void mainS () {
		mainS = new Panel ();
		mainS.setSize(610, 610);
		mainS.setLayout(null);
		//Creates the panel, sets the layout to Absolute so that each widget can be stacked and the background image can be at the very bottom.

		JButton gameModes = new JButton("GAME MODES");
		gameModes.setForeground(Color.CYAN);
		gameModes.setBackground(new Color (51, 65, 133));
		gameModes.setFont(new Font ("Arial", Font.BOLD, 20));
		gameModes.addActionListener(this);
		gameModes.setActionCommand("mode");
		gameModes.setBounds(110, 390, 185, 49);

		JButton instructions = new JButton("INSTRUCTIONS");
		instructions.setForeground(Color.BLUE);
		instructions.setBackground(new Color (39, 124, 185));
		instructions.setFont(new Font ("Arial", Font.BOLD, 20));
		instructions.addActionListener(this);
		instructions.setActionCommand("instruc");
		instructions.setBounds(340, 390, 185, 49);

		JButton quit = new JButton(createImageIcon("quit.jpg"));
		quit.addActionListener(this);
		quit.setActionCommand("endGame");
		quit.setBounds(260, 460, 100, 100);

		JLabel musicVol = new JLabel("MUSIC VOLUME: " + volChange);
		musicVol.setForeground(new Color(102, 0, 102));
		musicVol.setBounds(435, 0, 137, 20);
		
		//Sets up the screen's JButtons and JLabels, including their bounds.

		slider = new JSlider();
		slider.setValue(volChange);
		slider.setMinimum(0);
		slider.setMaximum(100);
		slider.setBounds(380, 25, 200, 26);
		//Sets the highest and lowest possible values within the slider's reach, then sets the coordinate position of the slider.
		//This slider would be used by the player to lower/raise music volume.

		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		//Adds spacing on the slider that allows the user to more easily see base points for different volumes are.

		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				volChange = slider.getValue();
				musicVol.setText ("MUSIC VOLUME: " + volChange);
			}
		});
		//Adds a changeable actionlistener to the slider that shows the current music volume to the user when the slider's position is changed.
		
		mainS.add(gameModes);
		mainS.add(instructions);
		mainS.add(quit); 
		mainS.add(musicVol);
		mainS.add(slider);
		
		JLabel backGround = new JLabel (createImageIcon ("battleship0.jpg"));
		backGround.setBounds(0, 0, 610, 610);
		mainS.add(backGround);
		//Adds a background image to the screen.
		
		scrAll.add("1", mainS);
	}

	public void instructions () {
		instruc = new Panel ();
		instruc.setLayout(null);
		instruc.setSize(610,610);
		
		JLabel instrucs = new JLabel ("Instructions");
		instrucs.setBounds(207, 16, 200, 80);
		instrucs.setFont(new Font ("Ravie", Font.BOLD, 20));
		instrucs.setForeground(new Color (39, 124, 185));

		JButton setup = new JButton ("How to Setup");
		setup.setBounds(98, 132, 150, 61);
		setup.setActionCommand("noCheck");
		
		JButton goSetup = new JButton ("Choose Game Mode!");
		goSetup.setBounds (313, 464, 150, 29);
		goSetup.addActionListener(this);
		goSetup.setActionCommand("mode");

		JButton play = new JButton ("How to Play");
		play.setBounds(318, 132, 134, 61);
		play.setActionCommand("noCheck");

		JTextArea descrip = new JTextArea ("");
		descrip.setBounds(56, 225, 491, 200);

		JButton back = new JButton ("Back");
		back.setBounds(203, 464, 100, 29);
		back.addActionListener(this);
		back.setActionCommand("backToMain");

		setup.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean rollover = setup.getModel().isRollover();
				if (rollover) {
					descrip.setText("");
					descrip.append("1. Ships can be placed in a horizontal or vertical position anywhere within the grid.\r\n" + 
							"2. No part of the ship can be out of the grid, nor can they overlap in any \nposition with another ship. \nThey can be next to each other, as long as their grid positions do not overlap.\r\n" + 
							"3. You may not place ships diagonally within the grid.\r\n" + 
							"4. Ship placement is permanent and cannot be changed once the game has started.");
				}
			}
		});
		//Adds a changelistener that appends the setup rules to the JTextArea when the button is hovered over (MouseOver)r.
		
		String shipName[] = {"Aircraft Carrier - 5 Pieces", "Batleship - 4 Pieces", " Cruiser - 3 Pieces", "Submarine - 3 Pieces", "Destroyer - 2 Pieces"};
		//Declares a string array to hold the string names and length of all of the ships.
		
		play.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				boolean rollover = play.getModel().isRollover();
				if (rollover) {
					//Adds a change listener to the button, which has code that will execute when the users mouse hovers over the button.
					String all = "";
					for (int i = 0; i < shipName.length; i++)
						all += (shipName[i] + " ");
					//Added here because the all var had to be final within thee brackets, but essentially adds all the names
					//within the array into one big string.
					descrip.setText("");
					descrip.append("1. You can guess the location of one enemy ship each round by clicking on a button \non the enemy’s ocean grid!\r\n" + 
							"2. You cannot attack the same location twice.\r\n" + 
							"3. If your opponent has not placed their ship on that location, your shot will be shown as \na miss and it will be your opponent’s turn.\r\n" + 
							"4. If your opponent has placed a ship on that location, your shot will be shown as a hit, \nand you will get to guess another location!\r\n" + 
							"5. On your opponent’s turn, they will also attack a position on your map. If they miss, it will \nshow as a miss on your own grid. If they hit, they will be able to go again, and the position your enemy \nhits will be on fire! \r\n" + 
							"6. The first to sink all five of the enemy’s battleships wins.\r\n" + 
							"7. For your info, the ship names are: " + all);
					//Appends the 'how to play' rules to the text area.
				}
			}
		});
		
		instruc.add(instrucs);
		instruc.add(setup);
		instruc.add(play);
		instruc.add(descrip);
		instruc.add(back);
		instruc.add(goSetup);
		//Adds all of the widgets to the panel.
		
		JLabel backGround = new JLabel (createImageIcon ("battleship2.jpg"));
		backGround.setBounds(0, 0, 610, 610);
		instruc.add(backGround);

		scrAll.add("2", instruc);
	}

	public void modes () {
		modeS = new Panel (null);
		modeS.setSize(610, 610);
		
		JLabel modes = new JLabel("MODES");
		modes.setBounds(180, 40, 291, 105);
		modes.setForeground(new Color(0, 128, 128));
		modes.setFont(new Font ("Snap ITC", Font.BOLD, 60));
		modeS.add(modes);

		JButton back = new JButton ("Back");
		back.setForeground(Color.WHITE);
		back.setBackground(new Color (39, 124, 185));
		back.addActionListener(this);
		back.setActionCommand("backToMain");
		back.setBounds(170, 133, 100, 29);

		JButton instructions = new JButton("INSTRUCTIONS");
		instructions.setForeground(Color.WHITE);
		instructions.setBackground(new Color (39, 124, 185));
		instructions.addActionListener(this);
		instructions.setActionCommand("instruc");
		instructions.setBounds(300, 133, 150, 29);

		JButton playEz = new JButton ("Easy");
		playEz.setBackground(new Color(255, 204, 153));
		playEz.addActionListener(this);
		playEz.setActionCommand("easy");
		playEz.setBounds(94, 386, 100, 29);
		
		JButton playOk = new JButton ("Medium");
		playOk.setBackground(new Color(255, 153, 0));
		playOk.addActionListener(this);
		playOk.setActionCommand("medium");
		playOk.setBounds(237, 386, 100, 29);

		playHard = new JButton ("Hard");
		playHard.setBackground(new Color(255, 51, 0));
		playHard.addActionListener(this);
		playHard.setActionCommand("hard");
		playHard.setBounds(391, 386, 100, 29);
		playHard.setEnabled(false);
		playHard.setToolTipText("You need to earn a total score of 3000 points before unlocking this mode!");
		if (hardModeUnlock >= 3000)
			playHard.setEnabled(true);
		//Sets up the button that leads to the hard mode, and disables it as the button is locked until the player
		//reaches 3000 points. So that this can be easily tested, if the points are initialized at 3000 (modified)
		//then the button is enabled. Adds some text that shows up if the user hovers over the button to inform them
		//why the button is unavailable.

		prog = new JProgressBar (0, 0, 3000);
		prog.setValue (hardModeUnlock);
		prog.setBounds(391, 420, 100, 29);
		prog.setStringPainted (true);
		//Creates a JProgressBar that keeps track of the users progress towards unlocking the hard mode.

		highScore1 = new JLabel("HIGH SCORE: " + highScore);
		highScore1.setBounds(43, 25, 175, 30);
		highScore1.setForeground(new Color (10, 195, 185));
		highScore1.setFont(new Font ("Ravie", Font.BOLD, 13));
		//Sets up the high score var
		
		modeS.add(back);
		modeS.add(instructions);
		modeS.add(playEz);
		modeS.add(playOk);
		modeS.add(playHard);
		modeS.add(highScore1);
		modeS.add(prog);

		JLabel backGround1 = new JLabel (createImageIcon ("battleship1.jpg"));
		backGround1.setBounds(0, 0, 610, 610);
		modeS.add(backGround1);  

		scrAll.add("3", modeS); 
	}

	public void placeBoats () {		
		placeB = new Panel (new BorderLayout());
		placeB.setSize(610, 610);
		placeB.setBackground (new Color (77, 32, 224));
		//Creates the panel, sets it up.

		Panel setSide = new Panel (new GridLayout (3, 1, 20, 0));

		JButton back = new JButton ("Back to Modes");
		back.addActionListener(this);
		back.setActionCommand("mode");
		back.setBackground(new Color(39, 124, 185));
		
		rotate = new JButton ("Rotate Ship");
		rotate.addActionListener(this);
		rotate.setActionCommand("rotate");
		rotate.setBackground(new Color(39, 124, 185));

		reset = new JButton ("Reset My Ships!");
		reset.addActionListener(this);
		reset.setActionCommand("clear");
		reset.setBackground(new Color(39, 124, 185));
		reset.setEnabled(false);

		donePlace = new JButton ("Finished!");
		donePlace.addActionListener(this);
		donePlace.setActionCommand("done1");
		donePlace.setBackground(new Color(39, 124, 185));
		donePlace.setEnabled(false);
		//Sets up the buttons that the user can use to place their boats vertically, clear all boats and move on to playing.

		setSide.add(rotate);
		setSide.add(reset);
		setSide.add(donePlace);

		shipDirec = new JLabel ("Next ship will be placed: Vertically");
		shipDirec.setFont (new Font ("Ravie", Font.PLAIN, 17));
		shipDirec.setForeground(new Color (77, 38, 100));

		Panel topRow = new Panel ();
		topRow.add(shipDirec);
		topRow.add(back);
		
		b1 = new JButton[row * col];
		Panel grid = new Panel (new GridLayout (row, col));
		for (int i = 0 ; i < row ; i++) {
			for (int x = 0 ; x < col ; x++) {
				int m = i * row + x;
				b1 [m] = new JButton (createImageIcon (boardP[i][x] + ".jpg"));
				b1 [m].setPreferredSize (new Dimension (50, 50));
				b1 [m].setBackground(new Color (17, 46, 179));
				b1 [m].addActionListener(this);
				b1 [m].setActionCommand(m + "");
				grid.add (b1 [m]);
			} 
		}
		//Sets up the button grid the player uses to 

		placeB.add(grid, BorderLayout.CENTER);
		placeB.add(topRow, BorderLayout.PAGE_START);
		placeB.add(setSide, BorderLayout.PAGE_END);

		scrAll.add("4", placeB);
	}

	public void confirm () {
		confirm = new Panel(new BorderLayout());
		confirm.setSize(610, 610);
		confirm.setBackground (new Color (77, 32, 224));

		JLabel finalC = new JLabel ("Is this your final choice?");
		finalC.setFont (new Font ("Ravie", Font.PLAIN, 17));
		finalC.setForeground(new Color (77, 38, 100));

		j1 = new JLabel[row * col];
		Panel grid1 = new Panel (new GridLayout (row, col, 1, 1));
		for (int i = 0 ; i < row ; i++) {
			for (int x = 0 ; x < col ; x++) {
				int m = i * row + x;
				j1 [m] = new JLabel (createImageIcon (boardP [i][x] + ".jpg"));
				grid1.add (j1 [m]);
			}
		}

		JButton yes = new JButton ("Yes!");
		yes.setBackground(new Color(39, 124, 185));
		yes.addActionListener(this);
		yes.setActionCommand("playG");

		JButton back = new JButton ("Back");
		back.setBackground(new Color(39, 124, 185));
		back.addActionListener(this);
		back.setActionCommand("goToSetup");

		Panel bRow1 = new Panel (new GridLayout (2, 1, 20, 0));

		bRow1.add(yes);
		bRow1.add(back);

		confirm.add(finalC, BorderLayout.PAGE_START);
		confirm.add(grid1, BorderLayout.CENTER);
		confirm.add(bRow1, BorderLayout.PAGE_END);

		scrAll.add("5", confirm);
	}


	public void gameScr1 () {
		gameSc1 = new Panel(new BorderLayout());
		gameSc1.setSize(610, 610);
		gameSc1.setBackground (new Color (77, 32, 224));

		JLabel blankFormat = new JLabel ("");
		blankFormat.setFont (new Font ("Ravie", Font.PLAIN, 19));
		blankFormat.setForeground(new Color (77, 38, 100));

		pUpdate = new JLabel ("It's your turn! Choose a position to attack!");
		pUpdate.setFont (new Font ("Ravie", Font.PLAIN, 18));
		pUpdate.setForeground(new Color (77, 38, 100));

		pHitMiss = new JLabel ("Hits: " + pHit + " Missed: " + pMiss);
		pHitMiss.setFont(new Font ("Ravie", Font.PLAIN, 15));
		pHitMiss.setForeground(Color.green);

		nextP = new JButton ("View Opponent's Move!");
		nextP.setBackground(new Color(39, 124, 185));
		nextP.addActionListener(this);
		nextP.setActionCommand("enemyTurn");
		nextP.setEnabled(false);

		Panel topRow = new Panel (new GridLayout (2, 1, 5, 0));
		topRow.add(pUpdate);
		topRow.add(blankFormat);

		Panel botRow = new Panel (new GridLayout (1, 2, 0, 20));
		botRow.add(pHitMiss);
		botRow.add(nextP);

		botBoatSet (5, 0);
		botBoatSet (4, 1);
		botBoatSet (3, 2);
		botBoatSet (3, 3);
		botBoatSet (2, 4);

		o1 = new JButton[row * col];
		Panel grid2 = new Panel (new GridLayout (row, col));
		for (int i = 0 ; i < row ; i++) {
			for (int x = 0 ; x < col ; x++) {
				int m = i * row + x;
				o1 [m] = new JButton (createImageIcon (opBoard[i][x] + ".jpg"));
				o1 [m].setPreferredSize (new Dimension (50, 50));
				o1 [m].setBackground(new Color (17, 46, 179));
				o1 [m].addActionListener(this);
				o1 [m].setActionCommand(m + "");
				grid2.add (o1 [m]);
			}
		}
		drawBoard ('b', 1);
		//Creates the  grid for the user to guess the bot's boat positions. 

		gameSc1.add(topRow, BorderLayout.PAGE_START);
		gameSc1.add(grid2, BorderLayout.CENTER);
		gameSc1.add(botRow, BorderLayout.PAGE_END);

		scrAll.add("6", gameSc1);
	}

	public void gameScr2 () {
		gameSc2 = new Panel (new BorderLayout());
		gameSc2.setSize(610, 610);
		gameSc2.setBackground (new Color (77, 32, 224));

		j2 = new JLabel[row * col];
		Panel grid4 = new Panel (new GridLayout (row, col, 1, 1));
		for (int i = 0 ; i < row ; i++) {
			for (int x = 0 ; x < col ; x++) {
				int m = i * row + x;
				j2 [m] = new JLabel (createImageIcon (boardP [i][x] + ".jpg"));
				grid4.add (j2 [m]);
			}
		}
		ready = new JButton ("See Enemy's Move!");
		ready.setBackground(new Color(39, 124, 185));
		ready.addActionListener(this);
		ready.setActionCommand("enemyFire");

		switchTurn = new JButton ("Take my shot!");
		switchTurn.setBackground(new Color(39, 124, 185));
		switchTurn.addActionListener(this);
		switchTurn.setActionCommand("playG");
		switchTurn.setEnabled(false);

		Panel btnCol = new Panel (new GridLayout (2, 1, 10, 0));
		btnCol.add(ready);
		btnCol.add(switchTurn);
		
		eUpdate = new JLabel ("Click on 'See Enemy's Move'!");
		eUpdate.setFont(new Font ("Ravie", Font.BOLD, 15));
		eUpdate.setForeground(new Color (77, 38, 100));
		
		gameSc2.add(eUpdate, BorderLayout.PAGE_START);
		gameSc2.add(grid4, BorderLayout.CENTER);
		gameSc2.add(btnCol, BorderLayout.PAGE_END);

		scrAll.add("7", gameSc2);
	}

	public void actionPerformed (ActionEvent e) {
		ImageIcon jackS = createImageIcon ("jackSparrow.png");
		if (e.getActionCommand().equals("endGame"))
			System.exit(0);
		
		else if (e.getActionCommand().equals("backToMain"))
			cdLayout.show(scrAll, "1");
		
		else if (e.getActionCommand().equals("instruc")) {
			cdLayout.show(scrAll, "2");
			JOptionPane.showMessageDialog(null, "All wars have rules, and so does this one! Hover over either button\nto see specific instructions, should you need them!", "Jack Sparrow", 
					JOptionPane.INFORMATION_MESSAGE, jackS);
		}
		
		else if (e.getActionCommand().equals("mode")) {
			cdLayout.show(scrAll, "3");
			JOptionPane.showMessageDialog(null, "Choose the mode you wish to play!", "Jack Sparrow", 
					JOptionPane.INFORMATION_MESSAGE, jackS);
			}
		
		else if (e.getActionCommand().equals("goToSetup")) {
			cdLayout.show(scrAll, "4");
			gameStage = 1;
		}
		//Changes gameStage, shows messages and screens accordingly if the user presses a button that should switch to a screen.
		
		else if (e.getActionCommand().equals("done1")) {
			cdLayout.show (scrAll, "5");
			drawBoard ('j', 0);
		}
		
		else if (e.getActionCommand().equals("playG")) {
			cdLayout.show(scrAll, "6");
			gameStage = 2;
			pUpdate.setText("It's your turn! Choose a position to attack!");
		}
		
		else if (e.getActionCommand().equals("enemyTurn")) {
			cdLayout.show(scrAll, "7");
			ready.setEnabled(true);
			drawBoard ('j', 1);
		}

		else if (e.getActionCommand().equals("rotate")) {
			if (alignment == 'v') {
				alignment = 'h';
				shipDirec.setText("Next ship will be placed: Horizontally");
			}
			else {
				alignment = 'v';
				shipDirec.setText("Next ship will be placed: Vertically");
			}
		}
		//Rotates the ship placement and informs the user of the next ship's orientation. 
		
		else if (e.getActionCommand().equals("clear")) {
			boatsPlaced = 0;
			rotate.setEnabled(true);
			donePlace.setEnabled(false);
			reset.setEnabled(false);
			for (int i = 0; i < row; i++) {
				for (int m = 0; m < col; m++) {
					boardP [i][m] = 1;
				}
			}
			drawBoard ('b', 0);
			if (alignment == 'h') {
				shipDirec.setText("Next ship will be placed: Horizontally");
			}
			else 
				shipDirec.setText("Next ship will be placed: Vertically");
			//If the user wants to, resets the setup board.
		}
		
		else if (e.getActionCommand().equals("easy") || e.getActionCommand().equals("medium") || e.getActionCommand().equals("hard")) {
			JOptionPane.showMessageDialog(null, "Strategically place our ships down so that we can battle!", "Jack Sparrow", 
					JOptionPane.INFORMATION_MESSAGE, jackS);
			if (e.getActionCommand().equals("easy"))
				gameMode = 'e';  		
			else if (e.getActionCommand().equals("medium"))
				gameMode = 'm';
			else
				gameMode = 'h';    		
			cdLayout.show(scrAll, "4");
			gameStage = 1;
			//Sets up the var used to check gameMode.
		}
		
		else {
			if (gameStage == 1) {
				/*Using this variable allows for me to be able to have my grid buttons to have similar actionCommands and be able to
				* Ensure the buttons on other grids don't work when they are supposed to, without disabling all 100. This stage is
				* where the user sets up their ships*/
				for (int i = 0; i < row * col; i++) {
					if (e.getActionCommand().equals(i + "")){
						int x1 = i / row;
						int y1 = i % row;
						if (alignment == 'v') {
							boatsPlaced = placeShipV (x1, y1, boatsPlaced, boardP);
							drawBoard('b', 0); 
						}
						else {
							boatsPlaced = placeShipH (x1, y1, boatsPlaced, boardP);
							drawBoard('b', 0);
						}
					}
				}
			}
			//Places the boat down according to where the user clicked and which orientation they wanted.
			
			else if (gameStage == 2) {
				//This is the player's attacking stage.
				switchTurn.setEnabled(false);
				for (int i = 0; i < row * col; i++) {
					if (e.getActionCommand().equals(i + "")){
						int x2 = i / row;
						int y2 = i % row;
						if (opBoard[x2][y2] == 1) {
							playFile ("miss sfx");
							pUpdate.setText("Miss! It is your opponent's turn!");
							slow(400);
							pMiss++;
							opBoard[x2][y2] = 3;
							drawBoard('b', 1);
							pHitMiss.setText("Hits: " + pHit + " Misses: " + pMiss);
							nextP.setEnabled(true);
							gameStage = 3;
						}
						else if (opBoard[x2][y2] >=4 && opBoard[x2][y2] <= 9) {
							playFile ("hit sfx");
							pUpdate.setText("Hit! You get another shot!");
							pHit++;
							opBoard[x2][y2] = 2;
							drawBoard('b', 1);
							pHitMiss.setText("Hits: " + pHit + " Missed: " + pMiss);
							checkWinner(pHit, eHit);
						}
					}
					//Runs the player's move, playing different sounds for hit/miss and informing the user of their new status.
					//Then checks if someone has won.
				}
			}
			
			else if (gameStage == 3) {
				//This is the enemy's attacking stage.
				for (int i = 0; i < row * col; i++) {
					if (e.getActionCommand().equals(i + ""))
						pUpdate.setText("Your turn is over. Click 'View Opponent's Move!'");
				}
				//If the user has missed and tries to go again before seeing the enemy's turn, tells them to go to the second game screen.
				if (e.getActionCommand().equals("enemyFire")) {
					if (gameMode == 'e')
						easyBot ();
					else if (gameMode == 'm')
						mediumBot();
					else if (gameMode == 'h')
						hardBot();
					drawBoard ('j', 1);
					checkWinner(pHit, eHit);
					//Runs the bot move according to the mode, checks if the bot or user has won.
				}
			}
		}
	}

	public void botBoatSet (int bLen, int shipNum) {
		int x, y;

		int random = (int) (Math.random() * 2);

		if (random == 0) {
			do {
				x = (int) (Math.random() * 10);
				y = (int) (Math.random() * 10);
			}
			while (!checkPosV(x, y, bLen, opBoard));
			placeShipV (x, y, shipNum, opBoard);
		}
		else {
			do {
				x = (int) (Math.random() * 10);
				y = (int) (Math.random() * 10);
			}
			while (!checkPosH(x, y, bLen, opBoard));
			placeShipH (x, y, shipNum, opBoard);
		}
	}

	public void replay () {
		for (int i = 0; i < row; i++) {
			for (int x = 0; x < 10; x++) {
				boardP[i][x] = 1;
				opBoard[i][x] = 1;
			}
		}
		drawBoard ('b', 0);
		drawBoard ('b', 1);
		drawBoard ('j', 0);
		drawBoard ('j', 1);

		slider.setValue(100);
		shipDirec.setText("Next ship will be placed: Vertically");
		pUpdate.setText("Hit: " + pHit + " Missed: " + pMiss);
		eUpdate.setText("Click on 'See Enemy's Move'!");
		pHitMiss.setText("It's your turn! Choose a position to attack!");
		
		donePlace.setEnabled(false);
		rotate.setEnabled(true);
		reset.setEnabled(false);
		nextP.setEnabled(false);
		switchTurn.setEnabled(false); 
		ready.setEnabled(true);	
		
		boatsPlaced = 0;
		gameStage = 1;
		pHit = 0;
		eHit = 0;
		pMiss = 0;
		alignment = 'v';
		volChange = 100;
		//Resetting all of the global variables to their inititial values, JLabels to their initial text, and JButtons to how they 
		//started off (enabled or disabled). Basically, everything done to global vars within the screen layout methods is redone.
		
	}

	public void easyBot () {
		int x, y;

		do {
			x = (int) (Math.random() * 10);
			y = (int) (Math.random() * 10);
		}
		while (boardP[x][y] == 3 || boardP[x][y] > 9);

		if (boardP[x][y] == 1) {
			boardP[x][y] = 3;
			playFile ("miss sfx");
			eUpdate.setText("The enemy missed!");
		}
		else {
			boardP[x][y] += 6;
			playFile ("hit sfx");
			eUpdate.setText("The enemy hit one of your battleships!");
			eHit++;
		}
		switchTurn.setEnabled(true);
		ready.setEnabled(false);
		nextP.setEnabled(false);
		gameStage = 2;
		//Generates a random number that would within a 10x10 array that the bot has not already chosen, informs user if the bot has hit or missed.
	}

	public void mediumBot () {
		int x, y;

		do {
			x = (int) (Math.random() * 10);
			y = (int) (Math.random() * 10);
		}
		while (boardP[x][y] == 3 || boardP[x][y] > 9);

		if (boardP[x][y] == 1) {
			boardP[x][y] = 3;
			playFile ("miss sfx");
			eUpdate.setText("The enemy missed!");
			switchTurn.setEnabled(true);
			ready.setEnabled(false);
			nextP.setEnabled(false);
			gameStage = 2;
		}
		else {
			boardP[x][y] += 6;
			playFile ("hit sfx");
			eUpdate.setText("The enemy hit one of your ships and will shoot again!");
			eHit++;
		}
		//Similar to easy, but if the bot hits, it gets to go again. 
	}
	
	public void hardBot () {
		int x, y;

		if (hardAccuracy < 3) {
			do {
				x = (int) (Math.random() * 10);
				y = (int) (Math.random() * 10);
			}
			while (boardP[x][y] == 3 || boardP[x][y] > 9);
			hardAccuracy++;
			if (boardP[x][y] == 1) {
				boardP[x][y] = 3;
				playFile ("miss sfx");
				eUpdate.setText("The enemy missed!");
			}
			else {
				boardP[x][y] += 6;
				playFile ("hit sfx");
				eUpdate.setText("The enemy hit one of your battleships!");
				eHit++;
			}
		}
		else {
			do {
				x = (int) (Math.random() * 10);
				y = (int) (Math.random() * 10);
			}
			while (boardP[x][y] < 4 || boardP[x][y] > 9);
			boardP[x][y] += 6;
			playFile ("hit sfx");
			eUpdate.setText("The enemy used their radar scan and hit!");
			eHit++;
			
			hardAccuracy = 1;
		}
		//Similar to easy, but once every 3 shots the bot gets a guaranteed hit. 

		switchTurn.setEnabled(true);
		ready.setEnabled(false);
		nextP.setEnabled(false);
		gameStage = 2;
	}
	
	public void checkWinner (int pHits, int eHits) {		
		if (pHits == 17) {
			gameOver (true);
		}
		else if (eHits == 17) {
			gameOver (false);
		}
		//Calls the method that wraps up the game if the player or the bot has won.
	}
	
	public void gameOver (boolean win) {
		int cont;
		int score = calcScore (win, pMiss, pHit, gameMode);
		if (highScore < score) {
			highScore = score;
			highScore1.setText("HIGH SCORE: " + highScore);
			
			ImageIcon jackS = createImageIcon ("jackSparrow.png");
			JOptionPane.showMessageDialog(null, "Ahoy Matey! It seems you have reached a new high score: " + score, "NEW HIGH SCORE!", 
					JOptionPane.INFORMATION_MESSAGE, jackS);
		}
		//Calculates the game score, checks if the user has a new high score.
		
		hardModeUnlock += score;
		if (hardModeUnlock >= 3000) {
			hardModeUnlock = 3000;
			playHard.setEnabled(true);
		}
		prog.setValue(hardModeUnlock);
		//Adds on user's progress to unlocking hardMode. 
		
		
		if (win)
			cont = JOptionPane.showConfirmDialog (null, "Jack: Haha! this is the day you will always remember as the day you ALMOST caught Captain Jack Sparrow, Trading Company!\n\nYour final score was " + score + ". Do you wish to play again?",
					"You win!", JOptionPane.YES_NO_OPTION);
		else
			cont = JOptionPane.showConfirmDialog (null, "Good try! Your final score was " + score + ". Take another shot at it?",
					"Replay?", JOptionPane.YES_NO_OPTION);
		//Asks user if the want to replay.
		
		if (cont == 0) {
			replay();
			cdLayout.show(scrAll, "3");
		}
		else {
			System.exit(0);
		}
		//Does replay if they said yes, otherwise, ends applet.

	}
	
	public int calcScore (boolean win, int missed, int hit, char mode) {
		int score = (hit * 100) - (missed * 12);
		//Calculates the score using an algorithm which uses values of my own choice.

		if (win)
			score += 500;
		else
			score -= 100;
		//Gives a user a 500 point boost if they won, or subtracts 100 for losing.

		if (score < 0)
			score = 0;
		//Sets score to 0 to avoid score being negative.

		if (mode == 'm')
			score *= 1.15;
		else if (mode == 'h')
			score *= 1.5;
		//Using values of my own choice, if the user played a higher difficulty a fraction of their score was increased,
		//depending on the mode they chose to play.

		return score;
		//Returns the final score the user has earned within their game.
	}

	public void drawBoard (char type, int num) {
		for (int n = 0 ; n < row ; n++) {
			for (int o = 0 ; o < col ; o++) {
				int m = n * row + o;
				if (type == 'b') {
					if (num == 0)
						b1 [m].setIcon(createImageIcon (boardP[n][o] + ".jpg"));
					else if (num == 1) {
						if (opBoard [n][o] > 3)
							o1 [m].setIcon(createImageIcon ("1.jpg"));
						else
							o1 [m].setIcon(createImageIcon (opBoard[n][o] + ".jpg"));
					}
				}
				else if (type == 'j')
					if (num == 0)
						j1 [m].setIcon(createImageIcon (boardP[n][o] + ".jpg"));
					else if (num == 1)
						j2 [m].setIcon(createImageIcon (boardP[n][o] + ".jpg"));
			}
		}
	}

	public boolean checkPosV (int x, int y, int bLen, int board[][]) {
		boolean isOk = true;
		boolean skip = false;
		if (x > (row - bLen)) {
			isOk = false;
			skip = true;
		}

		while (skip == false) {
			for (int i = 0; i < bLen; i++) {
				if (board [x+i][y] != 1) {
					isOk = false;
					skip = true;
				}
			}
			skip = true;
		}
		return isOk;
	}

	public boolean checkPosH (int x, int y, int bLen, int board[][]) {
		boolean isOk = true;
		boolean skip = false;
		if (y > (col - bLen)) {
			isOk = false;
			skip = true;
		}
		//Checks if the boat would go out of bounds when placed

		while (skip == false) {
			for (int i = 0; i < bLen; i++) {
				if (board [x][y+i] != 1) {
					isOk = false;
					skip = true;
				}
			}
			skip = true;
		}
		return isOk;
		//Checks whether or not the boat would collide with another boat when placed, then returns if the boat placements is possible or not.
	}

	public int placeShipV (int x, int y, int placedB, int board[][]) {
		if (placedB == 0) {
			boolean isFine = checkPosV(x, y, 5, board);
			if (isFine == true) {
				board [x][y] = 7;
				board [x+1][y] = 9;
				board [x+2][y] = 9;
				board [x+3][y] = 9;
				board [x+4][y] = 4;
				placedB++;
				if (board == boardP)
					reset.setEnabled(true);
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 1) {
			boolean isFine = checkPosV(x, y, 4, board);
			if (isFine == true) {
				board [x][y] = 7;
				board [x+1][y] = 9;
				board [x+2][y] = 9;
				board [x+3][y] = 4;
				placedB++;
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 2) {
			boolean isFine = checkPosV(x, y, 3, board);
			if (isFine == true) {
				board [x][y] = 7;
				board [x+1][y] = 9;
				board [x+2][y] = 4;
				placedB++;
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 3) {
			boolean isFine = checkPosV(x, y, 3, board);
			if (isFine == true) {
				board [x][y] = 7;
				board [x+1][y] = 9;
				board [x+2][y] = 4;
				placedB++;
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 4) {
			boolean isFine = checkPosV(x, y, 2, boardP);
			if (isFine == true) {
				board [x][y] = 7;
				board [x+1][y] = 4;
				placedB++;
				if (board == boardP) {
					rotate.setEnabled(false);
					donePlace.setEnabled(true);
				}

			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		//This entire section basically checks which boat is being placed, checks if the position clicked will work for the boat,
		//then changes the player's board array to the boat.
		else 
			shipDirec.setText("You have already placed all 5 boats!");
		return placedB;
	}

	public int placeShipH (int x, int y, int placedB, int board[][]) {
		if (placedB == 0) {
			boolean isFine = checkPosH(x, y, 5, boardP);
			if (isFine == true) {
				board [x][y] = 6;
				board [x][y+1] = 8;
				board [x][y+2] = 8;
				board [x][y+3] = 8;
				board [x][y+4] = 5;
				placedB++;
				if (board == boardP)
					reset.setEnabled(true);
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 1) {
			boolean isFine = checkPosH(x, y, 4, boardP);
			if (isFine == true) {
				board [x][y] = 6;
				board [x][y+1] = 8;
				board [x][y+2] = 8;
				board [x][y+3] = 5;
				placedB++;
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 2) {
			boolean isFine = checkPosH(x, y, 3, boardP);
			if (isFine == true) {
				board [x][y] = 6;
				board [x][y+1] = 8;
				board [x][y+2] = 5;
				placedB++;
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 3) {
			boolean isFine = checkPosH(x, y, 3, boardP);
			if (isFine == true) {
				board [x][y] = 6;
				board [x][y+1] = 8;
				board [x][y+2] = 5;;
				placedB++;
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else if (placedB == 4) {
			boolean isFine = checkPosH(x, y, 2, boardP);
			if (isFine == true) {
				board [x][y] = 6;
				board [x][y+1] = 5;
				placedB++;
				if (board == boardP) {
					rotate.setEnabled(false);
					donePlace.setEnabled(true);
				}
			}
			else
				JOptionPane.showMessageDialog(null, "Boat would go off the board or collide with another boat!", "Try another position!", 
						JOptionPane.WARNING_MESSAGE);
		}
		else 
			shipDirec.setText("You have already placed all 5 boats!");
		return placedB;
	}
	

	public void slow (int slowT) {
		try {
			Thread.sleep(slowT);
		}
		catch (InterruptedException m){;}
		//Sets the thread to sleep for an amount of time relative to the number parameter the method was called in, allowing
		//for Thread.sleep to be used flexibly.
	}

	public static void playSound(String audioFileName)
	// A method used to play audio files (compatibility is guaranteed only with .wav files)
	{
		try {
			// Tries the code, but provides a "catch" to handle any exceptions
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(Battleship.class.getResource(audioFileName)); 
			// Opens an audio input stream
			Clip clip = AudioSystem.getClip();
			// Gets a sound clip resource
			clip.close();
			//Closes any previously running clip.
			clip.open(audioIn);
			// Open audio clip and loads clip from the audio input stream.
			clip.start();
			// Starts clip


			if (audioFileName.equals("Pirates of The Caribbean Theme - 1 hour version.wav")) {
				FloatControl control = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
				control.setValue(-10.0f);
			}
			//Lowers audio of the theme music by 10 decibels.
		}
		catch (Exception ex) {
			System.out.println("Customized Error 404! AudioFile Not Found!\nCould be due to user settings");
		} // In case of error, message to console is printed
	}

	public void playFile (String fileName) {
		fileName = fileName.trim() + ".wav";
		playSound(fileName);
		//Sets the parameter to the music file name, adds the file extension, and calls a method that will play the music.
	}

	protected static ImageIcon createImageIcon (String path) {
		java.net.URL pictureURL = Battleship.class.getResource(path);
		if (pictureURL != null)
			return new ImageIcon (pictureURL);
		else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}