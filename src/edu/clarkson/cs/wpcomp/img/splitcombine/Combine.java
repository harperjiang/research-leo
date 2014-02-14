package edu.clarkson.cs.wpcomp.img.splitcombine;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.clarkson.cs.wpcomp.img.GeometryHelper;

public class Combine {

	private int threshold = 10;

	private Map<Rectangle, Integer> number;

	private Map<Integer, List<Rectangle>> region;

	public List<Rectangle> combine(List<Rectangle> rects) {
		number = new HashMap<Rectangle, Integer>();
		region = new HashMap<Integer, List<Rectangle>>();

		for (Rectangle rect : rects) {
			int num = number.size();
			number.put(rect, num);
			List<Rectangle> list = new ArrayList<Rectangle>();
			list.add(rect);
			region.put(num, list);
		}

		for (int i = 0; i < rects.size(); i++) {
			for (int j = i + 1; j < rects.size(); j++) {
				Rectangle first = rects.get(i);
				Rectangle second = rects.get(j);
				Rectangle cover = GeometryHelper.cover(first, second);
				if (cover.width <= Math.max(first.width, second.width)
						&& cover.height <= first.height + second.height
								+ threshold) {
					// top bottom
					merge(first, second);
				} else if (cover.height <= Math
						.max(first.height, second.height)
						&& cover.width <= first.width + second.width
								+ threshold) {
					// left right
					merge(first, second);
				}
			}
		}

		Map<Integer, Set<Rectangle>> mapping = new HashMap<Integer, Set<Rectangle>>();
		for (Entry<Rectangle, Integer> entry : number.entrySet()) {
			if (!mapping.containsKey(entry.getValue())) {
				mapping.put(entry.getValue(), new HashSet<Rectangle>());
			}
			mapping.get(entry.getValue()).add(entry.getKey());
		}

		List<Rectangle> result = new ArrayList<Rectangle>();

		for (Set<Rectangle> set : mapping.values()) {
			Rectangle[] array = new Rectangle[set.size()];
			set.toArray(array);
			result.add(GeometryHelper.cover(array));
		}

		return result;
	}

	private void merge(Rectangle a, Rectangle b) {
		int numa = number.get(a);
		int numb = number.get(b);
		if (numa == numb)
			return;
		int min = Math.min(numa, numb);
		int max = Math.max(numa, numb);

		for (Rectangle r : region.get(max)) {
			number.put(r, min);
		}
		region.get(min).addAll(region.remove(max));
	}
}
