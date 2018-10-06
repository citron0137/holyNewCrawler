package bttSQL;

import Struct.BoardDTO;
import Struct.PostDTO;
import Struct.SiteDTO;
import Struct.*;


import java.sql.*;
import java.util.List;

public class DBHandler {

    private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private final String SQL_URL = "jdbc:mysql://localhost";
    private final String SQL_USER = "root";
    private final String SQL_PW = "ehgns123#";

    private Connection con;
    private Statement stat;

    public boolean setCon() {
        try {
            Class.forName(JDBC_DRIVER);
            this.con = DriverManager.getConnection(SQL_URL, SQL_USER, SQL_PW);
            this.stat = this.con.createStatement();
            this.stat.executeQuery("USE btt");
        }catch (Exception e){
            System.out.println(e);
            return false;
        }
        return true;
    }

    public boolean insertTorrent( TorrentFileDTO torrentFile){
        String col = "post_seq, magnet, infohash, file_size";
        String values = torrentFile.getPost_seq()+", '"+torrentFile.getMagnet()+ "','" + torrentFile.getInfohash_dir()+"', '"+torrentFile.getFile_size() +"'";
        String SQL_Q = "insert torrent_file ("+col+") values ("+values+")";
        System.out.println(SQL_Q);
        try {
            this.stat.execute(SQL_Q);

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertPostImg(PostImageDTO postImage){
        String col = "post_seq, img_path, img_hash";
        String values = postImage.getPost_seq()+", '"+postImage.getImg_path()+ "','" + postImage.getImg_hash()+"'";
        String SQL_Q = "insert post_img ("+col+") values ("+values+")";
        System.out.println(SQL_Q);
        try {
            this.stat.execute(SQL_Q);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean insertPost(PostDTO post){
        String col = "board_link, post_link, post_name, post_date, user_id";
        String values = "'"+post.getBoard_seq()+"', '"+post.getPost_link() + "','" + post.getPost_name()+"', '"+post.getPost_date() +"','"+post.getUser_id()+"'";
        String SQL_Q = "insert post ("+col+") values ("+values+")";
        System.out.println(SQL_Q);
        try {
            this.stat.execute(SQL_Q);
            ResultSet rs = this.stat.executeQuery("select * from post where ("+col+") = ("+values+")");
            if(rs.next()) {
                post.setSeq(Integer.parseInt(rs.getString(1)));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        if(post.getTorrents() == null) {
            System.out.println("no torrents file");
            return false;
        }
        for (TorrentFileDTO eachTorrent: post.getTorrents()) {
            eachTorrent.setPost_seq(post.getSeq());
            insertTorrent(eachTorrent);
        }
        if(post.getImages() != null) {
            for (PostImageDTO eachImage : post.getImages()) {
                eachImage.setPost_seq(post.getSeq());
                insertPostImg(eachImage);
            }
        }
        return true;
    }

    public void insertPosts(List<PostDTO> posts){
        for (PostDTO iter: posts) {
            insertPost(iter);
        }
        return;
    }
    public SiteDTO getSiteInfo(String SITE_URL){
        try {
            ResultSet rs = this.stat.executeQuery("select * from site where site_link = \""+SITE_URL+"\"");
            if(rs.next())
            {

                System.out.println("seq : " + rs.getString(1));
                System.out.println("site-link : " +rs.getString(2));
                System.out.println("id :"+rs.getString(3));
                System.out.println("pw :"+rs.getString(4));
                System.out.println("");

                return new SiteDTO(
                        Integer.parseInt(rs.getString(1)),
                        rs.getString(2),
                        rs.getString(3),
                        rs.getString(4)
                );
            }
            else{
                System.out.println("no site results");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public BoardDTO getBoardInfo(String BOARD_URL){
        try {
            ResultSet rs = this.stat.executeQuery("select * from board where board_link = \""+BOARD_URL+"\"");
            if(rs.next())
            {

                System.out.println("seq : " + rs.getString(1));
                System.out.println("site-seq : " + rs.getString(2));
                System.out.println("board-link : " +rs.getString(3));
                System.out.println("");

                return new BoardDTO(
                        Integer.parseInt(rs.getString(1)),
                        Integer.parseInt(rs.getString(2)),
                        rs.getString(3)
                );
            }
            else{
                System.out.println("no board results");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    public boolean close(){
        try {
            this.stat.close();
            this.con.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
