package bttSQL;

import Struct.BoardDTO;
import Struct.PostDTO;
import Struct.SiteDTO;

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

public class DBHandler {
    Connection con;
    Statement stat;

    public void setCon(){

        return;
    }
    public void insertTorrent(){
        return;
    }
    public void insertPostImg(){
        return;
    }
    public void insertPost(PostDTO post){
        insertPostImg();
        insertTorrent();
        return;
    }
    public void insertPosts(List<PostDTO> posts){
        return;
    }
    public List<BoardDTO> getBoardinfo(){
        return null;
    }
    public SiteDTO getSiteInfo(){
        return null;
    }
    public void close(){
        return;
    }
}
