package venture.Aust1n46.shopsearcher.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.entity.Player;

import com.google.inject.Singleton;

import venture.Aust1n46.shopsearcher.model.InventorySession;
import venture.Aust1n46.shopsearcher.model.PlayerWrapper;

@Singleton
public class ApiService {
	private final Map<UUID, PlayerWrapper> playerWrappers = new HashMap<>();
	private final Map<UUID, InventorySession> inventorySessions = new HashMap<>();
	private final Map<String, UUID> usernamesToUuids = new HashMap<>();

	public void addPlayerWrapper(final PlayerWrapper playerWrapper) {
		playerWrappers.put(playerWrapper.getUuid(), playerWrapper);
	}

	public PlayerWrapper getPlayerWrapper(final UUID uuid) {
		return playerWrappers.get(uuid);
	}

	public PlayerWrapper getPlayerWrapper(final Player player) {
		return getPlayerWrapper(player.getUniqueId());
	}

	public PlayerWrapper getPlayerWrapper(final String username) {
		return getPlayerWrapper(usernamesToUuids.get(username));
	}

	public void removePlayerWrapper(final UUID uuid) {
		playerWrappers.remove(uuid);
	}

	public Collection<PlayerWrapper> getPlayerWrappers() {
		return playerWrappers.values();
	}

	public void createInventorySession(final InventorySession inventorySession) {
		inventorySessions.put(inventorySession.getUuid(), inventorySession);
	}

	public InventorySession getInventorySession(final UUID uuid) {
		return inventorySessions.get(uuid);
	}

	public void addUsernameToUuidMap(final String username, final UUID uuid) {
		usernamesToUuids.put(username, uuid);
	}

	public void removeUsernameFromMap(final String username) {
		usernamesToUuids.remove(username);
	}
}
