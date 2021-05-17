package in.virendraoswal;

import in.virendraoswal.shopify.output.IOutputGenerator;
import in.virendraoswal.shopify.output.impl.ConsoleOutputGenerator;
import in.virendraoswal.shopify.output.impl.ExcelOutputGenerator;
import in.virendraoswal.shopify.scrapper.ShopifyAppScrapper;

/**
 * Trigger Point.
 * @author Virendra
 *
 */
public class MainApp {

	public static void main(String[] args) {
		new ShopifyAppScrapper(OutputGeneratorFactory.getOutputFactory()).scrape();
	}

	static class OutputGeneratorFactory {
		public static IOutputGenerator getOutputFactory() {
			String OUTPUT = System.getProperty("output.generator", "EXCEL");
			switch (OUTPUT) {
			case "CONSOLE":
				return new ConsoleOutputGenerator();
			default:
				return ExcelOutputGenerator.getInstance();
			}
		}
	}

}
