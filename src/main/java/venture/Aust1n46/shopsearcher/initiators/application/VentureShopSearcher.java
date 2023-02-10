package venture.Aust1n46.shopsearcher.initiators.application;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Singleton;
import com.nisovin.shopkeepers.SKShopkeepersPlugin;

import venture.Aust1n46.shopsearcher.controller.FlatFileController;
import venture.Aust1n46.shopsearcher.initiators.listeners.CommandListener;
import venture.Aust1n46.shopsearcher.initiators.listeners.InventoryListener;
import venture.Aust1n46.shopsearcher.initiators.listeners.LoginListener;
import venture.Aust1n46.shopsearcher.localization.Localization;
import venture.Aust1n46.shopsearcher.service.ConfigService;

@Singleton
public class VentureShopSearcher extends JavaPlugin {
	@Inject
	private FlatFileController flatFileController;
	@Inject
	private InventoryListener inventoryListener;
	@Inject
	private LoginListener loginListener;
	@Inject
	private ConfigService configService;

	@Override
	public void onEnable() {
		final SKShopkeepersPlugin shopKeepersPlugin = (SKShopkeepersPlugin) getServer().getPluginManager().getPlugin("Shopkeepers");
		final PluginModule pluginModule = new PluginModule(this, shopKeepersPlugin);
		final Injector injector = Guice.createInjector(pluginModule);
		injector.injectMembers(this);
		injector.injectMembers(new CommandListener());
		final PluginManager pluginManager = getServer().getPluginManager();
		pluginManager.registerEvents(inventoryListener, this);
		pluginManager.registerEvents(loginListener, this);
		Localization.initialize(this);
		final BukkitScheduler scheduler = getServer().getScheduler();
		scheduler.runTaskTimerAsynchronously(this, new Runnable() {
			@Override
			public void run() {
				flatFileController.savePlayerData();
			}
		}, 0L, configService.getSaveInterval() * 1200); // one minute * save interval
	}

	@Override
	public void onDisable() {
		flatFileController.savePlayerData();
	}
}
