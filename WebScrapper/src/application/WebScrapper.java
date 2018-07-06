package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import javafx.application.HostServices;
import javafx.scene.control.Hyperlink;

public class WebScrapper {
	private String linkToScrape;
	private Document document;
	private static final String DEFAULT_LINK = "https://www.google.com/";
	
	private HostServices hostService;
	
	public HostServices getHostService() {
		return hostService;
	}

	public void setHostService(HostServices hostService) {
		this.hostService = hostService;
	}

	public String getLinkToScrape() {
		return linkToScrape;
	}

	public void setLinkToScrape(String linkToScrape) {
		this.linkToScrape = linkToScrape;		
	}

	public WebScrapper() {
		this(DEFAULT_LINK);
	}
	
	public WebScrapper(String linkToScrape) {
		this.linkToScrape = linkToScrape;		
		try {
			this.document = setUpDocument(this.linkToScrape);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
	
	private Document setUpDocument(String link) throws IOException {
		return Jsoup.connect(link).get();
	}
	
	public String collectLinks() {
		return collectLinks(this.document);
	}
	
	private String collectLinks(Document document) {
		StringBuilder sb = new StringBuilder();
		Document doc = document;
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");

		for(Element m : media) {
			String str = String.format("%s : %s%n", m.tagName(), m.attr("abs:src"));
			sb.append(str);
		}
		
		for(Element link : links) {
			String str = String.format("%s : %s%n",
					link.tagName(),link.attr("abs:href"));
			sb.append(str);
		}
		
		for(Element link : imports) {
			String str = String.format("%s : %s (%s)%n",
					link.tagName(),link.attr("abs:href"), link.attr("rel"));
			sb.append(str);
		}

		return sb.toString();
	}
	
	public List<Hyperlink> collectLinksAsHL(){
		return this.collectLinksAsHL(document);
	}
	
	private List<Hyperlink> collectLinksAsHL(Document document){
		Document doc = document;
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");
		List<Hyperlink> list = new ArrayList<>();
		
		for(Element m : media) {
			Hyperlink hl = new Hyperlink(m.attr("abs:src"));
			list.add(hl);
			hl.setOnAction(e -> {
				hostService.showDocument(hl.getText());
			});
		}
		
		for(Element link : links) {
			Hyperlink hl = new Hyperlink(link.attr("abs:href"));
			list.add(hl);
			hl.setOnAction(e -> {
				hostService.showDocument(hl.getText());
			});
		}
		
		for(Element link : imports) {
			Hyperlink hl = new Hyperlink(link.attr("abs:href"));
			list.add(hl);
			hl.setOnAction(e -> {
				hostService.showDocument(hl.getText());
			});
		}
		
		return list;
	}

}
