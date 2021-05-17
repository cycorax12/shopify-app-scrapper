package in.virendraoswal.shopify.output.impl;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import in.virendraoswal.shopify.output.IOutputGenerator;
import in.virendraoswal.shopify.scrapper.model.ShopifyApp;
import io.github.millij.poi.ss.writer.SpreadsheetWriter;

/**
 * Outputs all apps fetch to excel.
 * @author Virendra
 *
 */
@SuppressWarnings("deprecation")
public class ExcelOutputGenerator implements IOutputGenerator {

	private final String OUTPUT_PATH = "shopify_app.xlsx";

	@SuppressWarnings("deprecation")
	private SpreadsheetWriter writer;

	private static ExcelOutputGenerator INSTANCE = new ExcelOutputGenerator();

	public static ExcelOutputGenerator getInstance() {
		return INSTANCE;
	}

	private ExcelOutputGenerator() {
		try {
			writer = new SpreadsheetWriter(OUTPUT_PATH);
		} catch (FileNotFoundException e) {
			System.err.println("Error initialsing excel file");
			throw new IllegalStateException("Ecel file not initialised");
		}
	}

	@Override
	public void output(List<ShopifyApp> apps) {
		writer.addSheet(ShopifyApp.class, apps);
		try {
			writer.write();
		} catch (IOException e) {
			System.err.println("Error while writing to excel file");
			System.err.println(e);
		}
	}

}
