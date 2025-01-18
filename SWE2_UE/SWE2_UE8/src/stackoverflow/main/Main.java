package stackoverflow.main;

import stackoverflow.Data;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Data data = Data.loadFromWeb();

        System.out.println("\nThese are all the Questions: \n");

        data.stream().forEach(q -> System.out.println(q.getTitle()));


        System.out.println("\n=====================================================\n");


        System.out.println("These are all the Questions sorted descending by score: \n");
        System.out.println("(The list above was sorted already, so its basically the same list again) \n");

        data.sortedStream().forEach(q -> System.out.println(q.getTitle()));


        System.out.println("\n=====================================================\n");


        int n = 100000;
        System.out.println("This is the highest scoring Question with a minimum Viewcount of " + n + ": \n");

        Optional<Data.Question> opt1 = data.findHighestScoringQuestionWith(n);
        if (opt1.isPresent()) System.out.println(opt1.get().getTitle());
        else System.out.println("No Results found.");


        System.out.println("\n=====================================================\n");


        System.out.println("This is the question with the longest title: \n");

        Optional<String> opt2 = data.getLongestTitle();
        if (opt2.isPresent()) System.out.println(opt2.get());
        else System.out.println("No Results found.");


        System.out.println("\n=====================================================\n");


        String str = "java";

        System.out.println("These are the Questions that contain '" + str + "': \n");

        data.findQuestions(str).forEach(q -> System.out.println(q.getTitle()));


        System.out.println("\n=====================================================\n");


        System.out.println("That many Questions had no accepted answer: \n");

        System.out.println(data.countQuestionsWithoutAcceptedAnswer());


        System.out.println("\n=====================================================\n");


        System.out.println("The average Questioner has a reputation score of: \n");

        OptionalDouble opt3 = data.averageOwnerReputation();
        if (opt3.isPresent()) System.out.println(opt3.getAsDouble());
        else System.out.println("No Results found.");


        System.out.println("\n=====================================================\n");


        System.out.println("The average Questioner has a reputation score of: ");
        System.out.println("(Calculated using a different method.)\n");

        System.out.println(data.averageOwnerReputation2());


        System.out.println("\n=====================================================\n");


        System.out.println("The sum of all the answers: \n");

        System.out.println(data.sumOfAllAnswerCounts());


        System.out.println("\n=====================================================\n");


        System.out.println("Questions grouped by the amount of tags:");

        data.groupQuestionsByTagCount().forEach((k, v) -> {
            System.out.println("\nQuestions with " + k + " tag"+(k>1 ? "s" : "")+":\n");
            v.forEach(q -> System.out.println(q.getTitle() + " --- tags: " + Arrays.toString(q.getTags())));
        });


        System.out.println("\n=====================================================\n");


        System.out.println("Question split into groups with and without accepted answers: \n");

        data.partitionByAcceptedAnswer().forEach((k, v) -> {
            System.out.println("\nQuestions " + (k ? "with" : "without") + " accepted answers:\n");
            v.forEach(q -> System.out.println(q.getTitle()));
        });


        System.out.println("\n=====================================================\n");


        System.out.println("The Questioner with the shortest name:\n");

        Optional<Data.Owner> opt4 = data.getOwnerWithShortestName();
        if (opt4.isPresent()) System.out.println(opt4.get().getDisplayName());
        else System.out.println("No Results found.");


        System.out.println("\n=====================================================\n");


        System.out.println("A list of all the distinct tags: \n");

        System.out.println(data.distinctTags());


        System.out.println("\n=====================================================\n");


        int m = 5;

        System.out.println("A list of the " + m + " most used tags: \n");
        System.out.println(data.topTags(m));


    }
}
