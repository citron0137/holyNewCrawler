package Struct;

public class PostImageDTO {
    private int post_seq;
    private String img_path;
    private String img_hash;

    public PostImageDTO(
            String img_path,
            String img_hash
    ){
        this.img_hash = img_hash;
        this. img_path = img_path;
    }

    public int getPost_seq() {
        return post_seq;
    }

    public String getImg_hash() {
        return img_hash;
    }

    public String getImg_path() {
        return img_path;
    }

    public void setPost_seq(int post_seq) {
        this.post_seq = post_seq;
    }
}
