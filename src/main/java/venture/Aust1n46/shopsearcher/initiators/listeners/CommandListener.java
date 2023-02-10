package venture.Aust1n46.shopsearcher.initiators.listeners;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabExecutor;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import venture.Aust1n46.shopsearcher.controllers.commands.ClearPlot;
import venture.Aust1n46.shopsearcher.controllers.commands.GetPlot;
import venture.Aust1n46.shopsearcher.controllers.commands.SetPlot;
import venture.Aust1n46.shopsearcher.controllers.commands.Shop;
import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.model.VentureCommand;

@Singleton
public class CommandListener implements TabExecutor {
	private final Map<String, VentureCommand> commands = new HashMap<>();

	@Inject
	private VentureShopSearcher plugin;
	@Inject
	private Shop shop;
	@Inject
	private SetPlot setPlot;
	@Inject
	private GetPlot getPlot;
	@Inject
	private ClearPlot clearPlot;

	@Inject
	private void postConstruct() {
		commands.put("shop", shop);
		commands.put("setplot", setPlot);
		commands.put("getplot", getPlot);
		commands.put("clearplot", clearPlot);
		for (final String command : commands.keySet()) {
			registerCommand(command, this);
		}
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] parameters) {
		commands.get(command.getName()).execute(sender, command.getName(), parameters);
		return true;
	}

	@Override
	public List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
		return commands.get(command.getName()).onTabComplete(sender, command, label, args);
	}

	private void registerCommand(final String commandLabel, final CommandExecutor commandExecutor) {
		final PluginCommand command = plugin.getCommand(commandLabel);
		if (command != null) {
			command.setExecutor(commandExecutor);
		}
	}
}
