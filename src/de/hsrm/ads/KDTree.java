package de.hsrm.ads;

import java.util.Arrays;

public class KDTree {

	class KDNode {
		int dim;
		double t;
		int point;

		// inner node
		public KDNode(int dim, double t) {
			this.dim = dim;
			this.t = t;
			this.point = -1;
		}

		// leaf
		public KDNode(int point) {
			this.dim = -1;
			this.t = 0;
			this.point = point;
		}
	}

	KDNode[] nodes;
	double[][] data;

	public KDTree(double[][] data) {
		this.data = data;
		build(data);
	}

	static int NextPowerOfTwo(int n) {
		int x = 0;
		for (int i = 1; i < n; i = i * 2) {
			x = i;
		}
		return x * 2;
	}

	static double median(double[] data) {
		double[] copyarray = new double[data.length];
		System.arraycopy(data, 0, copyarray, 0, data.length);
		Arrays.sort(copyarray);
		if (copyarray.length % 2 == 1) { // wenn der Anzahl des Arrays ungerade ist
			/*
			 * for(int i = 0; i<copyarray.length;i++) System.out.println(copyarray[i]);
			 */

			int halfsize = copyarray.length / 2;
			double mittelwert_ungerade = copyarray[halfsize];
			// System.out.println("mittelwert(ungerade): " + mittelwert_ungerade);
			return mittelwert_ungerade;
		} else { // wenn der Anzahl des Arrays gerade ist
			for (int i = 0; i < copyarray.length; i++)
				System.out.println(copyarray[i]);

			int halfsize2 = copyarray.length / 2;
			double mittelwert_gerade = (copyarray[halfsize2] + copyarray[halfsize2 - 1]) / 2;
			// System.out.println("mittelwert(gerade): " + mittelwert_gerade);
			return mittelwert_gerade;
		}
	}

	public void build(double[][] data) {

		int n = NextPowerOfTwo(data[0].length);
		nodes = new KDNode[2 * n]; // nodes[] länge einsetzen
		int dimensions = data.length; // Anzahl der Dimensionen bestimmen
		int current_dimension = 0; // aktuelle Dimension
		int nodes_index = 1;
		int ebene = 1; // Das ist unnötig

		// points array erstellen
		int[] points = new int[data[0].length];

		for (int i = 0; i < points.length; i++) {
			points[i] = i;
		}

		build(data, dimensions, current_dimension, points, nodes_index, ebene);
	}

	public void build(double[][] data, int dimensions, int current_dimension, int[] points, int nodes_index,
			int ebene) {
		//points test
		for(int test=0; test < points.length; test++)
		System.out.println("points: "+points[test]);

		// points value bestimmen
		double[] points_value = new double[points.length];
		for (int x = 0; x < points.length; x++) {
			points_value[x] = data[current_dimension][points[x]];
		}
		// inner node erstellen
		System.out.println("nodes_index: "+nodes_index);
		if (points.length > 1) {
			nodes[nodes_index] = new KDNode(current_dimension, median(points_value));
			System.out.println("dim = " + nodes[nodes_index].dim + " schwellwert = " + nodes[nodes_index].t
					+ "NodeIndex" + nodes_index + "ebene " + ebene);
		}
		// test
		/*
		 * for (int j = 0; j < nodes.length; j++) System.out.println("nodes[" + j + "] "
		 * + nodes[j]);
		 * 
		 * System.out.println("dimensions " + dimensions);
		 * System.out.println("current_dimension " + current_dimension);
		 * System.out.println("nodes_index " + nodes_index);
		 * 
		 * for (int i = 0; i < points.length; i++) System.out.println("points[" + i +
		 * "]" + points[i]);
		 */
		// #test

		// leaf erstellen
		System.out.println("nodes_index: "+nodes_index);
		System.out.println("ebene: "+ebene);
		if (points.length == 1) {
			nodes[nodes_index] = new KDNode(points[0]);
			ebene++;
		}

		int data_index = 0;
		int points0_index = 0;
		int points1_index = 0;

		if (points.length > 1 /* /&& !left/ */) {
			int points0_tmp[] = new int[data[0].length];
			int points1_tmp[] = new int[data[0].length];

			ebene++;
			// der median bestimmen
			var current_median = median(points_value);

			// point0 and point1 array erstellen
			for (var current_point : points) {
				if (data[current_dimension][current_point] >= current_median) {
					points1_tmp[points1_index] = current_point;
					points1_index++;
				} else if (data[current_dimension][current_point] < current_median) {
					points0_tmp[points0_index] = current_point;
					points0_index++;
				}
				data_index++;
			}

			if (current_dimension + 1 < dimensions) {
				current_dimension++;
			} else if (current_dimension + 1 == dimensions) {
				current_dimension = 0;
			}

			int[] points0 = new int[points0_index];
			int[] points1 = new int[points1_index];

			int[] points_tmp = new int[points1_index];

			// points0 array länge limitieren
			System.arraycopy(points0_tmp, 0, points0, 0, points0_index);
			// points1 array länge limitieren
			System.arraycopy(points1_tmp, 0, points1, 0, points1_index);
			
			for(int z = 1; z < nodes.length; z++)
			    System.out.println("nodes["+z+"]: "+nodes[z]);
			
			// recursive Aufruf der build methode
			if (points0.length >= 1)
				build(data, dimensions, current_dimension, points0, nodes_index * 2, ebene);

			points_tmp = points1;
			build(data, dimensions, current_dimension, points_tmp, nodes_index * 2 + 1, ebene);
		}
	}

