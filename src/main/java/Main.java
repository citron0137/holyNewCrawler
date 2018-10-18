import Crawler.Crawler;
import Crawler.TorrentLinCrawl;
import bttSQL.*;
import Struct.PostDTO;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.time.LocalDateTime;

public class Main {
    public  static void main(String[] args){
        Controller controller = new Controller();
        //controller.getTorrentLinPosts("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_new",LocalDateTime.of(2018,10,4,0,0),LocalDateTime.of(2018,10,7,1,1));
        //controller.getTorrentLinPosts("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_old",LocalDateTime.of(2018,10,4,0,0),LocalDateTime.of(2018,10,7,1,1));
        //controller.getTorrentLinPosts("https://torrentlin.com/bbs/board.php?bo_table=torrent_movie_3d",LocalDateTime.of(2018,10,4,0,0),LocalDateTime.of(2018,10,7,1,1));
        //controller.getTorrentBozzaPosts("https://torrentboza.com/bbs/board.php?bo_table=ko_movie",LocalDateTime.of(2018,10,4,0,0),LocalDateTime.of(2018,10,7,1,1));
        controller.getBeeTorrentPosts("https://beetorrent.com/영화/최신영화/?board_name=newmovie",LocalDateTime.of(2018,10,4,0,0),LocalDateTime.of(2018,10,7,1,1));
    }
}
