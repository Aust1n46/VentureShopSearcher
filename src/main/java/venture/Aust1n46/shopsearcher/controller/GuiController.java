package venture.Aust1n46.shopsearcher.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.collect.Multimap;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;

import venture.Aust1n46.shopsearcher.initiators.application.VentureShopSearcher;
import venture.Aust1n46.shopsearcher.model.InventorySession;
import venture.Aust1n46.shopsearcher.model.TradingRecipeWrapper;
import venture.Aust1n46.shopsearcher.model.enums.InventoryViewType;
import venture.Aust1n46.shopsearcher.service.ConfigService;

@Singleton
public class GuiController {
	private static final int ITEM_SEARCH_INVENTORY_SIZE = 54;
	private static final int ITEM_SEARCH_INVENTORY_USABLE_SIZE = 45;
	private static final int ITEM_SEARCH_PREVIOUS_ICON_INDEX = 45;
	private static final int ITEM_SEARCH_CLOSE_ICON_INDEX = 49;
	private static final int ITEM_SEARCH_NEXT_ICON_INDEX = 53;
	private static final int PLOT_SEARCH_INVENTORY_SIZE = 45;
	private static final int PLOT_SEARCH_INVENTORY_WIDTH = 9;
	private static final int PLOT_SEARCH_PREVIOUS_ICON_INDEX = 36;
	private static final int PLOT_SEARCH_CLOSE_ICON_INDEX = 40;
	private static final int PLOT_SEARCH_NEXT_ICON_INDEX = 44;
	private static final int CURRENT_PAGE_INDEX = 0;
	private static final int INVENTORY_SLOT_INDEX = 0;

	@Inject
	private VentureShopSearcher plugin;
	@Inject
	private ConfigService configService;

	public void openItemSearchInventory(final Player player, final InventorySession session) {
		final Inventory shopInventory = plugin.getServer().createInventory(null, ITEM_SEARCH_INVENTORY_SIZE, configService.getItemSearchTitle());
		final List<ItemStack> itemsToDisplay = session.getItemsToDisplay();
		int start = session.getItemSearchCurrentPage() * ITEM_SEARCH_INVENTORY_USABLE_SIZE;
		int firstPageDisplaySize = start + ITEM_SEARCH_INVENTORY_USABLE_SIZE;
		if (itemsToDisplay.size() < firstPageDisplaySize) {
			firstPageDisplaySize = itemsToDisplay.size();
		}
		int slotIndex = INVENTORY_SLOT_INDEX;
		for (int i = start; i < firstPageDisplaySize; i++) {
			shopInventory.setItem(slotIndex++, itemsToDisplay.get(i));
		}
		final int totalPages = session.getItemSearchTotalPages();
		final int currentPage = session.getItemSearchCurrentPage();
		final Material pages = configService.getPages();
		if (currentPage > CURRENT_PAGE_INDEX) {
			final ItemStack pageBackIcon = new ItemStack(pages);
			setDisplayName(pageBackIcon, configService.getPreviousPageText());
			shopInventory.setItem(ITEM_SEARCH_PREVIOUS_ICON_INDEX, pageBackIcon);
		}
		final ItemStack backIcon = new ItemStack(configService.getCloseGui());
		setDisplayName(backIcon, configService.getCloseGuiText());
		shopInventory.setItem(ITEM_SEARCH_CLOSE_ICON_INDEX, backIcon);
		if (currentPage < totalPages) {
			final ItemStack pageNextIcon = new ItemStack(pages);
			setDisplayName(pageNextIcon, configService.getNextPageText());
			shopInventory.setItem(ITEM_SEARCH_NEXT_ICON_INDEX, pageNextIcon);
		}
		session.setInventoryViewType(InventoryViewType.ITEM_SEARCH);
		player.openInventory(shopInventory);
	}

	public void openPlotSearchInventory(final Player player, final InventorySession session) {
		final Inventory shopInventory = plugin.getServer().createInventory(null, PLOT_SEARCH_INVENTORY_SIZE, configService.getPlotSearchTitle());
		final Multimap<Material, TradingRecipeWrapper> itemsForSale = session.getItemsForSale();
		final List<TradingRecipeWrapper> recipeWrappers = new ArrayList<>(itemsForSale.get(session.getSelectedMaterial()));
		final Map<Material, Integer> plotSearchCurrentPages = session.getPlotSearchCurrentPages();
		final int plotSearchCurrentPage = plotSearchCurrentPages.get(session.getSelectedMaterial());
		int start = plotSearchCurrentPage * PLOT_SEARCH_INVENTORY_WIDTH;
		int firstPageDisplaySize = start + PLOT_SEARCH_INVENTORY_WIDTH;
		if (recipeWrappers.size() < firstPageDisplaySize) {
			firstPageDisplaySize = recipeWrappers.size();
		}
		int slotIndex = INVENTORY_SLOT_INDEX;
		for (int i = start; i < firstPageDisplaySize; i++) {
			final TradingRecipeWrapper recipeWrapper = recipeWrappers.get(i);
			final TradingRecipe recipe = recipeWrapper.getRecipe();
			final ItemStack resultItem = recipe.getResultItem().copy();
			final ItemStack item1 = recipe.getItem1().copy();
			final ItemStack item2 = recipe.getItem2() != null ? recipe.getItem2().copy() : null;
			final ItemStack isInStock = recipe.isOutOfStock() ? new ItemStack(configService.getOutOfStock()) : new ItemStack(configService.getInStock());
			final String location = recipeWrapper.getLocation();
			setDisplayName(isInStock, location);
			shopInventory.setItem(slotIndex, resultItem);
			shopInventory.setItem(slotIndex + PLOT_SEARCH_INVENTORY_WIDTH, isInStock);
			shopInventory.setItem(slotIndex + PLOT_SEARCH_INVENTORY_WIDTH * 2, item1);
			shopInventory.setItem(slotIndex + PLOT_SEARCH_INVENTORY_WIDTH * 3, item2);
			slotIndex++;
		}

		final int totalPages = recipeWrappers.size() / PLOT_SEARCH_INVENTORY_WIDTH;
		final Material pages = configService.getPages();
		if (plotSearchCurrentPage > CURRENT_PAGE_INDEX) {
			final ItemStack pageBackIcon = new ItemStack(pages);
			setDisplayName(pageBackIcon, configService.getPreviousPageText());
			shopInventory.setItem(PLOT_SEARCH_PREVIOUS_ICON_INDEX, pageBackIcon);
		}
		final ItemStack backIcon = new ItemStack(configService.getCloseGui());
		setDisplayName(backIcon, configService.getGoBackText());
		shopInventory.setItem(PLOT_SEARCH_CLOSE_ICON_INDEX, backIcon);
		if (plotSearchCurrentPage < totalPages) {
			final ItemStack pageNextIcon = new ItemStack(pages);
			setDisplayName(pageNextIcon, configService.getNextPageText());
			shopInventory.setItem(PLOT_SEARCH_NEXT_ICON_INDEX, pageNextIcon);
		}
		session.setInventoryViewType(InventoryViewType.PLOT_SEARCH);
		player.openInventory(shopInventory);
	}

	private void setDisplayName(final ItemStack itemStack, final String displayName) {
		final ItemMeta itemMeta = itemStack.getItemMeta();
		itemMeta.setDisplayName(displayName);
		itemStack.setItemMeta(itemMeta);
	}
}
