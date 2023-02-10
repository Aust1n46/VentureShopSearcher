package venture.Aust1n46.shopsearcher.service;

import static org.bukkit.Material.getMaterial;

import java.io.File;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import lombok.Getter;
import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.utilities.FormatUtils;

@Singleton
@Getter
public class ConfigService {
	@Inject
	private VentureShopSearcher plugin;

	private Material closeGui;
	private Material pages;
	private Material inStock;
	private Material outOfStock;
	private String nextPageText;
	private String previousPageText;
	private String goBackText;
	private String closeGuiText;
	private String itemSearchTitle;
	private String plotSearchTitle;
	private String plotText;
	private String noPlotFoundText;
	private String adminPlotText;
	private int saveInterval;

	@Inject
	private void postConstruct() {
		try {
			if (!plugin.getDataFolder().exists()) {
				plugin.getDataFolder().mkdirs();
			}
			File file = new File(plugin.getDataFolder(), "config.yml");
			if (!file.exists()) {
				plugin.getServer().getConsoleSender().sendMessage(FormatUtils.FormatStringAll("&8[&VentureShopSearcher&8]&e - Config not found! Generating file."));
				plugin.saveDefaultConfig();
			} else {
				plugin.getServer().getConsoleSender().sendMessage(FormatUtils.FormatStringAll("&8[&VentureShopSearcher&8]&e - Config found! Loading file."));
			}
		} catch (Exception ex) {
			plugin.getServer().getConsoleSender()
					.sendMessage(FormatUtils.FormatStringAll("&8[&VentureShopSearcher&8]&e - &cCould not load configuration! Something unexpected went wrong!"));
		}
		final FileConfiguration config = plugin.getConfig();
		closeGui = getMaterial(config.getString("closeGui", "BARRIER"));
		pages = getMaterial(config.getString("pages", "WRITABLE_BOOK"));
		inStock = getMaterial(config.getString("inStock", "LIME_STAINED_GLASS_PANE"));
		outOfStock = getMaterial(config.getString("outOfStock", "RED_STAINED_GLASS_PANE"));
		nextPageText = FormatUtils.FormatStringAll(config.getString("nextPageText", "&6Next Page ->"));
		previousPageText = FormatUtils.FormatStringAll(config.getString("previousPageText", "&6<- Previous Page"));
		goBackText = FormatUtils.FormatStringAll(config.getString("goBackText", "&cGo Back To Item Search"));
		closeGuiText = FormatUtils.FormatStringAll(config.getString("closeGuiText", "&cClose GUI"));
		itemSearchTitle = FormatUtils.FormatStringAll(config.getString("itemSearchTitle", "Item Search"));
		plotSearchTitle = FormatUtils.FormatStringAll(config.getString("plotSearchTitle", "Available Listings"));
		plotText = FormatUtils.FormatStringAll(config.getString("plotText", "Plot: {location}"));
		noPlotFoundText = FormatUtils.FormatStringAll(config.getString("noPlotFoundText", "No plot found for this item"));
		adminPlotText = FormatUtils.FormatStringAll(config.getString("adminPlotText", "Admin Shop"));
		saveInterval = config.getInt("save_interval");
	}
}
