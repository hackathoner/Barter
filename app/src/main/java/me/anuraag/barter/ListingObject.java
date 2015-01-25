package me.anuraag.barter;

/**
 * Created by Anuraag on 12/24/14.
 */
public class ListingObject {
    private String title,address,description,creator,creatorName;

    public ListingObject(String t, String a, String d, String c,String cName){
        this.title = t;
        this.address = a;
        this.description = d;
        this.creator = c;
        this.creatorName = cName;
    }
    public String getTitle(){
        return this.title;
    }
    public String getAddress(){
        return this.address;
    }
    public String getDescription(){
        return this.description;
    }
    public String getCreator(){
        return this.creator;
    }
    public String getCreatorName(){
        return this.creatorName;
    }
    public void setTitle(String t){
        this.title = t;
    }
    public void setCreatorName(String cName){
        this.creatorName = cName;
    }
    public void setAddress(String a){
        this.address = a;
    }
    public void setDescription(String d){
        this.description = d;
    }
    public void setCreator(String c){
        this.creator = c;
    }
    public String toString(){
        return getTitle() + " " + getAddress() + " " + getDescription() + " " + this.creator;

    }
}
