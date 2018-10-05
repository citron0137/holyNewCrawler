package Struct;

public class SiteDTO {
    private int SITE_SEQ;
    private String SITE_LINK;
    private String ID;
    private String PW;

    public SiteDTO(
            int SITE_SEQ,
            String SITE_LINK,
            String ID,
            String PW
    ){
        this.SITE_SEQ = SITE_SEQ;
        this.SITE_LINK = SITE_LINK;
        this.ID = ID;
        this.PW = PW;
    }
    public int getSITE_SEQ() {
        return SITE_SEQ;
    }
    public String getSITE_LINK(){
        return SITE_LINK;
    }
    public String getID(){
        return ID;
    }
    public String getPW(){
        return PW;
    }
}
