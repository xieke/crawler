package weibo4j.examples.search;

import weibo4j.Search;
import weibo4j.model.WeiboException;

public class SearchSuggestionsStatuses {

	public static void main(String[] args) {
		String access_token="2.00qr5gqB6gt2kC686f16c2deDBiESC";
		Search search = new Search();
		search.client.setToken(access_token);
		try {
			search.searchSuggestionsStatuses("abcd");
		} catch (WeiboException e) {
			e.printStackTrace();
		}

	}

}
