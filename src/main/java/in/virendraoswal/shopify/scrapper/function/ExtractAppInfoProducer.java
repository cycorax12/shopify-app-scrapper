package in.virendraoswal.shopify.scrapper.function;

import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.nodes.Element;

import in.virendraoswal.shopify.scrapper.model.ShopifyApp;

/**
 * Produces final result to fetch app name, url, reviews count, rating, and app
 * usage text.
 * 
 * @author Virendra
 *
 */

public class ExtractAppInfoProducer implements Function<Element, ShopifyApp> {

	private Pattern p = Pattern.compile("\\d+");

	private String category, subCategory;

	public ExtractAppInfoProducer(String category, String subCategory) {
		this.category = category;
		this.subCategory = subCategory;
	}

	@Override
	public ShopifyApp apply(Element appElement) {

		ShopifyApp app = new ShopifyApp(category, subCategory);

		/**
		 * Most approach is brute force to expect elements to be rendered on screen to
		 * avoid null checks and running iterator unwantedly.
		 */
		app.setAppName(appElement.getElementsByTag("h2").get(0).text());
		app.setAppUrl(appElement.getElementsByTag("a").get(0).attr("href"));
		app.setUsage(appElement.select("div.ui-app-pricing.ui-app-pricing--format-short").get(0).text());
		try {
			String rating = appElement.select("span.ui-star-rating__rating").get(0).text();
			app.setRating(Double.valueOf(rating.substring(0, rating.indexOf("of")).trim()));
		} catch (Exception e) {
			System.err.println(String.format("No rating found for %s. Category: %s | Subcategory: %s", app.getAppName(),
					category, subCategory));
		}
		try {
			Matcher m = p.matcher(appElement.select("span.ui-review-count-summary").get(0).text());
			if (m.find()) {
				app.setReviews(Integer.valueOf(m.group()));
			}
		} catch (Exception e) {
			System.err.println(String.format("No review found for %s. Category: %s | Subcategory: %s", app.getAppName(),
					category, subCategory));
		}

		return app;
	}

}
