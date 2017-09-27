package javasource.responses;

public class NoContentResponse extends Response {

	public NoContentResponse() {
		super.setHttpStatusCode(204);
		super.setHttpStatus("No Content");
	}
	
}
