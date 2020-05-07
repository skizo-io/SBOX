package io.sbox.sample

import android.text.Html
import io.comico.library.extensions.trace
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import java.io.IOException


class WebParsing {


    companion object {
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
//                runOnUiThread {
//                trace("@@@@@@@@@@@@@@@@@@@@ ", builder.toString())
//                }
            }).start()
        }


    }


}