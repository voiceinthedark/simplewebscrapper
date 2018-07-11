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

/**
 * {@link WebScrapper} class will make all the required operation to scrape a
 * web page
 * 
 * @author porphiros
 *
 */
public class WebScrapper {
	private String linkToScrape;
	// Document element of JSoup to hold the document to scrape
	private Document document;
	// default link will point to Google
	private static final String DEFAULT_LINK = "https://www.google.com/";
	/**
	 * {@link HostServices} is used to open links scrapped on the system default
	 * browser
	 */
	private HostServices hostService;

	/**
	 * 
	 * @return the host service
	 */
	public HostServices getHostService() {
		return hostService;
	}

	/**
	 * Set the host service
	 * 
	 * @param hostService
	 */
	public void setHostService(HostServices hostService) {
		this.hostService = hostService;
	}

	public String getLinkToScrape() {
		return linkToScrape;
	}

	/**
	 * Set the link to scrape
	 * 
	 * @param linkToScrape
	 */
	public void setLinkToScrape(String linkToScrape) {
		this.linkToScrape = linkToScrape;
	}

	/**
	 * Default constructor hooked on the default link
	 */
	public WebScrapper() {
		this(DEFAULT_LINK);
	}

	/**
	 * Construct the {@link WebScrapper} to point to a non-default link
	 * 
	 * @param linkToScrape
	 */
	public WebScrapper(String linkToScrape) {
		this.linkToScrape = linkToScrape;
		try {
			this.document = setUpDocument(this.linkToScrape);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}

	/**
	 * Helper method to setup the Jsoup connection using method
	 * {@link Jsoup.connect()}
	 * 
	 * @param link to hook on
	 * @return a connected document element
	 * @throws IOException
	 */
	private Document setUpDocument(String link) throws IOException {
		return Jsoup.connect(link).get();
	}

	/**
	 * Collect links calls a helper method on the available initialized document
	 * 
	 * @return string representation of all links collected
	 */
	public String collectLinks() {
		return collectLinks(this.document);
	}

	/**
	 * Helper method to {@link WebScrapper.collectLinks()} will return a string
	 * representation of links collected
	 * 
	 * @param document
	 * @return
	 */
	private String collectLinks(Document document) {
		StringBuilder sb = new StringBuilder();
		Document doc = document;
		/**
		 * links selector using {@link Jsoup.Document.select()} method the pattern here
		 * is {code a[href]}
		 */
		Elements links = doc.select("a[href]");
		/**
		 * for media elements we collect all elements that have a [src] tag
		 */
		Elements media = doc.select("[src]");
		/**
		 * imports are collected with the pattern link[href]
		 */
		Elements imports = doc.select("link[href]");

		// Collect the media elements
		for (Element m : media) {
			String str = String.format("%s : %s%n", m.tagName(), m.attr("abs:src"));
			sb.append(str);
		}

		for (Element link : links) {
			String str = String.format("%s : %s%n", link.tagName(), link.attr("abs:href"));
			sb.append(str);
		}

		for (Element link : imports) {
			String str = String.format("%s : %s (%s)%n", link.tagName(), link.attr("abs:href"), link.attr("rel"));
			sb.append(str);
		}

		return sb.toString();
	}

	/**
	 * Collect Links as HyperLinks, calls a helper method
	 * 
	 * @return
	 */
	public List<Hyperlink> collectLinksAsHL() {
		return this.collectLinksAsHL(document);
	}

	private List<Hyperlink> collectLinksAsHL(Document document) {
		Document doc = document;
		Elements links = doc.select("a[href]");
		Elements media = doc.select("[src]");
		Elements imports = doc.select("link[href]");
		List<Hyperlink> list = new ArrayList<>();

		for (Element m : media) {
			/**
			 * Creates a hyperlink on each media element with attaching the src attribute
			 */
			Hyperlink hl = new Hyperlink(m.attr("abs:src"));
			// Add the hyperlink to the List
			list.add(hl);
			/**
			 * Attach an onclick hyperlink to each element in the list
			 */
			hl.setOnAction(e -> {
				/**
				 * Call the hostService {@link HostServices.showDocument()} method to open the
				 * Hyperlink location on click
				 */
				hostService.showDocument(hl.getText());
			});
		}

		for (Element link : links) {
			Hyperlink hl = new Hyperlink(link.attr("abs:href"));
			list.add(hl);
			hl.setOnAction(e -> {
				hostService.showDocument(hl.getText());
			});
		}

		for (Element link : imports) {
			Hyperlink hl = new Hyperlink(link.attr("abs:href"));
			list.add(hl);
			hl.setOnAction(e -> {
				hostService.showDocument(hl.getText());
			});
		}

		return list;
	}

}
