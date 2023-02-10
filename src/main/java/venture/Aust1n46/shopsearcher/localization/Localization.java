package venture.Aust1n46.shopsearcher.localization;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.utilities.FormatUtils;

//This class is used to create objects of localization for different languages.
public class Localization {
	private static final String VERSION = "1.0.0";
	private static FileConfiguration localization;

	public static void initialize(final VentureShopSearcher plugin) {
		File localizationFile = new File(plugin.getDataFolder().getAbsolutePath(), "Messages.yml");
		if (!localizationFile.isFile()) {
			plugin.saveResource("Messages.yml", true);
		}

		localization = YamlConfiguration.loadConfiguration(localizationFile);

		String fileVersion = localization.getString("Version", "null");

		if (!fileVersion.equals(VERSION)) {
			plugin.getServer().getConsoleSender()
					.sendMessage(FormatUtils.FormatStringAll("&8[&eVentureShopSearcher&8]&e - Version Change Detected!  Saving Old Messages.yml and Generating Latest File"));
			localizationFile.renameTo(new File(plugin.getDataFolder().getAbsolutePath(), "Messages_Old_" + fileVersion + ".yml"));
			plugin.saveResource("Messages.yml", true);
			localization = YamlConfiguration.loadConfiguration(localizationFile);
		}
	}

	public static FileConfiguration getLocalization() {
		return localization;
	}
}
