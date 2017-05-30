package ch.aymenfurter.imops.cxfrs.pojo;

public class ImageOperationResponse {

	private String img;
	private String message;
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getImg() {
		return img;
	}
	
	public void setImg(String img) {
		this.img = img;
	}
	
	@Override
	public String toString() {
		return "ImageOperationResponse [img=" + img + ", message="
				+ message + "]";
	}
	
}
