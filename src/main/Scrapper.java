package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
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

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;





public class Scrapper {
	public static TreeMap<Integer, Object[]> product = new TreeMap<Integer,Object[]>();
	public static int iterator = 0;
	public static int total = 100;
	
	public static void main(String[] args) throws IOException {
		int page = totalOfPages(total);
		System.out.println(page);
		for (int i=1; i<=page; i++) {
			String data = fetchDatas(i, startFrom(i));
			storeData(data,i);
		}
		createExcel();	
		
	}
	
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
            FileOutputStream out = new FileOutputStream(new File("D:/Belajar/Tokopedia.xls"));
            workbook.write(out);
            out.close();
            workbook.close();
            System.out.println("Tokopedia.xls written successfully on disk.");
        } 
        catch (Exception e) 
        {
            e.printStackTrace();
        }
	}
	
	private static void storeData(String data, int page) {
		try {
			JSONArray json = new JSONArray(data);
			JSONObject obj = json.getJSONObject(0).getJSONObject("data").getJSONObject("CategoryProducts");
			JSONArray items = obj.getJSONArray("data");
//			System.out.println(items);
			
			for (int i=0;i<items.length()&&iterator<total;i++) {
				JSONObject item = items.getJSONObject(i);
				String name = (String) item.get("name");
				String urll = (String) item.get("url");
				String imgLink = (String) item.get("imageUrlLarge");
				String price = (String) item.get("price");
				int rating = (int) item.get("rating");
				String storeName = (String) item.getJSONObject("shop").get("name");
				String description = getDescriptionJsop(urll);
//				System.out.println(name);
				product.put(iterator, new Object[] {name, urll, description, imgLink, price, rating, storeName});
				iterator++;
//				
			}
			
		}
		catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getDescriptionJsop(String urll) {
		String url = urll;
		String desc = null;
		try {
			Document document = Jsoup.connect(url).get();
			Elements elements = document.getElementsByAttributeValue("data-testid", "lblPDPDescriptionProduk");

            for(Element element : elements) {
//                System.out.println(element.text());
                desc = element.text();
            }
            System.out.println(desc);
            		
		}
		catch (Exception e) 
        {
            e.printStackTrace();
        }
		return desc;
		
	}
	
	public static String getDesc(String urll) {
		String Description = null;
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);

		// Set up the URL with the search term and send the request
		try {
			HtmlPage page = client.getPage(urll);
			HtmlElement desc1 = ((HtmlElement) page.getFirstByXPath(".//div[@data-testid='lblPDPDescriptionProduk']")) ;
			Description = desc1.asNormalizedText();
			System.out.println(Description);
		} catch (FailingHttpStatusCodeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		client.close();
		return Description;
	}
	
	private static int totalOfPages(int total) {
		return (total+60-1)/60;
	}
	
	private static int startFrom(int page) {
		return ((page-1)*60) + 1;
	}
}
