import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    // ===================== PROBLEM 6: RATE LIMITER =====================
    static class TokenBucket {
        int tokens, maxTokens, refillRate;
        long lastRefill;

        TokenBucket(int maxTokens, int refillRate) {
            this.maxTokens = maxTokens;
            this.refillRate = refillRate;
            this.tokens = maxTokens;
            this.lastRefill = System.currentTimeMillis();
        }

        synchronized boolean allowRequest() {
            long now = System.currentTimeMillis();
            long seconds = (now - lastRefill) / 1000;
            if (seconds > 0) {
                tokens = Math.min(maxTokens, tokens + (int)(seconds * refillRate));
                lastRefill = now;
            }
            if (tokens > 0) {
                tokens--;
                return true;
            }
            return false;
        }
    }

    static class RateLimiter {
        ConcurrentHashMap<String, TokenBucket> map = new ConcurrentHashMap<>();

        boolean check(String clientId) {
            map.putIfAbsent(clientId, new TokenBucket(1000, 1000 / 3600));
            return map.get(clientId).allowRequest();
        }
    }

    // ===================== PROBLEM 7: AUTOCOMPLETE =====================
    static class TrieNode {
        Map<Character, TrieNode> children = new HashMap<>();
        Map<String, Integer> freqMap = new HashMap<>();
        boolean isEnd;
    }

    static class Autocomplete {
        TrieNode root = new TrieNode();

        void insert(String word) {
            TrieNode node = root;
            for (char c : word.toCharArray()) {
                node.children.putIfAbsent(c, new TrieNode());
                node = node.children.get(c);
                node.freqMap.put(word, node.freqMap.getOrDefault(word, 0) + 1);
            }
            node.isEnd = true;
        }

        List<String> search(String prefix) {
            TrieNode node = root;
            for (char c : prefix.toCharArray()) {
                if (!node.children.containsKey(c)) return new ArrayList<>();
                node = node.children.get(c);
            }
            PriorityQueue<Map.Entry<String, Integer>> pq =
                    new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
            pq.addAll(node.freqMap.entrySet());
            List<String> res = new ArrayList<>();
            int k = 10;
            while (!pq.isEmpty() && k-- > 0) res.add(pq.poll().getKey());
            return res;
        }
    }

    // ===================== PROBLEM 8: PARKING LOT =====================
    static class ParkingLot {
        String[] table;
        int size;

        ParkingLot(int size) {
            this.size = size;
            table = new String[size];
        }

        int hash(String plate) {
            return Math.abs(plate.hashCode()) % size;
        }

        int park(String plate) {
            int idx = hash(plate);
            int probes = 0;
            while (table[idx] != null) {
                idx = (idx + 1) % size;
                probes++;
            }
            table[idx] = plate;
            return idx;
        }

        void exit(String plate) {
            int idx = hash(plate);
            while (table[idx] != null) {
                if (table[idx].equals(plate)) {
                    table[idx] = null;
                    return;
                }
                idx = (idx + 1) % size;
            }
        }
    }

    // ===================== PROBLEM 9: TWO SUM VARIANTS =====================
    static class Transaction {
        int id, amount;
        String merchant, time;

        Transaction(int id, int amount, String merchant, String time) {
            this.id = id;
            this.amount = amount;
            this.merchant = merchant;
            this.time = time;
        }
    }

    static List<int[]> twoSum(List<Transaction> list, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        List<int[]> res = new ArrayList<>();
        for (Transaction t : list) {
            if (map.containsKey(target - t.amount)) {
                res.add(new int[]{map.get(target - t.amount), t.id});
            }
            map.put(t.amount, t.id);
        }
        return res;
    }

    static List<List<Integer>> kSum(int[] nums, int target, int k) {
        List<List<Integer>> res = new ArrayList<>();
        Arrays.sort(nums);
        kSumHelper(nums, target, k, 0, new ArrayList<>(), res);
        return res;
    }

    static void kSumHelper(int[] nums, int target, int k, int start,
                           List<Integer> path, List<List<Integer>> res) {
        if (k == 2) {
            int l = start, r = nums.length - 1;
            while (l < r) {
                int sum = nums[l] + nums[r];
                if (sum == target) {
                    List<Integer> temp = new ArrayList<>(path);
                    temp.add(nums[l]);
                    temp.add(nums[r]);
                    res.add(temp);
                    l++; r--;
                } else if (sum < target) l++;
                else r--;
            }
        } else {
            for (int i = start; i < nums.length; i++) {
                path.add(nums[i]);
                kSumHelper(nums, target - nums[i], k - 1, i + 1, path, res);
                path.remove(path.size() - 1);
            }
        }
    }

    // ===================== PROBLEM 10: MULTI LEVEL CACHE =====================
    static class LRUCache {
        int capacity;
        LinkedHashMap<String, String> map;

        LRUCache(int capacity) {
            this.capacity = capacity;
            this.map = new LinkedHashMap<>(capacity, 0.75f, true) {
                protected boolean removeEldestEntry(Map.Entry<String, String> eldest) {
                    return size() > capacity;
                }
            };
        }

        String get(String key) {
            return map.getOrDefault(key, null);
        }

        void put(String key, String value) {
            map.put(key, value);
        }
    }

    static class MultiLevelCache {
        LRUCache l1 = new LRUCache(10000);
        LRUCache l2 = new LRUCache(100000);
        Map<String, String> db = new HashMap<>();

        String get(String key) {
            String val = l1.get(key);
            if (val != null) return "L1 HIT";

            val = l2.get(key);
            if (val != null) {
                l1.put(key, val);
                return "L2 HIT -> Promoted";
            }

            val = db.get(key);
            if (val != null) {
                l2.put(key, val);
                return "DB HIT -> Added to L2";
            }

            return "MISS";
        }
    }

    public static void main(String[] args) {

        RateLimiter rl = new RateLimiter();
        System.out.println("RateLimiter: " + rl.check("abc"));

        Autocomplete ac = new Autocomplete();
        ac.insert("tutorial");
        ac.insert("script");
        ac.insert("download");
        System.out.println("Autocomplete: " + ac.search("tu"));

        ParkingLot pl = new ParkingLot(500);
        int spot = pl.park("ABC123");
        System.out.println("Parked at: " + spot);

        List<Transaction> tx = Arrays.asList(
                new Transaction(1, 500, "A", "10:00"),
                new Transaction(2, 300, "B", "10:15"),
                new Transaction(3, 200, "C", "10:30")
        );
        System.out.println("TwoSum: " + twoSum(tx, 500).size());

        MultiLevelCache cache = new MultiLevelCache();
        cache.db.put("video1", "data");
        System.out.println("Cache: " + cache.get("video1"));
    }
}