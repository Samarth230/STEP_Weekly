import java.util.*;

public class Main {

    // ===================== PROBLEM 1 =====================
    static class Transaction {
        int id;
        double fee;
        String time;

        Transaction(int id, double fee, String time) {
            this.id = id;
            this.fee = fee;
            this.time = time;
        }
    }

    static void bubbleSortFee(List<Transaction> list) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - i - 1; j++) {
                if (list.get(j).fee > list.get(j + 1).fee) {
                    Collections.swap(list, j, j + 1);
                    swapped = true;
                }
            }
            if (!swapped) break;
        }
    }

    static void insertionSortTime(List<Transaction> list) {
        for (int i = 1; i < list.size(); i++) {
            Transaction key = list.get(i);
            int j = i - 1;
            while (j >= 0 && list.get(j).time.compareTo(key.time) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    static List<Transaction> highFee(List<Transaction> list) {
        List<Transaction> res = new ArrayList<>();
        for (Transaction t : list) if (t.fee > 50) res.add(t);
        return res;
    }

    // ===================== PROBLEM 2 =====================
    static class Client {
        String name;
        int risk, balance;

        Client(String name, int risk, int balance) {
            this.name = name;
            this.risk = risk;
            this.balance = balance;
        }
    }

    static void bubbleRiskAsc(List<Client> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - i - 1; j++) {
                if (list.get(j).risk > list.get(j + 1).risk) {
                    Collections.swap(list, j, j + 1);
                }
            }
        }
    }

    static void insertionRiskDesc(List<Client> list) {
        for (int i = 1; i < list.size(); i++) {
            Client key = list.get(i);
            int j = i - 1;
            while (j >= 0 &&
                    (list.get(j).risk < key.risk ||
                            (list.get(j).risk == key.risk && list.get(j).balance < key.balance))) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    // ===================== PROBLEM 3 =====================
    static class Trade {
        int volume;

        Trade(int v) { volume = v; }
    }

    static void mergeSort(List<Trade> arr, int l, int r) {
        if (l >= r) return;
        int m = (l + r) / 2;
        mergeSort(arr, l, m);
        mergeSort(arr, m + 1, r);
        merge(arr, l, m, r);
    }

    static void merge(List<Trade> arr, int l, int m, int r) {
        List<Trade> temp = new ArrayList<>();
        int i = l, j = m + 1;
        while (i <= m && j <= r) {
            if (arr.get(i).volume <= arr.get(j).volume)
                temp.add(arr.get(i++));
            else temp.add(arr.get(j++));
        }
        while (i <= m) temp.add(arr.get(i++));
        while (j <= r) temp.add(arr.get(j++));
        for (int k = 0; k < temp.size(); k++) arr.set(l + k, temp.get(k));
    }

    static void quickSort(List<Trade> arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    static int partition(List<Trade> arr, int low, int high) {
        int pivot = arr.get(high).volume;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).volume > pivot) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        return i + 1;
    }

    // ===================== PROBLEM 4 =====================
    static class Asset {
        String name;
        double ret;

        Asset(String n, double r) {
            name = n;
            ret = r;
        }
    }

    static void mergeSortAssets(List<Asset> arr, int l, int r) {
        if (l >= r) return;
        int m = (l + r) / 2;
        mergeSortAssets(arr, l, m);
        mergeSortAssets(arr, m + 1, r);
        mergeAssets(arr, l, m, r);
    }

    static void mergeAssets(List<Asset> arr, int l, int m, int r) {
        List<Asset> temp = new ArrayList<>();
        int i = l, j = m + 1;
        while (i <= m && j <= r) {
            if (arr.get(i).ret <= arr.get(j).ret)
                temp.add(arr.get(i++));
            else temp.add(arr.get(j++));
        }
        while (i <= m) temp.add(arr.get(i++));
        while (j <= r) temp.add(arr.get(j++));
        for (int k = 0; k < temp.size(); k++) arr.set(l + k, temp.get(k));
    }

    static void quickSortAssets(List<Asset> arr, int low, int high) {
        if (low < high) {
            int pi = partitionAssets(arr, low, high);
            quickSortAssets(arr, low, pi - 1);
            quickSortAssets(arr, pi + 1, high);
        }
    }

    static int partitionAssets(List<Asset> arr, int low, int high) {
        double pivot = arr.get(high).ret;
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr.get(j).ret > pivot) {
                i++;
                Collections.swap(arr, i, j);
            }
        }
        Collections.swap(arr, i + 1, high);
        return i + 1;
    }

    // ===================== PROBLEM 5 =====================
    static int linearSearch(String[] arr, String target) {
        for (int i = 0; i < arr.length; i++)
            if (arr[i].equals(target)) return i;
        return -1;
    }

    static int binarySearch(String[] arr, String target) {
        int l = 0, r = arr.length - 1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m].equals(target)) return m;
            else if (arr[m].compareTo(target) < 0) l = m + 1;
            else r = m - 1;
        }
        return -1;
    }

    // ===================== PROBLEM 6 =====================
    static int floor(int[] arr, int target) {
        int l = 0, r = arr.length - 1, res = -1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m] <= target) {
                res = arr[m];
                l = m + 1;
            } else r = m - 1;
        }
        return res;
    }

    static int ceil(int[] arr, int target) {
        int l = 0, r = arr.length - 1, res = -1;
        while (l <= r) {
            int m = (l + r) / 2;
            if (arr[m] >= target) {
                res = arr[m];
                r = m - 1;
            } else l = m + 1;
        }
        return res;
    }

    // ===================== MAIN =====================
    public static void main(String[] args) {

        List<Transaction> tx = Arrays.asList(
                new Transaction(1, 10.5, "10:00"),
                new Transaction(2, 25.0, "09:30"),
                new Transaction(3, 5.0, "10:15")
        );
        bubbleSortFee(tx);
        insertionSortTime(tx);

        List<Client> clients = Arrays.asList(
                new Client("A", 20, 1000),
                new Client("B", 50, 2000),
                new Client("C", 80, 500)
        );
        bubbleRiskAsc(clients);
        insertionRiskDesc(clients);

        List<Trade> trades = Arrays.asList(new Trade(500), new Trade(100), new Trade(300));
        mergeSort(trades, 0, trades.size() - 1);
        quickSort(trades, 0, trades.size() - 1);

        List<Asset> assets = Arrays.asList(
                new Asset("AAPL", 12),
                new Asset("TSLA", 8),
                new Asset("GOOG", 15)
        );
        mergeSortAssets(assets, 0, assets.size() - 1);
        quickSortAssets(assets, 0, assets.size() - 1);

        String[] logs = {"accA", "accB", "accC"};
        System.out.println(linearSearch(logs, "accB"));
        System.out.println(binarySearch(logs, "accB"));

        int[] risks = {10, 25, 50, 100};
        System.out.println(floor(risks, 30));
        System.out.println(ceil(risks, 30));
    }
}