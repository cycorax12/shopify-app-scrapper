package in.virendraoswal.shopify.scrapper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import in.virendraoswal.shopify.output.IOutputGenerator;
import in.virendraoswal.shopify.scrapper.function.ExtractAppInfoProducer;
import in.virendraoswal.shopify.scrapper.model.ShopifyApp;

/**
 * Scrapper, outputs apps based on data provided as part of
 * in.virendraoswal.shopify.scrapper.model.ShopifyApp model.
 * 
 * @author Virendra
 *
 */
public class ShopifyAppScrapper {

	private static final String NAV_ITEM_CATEGORIES = "Categories";
	private static final String SHOPIFY_APPS_URL = "https://apps.shopify.com/";

	private IOutputGenerator outputGenerator;

	public ShopifyAppScrapper(IOutputGenerator outputGenerator) {
		this.outputGenerator = outputGenerator;
	}

	public void scrape() {
		try {
			List<ShopifyApp> apps = new ArrayList<ShopifyApp>();
			Document doc = Jsoup.connect(SHOPIFY_APPS_URL).get();
			Elements navigationBarItems = doc.select("ul.marketing-nav__items.display--expanded-nav");
			Iterator<Element> navBarItemsIterator = navigationBarItems.iterator();
			while (navBarItemsIterator.hasNext()) {
				Element navbarItem = navBarItemsIterator.next();

				if (navbarItem.hasClass("marketing-nav__user")) {
					continue;
				}

				Iterator<Element> navbarChilds = navbarItem.children().iterator(); // gets categories and collections as
																					// only immediate child.

				while (navbarChilds.hasNext()) {
					Element navbarChild = navbarChilds.next();
					if (navbarChild.select("button.popover__trigger.marketing-nav__item.marketing-nav__item--primary")
							.get(0).text().equals(NAV_ITEM_CATEGORIES)) {
						Elements allCategories = navbarChild.select("li.as-nav__item.as-nav__item--primary"); // there
																												// are
																												// few
																												// categories
																												// having
																												// subcategories.
						Iterator<Element> categories = allCategories.iterator();
						while (categories.hasNext()) {
							Element category = categories.next();
							Elements topLevelCategory = category.getElementsByTag("span"); // means its top level
																							// category having
																							// subcategory.
							if (topLevelCategory != null && topLevelCategory.size() == 1) {
								Iterator<Element> subCategories = category.select("li.as-nav__item").iterator();
								while (subCategories.hasNext()) {
									Element subCategory = subCategories.next();
									if (subCategory.hasClass("as-nav__item--primary")) { // ignore root category
										continue;
									}
									CategoryUrl subCategoryUrl = fetchApps(subCategory);
									if (subCategoryUrl == null) { // this will be likely null for all subcategories data together, which we are not interested in.
										continue;
									}
									apps.addAll(fetchAppsInformation(topLevelCategory.get(0).text(),
											subCategoryUrl.name, subCategoryUrl.url));
								}
							} else {
								CategoryUrl categoryUrl = fetchApps(category); // means this category doesnt have sub
																				// category
								apps.addAll(fetchAppsInformation(categoryUrl.name, categoryUrl.name, categoryUrl.url));
							}
						}
					}
				}
			}

			outputGenerator.output(apps);
		} catch (Exception e) {
			System.err.println(
					String.format("Failed to fetch document for %s. Error: %s", SHOPIFY_APPS_URL, e.getMessage()));
			e.printStackTrace();
		}
	}

	private List<ShopifyApp> fetchAppsInformation(String category, String subCategory, String categoryAppsUrl) {
		List<ShopifyApp> apps = new ArrayList<ShopifyApp>();
		try {
			Document subCategoryPage = Jsoup.connect(categoryAppsUrl).get();
			Elements appsCount = subCategoryPage.select(
					"div.grid.grid--bleed.grid--vertically-centered.gutter-bottom.search-filters__header.search-results__grid"); // get grid containing all items.
			if (appsCount != null && appsCount.size() == 1) {
				Integer pagesToScrap = 1;
				try {
					String resultsString = appsCount.get(0).child(0).text();
					if (resultsString.contains("of")) { // means results are less than 24 and have only one page.
						Integer resultantApps = Integer.valueOf(resultsString
								.substring(resultsString.indexOf("of") + 2, resultsString.indexOf("results")).trim());
						pagesToScrap = (resultantApps / 24) + (resultantApps % 24 != 0 ? 1 : 0); // if grid is full no
						// need to add extra
						// page, else extra
						// page for partial
						// grid.
					}

					apps.addAll(fetchAllAppsInformation(subCategoryPage, category, subCategory)); // First page is
																									// already
																									// loaded, so no
																									// need to
																									// make extra
				} catch (Exception e) {
					System.err.println(e);
				}
				// call;

				for (int i = 2; i <= pagesToScrap; i++) {
					subCategoryPage = Jsoup.connect(String.format("%s?page=%d", categoryAppsUrl, pagesToScrap)).get();
					apps.addAll(fetchAllAppsInformation(subCategoryPage, category, subCategory));
				}

			}
		} catch (IOException e) {
			System.err.println(
					String.format("Failed to fetch document for %s. Error: %s", SHOPIFY_APPS_URL, e.getMessage()));
			e.printStackTrace();
		}

		return apps;
	}

	private List<ShopifyApp> fetchAllAppsInformation(Document subCategoryPage, String category, String subCategory) {
		Element searchResults = subCategoryPage.getElementById("SearchResultsListings"); // div wrapper displaying all
																							// results.
		Elements apps = searchResults.select("div.grid__item.grid__item--tablet-up-half.grid-item--app-card-listing"); // gets
																														// all
																														// items(apps)
																														// in
																														// grid
		return apps.parallelStream().map(new ExtractAppInfoProducer(category, subCategory))
				.collect(Collectors.toList());
	}

	private CategoryUrl fetchApps(Element subCategory) {
		if (subCategory.hasClass("as-nav__item--secondary-link-appearance")) {
			return null;
		}
		Elements categoryLink = subCategory.getElementsByTag("a");
		Element actualLinkToBrowseApps = categoryLink != null && categoryLink.size() == 1 ? categoryLink.get(0) : null;
		if (actualLinkToBrowseApps != null) {
			return new CategoryUrl(actualLinkToBrowseApps.text(),
					SHOPIFY_APPS_URL.concat(actualLinkToBrowseApps.attr("href")));
		}

		return null;
	}

	static class CategoryUrl {
		String name, url;

		public CategoryUrl(String name, String url) {
			this.name = name;
			this.url = url;
		}

	}

}
