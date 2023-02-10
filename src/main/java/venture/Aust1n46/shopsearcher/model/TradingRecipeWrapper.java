package venture.Aust1n46.shopsearcher.model;

import com.nisovin.shopkeepers.api.shopkeeper.TradingRecipe;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TradingRecipeWrapper {
	private TradingRecipe recipe;
	private String location;
}
