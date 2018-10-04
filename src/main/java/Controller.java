import Crawler.Crawler;
import Crawler.TorrentLinCrawler;
import Struct.PostDTO;
import Struct.SiteDTO;
import bttSQL.DBHandler;

import java.util.Date;
import java.util.List;

public class Controller {
    public void main(String[] args){
        DBHandler bttDb = new DBHandler();
        bttDb.setCon();

        Crawler torrentLinCrawler = new TorrentLinCrawler();
        SiteDTO torrentLinSite = bttDb.getSiteInfo();
            //if I throw site link -> set all data about site
        torrentLinCrawler.setBoards(bttDb.getBoardinfo());
            //if I throw board link -> set all data about board
        torrentLinCrawler.login(null,null);
            //Login
        List<PostDTO> torrentLinPosts = torrentLinCrawler.getPosts(new Date(),new Date());
            //get Posts
        bttDb.insertPosts(torrentLinPosts);
            //inset Posts at Database



        bttDb.close();
    }
}
