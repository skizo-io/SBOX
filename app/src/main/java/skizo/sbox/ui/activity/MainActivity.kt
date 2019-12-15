package skizo.sbox.ui.activity

import android.os.Bundle
import android.text.Html
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import skizo.library.Builder
import skizo.library.extensions.trace
import skizo.sbox.R
import skizo.sbox.ui.base.BaseActivity
import java.io.IOException


@Suppress("DEPRECATION")
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as stop level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_dashboard,
                R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)



        getWebsite()
        getWebsite2()
    }


    private fun getWebsite() {
        Thread(Runnable {
            val builder = StringBuilder()
            try {
                var url = "https://buscatch.net/mobile/gyosei/?u=a10375314944f584660014"


                val doc: Document = Jsoup.connect(url).get()

                val title: String = doc.title()
                builder.append(title).append("\n")


                trace("1@@@@@@@@@@@@@ ", title)
                val links: Elements = doc.select("a[href]")


//                trace("3@@@@@@@@@@@@@ ", links)

//                val articles: Elements = rawData.select("tr:not(.tr_notice) a") //
                /*

for(Element article : articles) {

  String href = article.attr("abs:href"); // a태그 href의 절대주소를 얻어낸다.

  // a 태그 안에 포함된 div들
  Elements articleDiv = article.select("div");

  String thumbUrl = ROOT_URL
                    + articleDiv.first() // 첫 번째 div에서 썸네일 url을 얻어온다.
                                .attr("style")
                                .replace("background-image:url(", "")
                                .replace(")", "");

  String title = articleDiv.get(1).ownText(); // 두 번째 div에서 제목을 얻어낸다.

  String date = articleDiv.get(1).select("small").text()
                                  .split("\\|")[0];

  System.out.println(href); // http://ma../b/mangup/00000
  System.out.println(thumbUrl); // http://ma../quickimage/...
  System.out.println(title); // 제목
  System.out.println(date); // 날짜
                 */

//                val imgs: Elements =    rawData.select("img[class=lz-lazyload]") // lz-lazyload 클래스를 가진 img들

                val notice: Elements = doc.select("div[id=announce] span")

                trace("4@@@@@@@@@@@@@ ", notice.first().html())
                trace("4@@@@@@@@@@@@@ ", notice.first().text())
                trace("4@@@@@@@@@@@@@ ", Html.fromHtml(notice.first().text()).toString())

/*

V: 1@@@@@@@@@@@@@  : れんらくアプリ|暁星国際学園新浦安幼稚園
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行


V: 1@@@@@@@@@@@@@  : れんらくアプリ|暁星国際学園新浦安幼稚園
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行 通常通り運行しております あと44分で到着予定
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行 通常通り運行しております あと44分で到着予定



V: 4@@@@@@@@@@@@@  : [本日の運行]：<font style="color:#ffff00;">◆</font>通常運行<br>通常通り運行しております<br> あと43分で到着予定
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行 通常通り運行しております あと43分で到着予定
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行 通常通り運行しております あと43分で到着予定


V: 4@@@@@@@@@@@@@  : [本日の運行]：<font style="color:#ffff00;">◆</font>通常運行<br>予定より早く運行しております<br> あと5分で到着予定
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行 予定より早く運行しております あと5分で到着予定
V: 4@@@@@@@@@@@@@  : [本日の運行]：◆通常運行 予定より早く運行しております あと5分で到着予定
 */
//                trace("4@@@@@@@@@@@@@ ", builder)   あと39分で到着予定


                for (link in links) {
                    builder.append("\n").append("Link : ").append(link.attr("href"))
                        .append("\n").append("Text : ").append(link.text())
                }


            } catch (e: IOException) {
                builder.append("Error : ").append(e.message).append("\n")
            }
            runOnUiThread {
//                trace("@@@@@@@@@@@@@@@@@@@@ ", builder.toString())
            }
        }).start()
    }


    private fun getWebsite2() {

        trace("@@@@@@@@@@@@@ getWebsite2")


    }

    /*

    // 간략화된 GET, POST
Document google1 = Jsoup.connect("http://www.google.com").get();
Document google2 = Jsoup.connect("http://www.google.com").post();

// Response로부터 Document 얻어오기
Connection.Response response = Jsoup.connect("http://www.google.com")
                                    .method(Connection.Method.GET)
                                    .execute();
Document google3 = response.parse();


Connection.Response response = Jsoup.connect("http://www.google.com")
                                    .method(Connection.Method.GET)
                                    .execute();
Document document = response.parse();

String html = document.html();
String text = document.text();


Connection.Response response = Jsoup.connect("http://www.google.com")
                                    .method(Connection.Method.GET)
                                    .execute();
Document googleDocument = response.parse();
Element btnK = googleDocument.select("input[name=btnK]").first();
String btnKValue = btnK.attr("value");

System.out.println(btnKValue); // Google 검색
     */


//    private class MainPageTask extends AsyncTask<Void,Void,Void> {
//    private Elements element;
//    @Override protected void onPostExecute(Void ®) {
// doInBackground 작업이 끝나고 난뒤의 작업 mainHello.setText(element.text()); }

// @Override protected Void doInBackground(Void... params) {
// 백그라운드 작업이 진행되는 곳. try {
// Document doc = Jsoup.connect("http://example.com").get();
// element = doc.select("#algoList > tbody");
// } catch (IOException e) {
// e.printStackTrace();
// }
// return null; } }
}


