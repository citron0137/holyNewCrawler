package Struct;

public class BoardDTO {
    private String BOARD_URL;
    private String LASTUPDATED_SELECTOR;
    private String BOARD_BASE_URL;
    private String POST_BASE_URL;
    private String TITLE_SELECTOR;
    private String USER_NAME_SELECTOR;
    private String CREATED_DATE_SELECTOR;
    private String DATE_TIME_FORMAT;

    private String MAGNET_URL_SELECTOR;
    private int FIRST_MAGNET_IDX;
    private int INC_MAGNET_IDX;
    private String MAGNET_URL_SELECTOR_BACK;

    private String TORRENT_URL_SELECTOR;
    private int FIRST_TORRENT_IDX;
    private int INC_TORRENT_IDX;
    private String TORRENT_URL_SELECTOR_BACK;
    public BoardDTO() { }
    public BoardDTO(
            String BOARD_URL,
            String LAST_UPDATED_SELECTOR,
            String BOARD_BASE_URL,
            String POST_BASE_URL,
            String TITLE_SELECTOR,
            String USER_NAME_SELECTOR,
            String CREATED_DATE_SELECTOR,
            String DATE_TIME_FORMAT,
            String MAGNET_URL_SELECTOR, int FIRST_MAGNET_IDX, int INC_MAGNET_IDX, String MAGNET_URL_SELECTOR_BACK,
            String TORRENT_URL_SELECTOR, int FIRST_TORRENT_IDX, int INC_TORRENT_IDX, String TORRENT_URL_SELECTOR_BACK
    ) {
        this.BOARD_URL = BOARD_URL;
        this.LASTUPDATED_SELECTOR = LAST_UPDATED_SELECTOR;
        this.BOARD_BASE_URL = BOARD_BASE_URL;
        this.POST_BASE_URL = POST_BASE_URL;
        this.TITLE_SELECTOR = TITLE_SELECTOR;
        this.USER_NAME_SELECTOR = USER_NAME_SELECTOR;
        this.CREATED_DATE_SELECTOR = CREATED_DATE_SELECTOR;
        this.DATE_TIME_FORMAT = DATE_TIME_FORMAT;
        this.MAGNET_URL_SELECTOR = MAGNET_URL_SELECTOR;
        this.FIRST_MAGNET_IDX = FIRST_MAGNET_IDX;
        this.INC_MAGNET_IDX = INC_MAGNET_IDX;
        this.MAGNET_URL_SELECTOR_BACK = MAGNET_URL_SELECTOR_BACK;
        this.TORRENT_URL_SELECTOR = TORRENT_URL_SELECTOR;
        this.FIRST_TORRENT_IDX = FIRST_TORRENT_IDX;
        this.INC_TORRENT_IDX = INC_TORRENT_IDX;
        this.TORRENT_URL_SELECTOR_BACK = TORRENT_URL_SELECTOR_BACK;
    }


}
