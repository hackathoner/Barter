package me.anuraag.barter;

/**
 * Created by Anuraag on 1/25/15.
 */
public class ChatObject {
    private String title,chatterName,chatterEmail;

    public ChatObject(String title, String chatterName, String chatterEmail){
        this.title = title;
        this.chatterName = chatterName;
        this.chatterEmail = chatterEmail;
    }
    public String getTitle(){
        return this.title;
    }
    public String getChatterName(){
       return this.chatterName;
    }
    public String getChatterEmail(){
        return this.chatterEmail;
    }
    public void setTitle(String title){
        this.title = title;
    }
    public void setChatterName(String chatterName){
        this.chatterName = chatterName;
    }
    public void setChatterEmail(String chatterEmail){
        this.chatterEmail = chatterEmail;
    }
    public String toString(){
        return this.title + " " + this.chatterName + " " + this.chatterEmail;
    }
}
