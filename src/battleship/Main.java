package battleship;

import java.net.UnknownHostException;

import battleship.controller.BattleshipController;
import battleship.model.BattleshipModel;
import battleship.view.BattleshipView;
import battleship.view.SplashScreenView;

/**
 * The Main class is the entry point for the Battleship game application. It
 * initializes and starts the game by setting up the MVC components and
 * displaying the splash screen.
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * java -jar BattleshipGame.jar
 * }
 * </pre>
 */
public class Main {

	/**
	 * Main method to start the Battleship game.
	 *
	 * @param args The command-line arguments.
	 * @throws UnknownHostException
	 */
	public static void main(String[] args) {
		// Display the splash screen
		SplashScreenView splashScreen = new SplashScreenView();
		try {
			// Keep the splash screen visible for 2.5 seconds
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		splashScreen.dispose();

		// Initialize the main game frame
		LocalizationLoader loader = new LocalizationLoader();
		BattleshipModel model = new BattleshipModel(loader);
		BattleshipView view = new BattleshipView(loader);
		BattleshipController controller = new BattleshipController(loader, model, view);
		model.setController(controller);
		model.setView(view);
		view.setController(controller);
		view.setModel(model);

	}
}
