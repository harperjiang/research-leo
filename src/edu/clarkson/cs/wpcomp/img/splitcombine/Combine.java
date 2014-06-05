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

	private double difference = 0.1;

	private Map<Rectangle, Integer> number;

	private Map<Integer, List<Rectangle>> region;

	public List<Rectangle> combine(List<Rectangle> rects) {
		List<Rectangle> targets = new ArrayList<Rectangle>();
		targets.addAll(rects);

		while (true) {
			number = new HashMap<Rectangle, Integer>();
			region = new HashMap<Integer, List<Rectangle>>();

			int oldSize = targets.size();

			for (Rectangle rect : targets) {
				int num = number.size();
				number.put(rect, num);
				List<Rectangle> list = new ArrayList<Rectangle>();
				list.add(rect);
				region.put(num, list);
			}

			for (int i = 0; i < targets.size(); i++) {
				for (int j = i + 1; j < targets.size(); j++) {
					Rectangle first = targets.get(i);
					Rectangle second = targets.get(j);
					Rectangle cover = GeometryHelper.cover(first, second);
					if (GeometryHelper.area(cover) <= (1 + difference)
							* (GeometryHelper.area(first) + GeometryHelper
									.area(second))) {
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

			targets.clear();

			for (Set<Rectangle> set : mapping.values()) {
				Rectangle[] array = new Rectangle[set.size()];
				set.toArray(array);
				targets.add(GeometryHelper.cover(array));
			}
			if (targets.size() == oldSize) {
				return targets;
			}
		}
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
