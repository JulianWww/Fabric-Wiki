package net.denanu.wiki.uitls;

import java.util.Comparator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(value=EnvType.CLIENT)
public class IntComparator implements Comparator<Integer> {

	@Override
	public int compare(final Integer o1, final Integer o2) {
		return Integer.compare(0, 0);
	}

}
