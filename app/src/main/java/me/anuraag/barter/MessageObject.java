package me.anuraag.barter;

/**
 * Created by Anuraag on 1/25/15.
 */
public class MessageObject {
    private String timeStamp,senderEmail,messageText;
    public MessageObject(String timpeStamp, String senderEmail, String messageText){
        this.timeStamp = timpeStamp;
        this.senderEmail = senderEmail;
        this.messageText = messageText;
    }
    public String getTimeStamp(){
        return  this.timeStamp;
    }
    public String getSenderEmail(){
        return this.senderEmail;
    }
    public String getMessageText(){
        return this.messageText;
    }
    public void setTimeStamp(String timeStamp){
        this.timeStamp = timeStamp;
    }
    public void setSenderEmail(String senderEmail){
        this.senderEmail = senderEmail;
    }
    public void setMessageText(String messageText){
        this.messageText = messageText;
    }
    public String toString(){
        return this.messageText + " " + this.senderEmail + " " + this.timeStamp;
    }
}
