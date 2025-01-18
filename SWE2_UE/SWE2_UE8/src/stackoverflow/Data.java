package stackoverflow;


import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.GZIPInputStream;

public class Data {
    private static final String STACKOVERFLOW_TOP_100_JAVA_QUESTIONS_URL_STRING = "https://api.stackexchange.com/2.2/questions?pagesize=100&order=desc&sort=votes&tagged=java&site=stackoverflow&filter=!FcbKfKGCsNP42X*WbKOg(Q-OgX";
    private final Question[] items = new Question[0];

    private Data() {
    }

    /**
     * @return The loaded data, or null if an exception occured
     */
    public static Data loadFromWeb() {
        // HttpClient would be supported in Java 11, fall back to HttpURLConnection to ensure Java 8 compatibility
        try {
            URL url = new URL(STACKOVERFLOW_TOP_100_JAVA_QUESTIONS_URL_STRING);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            // StackExchange pages use compression in HTTP access, default is GZIP
            // See: https://api.stackexchange.com/docs/compression
            con.setRequestProperty("Accept-Encoding", "gzip");

            BufferedReader reader;
            if ("gzip".equals(con.getContentEncoding())) {
                // As expected, data has been returned compressed
                reader = new BufferedReader(new InputStreamReader(new GZIPInputStream(con.getInputStream())));
            } else {
                // StackExchange should _never_ send uncompressed data
                // Still, why not have a fallback? :)
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            }

            // Use Gson library to convert JSON into Data.Question and Data.Owner objects
            Gson gson = new Gson();
            Data data = gson.fromJson(reader, Data.class);
            reader.close();

            return data;
        } catch (MalformedURLException ex) {
            System.err.println("Malformed URL: " + STACKOVERFLOW_TOP_100_JAVA_QUESTIONS_URL_STRING);
            ex.printStackTrace();
        } catch (ProtocolException ex) {
            System.err.println("Protocol exception:");
            ex.printStackTrace();
        } catch (IOException ex) {
            System.err.println("IO exception:");
            ex.printStackTrace();
        }

        return new Data();
    }

    public Stream<Question> stream() {
        return Arrays.stream(items);
    }

    public Stream<Question> sortedStream() {
        return stream().sorted((q1, q2) -> (int) (q2.getScore() - q1.getScore()));
    }

    public Optional<Question> findHighestScoringQuestionWith(int minimumViews) {
        return sortedStream().filter(q -> q.view_count >= minimumViews).findFirst();
    }

    public Optional<String> getLongestTitle() {
        return stream().map(v -> v.title).max(Comparator.comparingInt(String::length));
    }

    public List<Question> findQuestions(String titlePart) {
        return stream().filter(q -> q.title.contains(titlePart)).collect(Collectors.toCollection(ArrayList::new));
    }

    public long countQuestionsWithoutAcceptedAnswer() {
        return stream().filter(q -> q.getAcceptedAnswerId() == 0).count();
    }

    public OptionalDouble averageOwnerReputation() {
        return stream().mapToLong(q -> q.getOwner().getReputation()).average();
    }

    public double averageOwnerReputation2() {
        return stream().collect(Collectors.averagingLong(q -> q.getOwner().getReputation()));
    }

    public long sumOfAllAnswerCounts() {
        return stream().mapToLong(Question::getAnswerCount).sum();
    }

    public Map<Integer, List<Question>> groupQuestionsByTagCount() {
        return stream().collect(Collectors.groupingBy(q -> q.getTags().length));
    }

    public Map<Boolean, List<Question>> partitionByAcceptedAnswer() {
        return stream().collect(Collectors.partitioningBy(q -> q.getAcceptedAnswerId() != 0));
    }

    public Optional<Owner> getOwnerWithShortestName() {
        return stream().map(Question::getOwner).min(Comparator.comparingInt(o -> o.getDisplayName().length()));
    }

    public List<String> distinctTags() {
        return stream()
                .flatMap(q -> Arrays.stream(q.getTags()))
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<String> topTags(int n) {

        return stream().flatMap(q -> Arrays.stream(q.getTags()))
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .sorted((e1 , e2) -> (int) (e2.getValue() - e1.getValue()))
                .limit(n)
                .map(e -> e.getKey() + " x " + e.getValue().toString())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Data{" +
                "items=" + Arrays.toString(items) +
                '}';
    }

    public static class Owner {
        private long reputation;
        private String display_name;

        public long getReputation() {
            return reputation;
        }

        public String getDisplayName() {
            return display_name;
        }

        @Override
        public String toString() {
            return "Owner{" +
                    "reputation=" + reputation +
                    ", display_name='" + display_name + '\'' +
                    '}';
        }
    }

    public static class Question {
        private String[] tags;
        private Owner owner;
        private long view_count;
        /**
         * Is 0 if now answer is accepted
         */
        private long accepted_answer_id;
        private long answer_count;
        private long score;
        private String title;

        public String[] getTags() {
            return tags;
        }

        public Owner getOwner() {
            return owner;
        }

        public long getViewCount() {
            return view_count;
        }

        /**
         * @return The ID of the accepted answer, or 0 if no answer has been accepted.
         */
        public long getAcceptedAnswerId() {
            return accepted_answer_id;
        }

        public long getAnswerCount() {
            return answer_count;
        }

        public long getScore() {
            return score;
        }

        public String getTitle() {
            return title;
        }

        @Override
        public String toString() {
            return "Question{" +
                    "tags=" + Arrays.toString(tags) +
                    ", owner=" + owner +
                    ", view_count=" + view_count +
                    ", accepted_answer_id=" + accepted_answer_id +
                    ", answer_count=" + answer_count +
                    ", score=" + score +
                    ", title='" + title + '\'' +
                    '}';
        }
    }
}
