import org.neo4j.driver.*;

import static org.neo4j.driver.Values.parameters;

public class Neo4jConnection implements AutoCloseable {
    private final Driver driver;

    public Neo4jConnection(String uri, String user, String password) {
        driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password));
    }

    @Override
    public void close() throws Exception {
        driver.close();
    }

    public void insertTriple(final String object1, final String relation, final String object2) {
        try (Session session = driver.session()) {
            session.writeTransaction(tx -> {

                String cleanRelation = relation.replace("'", "").replace(" ","_");
                String cleanObject1 = object1.replace("'","").replace(" ", "_");
                String cleanObject2 = object2.replace("'","").replace(" ", "_");
                tx.run("MERGE (object:Object {name: $object})\n" +
                                "MERGE (object2:Object {name: $object2})\n" +
                                "MERGE (object)-[:" + cleanRelation + "]->(object2)",
                        parameters("object", cleanObject1, "object2", cleanObject2));

                tx.commit();

                return null;
            });
        }
    }

}