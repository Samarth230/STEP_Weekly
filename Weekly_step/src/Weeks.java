import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
public class Hash {
    // Problem 1
    static class UsernameChecker {
        private Map<String, Integer> userMap = new HashMap<>();
        private Map<String, Integer> attemptFrequency = new HashMap<>();
        public boolean checkAvailability(String username) {
            attemptFrequency.put(username, attemptFrequency.getOrDefault(username, 0) + 1);
            return !userMap.containsKey(username);
        }
        public void registerUser(String username, int userId) {
            userMap.put(username, userId);
        }
        public List<String> suggestAlternatives(String username) {
            List<String> suggestions = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                String alt = username + i;
                if (!userMap.containsKey(alt)) suggestions.add(alt);
            }
            suggestions.add(username.replace("_", "."));
            return suggestions;
        }
        public String getMostAttempted() {
            return attemptFrequency.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey).orElse(null);
        }
    }
    // Problem 2
    static class InventoryManager {
        private Map<String, AtomicInteger> stock = new HashMap<>();
        private Map<String, Queue<Integer>> waitingList = new HashMap<>();
        public void addProduct(String productId, int count) {
            stock.put(productId, new AtomicInteger(count));
            waitingList.put(productId, new LinkedList<>());
        }
        public String purchaseItem(String productId, int userId) {
            AtomicInteger count = stock.get(productId);
            if (count.get() > 0) {
                count.decrementAndGet();
                return "Success, " + count.get() + " units remaining";
            } else {
                waitingList.get(productId).add(userId);
                return "Added to waiting list, position #" + waitingList.get(productId).size();
            }
        }
        public int checkStock(String productId) {
            return stock.get(productId).get();
        }
    }
    //  Problem 3
    static class DNSEntry {
        String domain, ip;
        long expiryTime;
        DNSEntry(String domain, String ip, int ttl) {
            this.domain = domain;
            this.ip = ip;
            this.expiryTime = System.currentTimeMillis() + ttl * 1000;
        }
        boolean isExpired() {
            return System.currentTimeMillis() > expiryTime;
        }
    }
    static class DNSCache {
        private Map<String, DNSEntry> cache = new HashMap<>();
        private int hits = 0, misses = 0;
        public String resolve(String domain) {
            DNSEntry entry = cache.get(domain);
            if (entry == null || entry.isExpired()) {
                misses++;
                String ip = queryUpstream(domain);
                cache.put(domain, new DNSEntry(domain, ip, 5)); // TTL 5s for demo
                return ip + " (MISS)";
            } else {
                hits++;
                return entry.ip + " (HIT)";
            }
        }
        private String queryUpstream(String domain) {
            return "172.217." + new Random().nextInt(255) + "." + new Random().nextInt(255);
        }
        public String getStats() {
            return "Hit Rate: " + (100.0 * hits / (hits + misses)) + "%";
        }
    }
    // Problem 4
    static class PlagiarismDetector {
        private Map<String, Set<String>> nGramIndex = new HashMap<>();
        public void indexDocument(String docId, String text, int n) {
            String[] words = text.split(" ");
            for (int i = 0; i <= words.length - n; i++) {
                String nGram = String.join(" ", Arrays.copyOfRange(words, i, i + n));
                nGramIndex.computeIfAbsent(nGram, k -> new HashSet<>()).add(docId);
            }
        }
        public double analyze(String docId, String text, int n) {
            int matches = 0, total = 0;
            String[] words = text.split(" ");
            for (int i = 0; i <= words.length - n; i++) {
                total++;
                String nGram = String.join(" ", Arrays.copyOfRange(words, i, i + n));
                if (nGramIndex.containsKey(nGram)) matches++;
            }
            return (matches * 100.0) / total;
        }
    }
    // Problem 5
    static class AnalyticsDashboard {
        private Map<String, Integer> pageViews = new HashMap<>();
        private Map<String, Set<String>> uniqueVisitors = new HashMap<>();
        private Map<String, Integer> trafficSources = new HashMap<>();
        public void processEvent(String url, String userId, String source) {
            pageViews.put(url, pageViews.getOrDefault(url, 0) + 1);
            uniqueVisitors.computeIfAbsent(url, k -> new HashSet<>()).add(userId);
            trafficSources.put(source, trafficSources.getOrDefault(source, 0) + 1);
        }
        public void getDashboard() {
            pageViews.entrySet().stream()
                    .sorted((a, b) -> b.getValue() - a.getValue())
                    .limit(10)
                    .forEach(e -> System.out.println(e.getKey() + " - " + e.getValue() +
                            " views (" + uniqueVisitors.get(e.getKey()).size() + " unique)"));
            System.out.println("Traffic Sources: " + trafficSources);
        }
    }
    public static void main(String[] args) {
        // Problem 1
        UsernameChecker checker = new UsernameChecker();
        checker.registerUser("john_doe", 1);
        System.out.println("Availability of john_doe: " + checker.checkAvailability("john_doe"));
        System.out.println("Suggestions: " + checker.suggestAlternatives("john_doe"));
        // Problem 2
        InventoryManager inv = new InventoryManager();
        inv.addProduct("IPHONE15", 2);
        System.out.println(inv.purchaseItem("IPHONE15", 101));
        System.out.println(inv.purchaseItem("IPHONE15", 102));
        System.out.println(inv.purchaseItem("IPHONE15", 103));
        // Problem 3
        DNSCache dns = new DNSCache();
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.resolve("google.com"));
        System.out.println(dns.getStats());
        // Problem 4
        PlagiarismDetector pd = new PlagiarismDetector();
        pd.indexDocument("doc1", "this is a test document", 2);
        double sim = pd.analyze("doc2", "this is another test", 2);
        System.out.println("Similarity: " + sim + "%");
        // Problem 5
        AnalyticsDashboard dash = new AnalyticsDashboard();
        dash.processEvent("/article/news", "user1", "google");
        dash.processEvent("/article/news", "user2", "facebook");
        dash.getDashboard();
    }
}