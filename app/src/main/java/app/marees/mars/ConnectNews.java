package app.marees.mars;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

import app.marees.mars.Singletons.Myapp;

public class ConnectNews extends AsyncTask<String,Integer,ArrayList<String>> {
    private static ArrayList<String> latest=new ArrayList<>();
    private static ArrayList<String> others=new ArrayList<>();
    private static ArrayList<String> trends=new ArrayList<>();
    private static ArrayList<String> tech=new ArrayList<>();
    private static ArrayList<String> astro=new ArrayList<>();
    private static ArrayList<String> study=new ArrayList<>();
    private static ArrayList<String> kovil=new ArrayList<>();
    private static ArrayList<String> health=new ArrayList<>();
    private static ArrayList<String> tour=new ArrayList<>();
    private static ArrayList<String> recipe=new ArrayList<>();
    private static ArrayList<String> cinema=new ArrayList<>();
    private static ArrayList<String> sports=new ArrayList<>();
    private static ArrayList<String> life=new ArrayList<>();
    private static ArrayList<String> mukiyaNews=new ArrayList<>();
    private static ArrayList<String> latestu=new ArrayList<>();
    private static ArrayList<String> othersu=new ArrayList<>();
    private static ArrayList<String> trendsu=new ArrayList<>();
    private static ArrayList<String> techu=new ArrayList<>();
    private static ArrayList<String> astrou=new ArrayList<>();
    private static ArrayList<String> studyu=new ArrayList<>();
    private static ArrayList<String> kovilu=new ArrayList<>();
    private static ArrayList<String> healthu=new ArrayList<>();
    private static ArrayList<String> touru=new ArrayList<>();
    private static ArrayList<String> recipeu=new ArrayList<>();
    private static ArrayList<String> cinemau=new ArrayList<>();
    private static ArrayList<String> sportsu=new ArrayList<>();
    private static ArrayList<String> lifeu=new ArrayList<>();
    private static ArrayList<String> mukiyaNewsu=new ArrayList<>();




    @Override
    protected ArrayList<String> doInBackground(String... string) {


        Document document = null;
        Document document2 = null;
        Document document3 = null;
        Document document4 = null;
        try {
            document = Jsoup.connect(string[0]).get();

            document2 = Jsoup.connect("https://tamil.oneindia.com/").get();
            document3 = Jsoup.connect("https://ta.wikipedia.org/wiki/%E0%AE%AE%E0%AF%81%E0%AE%A4%E0%AE%B1%E0%AF%8D_%E0%AE%AA%E0%AE%95%E0%AF%8D%E0%AE%95%E0%AE%AE%E0%AF%8D").get();
            document4 = Jsoup.connect("http://www.vanakkamlondon.com/category/ilakiya-saral/").get();
Document document11 = Jsoup.connect("https://www.youtube.com/").get();
            Elements latestNewss = document.select(".other_main_news1 a");

            Elements trendss = document2.select("div.news-desc a");
            Elements story = document4.select("div.category-ilakiya-saral a");
            Elements today = document3.select("div#mp-otd a");
            Elements todayy = document3.select("div#mp-otd");
            Elements fullStory = document4.select("div.category-ilakiya-saral");
            Elements youtube = document11.select("ytd-grid-video-renderer");

            latest.clear();
            latestu.clear();
            others.clear();
            othersu.clear();
            trends.clear();
            trendsu.clear();


//            for(Element tech2:techs){
//
//                latest.add(String.valueOf(tech2));
//                String url = techs.attr("href");
//                latestu.add("https://tamil.samayam.com/"+url);
//
//            }
            for(Element tech2:todayy){


                others.add(String.valueOf(tech2));

            }
            for(Element tech2:fullStory){



                life.add(String.valueOf(tech2));

            }
            for(Element tech2:latestNewss){

                String url = tech2.attr("href");

                    latest.add(String.valueOf(tech2));


                    latestu.add(url);



            }
            for(Element tech2:today){

                String url = tech2.attr("href");




                    othersu.add(url);


                    }
            for(Element tech2:trendss){

                String url = tech2.attr("href");

                trends.add(String.valueOf(tech2));


                trendsu.add(url);


            }
            for(Element tech2:story){

                String url = tech2.attr("href");




                lifeu.add(url);


            }
//            Elements astros = document.select(".other_main_news1 a");
//            Elements studys = document.select(".bdrt a");
//            Elements kovils = document.select(".bdrt a");
//            Elements healths = document.select(".bdrt a");
//            Elements tours = document.select(".other_main_news1 a");
//            Elements recipes = document.select(".bdrt a");
//            Elements cinemas = document.select(".bdrt a");
//            Elements sportss = document.select(".bdrt a");
//            Elements lifes = document.select(".other_main_news1 a");
//            Elements mukiyanewss = document.select(".bdrt a");
//
////            for(Element news:latestNewss){
//
//                latest.add(String.valueOf(news));
//
//                String url = news.attr("href");
//                latestu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:otherNewss){
//
//                others.add(String.valueOf(news));
//
//                String url = otherNewss.attr("href");
//                othersu.add("https://tamil.samayam.com/"+url);
//            }
//
//            for(Element news:trendss){
//
//                trends.add(String.valueOf(news));
//
//                String url = trendss.attr("href");
//                trendsu.add("https://tamil.samayam.com/"+url);
//            }
//
//            for(Element news:techs){
//
//                tech.add(String.valueOf(news));
//
//                String url = techs.attr("href");
//                techu.add("https://tamil.samayam.com/"+url);
//            }
//
//            for(Element news:astros){
//
//                astro.add(String.valueOf(news));
//
//                String url = astros.attr("href");
//                astrou.add("https://tamil.samayam.com/"+url);
//            }
//
//            for(Element news:studys){
//
//                study.add(String.valueOf(news));
//
//                String url = studys.attr("href");
//                studyu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:kovils){
//
//                kovil.add(String.valueOf(news));
//
//                String url = kovils.attr("href");
//                kovilu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:healths){
//
//                health.add(String.valueOf(news));
//
//                String url = healths.attr("href");
//                healthu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:tours){
//
//                tour.add(String.valueOf(news));
//
//                String url = tours.attr("href");
//                astrou.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:recipes){
//
//                recipe.add(String.valueOf(news));
//
//                String url = recipes.attr("href");
//                recipeu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:cinemas){
//
//                cinema.add(String.valueOf(news));
//
//                String url = cinemas.attr("href");
//                cinemau.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:lifes){
//
//                life.add(String.valueOf(news));
//
//                String url = lifes.attr("href");
//                lifeu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:mukiyanewss){
//
//                mukiyaNews.add(String.valueOf(news));
//
//                String url = mukiyanewss.attr("href");
//                mukiyaNewsu.add("https://tamil.samayam.com/"+url);
//            }
//            for(Element news:sportss){
//
//                sports.add(String.valueOf(news));
//
//                String url = sportss.attr("href");
//                sportsu.add("https://tamil.samayam.com/"+url);
//            }
            Myapp.setLatest(latest);
            Myapp.setLatestu(latestu);
            Myapp.setOthers(others);
            Myapp.setOthersu(othersu);
            Myapp.setTrendsu(trendsu);
            Myapp.setTrends(trends);
            Myapp.setLife(life);
            Myapp.setLifeu(lifeu);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(ArrayList<String> strings) {
        super.onPostExecute(strings);
    }
}

