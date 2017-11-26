package model;

public class User {
    public int id;
    public String firstName;
    public String lastName;
    public String middleName;
    public int age;
    public String gender;
    public String[] posts;

    public User() {
    }

    public User(int id,String firstName, String lastName,int age,String gender,String[] posts) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.posts = posts;
    }

    @Override
    public final int hashCode(){
        int hash = 0;
        hash += id;
        hash += firstName.hashCode();
        hash += lastName.hashCode();
        hash += middleName.hashCode();
        hash += gender.hashCode();
        hash -= age;
        return hash;
    }
    @Override
    public final boolean equals(Object obj){
        if (obj instanceof User)
        {
            if (obj == null)
            {return false;}

            if (this == obj)
            {return true;}

            if (this.hashCode() == obj.hashCode())
            {return true;}
        }
        return false;
    }
    @Override
    public String toString() {
        return "[User: " + this.firstName + " " + this.middleName + " " + this.lastName + ", Age: " + this.age + "]";
    }
}