	static double dist(double[] p1, double[] p2) {
		double result = 0;
		int dimensions = p1.length;
		double sum = 0;
		System.out.println(dimensions);
		for (int i = 0; i < dimensions; i++) {
			sum += Math.pow(p2[i] - p1[i], 2);
		}
		System.out.println(sum);
		result = Math.sqrt(sum);
		System.out.println(result);
		return result;
	}

	double searchGreedy(double[] query) {
		double distance = 0;
		int dimensions = data.length; // Anzahl der Dimensionen bestimmen
		int current_dimension = 0; // aktuelle Dimension
		int wurzel = 1;
		// double[] arrayelements = new double[];
		System.out.println(dimensions);
		System.out.println(current_dimension);
		System.out.println(nodes.length);

		for (int i = wurzel; i < nodes.length; i++) {
			if (query[current_dimension] >= nodes[wurzel].t && nodes[wurzel].point == -1) {
				wurzel = wurzel * 2 + 1;
				// System.out.println(wurzel);
				// System.out.println(nodes[wurzel].t);
			} else if (query[current_dimension] < nodes[wurzel].t && nodes[wurzel].point == -1) {
				wurzel = wurzel * 2;
				// System.out.println(wurzel);
				// System.out.println(nodes[wurzel].t);
			} else {
				double[] point = new double[dimensions];
				for (int i1 = 0; i1 < dimensions; i1++) {
					point[i1] = data[i1][nodes[wurzel].point];
				}
				distance = dist(point, query);
				return distance;
			}

			if (current_dimension + 1 < dimensions) {
				current_dimension++;
			} else if (current_dimension + 1 == dimensions) {
				current_dimension = 0;
			}
			System.out.println(current_dimension);

		}
		return distance;
	}

	double searchBacktracking(double[] query) {
		double max = Double.MAX_VALUE;
		int current_dimension = 0;
		return searchBacktracking(query, max, 1, current_dimension);
	}

	double searchBacktracking(double[] query, double distance, int current_index, int current_dimension) {
		double[] array = new double[data.length];
		if (nodes[current_index].point >= 0) {
			for (int index = 0; index < query.length; index++)
				array[index] = data[index][nodes[current_index].point];
			if (dist(query, array) < distance)
				distance = dist(query, array);
			return distance;
		}
		if (nodes[current_index].t < (query[current_dimension] + distance)) {
			distance = searchBacktracking(query, distance, (current_index * 2) + 1,
					(current_dimension + 1) % data.length);
		}
		if (query[current_dimension] - distance < nodes[current_index].t || distance == Double.MAX_VALUE) {
			distance = searchBacktracking(query, distance, current_index * 2, (current_dimension + 1) % data.length);
		}
		return distance;
	}

	public static void main(String[] args) {
		// Beispiel vom Übungsblatt.
		double data[][] = new double[][] { { 4, 7, 2, 5, 1 }, { 3, 5, 7, 1, 2 } };
		/*double data[][] = new double[][] { { 1, 2, 3, 4, 5, 6, 7, 8 }, { 1, 2, 3, 4, 5, 6, 7, 8 },
				{ 1, 2, 3, 4, 5, 6, 7, 8 } };*/
		KDTree kdt = new KDTree(data);

		// System.out.println("kdt.nodes.length: "+kdt.nodes.length);

		for (int x = 1; x < kdt.nodes.length; x++)
			System.out.println("nodes[" + x + "] = " + kdt.nodes[x]);

		// System.out.println(kdt.nodes[1].t);

		// double result = kdt.searchGreedy(new double[] {4,2});
		// double result = kdt.searchGreedy(new double[] { 1, 5, 1 });
		double result = kdt.searchBacktracking(new double[] { 1, 5, 1 });
		System.out.println("result: "+result);
		// double p1[] = {3,2,1};
		// double p2[] = {5,4,2};
		// System.out.println("dist = "+kdt.dist(p1, p2));
		// System.out.println(kdt.NextPowerOfTwo(16));
		// System.out.println(data[0].length);
		// kdt.median(data2);
	}
}
