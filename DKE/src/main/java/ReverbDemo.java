
/* For representing a sentence that is annotated with pos tags and np chunks.*/
import edu.washington.cs.knowitall.nlp.ChunkedSentence;

/* String -> ChunkedSentence */
import edu.washington.cs.knowitall.nlp.OpenNlpSentenceChunker;

/* The class that is responsible for extraction. */
import edu.washington.cs.knowitall.extractor.ReVerbExtractor;

/* The class that is responsible for assigning a confidence score to an
 * extraction.
 */
import edu.washington.cs.knowitall.extractor.conf.ConfidenceFunction;
import edu.washington.cs.knowitall.extractor.conf.ReVerbOpenNlpConfFunction;

/* A class for holding a (arg1, rel, arg2) triple. */
import edu.washington.cs.knowitall.nlp.extraction.ChunkedBinaryExtraction;

public class ReverbDemo {


    public static void main(String[] args) throws Exception {

        String sentStr = """
                The Johannes Kepler University Linz (German: Johannes Kepler Universität Linz, short: JKU) is a public institution of higher education in Austria. It is located in Linz, the capital of Upper Austria. It offers bachelor's, master's, diploma and doctoral degrees in business, engineering, law, science, social sciences and medicine.
                Today, 19,930 students study at the park campus in the northeast of Linz, with one out of nine students being from abroad. The university was the first in Austria to introduce an electronic student ID in 1998.
                The university is the home of the Johann Radon Institute for Computational and Applied Mathematics (RICAM) of the Austrian Academy of Sciences.
                In 2012, the Times Higher Education ranked the JKU at # 41 and in 2015 at # 87 in its list of the top 100 universities under 50 years old. According to the 2012 ranking, the JKU was the fifth best young university in German-speaking Europe. The university attained high scores for quotations, third-party funding, and internationalization efforts.
                """;

        // Looks on the classpath for the default model files.
        OpenNlpSentenceChunker chunker = new OpenNlpSentenceChunker();
        ChunkedSentence sent = chunker.chunkSentence(sentStr);

        // Prints out the (token, tag, chunk-tag) for the sentence
        System.out.println(sentStr);
        for (int i = 0; i < sent.getLength(); i++) {
            String token = sent.getToken(i);
            String posTag = sent.getPosTag(i);
            String chunkTag = sent.getChunkTag(i);
            System.out.println(token + " " + posTag + " " + chunkTag);
        }

        // Prints out extractions from the sentence.
        ReVerbExtractor reverb = new ReVerbExtractor();
        ConfidenceFunction confFunc = new ReVerbOpenNlpConfFunction();

        try (Neo4jConnection connection = new Neo4jConnection("bolt://localhost:7687", "neo4j", "test")) {
            for (ChunkedBinaryExtraction extr : reverb.extract(sent)) {
                double conf = confFunc.getConf(extr);
                System.out.println("Arg1=" + extr.getArgument1());
                System.out.println("Arg1=" + extr.getArgument1());
                System.out.println("Rel=" + extr.getRelation());
                System.out.println("Arg2=" + extr.getArgument2());
                System.out.println("Conf=" + conf);

                connection.insertTriple(extr.getArgument1().toString(),
                        extr.getRelation().toString(),
                        extr.getArgument2().toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}