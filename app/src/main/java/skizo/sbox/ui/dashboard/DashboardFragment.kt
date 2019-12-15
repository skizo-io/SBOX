package skizo.sbox.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import skizo.sbox.R

class DashboardFragment : Fragment() {
    /*
    실제 동작되는 기능들을 모아둠... 리사이클러뷰. 그리드 타입
    기능은 액티브(실제 동작을 눌러서 실행하는... 사진 합치기, 타이머 같은...)
    서비스에서 동작되는 자동으로 돌아가는.... (페이지 파싱...등)
    액티브와 위젯...등 지원 (번역...)
    위젯 (이미지 검색, 번역... 화면 상단에 띄우기(삼성앱 기능))
    종류별로 분류해서 표시.
    자주 사용하는 앱은 즐겨찾기해서 가장 아래 표시..
    자주 사용하는 즐겨찾기 하지 않은 앱은 최근 사용으로 가장 아래에서 위에 표시...
    */
    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        val textView: TextView = root.findViewById(R.id.text_dashboard)
        dashboardViewModel.text.observe(this, Observer {
            textView.text = it
        })
        return root
    }
}