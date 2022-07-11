
# Tokopedia Scraper

Tokopedia Scraper is a webscraping program to retrieve list of product data from Tokopedia and generate it into csv. The App use two scraping method. First, **directly from json** that containing product name, url, image url, rating, price, store name, description. Second, using **jsop library** that will scrape for the description data.




## Features

- Fetch 100 top product data with 'handphone' tag
- Export the data to CSV file


## Usage/Examples

Fetch data directly from JSON. uncomment fetchDescription() function and comment getDescriptionJsop();
    
```java
private static void storeData(String data, int page) throws IOException {
    try {
        ...
        for (int i=0;i<items.length()&&iterator<total;i++) {
            ...
            
            ////fetch description using tokopedia json
            System.out.println("storeId: "+storeId);
            String description = fetchDescription(urll,storeId);
            ////fetch description using Jsoup
//			String description = getDescriptionJsop(urll);

            ...
        }
        ...
    }
    ...
}
```



Fetch data using Jsoup. Uncomment getDescriptionJsop() function and comment fetchDescription();
    
```java
private static void storeData(String data, int page) throws IOException {
    try {
        ...
        for (int i=0;i<items.length()&&iterator<total;i++) {
            ...
            
            ////fetch description using tokopedia json
            System.out.println("storeId: "+storeId);
//          String description = fetchDescription(urll,storeId);
            ////fetch description using Jsoup
		    String description = getDescriptionJsop(urll);

            ...
        }
        ...
    }
    ...
}
```


## Comparation

Time consumed to fetch data from json
![App Screenshot](https://github.com/baguztaji07/tokopedia-crawler/raw/main/usingjson.jpg)

TIme consumed to fetch data using Jsoup
![App Screenshot](https://github.com/baguztaji07/tokopedia-crawler/raw/main/usingjsoup.jpg)
