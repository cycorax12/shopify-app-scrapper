package in.virendraoswal.shopify.output.impl;

import java.util.List;

import in.virendraoswal.shopify.output.IOutputGenerator;
import in.virendraoswal.shopify.scrapper.model.ShopifyApp;

/**
 * Console outputs all apps fetched.
 * @author Virendra
 *
 */
public class ConsoleOutputGenerator implements IOutputGenerator {

	@Override
	public void output(List<ShopifyApp> apps) {
		apps.forEach(System.out::println);
	}

}
