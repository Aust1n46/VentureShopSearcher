package venture.Aust1n46.shopsearcher.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.model.PlayerWrapper;
import venture.Aust1n46.shopsearcher.service.ApiService;
import venture.Aust1n46.shopsearcher.utilities.FormatUtils;

@Singleton
public class FlatFileController {
	@Inject
	private VentureShopSearcher plugin;
	@Inject
	private ApiService apiService;
	private String pluginDataDirectoryPath;

	@Inject
	private void postConstruct() {
		pluginDataDirectoryPath = plugin.getDataFolder().getAbsolutePath() + "/PlayerData";
		loadPlayerData();
	}

	public void loadPlayerData() {
		try {
			final File playerDataDirectory = new File(pluginDataDirectoryPath);
			if (!playerDataDirectory.exists()) {
				playerDataDirectory.mkdirs();
			}
			Files.walk(Paths.get(pluginDataDirectoryPath)).filter(Files::isRegularFile).forEach((final Path path) -> readPlayerDataFile(path));
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	private void readPlayerDataFile(final Path path) {
		final File playerDataFile = path.toFile();
		if (!playerDataFile.exists()) {
			return;
		}
		try {
			final FileConfiguration playerDataFileYamlConfiguration = YamlConfiguration.loadConfiguration(playerDataFile);
			final String uuidString = playerDataFile.getName().replace(".yml", "");
			final UUID uuid = UUID.fromString(uuidString);
			final String username = playerDataFileYamlConfiguration.getString("username");
			final String location = playerDataFileYamlConfiguration.getString("location");
			final PlayerWrapper playerWrapper = new PlayerWrapper(null, uuid, username, location);
			apiService.addPlayerWrapper(playerWrapper);
			apiService.addUsernameToUuidMap(username, uuid);
		} catch (final Exception e) {
			Bukkit.getConsoleSender().sendMessage(FormatUtils.FormatStringAll("&8[&eVentureChat&8]&c - Error Loading Data File: " + playerDataFile.getName()));
			Bukkit.getConsoleSender().sendMessage(FormatUtils.FormatStringAll("&8[&eVentureChat&8]&c - File will be skipped and deleted."));
			playerDataFile.delete();
			return;
		}
	}

	public void savePlayerData(final PlayerWrapper playerWrapper) {
		if (playerWrapper == null) {
			return;
		}
		try {
			final File playerDataFile = new File(pluginDataDirectoryPath, playerWrapper.getUuid() + ".yml");
			final FileConfiguration playerDataFileYamlConfiguration = YamlConfiguration.loadConfiguration(playerDataFile);
			if (!playerDataFile.exists()) {
				playerDataFileYamlConfiguration.save(playerDataFile);
			}
			playerDataFileYamlConfiguration.set("username", playerWrapper.getUsername());
			playerDataFileYamlConfiguration.set("location", playerWrapper.getLocation());
			playerDataFileYamlConfiguration.save(playerDataFile);
		} catch (final IOException e) {
			e.printStackTrace();
		}
	}

	public void savePlayerData() {
		for (final PlayerWrapper playerWrapper : apiService.getPlayerWrappers()) {
			savePlayerData(playerWrapper);
		}
	}
}
