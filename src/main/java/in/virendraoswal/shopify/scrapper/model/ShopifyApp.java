package in.virendraoswal.shopify.scrapper.model;

import io.github.millij.poi.ss.model.annotations.SheetColumn;

/**
 * 
 * @author Virendra
 *
 */
public class ShopifyApp {

	private String category, subCategory, appName, appUrl;
	private Double rating = 0.0; // no rating found
	private Integer reviews = 0; // no reviews found
	private String usage;

	public ShopifyApp(String category, String subCategory) {
		super();
		this.category = category;
		this.subCategory = subCategory;
	}

	@Override
	public String toString() {
		return "ShopifyApp [category=" + category + ", subCategory=" + subCategory + ", appName=" + appName
				+ ", appUrl=" + appUrl + ", rating=" + rating + ", reviews=" + reviews + ", usage=" + usage + "]";
	}

	@SheetColumn("Category")
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@SheetColumn("SubCategory")
	public String getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(String subCategory) {
		this.subCategory = subCategory;
	}

	@SheetColumn("Application")
	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	@SheetColumn("Url")
	public String getAppUrl() {
		return appUrl;
	}

	public void setAppUrl(String appUrl) {
		this.appUrl = appUrl;
	}

	@SheetColumn("Rating")
	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	@SheetColumn("Reviews")
	public Integer getReviews() {
		return reviews;
	}

	public void setReviews(Integer reviews) {
		this.reviews = reviews;
	}

	@SheetColumn("Usage")
	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}
