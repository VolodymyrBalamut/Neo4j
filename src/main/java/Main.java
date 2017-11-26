package java;

import db.CreateDB;
import db.RequestToDB;
import model.Group;
import model.User;

import java.util.HashMap;

public class Main {

    public static void main( String... args ) throws Exception
    {
        try ( CreateDB socialNetwork = new CreateDB( "bolt://localhost:7687", "neo4j", "password" ) )
        {
            socialNetwork.deleteAllNodes();
            //create users
            HashMap<String, User> users = new HashMap<>();
            users.put("Smith",new User(10,"John","Smith",30,"male",new String[]{"ff"}));
            users.put("Yakovenko",new User(11,"Bogdan","Yakovenko",40,"male",new String[]{"ff","dfd"}));
            users.put("Bondarenko",new User(12,"Stepan","Bondarenko",25,"male",new String[]{"ff","gdd"}));
            users.put("Bulakh",new User(13,"Yaroslav","Bulakh",27,"male",new String[]{"ff","gdgd"}));
            users.put("Jouce",new User(14,"Steave","Jouce",18,"male",new String[]{"ff","dfdf"}));
            users.put("Dilan",new User(15,"Bob","Dilan",70,"male",new String[]{"ff","gdgd"}));
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
        RequestToDB requestToDB = new RequestToDB();
        System.out.println(requestToDB.getUsersNames());
        System.out.println(requestToDB.getMen());
        System.out.println(requestToDB.getFriends("Tern"));
        System.out.println(requestToDB.getFriendsOfFriends("Tern"));
        System.out.println(requestToDB.getCountOfFriends());
        System.out.println(requestToDB.getAllGroups());
        System.out.println(requestToDB.getGroupsOfUser("Nazarenko"));
        System.out.println(requestToDB.getCountOfSubscriber());
        System.out.println(requestToDB.getCountOfUsersGroups());
        System.out.println(requestToDB.getCountOfGroupsFriendsOfFriends("Tern"));
        System.out.println(requestToDB.getUserPosts("Yakovenko"));
        System.out.println(requestToDB.getPostsGreaterByNum(3));
        System.out.println(requestToDB.getCountOfPosts());
        System.out.println(requestToDB.getPostsFriendsOfFriends("Tern"));
        System.out.println(requestToDB.getAvgOfUsersPosts());
    }
}
