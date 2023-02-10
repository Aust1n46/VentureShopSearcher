package venture.Aust1n46.shopsearcher.controllers.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nisovin.shopkeepers.SKShopkeepersPlugin;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;
import com.nisovin.shopkeepers.api.shopkeeper.player.PlayerShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.AbstractShopkeeper;
import com.nisovin.shopkeepers.shopkeeper.registry.SKShopkeeperRegistry;

import venture.Aust1n46.shopsearcher.controller.GuiController;
import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.localization.LocalizedMessage;
import venture.Aust1n46.shopsearcher.model.InventorySession;
import venture.Aust1n46.shopsearcher.model.PlayerWrapper;
import venture.Aust1n46.shopsearcher.model.TradingRecipeWrapper;
import venture.Aust1n46.shopsearcher.model.VentureCommand;
import venture.Aust1n46.shopsearcher.model.enums.InventoryViewType;
import venture.Aust1n46.shopsearcher.service.ApiService;
import venture.Aust1n46.shopsearcher.service.ConfigService;

@Singleton
public class Shop implements VentureCommand {
	private static final int INVENTORY_SIZE = 45;
	private static final int CURRENT_PAGE_INDEX = 0;

	@Inject
	private SKShopkeepersPlugin shopKeepersPlugin;
	@Inject
	private ApiService apiService;
	@Inject
	private GuiController guiController;
	@Inject
	private VentureShopSearcher plugin;
	@Inject
	private ConfigService configService;

	@Override
	public void execute(final CommandSender sender, final String command, final String[] args) {
		if (!(sender instanceof Player)) {
			plugin.getServer().getConsoleSender().sendMessage(LocalizedMessage.COMMAND_MUST_BE_RUN_BY_PLAYER.toString());
			return;
		}
		if (sender.hasPermission("ventureshopsearcher.shop")) {
			final Player player = (Player) sender;
			final Set<Material> existingItems = new HashSet<>();
			final List<ItemStack> itemsToDisplay = new ArrayList<>();
			final Multimap<Material, TradingRecipeWrapper> itemsForSale = HashMultimap.create();
			final Map<Material, Integer> plotSearchCurrentPages = new HashMap<>();
			final SKShopkeeperRegistry registry = shopKeepersPlugin.getShopkeeperRegistry();
			for (final AbstractShopkeeper shopKeeper : registry.getAllShopkeepers()) {
				final UUID ownerUuid;
				if (shopKeeper instanceof PlayerShopkeeper) {
					final PlayerShopkeeper playerShopKeeper = (PlayerShopkeeper) shopKeeper;
					ownerUuid = playerShopKeeper.getOwnerUUID();
				} else {
					ownerUuid = null;
				}
				final String location;
				if (ownerUuid != null) {
					final PlayerWrapper playerWrapper = apiService.getPlayerWrapper(ownerUuid);
					location = playerWrapper != null && playerWrapper.getLocation() != null ? playerWrapper.getLocation() : configService.getNoPlotFoundText();
				} else {
					location = configService.getAdminPlotText();
				}
				for (final TradingRecipe recipe : shopKeeper.getTradingRecipes(null)) {
					final ItemStack itemStack = recipe.getResultItem().copy();
					final Material itemType = itemStack.getType();
					itemsForSale.put(itemType, new TradingRecipeWrapper(recipe, location));
					itemStack.setItemMeta(null);
					itemStack.setAmount(1);
					if (!existingItems.contains(itemType)) {
						plotSearchCurrentPages.put(itemType, CURRENT_PAGE_INDEX);
						itemsToDisplay.add(itemStack);
						existingItems.add(itemType);
					}
				}
			}
			final int itemsToDisplaySize = itemsToDisplay.size();
			final int totalPages = itemsToDisplaySize / INVENTORY_SIZE;
			final InventorySession inventorySession = new InventorySession(player.getUniqueId(), itemsToDisplay, itemsForSale, plotSearchCurrentPages,
					InventoryViewType.ITEM_SEARCH, CURRENT_PAGE_INDEX, totalPages, Material.AIR);
			apiService.createInventorySession(inventorySession);
			guiController.openItemSearchInventory(player, inventorySession);
		} else {
			sender.sendMessage(LocalizedMessage.COMMAND_NO_PERMISSION.toString());
		}
	}
}
