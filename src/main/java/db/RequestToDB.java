package db;

import org.neo4j.driver.v1.*;

import java.util.ArrayList;
import java.util.List;

import static org.neo4j.driver.v1.Values.parameters;

public class RequestToDB implements AutoCloseable{
    public static Driver driver;

    public RequestToDB() {
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4j"));
    }

    @Override
    public void close() throws Exception
    {
        driver.close();
    }
    public static String request(String query)
    {
        List<String> arr = new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(query);
            while (result.hasNext())
            {
                Record record = result.next();
                arr.add(record.values().toString());
            }
        }
        return arr.toString();
    }
    public static String request(String query, String name)
    {
        List<String> arr = new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(query, parameters("x", name));
            while (result.hasNext())
            {
                Record record = result.next();
                arr.add(record.values().toString());
            }
        }
        return arr.toString();
    }
    public static String request(String query, int num)
    {
        List<String> arr = new ArrayList<String>();
        try (Session session = driver.session())
        {
            StatementResult result = session.run(query, parameters("x", num));
            while (result.hasNext())
            {
                Record record = result.next();
                arr.add(record.values().toString());
            }
        }
        return arr.toString();
    }


    public String getUsersNames()
    {
        return request("match (n:users) return n.firstName, n.lastName order by n.lastName,n.firstName");
    }
    public String getMen()
    {
        return request("match (n:users) where n.gender='male' return n.firstName, n.lastName, n.age order by n.age desc");
    }
    public String getFriends(String name)
    {
        return request("match (node:users)<-[:Knows]-(n) where node.lastName = {x} return n.firstName, n.lastName order by n.firstName, n.lastName", name);
    }
    public String getFriendsOfFriends(String name)
    {
        return request("match (node:users)<-[:Knows]-(n)<-[:Knows]-(f) where node.lastName = {x} return f.firstName, f.lastName order by f.firstName, f.lastName", name);
    }
    public String getCountOfFriends()
    {
        return request("MATCH (n:users)<-[:Knows]-(f) RETURN n.firstName,n.lastName, count(f) as counter order by n.firstName,n.lastName");
    }
    public String getAllGroups()
    {
        return request("match (g:group) return g.name order by g.name");
    }
    public String getGroupsOfUser(String name)
    {
        return request("match (g:group)<-[:SUBSCRIBER]-(p:users) where p.lastName = {x} return g.name order by g.name", name);
    }
    public String getCountOfSubscriber()
    {
        return request("match (g:group)<-[:SUBSCRIBER]-(p:users) return g.name, count(p) order by count(p) desc");
    }
    public String getCountOfUsersGroups()
    {
        return request("match (g:group)<-[:SUBSCRIBER]-(p:users) return p.firstName,p.lastName, count(g) order by count(g) desc");
    }
    public String getCountOfGroupsFriendsOfFriends(String name)
    {
        return request("match (p:users)<-[:Knows]-(f:users)<-[:Knows]-(ff:users)-[:SUBSCRIBER]->(g:group) where p.lastName={x} return count(g)", name);
    }
    public String getUserPosts(String name)
    {
        return request("match (n) where n.lastName={x} return n.posts", name);
    }
    public String getPostsGreaterByNum(int num)
    {
        return request("match (n:users) where length(filter(row in n.posts where length(row)>{x}))>0  return n.lastName, filter(row in n.posts where length(row)>{x})", num);
    }
    public String getCountOfPosts()
    {
        return request("match (n:users) return n.firstName, n.lastName, size(n.posts) order by size(n.posts) desc,n.lastName ");
    }
    public String getPostsFriendsOfFriends(String name)
    {
        return request("match (n:users)<-[:Knows]-(f:users)<-[:Knows]-(ff:users) where n.lastName={x} return ff.lastName, ff.posts", name);
    }
    public String getAvgOfUsersPosts()
    {
        return request("match (p:users) with (reduce(total = 0, row in p.posts | total + length(row)))/size(p.posts) as num, p.lastName as name return name, num order by num desc,name");
    }
}
