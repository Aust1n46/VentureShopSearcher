package venture.Aust1n46.shopsearcher.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.Multimap;

import lombok.AllArgsConstructor;
import lombok.Data;
import venture.Aust1n46.shopsearcher.model.enums.InventoryViewType;

@Data
@AllArgsConstructor
public class InventorySession {
	private UUID uuid;
	private List<ItemStack> itemsToDisplay;
	private Multimap<Material, TradingRecipeWrapper> itemsForSale;
	private Map<Material, Integer> plotSearchCurrentPages;
	private InventoryViewType inventoryViewType;
	private int itemSearchCurrentPage;
	private int itemSearchTotalPages;
	private Material selectedMaterial;
}
