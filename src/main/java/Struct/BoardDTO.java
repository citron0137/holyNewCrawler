package Struct;

import Crawler.Selector;

public class BoardDTO {
    private String BOARD_URL;
    private int seq;
    private int site_seq;
    private Selector selector;

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

    public String getBoardUrl() {
        return  BOARD_URL;
    }

    public void setSelector(Selector selector) {
        this.selector = selector;
    }

    public Selector getSelector() {
        return selector;
    }

    public int getSeq() {
        return seq;
    }
}
