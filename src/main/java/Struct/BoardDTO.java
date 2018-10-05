package Struct;

public class BoardDTO {
    private String BOARD_URL;
    private int seq;
    private int site_seq;

    public BoardDTO() { }
    public BoardDTO(
            int seq,
            int site_seq,
            String BOARD_URL
            ) {
        this.seq = seq;
        this.site_seq =site_seq;
        this.BOARD_URL = BOARD_URL;

    }


}
