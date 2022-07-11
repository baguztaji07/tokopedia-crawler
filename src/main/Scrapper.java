package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
//import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Scrapper {
	public static TreeMap<Integer, Object[]> product = new TreeMap<Integer,Object[]>();
	public static int iterator = 0;
	public static int total = 100;
	static long startTime;
	static long endTime;
	static long totalTime;
	
	public static void main(String[] args) throws IOException {
		int page = totalOfPages(total);
		System.out.println("total product: "+total);
		System.out.println("total page: "+page+"\n");
		for (int i=1; i<=page; i++) {
			String data = fetchDatas(i, startFrom(i));
			storeData(data,i);
		}
		System.out.println("\nTotal time consumed to fetch all data: "+totalTime/1000000000+"s");
		createExcel();
	}
	
	///fetch list of product from json
	private static String fetchDatas(int page, int start) throws IOException {
		URL url = new URL("https://gql.tokopedia.com/graphql/SearchProductQuery");
		HttpURLConnection http = (HttpURLConnection)url.openConnection();
		http.setRequestMethod("POST");
		http.setDoOutput(true);
		http.setRequestProperty("authority", "gql.tokopedia.com");
		http.setRequestProperty("accept", "*/*");
		http.setRequestProperty("accept-language", "en-US,en;q=0.9");
		http.setRequestProperty("cache-control", "no-cache");
		http.setRequestProperty("content-type", "application/json");
		http.setRequestProperty("cookie", "_UUID_NONLOGIN_=57ecde30e3fa4eec4568aceafdb2895e; _UUID_NONLOGIN_.sig=6Hk2eCMiCZiCOnhAQfs5wkyGFUY; bm_sz=7342091A82A16265B3FF305453D74108~YAAQ7VN9cip9BZSBAQAA5Qmw6BD54Nqwh+MsoD6Wihv5UdZNed0fE7xa8YUY0Y/GNrroJCgsZfoCHIF7ZExOQHoC+jRAzbTL6ELNV783oiDJwu6Bx+q6PSHzSk4qQOwPZxT2rUyN+N2F50H8I0Rgg0o2sgauQVZO574fulXmH6wPPh2JaCmWipx78o65gUop8gERhm2bOLrh4HkTvKqbxwPJSLu0ry1bQqoLNrGijLEAlQ9jKSw2fJN+KJiFQD6BBeHnwftwd/SVUncHcXgjPivconiZcWOE2kTOuX4fN++hmkFhcVQ=~3486768~4338489; _abck=4682ACFA765EEFDC37F6821C0C3E4A19~0~YAAQ7VN9coR9BZSBAQAAkBOw6Aj0n8fvhoqk090EJIjFMsJ4z72gsBdtTyOKwXszFDHg7OR4Put5sV0tKsLhys3J1JO45UmfG1fxsyymelLBHqyj1lIvUY4UvApY9NjDd0953dn/L/bwiSj0yv9q+5v/wCPt8phOiQgNGodiW5cVoh7B/U85JmL2J0vAunlEo3rz29tsW4+QfsMSVBQTyFlG5K2FGs/VMDaDXkNIR6NefPhgzXCEGpB6mQevkGIYj7UbE9lFnflUY48/jjSE/7h1q5WK7VgbwpcbTXtPsa/EV0EcCnpesA4jMCVOQpO+lECkFIsROiiUKJ+XUmJnmM/hBquH5ujE/wxsuFs8bvHJtoFrBDwYb1mBBviiOBPsiF7BUX/uar5oCS+G2uHadIyjyJ+GWY5Y4MRZ~-1~-1~-1; _SID_Tokopedia_=CSE5BO0tNxyZR6hyuoKW6TZQmdhYXFs4romf1li2OUAwCpcfyYAaeGXvuZnFgDXafaoNj6qFvhsl7Jkj2QR7FjwM34OUmJorlCvj7NUf-XmE0gIVYUp3QeElFL5Opxpv; DID=29d7a266680df970cdee32f94f8781c7763141b9e6b412aae9c5e1d9e1f885f45480f7363cc0ef4d6a7ef19bd3589be2; DID_JS=MjlkN2EyNjY2ODBkZjk3MGNkZWUzMmY5NGY4NzgxYzc3NjMxNDFiOWU2YjQxMmFhZTljNWUxZDllMWY4ODVmNDU0ODBmNzM2M2NjMGVmNGQ2YTdlZjE5YmQzNTg5YmUy47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=; _gcl_au=1.1.1139828893.1657466263; _UUID_CAS_=109a1e46-63c2-432c-bb16-37962787a83e; _CASE_=7f26604d60263e363633302826654d60263e342826686668263e264e656f65767065245471776570262826674d60263e3533322826686b6a63263e26262826686570263e2626282674476b263e26262826734d60263e35363635343733312826774d60263e3535313734313337282677507d7461263e26366c262826736c77263e265f7f5826736576616c6b7177615b6d6058263e3536363534373331285826776176726d67615b707d746158263e5826366c58262858265b5b707d74616a65696158263e5826536576616c6b71776177582679287f5826736576616c6b7177615b6d6058263e34285826776176726d67615b707d746158263e582635316958262858265b5b707d74616a65696158263e5826536576616c6b71776177582679592679; _gid=GA1.2.1287425007.1657466263; ak_bmsc=57B61BA90274E7EAACC4DC5EE8E4E569~000000000000000000000000000000~YAAQ7VN9cp59BZSBAQAAORiw6BDkLfKkPOb7ukut9hgZo3J7i61s+kT00z4UysvVsD3RT6jHTOz5iYWmXIDTHXMK+DIOU0J6TYq8zcJvN++RVwdT6a2XiH7TiGy1eG2J620PsspiEBI2xoh8Iqrfx0NMhpCGQ9t0Juz9XmrBwCs06x9cYoE6+3humktRrq/zWkam8yuV14y/YBk6NYop0Vj+F70j8Eqlafv6sPR8WD04Jk5yOKmcIn0wAuJ1ODWfDWHFNc2Q7RxFBluhN22+YRh2AilBpkihk+8Ba/HW2hrl/3/vJA7jD3bUHKQKqA4+wl7H+SxBAQjoF3FhPI+6nx+8yC1DFyFvIjb1Ha3SCqm0viiqt7tBGyXyeTj0BhAahf0+2mbDa7c9CHZaILrthVutac1nX/b/NtlhBBOlS9EpcVWxry3IBS2YHBjZe8b0L9V24p9VctqNFjQEBz00171ivol6upag4tCLNROa3cY2+CUfldivY3dc3T4=; __asc=1a38d053181e8b0652c3bde72b8; __auc=1a38d053181e8b0652c3bde72b8; _dc_gtm_UA-9801603-1=1; _gat_UA-9801603-1=1; _dc_gtm_UA-126956641-6=1; _ga=GA1.2.1442062479.1657466263; _ga_70947XW48P=GS1.1.1657466263.1.1.1657466360.35");
		http.setRequestProperty("iris_session_id", "d3d3LnRva29wZWRpYS5jb20=.ba1e0d05714054277b3150ad96be4281.1657466263346");
		http.setRequestProperty("origin", "https://www.tokopedia.com");
		http.setRequestProperty("pragma", "no-cache");
		http.setRequestProperty("referer", "https://www.tokopedia.com/p/handphone-tablet/handphone?page="+page);
		http.setRequestProperty("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
		http.setRequestProperty("sec-ch-ua-mobile", "?0");
		http.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
		http.setRequestProperty("sec-fetch-dest", "empty");
		http.setRequestProperty("sec-fetch-mode", "cors");
		http.setRequestProperty("sec-fetch-site", "same-site");
		http.setRequestProperty("tkpd-userid", "0");
		http.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
		http.setRequestProperty("x-device", "desktop-0.0");
		http.setRequestProperty("x-source", "tokopedia-lite");
		http.setRequestProperty("x-tkpd-lite-service", "zeus");
		http.setRequestProperty("x-version", "45367b8");

		OutputStreamWriter writer = new OutputStreamWriter(http.getOutputStream());
		writer.write("[{\"operationName\":\"SearchProductQuery\",\"variables\":{\"params\":\"page="+page+"&ob=&identifier=handphone-tablet_handphone&sc=24&user_id=211921174&rows=60&start="+start+"&source=directory&device=desktop&page="+page+"&related=true&st=product&safe_search=false\",\"adParams\":\"page="+page+"&page="+page+"&dep_id=24&ob=&ep=product&item=15&src=directory&device=desktop&user_id=211921174&minimum_item=15&start="+start+"&no_autofill_range=5-14\"},\"query\":\"query SearchProductQuery($params: String, $adParams: String) {  CategoryProducts: searchProduct(params: $params) {    count    data: products {      id      url      imageUrl: image_url      imageUrlLarge: image_url_700      catId: category_id      gaKey: ga_key      countReview: count_review      discountPercentage: discount_percentage      preorder: is_preorder      name      price      original_price      rating      wishlist      labels {        title        color        __typename      }      badges {        imageUrl: image_url        show        __typename      }      shop {        id        url        name        goldmerchant: is_power_badge        official: is_official        reputation        clover        location        __typename      }      labelGroups: label_groups {        position        title        type        __typename      }      __typename    }    __typename  }  displayAdsV3(displayParams: $adParams) {    data {      id      ad_ref_key      redirect      sticker_id      sticker_image      productWishListUrl: product_wishlist_url      clickTrackUrl: product_click_url      shop_click_url      product {        id        name        wishlist        image {          imageUrl: s_ecs          trackerImageUrl: s_url          __typename        }        url: uri        relative_uri        price: price_format        campaign {          original_price          discountPercentage: discount_percentage          __typename        }        wholeSalePrice: wholesale_price {          quantityMin: quantity_min_format          quantityMax: quantity_max_format          price: price_format          __typename        }        count_talk_format        countReview: count_review_format        category {          id          __typename        }        preorder: product_preorder        product_wholesale        free_return        isNewProduct: product_new_label        cashback: product_cashback_rate        rating: product_rating        top_label        bottomLabel: bottom_label        __typename      }      shop {        image_product {          image_url          __typename        }        id        name        domain        location        city        tagline        goldmerchant: gold_shop        gold_shop_badge        official: shop_is_official        lucky_shop        uri        owner_id        is_owner        badges {          title          image_url          show          __typename        }        __typename      }      applinks      __typename    }    template {      isAd: is_ad      __typename    }    __typename  }}\"}]");
		writer.flush();
		writer.close();
		http.getOutputStream().close();

		InputStream responseStream = http.getResponseCode() / 100 == 2
				? http.getInputStream()
				: http.getErrorStream();
		try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
			String response = s.hasNext() ? s.next() : "";
			s.close();
			http.disconnect();
			
			String resp = response.toString();		
					
			return resp;
		}
	}
	
	///create excel from the data
	private static void createExcel() {
		//Blank workbook
		Workbook workbook = new HSSFWorkbook(); 
         
        //Create a blank sheet
        Sheet sheet = workbook.createSheet("Employee Data");
       //Iterate over data and write to sheet
        Set<Integer> keyset = product.keySet();
        int rownum = 0;
        Row rowhead = sheet.createRow(rownum++);
        rowhead.createCell(0).setCellValue("Product Name");
        rowhead.createCell(1).setCellValue("URL"); 
        rowhead.createCell(2).setCellValue("Description"); 
        rowhead.createCell(3).setCellValue("Image Link");  
        rowhead.createCell(4).setCellValue("Price");  
        rowhead.createCell(5).setCellValue("Rating");  
        rowhead.createCell(6).setCellValue("Store Name");  
        
        for (Integer key : keyset)
        {
            Row row = sheet.createRow(rownum++);
            Object [] objArr = product.get(key);
            int cellnum = 0;
            for (Object obj : objArr)
            {
               Cell cell = row.createCell(cellnum++);
               if(obj instanceof String)
                    cell.setCellValue((String)obj);
                else if(obj instanceof Integer)
                    cell.setCellValue((Integer)obj);
            }
        }
  
        // .xlsx is the format for Excel Sheets...
        // writing the workbook into the file...
        try
        {
            //Write the workbook in file system
            FileOutputStream out = new FileOutputStream(new File("D:/Belajar/Tokopedia.csv"));
            workbook.write(out);
            out.close();
            workbook.close();
            System.out.println("Tokopedia.csv written successfully on disk.");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	
	///store data from the fetchDatas function to object
	private static void storeData(String data, int page) throws IOException {
		try {
			JSONArray json = new JSONArray(data);
			JSONObject obj = json.getJSONObject(0).getJSONObject("data").getJSONObject("CategoryProducts");
			JSONArray items = obj.getJSONArray("data");
			startTime = System.nanoTime();
			for (int i=0;i<items.length()&&iterator<total;i++) {
				JSONObject item = items.getJSONObject(i);
				String name = (String) item.get("name");
				String urll = (String) item.get("url");
				String imgLink = (String) item.get("imageUrlLarge");
				String price = (String) item.get("price");
				int rating = (int) item.get("rating");
				String storeName = (String) item.getJSONObject("shop").get("name");
				String storeUrl = (String) item.getJSONObject("shop").get("url");
				String storeId = extractStoreId(storeUrl);
				
				////fetch description using tokopedia json
				System.out.println("storeId: "+storeId);
				String description = fetchDescription(urll,storeId);
				////fetch description using Jsoup
//				String description = getDescriptionJsop(urll);

				product.put(iterator, new Object[] {name, urll, description, imgLink, price, rating, storeName});
				iterator++;
			}
			endTime   = System.nanoTime();
			totalTime = endTime - startTime;
			
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	///fetch product description using jsoup
	public static String getDescriptionJsop(String urll) {
		String url = urll;
		String desc = null;
		try {
			Document document = Jsoup.connect(url).get();
			Elements elements = document.getElementsByAttributeValue("data-testid", "lblPDPDescriptionProduk");

            for(Element element : elements) {
                desc = element.text();
            }
            		
		}
		catch (Exception e) 
        {
            e.printStackTrace();
        }
		return desc;
		
	}
	
	///fetch product description using json
	private static String fetchDescription(String productUrl, String shopName) throws IOException, JSONException {
		String description = null;
		String refererUrl = extractProductUrl(productUrl);
		String productKey = extractProductKey(refererUrl,shopName);
		
		URL url = new URL("https://gql.tokopedia.com/graphql/PDPGetLayoutQuery");
		HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
		httpConn.setRequestMethod("POST");
		httpConn.setDoOutput(true);
		httpConn.setRequestProperty("authority", "gql.tokopedia.com");
		httpConn.setRequestProperty("accept", "*/*");
		httpConn.setRequestProperty("accept-language", "en-US,en;q=0.9");
		httpConn.setRequestProperty("cache-control", "no-cache");
		httpConn.setRequestProperty("content-type", "application/json");
		httpConn.setRequestProperty("cookie", "bm_sz=9E151B56A7503233F40035A493BFCB06~YAAQVfgrF7ckx+uBAQAAN6Q67RBVGVnhdwKjXoECx4Pe+Pjt3u8nMqcEuIqZADXZT50gcxyotFpV5lGharzHmUZrBvytGjlQ1VDpL9nvfWKGNvgjwG3KZbCJwN4XkFNgQqBuJTb7wBS+BJFOEjogm5e9SyoN7g7r2R0JuDnKuG4zVYnlw0qWCqlTCmKqR+jy0G8euIto9lWtVd6h8RWo9DzCD/rek5BW8N3+gn9hOGjmdm/l5GbaSg0hRMV5FVZO2l9lRVGVQOwKCYDp4aDvQo663I1tJLW0B7leoPuT9VN4+uOXo8M=~4601413~3420471; _gcl_au=1.1.1230393353.1657542452; ak_bmsc=8550935F5DF67E6FF0DA167BD2662918~000000000000000000000000000000~YAAQVfgrF8Ukx+uBAQAAtqg67RDoNPo8eRFO8bRRHCQ6jai7N5noYRYJWVrdEmXRdI1dI2JmjzXaHAzkqHBk8PnFZRV9pOwwLd2ZCSnlMpOJu//XZRynqMGu8+wlynhEQwfYT03EnJho7ikaDJoqE6944w+WUzcHzpedLbCrjZmF6xiQbzn27ht9NyTOzJDoleH3Rcr1pgIHCp0SCE+gi0P4C8B7lAewxb9gt5SNmmvG+6lIns+r1mbpw8DLuiOvV5WMZR/FB8uLAaXSJbW8hG7sAbPL3dRAHOJJstMJ24MglRaF+X16W7prxCQ1jdYkZteCD4TWGzQ0bw4B4e8n0PgXGJfmbzCOqp89pQH58fAS6YrYMhnr+Z3GgAWjTn9qJ57JBGT2gLGRrcL3qluZgZWgTF+86FjR10iYMK7vWpv6S7Vp4DM5lmaFF8qZiVAjdtixK1B37PVoge6KaJBxWRT6s5pXPL0YntpNaJuEBjF789tFaoQZztFZy7Fg; _gid=GA1.2.195762451.1657542453; _UUID_NONLOGIN_=d5e2e238e7817b95a57f3194b1c20a89; _SID_Tokopedia_=gt2r4oUZ1SUPU6RjtfTU7vZFurpyJZDSZkouUkqE34V6MEAq2p-4wempiAKAK9gzYLtpgGlsnu6eiI0dfbKVwjO3g590YDQXJhXElzEnD3RpO2gEIedeWBynGwABWlTW; DID=75dc51f61b75e824b19def512bdc3abef0e179fb30e61a3a64432cd660d1795edcd67788cf57e283ab1fcdfc4553da31; DID_JS=NzVkYzUxZjYxYjc1ZTgyNGIxOWRlZjUxMmJkYzNhYmVmMGUxNzlmYjMwZTYxYTNhNjQ0MzJjZDY2MGQxNzk1ZWRjZDY3Nzg4Y2Y1N2UyODNhYjFmY2RmYzQ1NTNkYTMx47DEQpj8HBSa+/TImW+5JCeuQeRkm5NMpJWZG3hSuFU=; AMP_TOKEN=%24NOT_FOUND; hfv_banner=true; _UUID_CAS_=56cb8ef6-7888-466b-aef2-7795b5b76d7c; _CASE_=752c6a476a2c343c3c393a222c6f476a2c343e222c626c622c342c446f656f7c7a6f2e5e7b7d6f7a2c222c6d476a2c343f3938222c626160692c342c2c222c626f7a2c342c2c222c7e4d612c342c2c222c79476a2c343f3c3c3f3e3d393b222c7d476a2c343f3f3b3d3e3b393d222c7d5a777e6b2c342c3c662c222c79667d2c342c5575522c796f7c6b66617b7d6b51676a522c343f3c3c3f3e3d393b22522c7d6b7c78676d6b517a777e6b522c34522c3c66522c22522c51517a777e6b606f636b522c34522c596f7c6b66617b7d6b7d522c732275522c796f7c6b66617b7d6b51676a522c343e22522c7d6b7c78676d6b517a777e6b522c34522c3f3b63522c22522c51517a777e6b606f636b522c34522c596f7c6b66617b7d6b7d522c73532c73; __asc=47d4e892181ed3acf0ad6dc1e4c; __auc=47d4e892181ed3acf0ad6dc1e4c; _dc_gtm_UA-126956641-6=1; _gat_UA-9801603-1=1; _dc_gtm_UA-9801603-1=1; _abck=4443C64CF343383BA7F7218F99951AE2~0~YAAQlFB9ctrTPtuBAQAAwRJL7QgYP3LQFcT64IxFB0K5jBvGJb99+1WAZZlT0dVxdCre0Dkgc7AESXKt+QwvTEMn/YYfq+VhIbtj5IQlnwHJVzMiXnSyaWmBJoPQcDJaG1SL5A/qmkJxh1zTpHoCw1Ms4HZjr+5Vl0AKEihTmhy3U/DtPSR7qtOPyKEL1jlf1628DaQEQ+WOzPzTD3CX2w8zPl1wOGhCkpNAZQ0pekz/qEXG3MRGbtS9aw1h/KON7q4ZW2d3upZJJSHngaOi2quFbubN1G+7UOz2J4clNW9fsApb3zhfz1W2XRB1aD95C11bjJWd5iuxQE2ohfU/Jn59RW79pX3tL1amCmoQk46x1dkCWX4GAgYgRfzfjeG3+c64FBtmbAKHnHhKQ2Kuoz3iVLDcmLrdC2PX~-1~-1~-1; bm_sv=5E9100F4D3FF3C7FB5FD570E13891790~YAAQlFB9ctvTPtuBAQAAwRJL7RDkCFuW/6a33fngqbP93AR5LMc08YMrmgEfZUJA2QPemQMm/GET/TXiS236ExbVTRfRlzg5AcMsuilpOfpjs/f4jZmKQNhz32H/kNctQewrDp6A93kQRMMtutZyv1wKD8rf6aaK0wJWk4/h9Bn0VWMuaGjDF7dXJDnaW+ajkYrJbrf87CWZK9p46hOnqUIypsCx6GxitgpT2HGq1FTctETceOnnJa3TyJoBCAnSNUB/~1; _ga_70947XW48P=GS1.1.1657542452.1.1.1657543529.27; _ga=GA1.2.575143191.1657542453");
		httpConn.setRequestProperty("origin", "https://www.tokopedia.com");
		httpConn.setRequestProperty("pragma", "no-cache");
		httpConn.setRequestProperty("referer", refererUrl+"?src=topads");
		httpConn.setRequestProperty("sec-ch-ua", "\".Not/A)Brand\";v=\"99\", \"Google Chrome\";v=\"103\", \"Chromium\";v=\"103\"");
		httpConn.setRequestProperty("sec-ch-ua-mobile", "?0");
		httpConn.setRequestProperty("sec-ch-ua-platform", "\"Windows\"");
		httpConn.setRequestProperty("sec-fetch-dest", "empty");
		httpConn.setRequestProperty("sec-fetch-mode", "cors");
		httpConn.setRequestProperty("sec-fetch-site", "same-site");
		httpConn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/103.0.0.0 Safari/537.36");
		httpConn.setRequestProperty("x-device", "desktop");
		httpConn.setRequestProperty("x-source", "tokopedia-lite");
		httpConn.setRequestProperty("x-tkpd-akamai", "pdpGetLayout");
		httpConn.setRequestProperty("x-tkpd-lite-service", "zeus");
		httpConn.setRequestProperty("x-version", "45367b8");
		
		OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
		writer.write("[{\"operationName\":\"PDPGetLayoutQuery\",\"variables\":{\"shopDomain\":\""+shopName+"\",\"productKey\":\""+productKey+"\",\"layoutID\":\"\",\"apiVersion\":1,\"userLocation\":{\"cityID\":\"176\",\"addressID\":\"0\",\"districtID\":\"2274\",\"postalCode\":\"\",\"latlon\":\"\"},\"extParam\":\"\"},\"query\":\"fragment ProductVariant on pdpDataProductVariant {  errorCode  parentID  defaultChild  sizeChart  variants {    productVariantID    variantID    name    identifier    option {      picture {        urlOriginal: url        urlThumbnail: url100        __typename      }      productVariantOptionID      variantUnitValueID      value      hex      __typename    }    __typename  }  children {    productID    price    priceFmt    optionID    productName    productURL    picture {      urlOriginal: url      urlThumbnail: url100      __typename    }    stock {      stock      isBuyable      stockWordingHTML      minimumOrder      maximumOrder      __typename    }    isCOD    isWishlist    campaignInfo {      campaignID      campaignType      campaignTypeName      campaignIdentifier      background      discountPercentage      originalPrice      discountPrice      stock      stockSoldPercentage      startDate      endDate      endDateUnix      appLinks      isAppsOnly      isActive      hideGimmick      isCheckImei      minOrder      __typename    }    thematicCampaign {      additionalInfo      background      campaignName      icon      __typename    }    __typename  }  __typename}fragment ProductMedia on pdpDataProductMedia {  media {    type    urlThumbnail: URLThumbnail    videoUrl: videoURLAndroid    prefix    suffix    description    __typename  }  videos {    source    url    __typename  }  __typename}fragment ProductHighlight on pdpDataProductContent {  name  price {    value    currency    __typename  }  campaign {    campaignID    campaignType    campaignTypeName    campaignIdentifier    background    percentageAmount    originalPrice    discountedPrice    originalStock    stock    stockSoldPercentage    threshold    startDate    endDate    endDateUnix    appLinks    isAppsOnly    isActive    hideGimmick    __typename  }  thematicCampaign {    additionalInfo    background    campaignName    icon    __typename  }  stock {    useStock    value    stockWording    __typename  }  variant {    isVariant    parentID    __typename  }  wholesale {    minQty    price {      value      currency      __typename    }    __typename  }  isCashback {    percentage    __typename  }  isTradeIn  isOS  isPowerMerchant  isWishlist  isCOD  isFreeOngkir {    isActive    __typename  }  preorder {    duration    timeUnit    isActive    preorderInDays    __typename  }  __typename}fragment ProductCustomInfo on pdpDataCustomInfo {  icon  title  isApplink  applink  separator  description  __typename}fragment ProductInfo on pdpDataProductInfo {  row  content {    title    subtitle    applink    __typename  }  __typename}fragment ProductDetail on pdpDataProductDetail {  content {    title    subtitle    applink    showAtFront    isAnnotation    __typename  }  __typename}fragment ProductDataInfo on pdpDataInfo {  icon  title  isApplink  applink  content {    icon    text    __typename  }  __typename}fragment ProductSocial on pdpDataSocialProof {  row  content {    icon    title    subtitle    applink    type    rating    __typename  }  __typename}query PDPGetLayoutQuery($shopDomain: String, $productKey: String, $layoutID: String, $apiVersion: Float, $userLocation: pdpUserLocation, $extParam: String) {  pdpGetLayout(shopDomain: $shopDomain, productKey: $productKey, layoutID: $layoutID, apiVersion: $apiVersion, userLocation: $userLocation, extParam: $extParam) {    name    pdpSession    basicInfo {      alias      isQA      id: productID      shopID      shopName      minOrder      maxOrder      weight      weightUnit      condition      status      url      needPrescription      catalogID      isLeasing      isBlacklisted      menu {        id        name        url        __typename      }      category {        id        name        title        breadcrumbURL        isAdult        isKyc        minAge        detail {          id          name          breadcrumbURL          isAdult          __typename        }        __typename      }      txStats {        transactionSuccess        transactionReject        countSold        paymentVerified        itemSoldFmt        __typename      }      stats {        countView        countReview        countTalk        rating        __typename      }      __typename    }    components {      name      type      position      data {        ...ProductMedia        ...ProductHighlight        ...ProductInfo        ...ProductDetail        ...ProductSocial        ...ProductDataInfo        ...ProductCustomInfo        ...ProductVariant        __typename      }      __typename    }    __typename  }}\"}]");
		writer.flush();
		writer.close();
		httpConn.getOutputStream().close();

		InputStream responseStream = httpConn.getResponseCode() / 100 == 2
				? httpConn.getInputStream()
				: httpConn.getErrorStream();
		String response2;
		try (Scanner s = new Scanner(responseStream).useDelimiter("\\A")) {
			response2 = s.hasNext() ? s.next() : "";
			s.close();
			httpConn.disconnect();
		}
		
		///mapping json array to json string
		ObjectMapper mapper = new ObjectMapper();
		List<Map<String, Object>> map = mapper.readValue(response2.toString(), new TypeReference<List<Map<String,Object>>>(){});

		///get description value from json string
		JSONArray json = new JSONArray(map);
		JSONObject pdpGetLayout = json.getJSONObject(0).getJSONObject("data").getJSONObject("pdpGetLayout");
		JSONArray component = pdpGetLayout.getJSONArray("components");
		JSONObject prodDetail = component.getJSONObject(4);
		JSONArray data = prodDetail.getJSONArray("data");
		JSONObject pdpDataProductDetail = data.getJSONObject(0);
		JSONArray content = pdpDataProductDetail.getJSONArray("content");
		JSONObject pdpContentProductDetail = content.getJSONObject(5);
		description = (String) pdpContentProductDetail.get("subtitle");
		
		return description;
		
	}
	
	private static String extractProductUrl(String url) {
		url = url.substring(0 , url.indexOf("?extParam"));
		return url;
	}
	
	private static String extractProductKey(String url, String shopName) {
		String startWith = "https://www.tokopedia.com/"+shopName+"/";
		url = url.substring(startWith.length());
		return url;
	}
	
	private static String extractStoreId(String url) {
		String storeId = url.substring(url.indexOf(".com/")+5 , url.length());
		return storeId;
	}
	
	private static int totalOfPages(int total) {
		return (total+60-1)/60;
	}
	
	private static int startFrom(int page) {
		return ((page-1)*60) + 1;
	}
}
