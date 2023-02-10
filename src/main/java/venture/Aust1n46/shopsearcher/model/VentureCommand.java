package venture.Aust1n46.shopsearcher.model;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface VentureCommand {
	public void execute(final CommandSender sender, final String command, final String[] args);

	public default List<String> onTabComplete(final CommandSender sender, final Command command, final String label, final String[] args) {
		return null;
	}
}
