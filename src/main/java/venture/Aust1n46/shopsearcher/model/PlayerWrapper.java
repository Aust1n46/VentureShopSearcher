package venture.Aust1n46.shopsearcher.model;

import java.util.UUID;

import org.bukkit.entity.Player;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PlayerWrapper {
	private Player player;
	private final UUID uuid;
	private String username;
	private String location;
}
