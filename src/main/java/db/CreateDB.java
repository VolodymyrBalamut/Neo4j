package db;
import model.Group;
import model.User;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;


import static org.neo4j.driver.v1.Values.parameters;

public class CreateDB implements AutoCloseable
{
    private final Driver driver;

    public CreateDB( String uri, String user, String password )
    {
        driver = GraphDatabase.driver( uri, AuthTokens.basic( user, password ) );
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }

    public void crateUser(User user)
    {
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    String node = user.lastName;
                    //tx.run("MATCH (a:Greeting) DETACH DELETE a");
                    StatementResult result = tx.run( "CREATE ("+node+":users) " +
                                    "SET "+node+".firstName  = $firstName, " +
                                     node +".lastName = $lastName, "+
                                     node +".age = $age, "+
                                     node +".id = $id, "+
                                     node +".gender = $gender, "+
                                     node +".posts = $posts "+
                                    "RETURN "+node+".firstName + ' '+  "+ node+".lastName+ ', from node ' + id("+node+")",
                            parameters( "firstName", user.firstName ,
                                    "lastName",user.lastName,"age",user.age,"id",user.id,"gender",user.gender,
                                    "posts",user.posts) );
                    return result.single().get( 0 ).asString();
                }
            } );
            System.out.println( greeting );
        }
    }
    public void createGroup(Group group)
    {
        try ( Session session = driver.session() )
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run(
                        "MERGE (" +
                                "a:group {name: {x}, id : {y}}" +
                                ")",
                        parameters(
                                "x", group.name,
                                "y", group.id
                        ));
                tx.success();
            }
        }
    }
    public void deleteAllNodes(){
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                @Override
                public String execute( Transaction tx )
                {
                    tx.run("MATCH (n) DETACH DELETE n");
                    return "Users have been deleted.";
                }
            } );
            System.out.println( greeting );
        }
    }
    public void CreateRelationship(User user1, User user2){
        try ( Session session = driver.session() )
        {
            String greeting = session.writeTransaction( new TransactionWork<String>()
            {
                String node1 = user1.lastName;
                String node2 = user2.lastName;
                @Override
                public String execute( Transaction tx )
                {
                    String str ="MATCH (a:users), (b:users) WHERE a.lastName = '"+user1.lastName+
                            "' AND b.lastName = '"+user2.lastName+"'"
                            +"CREATE (a)-[r:Knows]->(b)";
                    tx.run(str);
                    return "Created relationship";
                }
            } );
            System.out.println( greeting );
        }
    }
    public void createGroupRelation(Group group, User user)
    {
        try (Session session = driver.session())
        {
            try (Transaction tx = session.beginTransaction())
            {
                tx.run(
                        "match (a{id: {x} }),(b{id: {y} }) Merge (a)-[r: SUBSCRIBER]->(b)",
                        parameters(
                                "y", group.id,
                                "x",user.id
                        ));
                tx.success();
            }
        }
    }

}
