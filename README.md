# Shopify App Scrapper

This java based scrapper extract all [applications](https://apps.shopify.com/) on shopify outputing each applications following details:

* Application Name
* Reviews Count
* Rating out of 5
* Absolute URL for application.
* Category
* Subcategory (Few apps wont have subcategory in that case both category and subcategory are same.)
* And Plan Details (Free to use? Free trial period etc ?)

## Prequisites
* JDK 8+
* Maven

## External Dependencies 

* [JSoup](https://jsoup.org/)
* [POI Excel Java Object Mapper](https://github.com/millij/poi-object-mapper)

### How to run
* Clone Repository and import in your favorite IDE. Make sure code compiles once dependency are fetched.
* Run **in.virendraoswal.MainApp** as Java application
* By default code outputs a excel file **(shopify_app.xlsx)** containing all details as specified above in same directory. However if you want to console ouput. Pass environment property **-Doutput.generator=CONSOLE**

### Gotchas
* If you get any network issue or connection issues, may be issue with firewall or shopfiy bots acting to stop connection from your IP. In that case you can pass an active proxy to Jsoup object as part of HTML document fetch as below:

```
Proxy proxy = new Proxy(Proxy.Type.HTTP,new InetSocketAddress("127.0.0.1", 1080));

Jsoup.connect("<url-to-pull-document>")
  .proxy(proxy)
  .get();
```

You can pull proxy from online, many sites provide free proxy for use.



## NOTE
* This is just for some data analysis purpose, and not used in any commercial way or in production.
* Sample excel file generated can be found [here.](https://github.com/cycorax12/shopify-app-scrapper/blob/main/samples/shopify_app.xlsx)