package tests;

import db.CreateDB;
import db.RequestToDB;
import model.Group;
import model.User;
import org.junit.Test;

import java.util.HashMap;

import static org.junit.Assert.*;

public class RequestToDBTest {
    public RequestToDB requestToDB;
    public RequestToDBTest() throws Exception{
        try ( CreateDB socialNetwork = new CreateDB( "bolt://localhost:7687", "neo4j", "neo4j" ) )
        {
            socialNetwork.deleteAllNodes();
            //create users
            HashMap<String, User> users = new HashMap<>();
            users.put("Smith",new User(10,"John","Smith",30,"male",new String[]{"ff"}));
            users.put("Yakovenko",new User(11,"Bogdan","Yakovenko",40,"male",new String[]{"ff","dfd"}));
            users.put("Bondarenko",new User(12,"Stepan","Bondarenko",25,"male",new String[]{"ff","gdd"}));
            users.put("Bulakh",new User(13,"Yaroslav","Bulakh",27,"male",new String[]{"ff","gdgd"}));
            users.put("Jouce",new User(14,"Steave","Jouce",18,"male",new String[]{"ff","dfdf"}));
            users.put("Dilan",new User(15,"Bob","Dilan",70,"male",new String[]{"ff","gdgdd"}));
            users.put("Roberts",new User(16,"Julia","Roberts",65,"female",new String[]{"ff"}));
            users.put("Tern",new User(17,"Rosa","Tern",12,"female",new String[]{"ff"}));
            users.put("Nazarenko",new User(18,"Yaroslava","Nazarenko",20,"female",new String[]{"ff"}));
            users.put("Dilan1",new User(19,"Kate","Dilani",45,"female",new String[]{"ff"}));
            users.values().stream().forEach(user->socialNetwork.crateUser(user));

            //create friends relationships
            socialNetwork.CreateRelationship(users.get("Smith"),users.get("Jouce"));
            socialNetwork.CreateRelationship(users.get("Smith"),users.get("Bulakh"));
            socialNetwork.CreateRelationship(users.get("Bulakh"),users.get("Roberts"));
            socialNetwork.CreateRelationship(users.get("Nazarenko"),users.get("Dilan1"));
            socialNetwork.CreateRelationship(users.get("Nazarenko"),users.get("Tern"));
            socialNetwork.CreateRelationship(users.get("Bondarenko"),users.get("Yakovenko"));
            socialNetwork.CreateRelationship(users.get("Yakovenko"),users.get("Tern"));

            //create groups
            HashMap<String, Group> groups = new HashMap<>();
            groups.put("Football",new Group(1,"Football"));
            groups.put("Music",new Group(2,"Music"));
            groups.put("IT",new Group(3,"IT"));
            groups.values().stream().forEach(group->socialNetwork.createGroup(group));

            //create groups relationships
            socialNetwork.createGroupRelation(groups.get("IT"),users.get("Smith"));
            socialNetwork.createGroupRelation(groups.get("IT"),users.get("Yakovenko"));
            socialNetwork.createGroupRelation(groups.get("IT"),users.get("Bondarenko"));
            socialNetwork.createGroupRelation(groups.get("IT"),users.get("Nazarenko"));
            socialNetwork.createGroupRelation(groups.get("Music"),users.get("Smith"));
            socialNetwork.createGroupRelation(groups.get("Music"),users.get("Bulakh"));
            socialNetwork.createGroupRelation(groups.get("Music"),users.get("Dilan1"));
            socialNetwork.createGroupRelation(groups.get("Music"),users.get("Yakovenko"));
            socialNetwork.createGroupRelation(groups.get("Music"),users.get("Roberts"));
            socialNetwork.createGroupRelation(groups.get("Football"),users.get("Nazarenko"));
            socialNetwork.createGroupRelation(groups.get("Football"),users.get("Jouce"));
        }
        requestToDB = new RequestToDB();
    }
    @Test
    public void getUsersNames() throws Exception {
        String expected = "[[\"Stepan\", \"Bondarenko\"], " +
                "[\"Yaroslav\", \"Bulakh\"], " +
                "[\"Bob\", \"Dilan\"], " +
                "[\"Kate\", \"Dilani\"], " +
                "[\"Steave\", \"Jouce\"], " +
                "[\"Yaroslava\", \"Nazarenko\"], " +
                "[\"Julia\", \"Roberts\"], " +
                "[\"John\", \"Smith\"], " +
                "[\"Rosa\", \"Tern\"], " +
                "[\"Bogdan\", \"Yakovenko\"]]";
        assertEquals(requestToDB.getUsersNames(), expected);
    }
    @Test
    public void getMen() throws Exception {
        String expected = "[[\"Bob\", \"Dilan\", 70], " +
                "[\"Bogdan\", \"Yakovenko\", 40], " +
                "[\"John\", \"Smith\", 30], [\"Yaroslav\"," +
                " \"Bulakh\", 27], [\"Stepan\", \"Bondarenko\", 25]," +
                " [\"Steave\", \"Jouce\", 18]]";
        assertEquals(requestToDB.getMen(), expected);
    }
    @Test
    public void getFriends() throws Exception {
        String expected = "[[\"Bogdan\", \"Yakovenko\"], [\"Yaroslava\", \"Nazarenko\"]]";
        assertEquals(requestToDB.getFriends("Tern"), expected);
    }
    @Test
    public void getFriendsOfFriends() throws Exception {
        String expected = "[[\"Stepan\", \"Bondarenko\"]]";
        assertEquals(requestToDB.getFriendsOfFriends("Tern"), expected);
    }
    @Test
    public void getCountOfFriends() throws Exception {
        String expected = "[[\"Bogdan\", \"Yakovenko\", 1], " +
                "[\"Julia\", \"Roberts\", 1], " +
                "[\"Kate\", \"Dilani\", 1], " +
                "[\"Rosa\", \"Tern\", 2], " +
                "[\"Steave\", \"Jouce\", 1], " +
                "[\"Yaroslav\", \"Bulakh\", 1]]";
        assertEquals(requestToDB.getCountOfFriends(), expected);
    }
    @Test
    public void getAllGroups() throws Exception {
        String expected = "[[\"Football\"], [\"IT\"], [\"Music\"]]";
        assertEquals(requestToDB.getAllGroups(), expected);
    }
    @Test
    public void getGroupsOfUser() throws Exception {
        String expected = "[[\"Football\"], [\"IT\"]]";
        assertEquals(requestToDB.getGroupsOfUser("Nazarenko"), expected);
    }
    @Test
    public void getCountOfSubscriber() throws Exception {
        String expected = "[[\"Music\", 5], [\"IT\", 4], [\"Football\", 2]]";
        assertEquals(requestToDB.getCountOfSubscriber(), expected);
    }
    @Test
    public void getCountOfUsersGroups() throws Exception {
        String expected = "[[\"John\", \"Smith\", 2], " +
                "[\"Yaroslava\", \"Nazarenko\", 2], " +
                "[\"Bogdan\", \"Yakovenko\", 2], " +
                "[\"Yaroslav\", \"Bulakh\", 1], " +
                "[\"Stepan\", \"Bondarenko\", 1], " +
                "[\"Kate\", \"Dilani\", 1], " +
                "[\"Steave\", \"Jouce\", 1], " +
                "[\"Julia\", \"Roberts\", 1]]";
        assertEquals(requestToDB.getCountOfUsersGroups(), expected);
    }
    @Test
    public void getCountOfGroupsFriendsOfFriends() throws Exception {
        String expected = "[[1]]";
        assertEquals(requestToDB.getCountOfGroupsFriendsOfFriends("Tern"), expected);
    }
    @Test
    public void getUserPosts() throws Exception {
        String expected = "[[[\"ff\"]]]";
        assertEquals(requestToDB.getUserPosts("Tern"), expected);
    }
    @Test
    public void getPostsGreaterByNum() throws Exception {
        String expected = "[[\"Dilan\", [\"gdgdd\"]]]";
        assertEquals(requestToDB.getPostsGreaterByNum(4), expected);
    }
    @Test
    public void getCountOfPosts() throws Exception {
        String expected = "[[\"Stepan\", \"Bondarenko\", 2], " +
                "[\"Yaroslav\", \"Bulakh\", 2], " +
                "[\"Bob\", \"Dilan\", 2], " +
                "[\"Steave\", \"Jouce\", 2], " +
                "[\"Bogdan\", \"Yakovenko\", 2], " +
                "[\"Kate\", \"Dilani\", 1], " +
                "[\"Yaroslava\", \"Nazarenko\", 1], " +
                "[\"Julia\", \"Roberts\", 1], " +
                "[\"John\", \"Smith\", 1], " +
                "[\"Rosa\", \"Tern\", 1]]";
        assertEquals(requestToDB.getCountOfPosts(), expected);
    }
    @Test
    public void getPostsFriendsOfFriends() throws Exception {
        String expected = "[[\"Bondarenko\", [\"ff\", \"gdd\"]]]";
        assertEquals(requestToDB.getPostsFriendsOfFriends("Tern"), expected);
    }
    @Test
    public void getAvgOfUsersPosts() throws Exception {
        String expected = "[[\"Bulakh\", 3], " +
                "[\"Dilan\", 3], " +
                "[\"Jouce\", 3], " +
                "[\"Bondarenko\", 2], " +
                "[\"Dilani\", 2], " +
                "[\"Nazarenko\", 2], " +
                "[\"Roberts\", 2], " +
                "[\"Smith\", 2], " +
                "[\"Tern\", 2], " +
                "[\"Yakovenko\", 2]]";
        assertEquals(requestToDB.getAvgOfUsersPosts(), expected);
    }
}