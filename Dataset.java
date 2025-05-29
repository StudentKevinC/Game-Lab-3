// Dataset.java
import java.util.*;
import java.io.*;

public class Dataset {
    private ArrayList<Game> data;
    private String sortedByAttribute;

    public Dataset(ArrayList<Game> data) {
        this.data = data;
        this.sortedByAttribute = "";
    }

    public ArrayList<Game> getGamesByPrice(int price) {
        return search("price", price);
    }

    public ArrayList<Game> getGamesByPriceRange(int min, int max) {
        ArrayList<Game> result = new ArrayList<>();
        for (Game g : data) {
            if (g.getPrice() >= min && g.getPrice() <= max) result.add(g);
        }
        return result;
    }

    public ArrayList<Game> getGamesByCategory(String category) {
        return search("category", category);
    }

    public ArrayList<Game> getGamesByQuality(int quality) {
        return search("quality", quality);
    }

    private ArrayList<Game> search(String attr, Object val) {
        ArrayList<Game> result = new ArrayList<>();
        boolean useBinary = attr.equals(sortedByAttribute);
        Comparator<Game> comp = getComparator(attr);

        if (!useBinary) {
            for (Game g : data) {
                if ((attr.equals("price") && g.getPrice() == (Integer) val) ||
                    (attr.equals("quality") && g.getQuality() == (Integer) val) ||
                    (attr.equals("category") && g.getCategory().equals(val))) {
                    result.add(g);
                }
            }
            return result;
        }

        // Binary search for first match, then expand
        int low = 0, high = data.size() - 1;
        while (low <= high) {
            int mid = (low + high) / 2;
            Game g = data.get(mid);
            int cmp = 0;
            if (val instanceof Integer) {
                cmp = (attr.equals("price")) ? Integer.compare(g.getPrice(), (int) val)
                     : Integer.compare(g.getQuality(), (int) val);
            } else if (val instanceof String) {
                cmp = g.getCategory().compareTo((String) val);
            }
            if (cmp < 0) low = mid + 1;
            else if (cmp > 0) high = mid - 1;
            else {
                // Expand
                int i = mid;
                while (i >= 0 && matchAttr(data.get(i), attr, val)) i--;
                i++;
                while (i < data.size() && matchAttr(data.get(i), attr, val)) result.add(data.get(i++));
                break;
            }
        }
        return result;
    }

    private boolean matchAttr(Game g, String attr, Object val) {
        if (attr.equals("price")) return g.getPrice() == (int) val;
        if (attr.equals("quality")) return g.getQuality() == (int) val;
        return g.getCategory().equals(val);
    }

    public void sortByAlgorithm(String algorithm, String attribute) {
        Comparator<Game> comparator = getComparator(attribute);
        switch (algorithm) {
            case "bubbleSort": bubbleSort(comparator); break;
            case "insertionSort": insertionSort(comparator); break;
            case "selectionSort": selectionSort(comparator); break;
            case "mergeSort": data = mergeSort(data, comparator); break;
            case "quickSort": quickSort(0, data.size() - 1, comparator); break;
            case "countingSort":
                if (attribute.equals("quality")) countingSort();
                else Collections.sort(data, comparator);
                break;
            default: Collections.sort(data, comparator);
        }
        sortedByAttribute = attribute;
    }

    private Comparator<Game> getComparator(String attribute) {
        return switch (attribute) {
            case "category" -> Comparator.comparing(Game::getCategory);
            case "quality" -> Comparator.comparingInt(Game::getQuality);
            default -> Comparator.comparingInt(Game::getPrice);
        };
    }

    private void bubbleSort(Comparator<Game> comp) {
        for (int i = 0; i < data.size() - 1; i++) {
            for (int j = 0; j < data.size() - i - 1; j++) {
                if (comp.compare(data.get(j), data.get(j + 1)) > 0) {
                    Collections.swap(data, j, j + 1);
                }
            }
        }
    }

    private void insertionSort(Comparator<Game> comp) {
        for (int i = 1; i < data.size(); i++) {
            Game key = data.get(i);
            int j = i - 1;
            while (j >= 0 && comp.compare(data.get(j), key) > 0) {
                data.set(j + 1, data.get(j));
                j--;
            }
            data.set(j + 1, key);
        }
    }

    private void selectionSort(Comparator<Game> comp) {
        for (int i = 0; i < data.size() - 1; i++) {
            int minIdx = i;
            for (int j = i + 1; j < data.size(); j++) {
                if (comp.compare(data.get(j), data.get(minIdx)) < 0) minIdx = j;
            }
            Collections.swap(data, i, minIdx);
        }
    }

    private ArrayList<Game> mergeSort(ArrayList<Game> list, Comparator<Game> comp) {
        if (list.size() <= 1) return list;
        int mid = list.size() / 2;
        ArrayList<Game> left = mergeSort(new ArrayList<>(list.subList(0, mid)), comp);
        ArrayList<Game> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), comp);
        return merge(left, right, comp);
    }

    private ArrayList<Game> merge(ArrayList<Game> left, ArrayList<Game> right, Comparator<Game> comp) {
        ArrayList<Game> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (comp.compare(left.get(i), right.get(j)) <= 0) result.add(left.get(i++));
            else result.add(right.get(j++));
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }

    private void quickSort(int low, int high, Comparator<Game> comp) {
        if (low < high) {
            int pi = partition(low, high, comp);
            quickSort(low, pi - 1, comp);
            quickSort(pi + 1, high, comp);
        }
    }

    private int partition(int low, int high, Comparator<Game> comp) {
        Game pivot = data.get(high);
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (comp.compare(data.get(j), pivot) <= 0) {
                i++;
                Collections.swap(data, i, j);
            }
        }
        Collections.swap(data, i + 1, high);
        return i + 1;
    }

    private void countingSort() {
        List<List<Game>> buckets = new ArrayList<>();
        for (int i = 0; i <= 100; i++) buckets.add(new ArrayList<>());
        for (Game g : data) buckets.get(g.getQuality()).add(g);
        data.clear();
        for (List<Game> bucket : buckets) data.addAll(bucket);
    }

    public void exportToCSV(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (Game g : data) {
                bw.write(g.toString());
                bw.newLine();
            }
        }
    }
}