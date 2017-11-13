package com.rinon.shannoncode.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.rinon.shannoncode.R
import com.rinon.shannoncode.models.ShannonCode
import kotlinx.android.synthetic.main.activity_input_character.*

class InputCharacterActivity : AppCompatActivity() {

    companion object {
        var num = 0     // 文字数
        var pairList = ArrayList<Pair<EditText, EditText>>()     // first:char second:probability
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_input_character)

        calc_button.setOnClickListener {
            var shannon = calc()
            shannon.calc()
        }

        num = intent.getIntExtra(InputNumberActivity.NUMBER, 0)
        var counter = 0

        while(counter < num) {
            var row = layoutInflater.inflate(R.layout.row_container, scroll_view_content, true)
            var char = row.findViewById<EditText>(R.id.edit_character)
            var probability = row.findViewById<EditText>(R.id.edit_probability)

            // TODO: 処理の仕方がわからなすぎるのでとりあえずはこれ
            when(counter) {
                0 -> {
                    char.id = R.id.character0
                    probability.id = R.id.probability0
                }
                1 -> {
                    char.id = R.id.character1
                    probability.id = R.id.probability1
                }
                2 -> {
                    char.id = R.id.character2
                    probability.id = R.id.probability2
                }
                3 -> {
                    char.id = R.id.character3
                    probability.id = R.id.probability3
                }
                4 -> {
                    char.id = R.id.character4
                    probability.id = R.id.probability4
                }
                5 -> {
                    char.id = R.id.character5
                    probability.id = R.id.probability5
                }
                6 -> {
                    char.id = R.id.character6
                    probability.id = R.id.probability6
                }
                7 -> {
                    char.id = R.id.character7
                    probability.id = R.id.probability7
                }
                8 -> {
                    char.id = R.id.character8
                    probability.id = R.id.probability8
                }
                9 -> {
                    char.id = R.id.character9
                    probability.id = R.id.probability9
                }
                else -> {
                    // ありえないのでエラー処理
                }
            }

            pairList.add(Pair<EditText, EditText>(char, probability))
            counter++
        }
    }

    fun calc (): ShannonCode {

        var contentList = ArrayList<ShannonCode.Content>()

        // 変換作業
        for(content in pairList) {
            contentList.add(ShannonCode.Content(content.first.text.toString().get(0),
                                                content.second.text.toString().toInt()))
        }
        return ShannonCode(contentList)
    }
}