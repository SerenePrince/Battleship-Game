package battleship.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import battleship.FontLoader;

/**
 * The SplashScreenView class represents the splash screen that appears before
 * launching the main Battleship program.
 */
public class SplashScreenView extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private static final Color BACKGROUND_COLOR = Color.BLACK;
	private static final Color TEXT_COLOR = new Color(0x00FF00);

	private static final float FONT_SIZE = 94f;

	private Font agencyBold94;

	/**
	 * Constructor for SplashScreenView. Initializes the font, sets up the splash
	 * panel, and configures the frame.
	 */
	public SplashScreenView() {
		initializeFont();
		initializeSplashPanel();
		setupFrame();
	}

	/**
	 * Loads and initializes the custom font used for the splash screen text.
	 */
	private void initializeFont() {
		agencyBold94 = FontLoader.loadFont("resources/fonts/AgencyFB-Bold.ttf", Font.BOLD, FONT_SIZE);
	}

	/**
	 * Initializes the splash panel with the splash screen label.
	 */
	private void initializeSplashPanel() {
		JPanel splashPanel = new JPanel();
		splashPanel.setBackground(BACKGROUND_COLOR);

		JLabel splashLabel = new JLabel("BATTLESHIP", SwingConstants.CENTER);
		splashLabel.setPreferredSize(new Dimension(640, 130));
		splashLabel.setFont(agencyBold94);
		splashLabel.setForeground(TEXT_COLOR);
		splashPanel.add(splashLabel);

		add(splashPanel);
	}

	/**
	 * Configures the frame settings for the splash screen.
	 */
	private void setupFrame() {
		setUndecorated(true);
		pack();
		setLocationRelativeTo(null); // Center the frame on the screen
		setVisible(true); // Make the frame visible
	}
}
