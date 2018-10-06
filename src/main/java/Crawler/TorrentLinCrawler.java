package Crawler;

import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import Struct.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import Struct.BoardDTO;
import Struct.PostDTO;
import java.util.Date;

import javax.net.ssl.SSLHandshakeException;

public class TorrentLinCrawler implements Crawler{

    //셀렉터
    public String BOARD_URL;
    public String LASTUPDATED_SELECTOR = "td.subject a[href]";
    public String BOARD_BASE_URL = "../bbs/board.php?bo_table=torrent_movie_new&wr_id=";

    public int LASTUPDATED_IDX;
    public String POST_BASE_URL;
    public String TITLE_SELECTOR = "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(2) > table:nth-child(1) > tbody > tr > td:nth-child(1) > h1";
    public String USER_NAME_SELECTOR;
    public String CREATED_DATE_SELECTOR = "body > div > table > tbody > tr:nth-child(6) > td > table > tbody > tr > td:nth-child(1) > div:nth-child(2) > table:nth-child(3) > tbody > tr > td > div:nth-child(1) > div:nth-child(1) > span";
    public String DATE_TIME_FORMAT;
    public String MAGNET_URL_SELECTOR;
    public int FIRST_MAGNET_IDX;
    public int INC_MAGNET_IDX;
    public String MAGNET_URL_SELECTOR_BACK;
    public String TORRENT_URL_SELECTOR;
    public int FIRST_TORRENT_IDX;
    public int INC_TORRENT_IDX;
    public String TORRENT_URL_SELECTOR_BACK;

    private String title;
    private String saveTitle;

//TODO SocketTimeoutException 예외처리


    private Document document;

    private FileOutputStream out;

    List<BoardDTO> boards = new ArrayList<BoardDTO>();
    List<PostDTO> posts = new ArrayList<PostDTO>();
    List<TorrentFileDTO> torrentFile;

    private BoardDTO board;
    private PostDTO post;

    public TorrentLinCrawler() {}
    public TorrentLinCrawler(List<BoardDTO> boardList){
        this.boards = boardList;
    }


    public void login(String id, String pw) {
        //TODO Login
        return;
    }

    //데이터베이스에 저장된 해당 사이트의 BoardURL을 가져온다
    public void setBoards(List<BoardDTO> boards) {
        this.boards = boards;
    }

    //TODO 이름 바꿔야함

