package net.denanu.wiki.uitls;

import java.util.Iterator;
import java.util.Set;

public class SetUtils {
	public static <T> T getMod(final Set<T> set, final int idx) {
		final Iterator<T> iter = set.iterator();
		for (int i = 0; i < idx % set.size(); i++) {iter.next();}
		return iter.next();
	}
}
