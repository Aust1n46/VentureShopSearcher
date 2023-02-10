package venture.Aust1n46.shopsearcher.controllers.commands;

import org.bukkit.command.CommandSender;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import venture.Aust1n46.shopsearcher.localization.LocalizedMessage;
import venture.Aust1n46.shopsearcher.model.PlayerWrapper;
import venture.Aust1n46.shopsearcher.model.VentureCommand;
import venture.Aust1n46.shopsearcher.service.ApiService;

@Singleton
public class SetPlot implements VentureCommand {
	@Inject
	private ApiService apiService;

	@Override
	public void execute(final CommandSender sender, final String command, final String[] args) {
		if (sender.hasPermission("ventureshopsearcher.setplot")) {
			if (args.length < 2) {
				sender.sendMessage(LocalizedMessage.COMMAND_INVALID_ARGUMENTS.toString().replace("{command}", "/setplot").replace("{args}", "[player] [location]"));
				return;
			}
			final String targetPlayerUsername = args[0];
			final PlayerWrapper targetPlayerWrapper = apiService.getPlayerWrapper(targetPlayerUsername);
			if (targetPlayerWrapper != null) {
				final String location = args[1];
				targetPlayerWrapper.setLocation(location);
				sender.sendMessage("Set plot location to: " + location);
			} else {
				sender.sendMessage(LocalizedMessage.PLAYER_OFFLINE.toString().replace("{args}", targetPlayerUsername));
			}
		} else {
			sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
		}
	}
}