    public List<TorrentFileDTO> saveTorrentFile(String url) {
        torrentFile = new ArrayList<>();
        try {
            Connection.Response loginPageResponse = Jsoup.connect(url)
                    .timeout(3000)
                    .header("Origin", "http://torrentlin.com")
                    .header("Referer", "http://torrentlin.com")
                    .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                    .header("Content-Type", "application/x-www-form-urlencoded")
//                                    .header("Accept-Encoding", "gzip, deflate, br")
//                                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                    .method(Connection.Method.GET)
                    .execute();

            // 로그인 페이지에서 얻은 쿠키
            Map<String, String> cookie = loginPageResponse.cookies();

            // 로그인 페이지에서 로그인에 함께 전송하는 토큰 얻어내기
            this.document = loginPageResponse.parse();

            Elements elems2 = this.document.select("a[href]");

//            for (Element elem2 : elems2) {
//                System.out.println(elem2.toString());
//            }

            for (Element elem2 : elems2) {
                String[] array2 = elem2.toString().split(">");
                String param2 = "";
                int count = 0;
                for (String tmpStr : array2) {
                    if (tmpStr.contains("<a href=\"javascript:file_download")) {
                        count = count + 1;
//                        System.out.println(tmpStr);
                        String[] downloadInfo = tmpStr.split(",");
                        downloadInfo[0] = downloadInfo[0].replace("<a href=\"javascript:file_download('..", "")
                                .replace("\'", "").replace("amp;", "");
                        downloadInfo[1] = downloadInfo[1].replace("\'", "").replace(");\"", "");

//                        System.out.println(downloadInfo[0]);
//                        System.out.println(downloadInfo[1]);
                        String fileDownloadUrl = "https://torrentlin.com" + downloadInfo[0] + "&" + downloadInfo[1];
//                        System.out.println("download Url : "+ fileDownloadUrl);

                        Connection.Response response = Jsoup.connect(fileDownloadUrl)
//array[1]
                                .header("Origin", "http://torrentlin.com")
                                .header("Referer", url)
                                .header("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
                                .header("Content-Type", "text/html;charset=iso-8859-1")
                                .header("Accept-Encoding", "gzip, deflate, br")
//                                    .header("Accept-Language", "ko-KR,ko;q=0.8,en-US;q=0.6,en;q=0.4")
                                .method(Connection.Method.GET)
                                .cookies(cookie)
                                .ignoreContentType(true)
                                .execute();


                        Map<String, String> loginTryCookie = response.cookies();


                        if (downloadInfo[1].contains(".torrent")) {
                            System.out.println(this.title);

                            //TODO 정규표현식으로 변경
                            this.saveTitle = this.title
                                    .replace(".", " ")
                                    .replace("\\", "")
                                    .replace("/", "")
                                    .replace(":", "")
                                    .replace("*", "")
                                    .replace("?", "")
                                    .replace("\"", "")
                                    .replace("<", "")
                                    .replace(">", "")
                                    .replace("|", "");


                            out = (new FileOutputStream(new java.io.File(this.saveTitle + count + ".torrent")));
                            out.write(response.bodyAsBytes());
                            out.close();
                        }
//                        }else{
//                            out = (new FileOutputStream(new java.io.File(downloadInfo[1])));
//                        }


                        this.torrentFile.add(new TorrentFileDTO("",this.saveTitle + count + ".torrent"));

                        System.out.println("done");
                        break;
                    }
                }
            }

        } catch (IOException e) {

            e.printStackTrace();
            return null;
        }
        return this.torrentFile;
    }

    //
    public PostDTO getPost(int num) {
        try {
            //Post 테이블에 저장할 페이지 URL 얻기
//            System.out.println(this.board.getBoardUrl() + "&wr_id=" + num);
            this.document = Jsoup.connect(this.board.getBoardUrl() + "&wr_id=" + num).get();

//            System.out.println(this.document.text());
//
            String date = this.document.select(CREATED_DATE_SELECTOR).text();
            this.title = this.document.select(TITLE_SELECTOR).text();

            //TODO 이미지 저장 경로 가져와서 이미지 저장하기

//            String img = this.document.select(IMG_SELECTOR).text();

            //TODO 토렌트 파일 저장 경로 가져와서 .torrent파일 저장하기

//            System.out.println("");
            System.out.println(date);

            date = date.replace("업데이트 : ","");

            SimpleDateFormat dt = new SimpleDateFormat("yy-mm-dd hh:mm");

            Date dateData = dt.parse(date);

            this.post = new PostDTO();
//            this.post.setPost_date(dateData);
            this.post.setPost_date(LocalDateTime.of(2018,10,1,10,10));

            System.out.println(this.title);
            System.out.println(dateData.toString());

            post.setPost_link(this.board.getBoardUrl() + "&wr_id=" + num);
            post.setPost_name(this.title);
            //post.setPost_date(dateData);
            post.setTorrents(saveTorrentFile(this.board.getBoardUrl() + "&wr_id=" + num));

        } catch (UnknownHostException e) {
            System.out.println("It must be deleted page");
//            return new PostDTO();
        } catch (IOException e) {
            e.printStackTrace();
//            return new PostDTO();
        }catch (ParseException pe){
            pe.printStackTrace();
//            return new PostDTO();
        }
        return post;
    }
    //crawlPostData
    public int crawlTorrentSite() {
        String lastUpdated;
        String lastIndex;
        try {
            this.document = Jsoup.connect(this.BOARD_URL).get();
        } catch (UnknownHostException e) {
            System.out.println("It must be deleted page");

        } catch (IOException e) {
            e.printStackTrace();
        }

        lastUpdated = this.document.select(this.LASTUPDATED_SELECTOR).attr("href"); //.substring(this.BOARD_BASE_URL.length());
        if (lastUpdated!="") {
//            System.out.println(lastUpdated);
            lastIndex = lastUpdated.replaceAll("[^0-9]", " ");
            this.LASTUPDATED_IDX = Integer.parseInt(lastIndex.trim().split(" ")[0]);
            System.out.println(this.LASTUPDATED_IDX);

        }
        return this.LASTUPDATED_IDX;
    }

    public int doCrawl(BoardDTO board){

        this.BOARD_URL = board.getBoardUrl();
        this.board = board;

        System.out.println(this.BOARD_URL);
        return crawlTorrentSite();
    }

    //포스트 내용을 크롤링해서 저장
    public void setPost(List<BoardDTO> boards){
        this.boards = boards;
    }

    public PostDTO getPost(BoardDTO board, int postNum) {
        return null;
    }

    public List<PostDTO> getPosts(Date start, Date end) {
        return this.posts;
    }
}
