import de.uni_mannheim.minie.MinIE;
import de.uni_mannheim.minie.annotation.AnnotatedProposition;
import de.uni_mannheim.utils.coreNLP.CoreNLPUtils;

import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

import java.util.Arrays;


public class MinIEDemo {
    public static void main(String[] args) {
        // Dependency parsing pipeline initialization
        StanfordCoreNLP parser = CoreNLPUtils.StanfordDepNNParser();

        // Input sentence
        String text = """
                The Johannes Kepler University Linz (German: Johannes Kepler Universität Linz, short: JKU) is a public institution of higher education in Austria. It is located in Linz, the capital of Upper Austria. It offers bachelor's, master's, diploma and doctoral degrees in business, engineering, law, science, social sciences and medicine.
                Today, 19,930 students study at the park campus in the northeast of Linz, with one out of nine students being from abroad. The university was the first in Austria to introduce an electronic student ID in 1998.
                The university is the home of the Johann Radon Institute for Computational and Applied Mathematics (RICAM) of the Austrian Academy of Sciences.
                In 2012, the Times Higher Education ranked the JKU at # 41 and in 2015 at # 87 in its list of the top 100 universities under 50 years old. According to the 2012 ranking, the JKU was the fifth best young university in German-speaking Europe. The university attained high scores for quotations, third-party funding, and internationalization efforts.
                """;


        try (Neo4jConnection connection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "test")) {
            for (String sentence : Arrays.stream(text.split("\\.")).map(x -> x + ".").toList()) {
                // Generate the extractions (With SAFE mode)
                MinIE minie = new MinIE(sentence, parser, MinIE.Mode.COMPLETE);

                // Print the extractions
                System.out.println("\nInput sentence: " + sentence);
                System.out.println("=============================");
                System.out.println("Extractions:");
                for (AnnotatedProposition ap : minie.getPropositions()) {
                    connection.insertTriple(
                            ap.getTriple().get(0).toString(),
                            ap.getTriple().get(1).toString(),
                            ap.getTriple().get(2).toString());


                    System.out.println("\tTriple: " + ap.getTripleAsString());
                    System.out.print("\tFactuality: " + ap.getFactualityAsString());
                    if (ap.getAttribution().getAttributionPhrase() != null)
                        System.out.print("\tAttribution: " + ap.getAttribution().toStringCompact());
                    else
                        System.out.print("\tAttribution: NONE");
                    System.out.println("\n\t----------");
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("\n\nDONE!");
    }
}

