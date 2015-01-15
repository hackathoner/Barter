package me.anuraag.barter;

/**
 * Created by Anuraag on 12/24/14.
 */
public class ListingObject {
    private String title,address,description,creator;

    public ListingObject(String t, String a, String d, String c){
        this.title = t;
        this.address = a;
        this.description = d;
        this.creator = c;
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
    public void setTitle(String t){
        this.title = t;
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
