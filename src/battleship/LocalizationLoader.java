package battleship;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * The LocalizationLoader class manages the localization of messages for
 * different languages. It allows switching between languages and retrieving
 * localized messages from resource bundles.
 * 
 * <p>
 * Example usage:
 * </p>
 * 
 * <pre>
 * {@code
 * LocalizationLoader localizationLoader = new LocalizationLoader();
 * String currentLanguage = localizationLoader.getLanguage();
 * ResourceBundle messages = localizationLoader.getResourceBundle();
 * localizationLoader.switchLanguage("fr");
 * }
 * </pre>
 * 
 * @see java.util.Locale
 * @see java.util.ResourceBundle
 */
public class LocalizationLoader {

	/**
	 * The current language code (e.g., "en", "fr").
	 */
	private String language;

	/**
	 * The Locale object representing the current language settings.
	 */
	private Locale locale;

	/**
	 * The ResourceBundle for accessing localized messages.
	 */
	private ResourceBundle resourceBundle;

	/**
	 * Constructor to initialize with the default language (English).
	 */
	public LocalizationLoader() {
		this.language = "en"; // Default language is English
		this.locale = new Locale.Builder().setLanguage(language).build();
		this.resourceBundle = ResourceBundle.getBundle("resources.localization.messages", locale);
	}

	/**
	 * Gets the ResourceBundle containing localized messages for the current
	 * language.
	 *
	 * @return The ResourceBundle object.
	 */
	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	/**
	 * Gets the currently selected language code.
	 *
	 * @return The current language code (e.g., "en", "fr").
	 */
	public String getLanguage() {
		return language;
	}

	/**
	 * Switches the current language of the application.
	 *
	 * @param language The language code to switch to (e.g., "en", "fr").
	 */
	public void switchLanguage(String language) {
		this.language = language;
		this.locale = new Locale.Builder().setLanguage(language).build();
		this.resourceBundle = ResourceBundle.getBundle("resources.localization.messages", locale);
	}
}
