package net.denanu.wiki.search;

import java.util.Comparator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class SearchEntryComparator implements Comparator<SearchEntry> {

	@Override
	public int compare(final SearchEntry o1, final SearchEntry o2) {
		return Integer.compare(o1.getDistance(), o2.getDistance());
	}

}
