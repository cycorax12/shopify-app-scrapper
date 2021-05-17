package in.virendraoswal.shopify.output;

import java.util.List;

import in.virendraoswal.shopify.scrapper.model.ShopifyApp;

/**
 * Ways of Outputting apps details.
 * @author Virendra
 *
 */
public interface IOutputGenerator {
	void output(List<ShopifyApp> apps);
}
