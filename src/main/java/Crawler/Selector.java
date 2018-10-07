package Crawler;

public class Selector {

    public int LAST_UPDATED_IDX;
    public String BOARD_URL;
    public String LAST_UPDATED_SELECTOR;
    public String BOARD_BASE_URL;
    public String POST_BASE_URL;
    public String TITLE_SELECTOR;
    public String USER_NAME_SELECTOR;
    public String CREATED_DATE_SELECTOR;
    public String DATE_TIME_FORMAT;
    public String MAGNET_URL_SELECTOR;
    public int FIRST_MAGNET_IDX;
    public int INC_MAGNET_IDX;
    public String MAGNET_URL_SELECTOR_BACK;
    public String TORRENT_URL_SELECTOR;
    public int FIRST_TORRENT_IDX;
    public int INC_TORRENT_IDX;
    public String TORRENT_URL_SELECTOR_BACK;

    public Selector(
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
        this.LAST_UPDATED_SELECTOR = LAST_UPDATED_SELECTOR;
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

    public String getMagentUrl(int num){
        return this.MAGNET_URL_SELECTOR + (this.FIRST_MAGNET_IDX + this.INC_MAGNET_IDX * num) + this.MAGNET_URL_SELECTOR_BACK;
    }
    public String getTorrentUrl(int num){
        return this.TORRENT_URL_SELECTOR + (this.FIRST_TORRENT_IDX + this.INC_TORRENT_IDX * num) + this.TORRENT_URL_SELECTOR_BACK;
    }
}
