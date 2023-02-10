package venture.Aust1n46.shopsearcher.initiators.application;

import com.google.inject.AbstractModule;
import com.nisovin.shopkeepers.SKShopkeepersPlugin;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PluginModule extends AbstractModule {
	private final VentureShopSearcher plugin;
	private final SKShopkeepersPlugin shopKeepersPlugin;

	@Override
	protected void configure() {
		bind(VentureShopSearcher.class).toInstance(plugin);
		bind(SKShopkeepersPlugin.class).toInstance(shopKeepersPlugin);
	}
}
