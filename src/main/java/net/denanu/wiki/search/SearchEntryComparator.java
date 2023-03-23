package net.denanu.wiki.search;

import java.util.Comparator;

public class SearchEntryComparator implements Comparator<SearchEntry> {

	@Override
	public int compare(final SearchEntry o1, final SearchEntry o2) {
		return Integer.compare(o1.getDistance(), o2.getDistance());
	}

}
