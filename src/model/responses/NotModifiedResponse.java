package model.responses;

public class NotModifiedResponse extends Response {

    public NotModifiedResponse(){
        super.setHttpStatusCode(304);
        super.setHttpStatus("Not Modified");
    }

}