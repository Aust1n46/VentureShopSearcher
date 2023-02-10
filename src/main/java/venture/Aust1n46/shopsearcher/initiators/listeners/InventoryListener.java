package venture.Aust1n46.shopsearcher.initiators.listeners;

import java.util.Collection;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import venture.Aust1n46.shopsearcher.controller.GuiController;
import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.model.InventorySession;
import venture.Aust1n46.shopsearcher.model.TradingRecipeWrapper;
import venture.Aust1n46.shopsearcher.model.enums.InventoryViewType;
import venture.Aust1n46.shopsearcher.service.ApiService;
import venture.Aust1n46.shopsearcher.service.ConfigService;

@Singleton
public class InventoryListener implements Listener {
	private static final int PLOT_SEARCH_INVENTORY_WIDTH = 9;
	private static final int CURRENT_PAGE_INDEX = 0;

	@Inject
	private ApiService apiService;
	@Inject
	private GuiController guiController;
	@Inject
	private VentureShopSearcher plugin;
	@Inject
	private ConfigService configService;

	@EventHandler
	public void InventoryClick(final InventoryClickEvent event) {
		final String title = event.getView().getTitle();
		final String plotSearchTitle = configService.getPlotSearchTitle();
		if (!title.contains(configService.getItemSearchTitle()) && !title.contains(plotSearchTitle)) {
			return;
		}
		event.setCancelled(true);
		final ItemStack selectedItem = event.getCurrentItem();
		if (selectedItem == null) {
			return;
		}
		final Material type = selectedItem.getType();
		final Player player = (Player) event.getWhoClicked();
		final InventorySession inventorySession = apiService.getInventorySession(player.getUniqueId());
		final String displayName = selectedItem.getItemMeta().getDisplayName();
		if (type == configService.getCloseGui() && (displayName.equals(configService.getCloseGuiText()) || displayName.equals(configService.getGoBackText()))) {
			if (inventorySession.getInventoryViewType() == InventoryViewType.PLOT_SEARCH) {
				guiController.openItemSearchInventory(player, inventorySession);
			} else {
				plugin.getServer().getScheduler().runTaskLater(plugin, () -> {
					player.closeInventory();
				}, 1);
			}
		} else if (type == configService.getPages()) {
			if (inventorySession.getInventoryViewType() == InventoryViewType.PLOT_SEARCH) {
				final Material material = inventorySession.getSelectedMaterial();
				final Collection<TradingRecipeWrapper> recipeWrappers = inventorySession.getItemsForSale().get(material);
				final int totalPages = recipeWrappers.size() / PLOT_SEARCH_INVENTORY_WIDTH;
				final Map<Material, Integer> plotSearchCurrentPages = inventorySession.getPlotSearchCurrentPages();
				final int plotSearchCurrentPage = plotSearchCurrentPages.get(material);
				if (displayName.equals(configService.getPreviousPageText())) {
					if (plotSearchCurrentPage > CURRENT_PAGE_INDEX) {
						plotSearchCurrentPages.put(material, plotSearchCurrentPage - 1);
						guiController.openPlotSearchInventory(player, inventorySession);
					}
				} else if (displayName.equals(configService.getNextPageText())) {
					if (plotSearchCurrentPage < totalPages) {
						plotSearchCurrentPages.put(material, plotSearchCurrentPage + 1);
						guiController.openPlotSearchInventory(player, inventorySession);
					}
				}
			} else {
				if (displayName.equals(configService.getPreviousPageText())) {
					final int itemSearchCurrentPage = inventorySession.getItemSearchCurrentPage();
					if (itemSearchCurrentPage > CURRENT_PAGE_INDEX) {
						inventorySession.setItemSearchCurrentPage(itemSearchCurrentPage - 1);
					}
				} else if (displayName.equals(configService.getNextPageText())) {
					final int itemSearchCurrentPage = inventorySession.getItemSearchCurrentPage();
					if (itemSearchCurrentPage < inventorySession.getItemSearchTotalPages()) {
						inventorySession.setItemSearchCurrentPage(itemSearchCurrentPage + 1);
					}
				}
				guiController.openItemSearchInventory(player, inventorySession);
			}
		} else if (!title.equals(plotSearchTitle)) {
			inventorySession.setSelectedMaterial(type);
			guiController.openPlotSearchInventory(player, inventorySession);
		}
		player.updateInventory();
	}
}
