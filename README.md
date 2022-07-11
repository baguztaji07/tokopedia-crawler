# Tokopedia Scraper

Tokopedia Scraper is a webscraping program to retrieve list of product data from Tokopedia and generate it into csv. The App use two scraping method. First, directly from json that containing product name, url, image url, rating, price, store name. Second, using jsop library that will scrape for the description data.

## JSON data example
```json
{
  "id": 1792142206,
  "url": "https://www.tokopedia.com/ptpratamasemesta/iphone-11-128gb-garansi-resmi-tam-ibox-white-64-new-packed?extParam=ivf%3Dfalse",
  "imageUrl": "https://images.tokopedia.net/img/cache/200-square/VqbcmM/2021/12/29/e68cb8aa-739d-4600-b53c-8579fd193fd8.png",
  "imageUrlLarge": "https://images.tokopedia.net/img/cache/700/VqbcmM/2021/12/29/e68cb8aa-739d-4600-b53c-8579fd193fd8.png",
  "catId": 65,
  "gaKey": "/category/handphone-tablet/handphone/ptpratamasemesta/iphone-11-128gb-garansi-resmi-tam-ibox-white-64-new-packed",
  "countReview": 2479,
  "discountPercentage": 7,
  "preorder": false,
  "name": "Iphone 11 128GB Garansi Resmi TAM / Ibox",
  "price": "Rp7.429.000",
  "original_price": "Rp7.950.000",
  "rating": 5,
  "wishlist": false,
  "labels": [
    {
      "title": "Tukar Tambah",
      "color": "#42b549",
      "__typename": "AceSearchLabel"
    }
  ],
  "badges": [
    {
      "imageUrl": "https://images.tokopedia.net/img/official_store_badge.png",
      "show": true,
      "__typename": "AceSearchBadge"
    }
  ],
  "shop": {
    "id": 717871,
    "url": "https://www.tokopedia.com/ptpratamasemesta",
    "name": "PT Pratama Sntra Semesta",
    "goldmerchant": true,
    "official": true,
    "reputation": "https://inbox.tokopedia.com/reputation/v1/badge/shop/717871",
    "clover": "https://clover.tokopedia.com/badges/merchant/v1?shop_id=717871",
    "location": "Jakarta Barat",
    "__typename": "AceShop"
  },
  "labelGroups": [
    {
      "position": "promo",
      "title": "Cashback",
      "type": "lightGreen",
      "__typename": "AceSearchLabelUnify"
    },
    {
      "position": "offers",
      "title": "Bisa Tukar Tambah",
      "type": "lightGrey",
      "__typename": "AceSearchLabelUnify"
    },
    {
      "position": "integrity",
      "title": "Terjual 4 rb+",
      "type": "textDarkGrey",
      "__typename": "AceSearchLabelUnify"
    },
    {
      "position": "campaign",
      "title": "Produk Apple",
      "type": "",
      "__typename": "AceSearchLabelUnify"
    }
  ],
  "__typename": "AceSearchProduct"
}
```

