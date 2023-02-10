package venture.Aust1n46.shopsearcher.initiators.listeners;

import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import venture.Aust1n46.shopsearcher.model.PlayerWrapper;
import venture.Aust1n46.shopsearcher.service.ApiService;

@Singleton
public class LoginListener implements Listener {
	@Inject
	private ApiService apiService;

	@EventHandler
	public void onLogin(final PlayerLoginEvent event) {
		final Player player = event.getPlayer();
		final String username = player.getName();
		final UUID playerUuid = player.getUniqueId();
		PlayerWrapper playerWrapper = apiService.getPlayerWrapper(playerUuid);
		if (playerWrapper == null) {
			playerWrapper = new PlayerWrapper(player, playerUuid, username, null);
			apiService.addPlayerWrapper(playerWrapper);
			apiService.addUsernameToUuidMap(username, playerUuid);
		}
		final String oldUsername = playerWrapper.getUsername();
		if (!oldUsername.equals(username)) {
			apiService.removeUsernameFromMap(oldUsername);
			playerWrapper.setUsername(username);
			apiService.addUsernameToUuidMap(username, playerUuid);
		}
		playerWrapper.setPlayer(player);
	}
}
