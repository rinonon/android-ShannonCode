package com.rinon.shannoncode.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.rinon.shannoncode.R
import com.rinon.shannoncode.fragment.*
import com.rinon.shannoncode.helper.DialogHelper
import com.rinon.shannoncode.model.AbstractCode
import com.rinon.shannoncode.model.ShannonCode
import kotlinx.android.synthetic.main.activity_shannon_coding.*

import com.rinon.shannoncode.activity.TopActivity.Companion.Type as Type
import com.rinon.shannoncode.fragment.ResultFragment.Companion.QuizType as QuizType

class ShannonCodingActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener,
                                                   TopFragmentListener,
                                                   InputNumberFragmentListener,
                                                   InputCharacterFragmentListener,
                                                   ResultFragmentListener {

    companion object {
        var result: Array<ShannonCode.Code>? = null
        var quizType: QuizType = QuizType.None
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shannon_coding)

        // ツールバーの設定
        toolbar.title = resources.getString(R.string.shannon_coding)
        setSupportActionBar(toolbar)

        val toggle = ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.drawer_open,
                R.string.drawer_close)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        navigation_view.setNavigationItemSelectedListener(this)

        if (savedInstanceState == null) {
            val fragment = TopFragment.newInstance(TopActivity.Companion.Type.Shannon)

            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment, fragment.tag)
                    .commit()
        }
    }

    // --- Navigation View ---
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.menu_top -> {
                val intent = Intent(this, TopActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }

            R.id.menu_how_to_use -> {
                val intent = Intent(this, InformationActivity::class.java)
                intent.putExtra(InformationActivity.KEY_TYPE, InformationActivity.Companion.Type.HowToUse)
                startActivity(intent)
            }

            R.id.menu_about_this_app -> {
                val intent = Intent(this, InformationActivity::class.java)
                intent.putExtra(InformationActivity.KEY_TYPE, InformationActivity.Companion.Type.AboutThisApp)
                startActivity(intent)
            }

            R.id.menu_description -> {
                val intent = Intent(this, AlgorithmDescriptionActivity::class.java)
                intent.putExtra(AlgorithmDescriptionActivity.KEY_TYPE, Type.Shannon)
                startActivity(intent)
            }

            R.id.menu_shannon -> {

            }

            R.id.menu_shannon_fano -> {
                val intent = Intent(this, ShannonFanoActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
            }
        }
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    // --- Fragment listener ---
    override fun topListener(event: TopFragment.Companion.Event, quiz: QuizType) {
        when(event) {
            TopFragment.Companion.Event.Start -> {
                quizType = quiz
                val fragment = InputNumberFragment.newInstance()

                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right)
                        .replace(R.id.container, fragment, fragment.tag)
                        .addToBackStack(fragment.tag)
                        .commit()
            }

            TopFragment.Companion.Event.Info -> {
                val intent = Intent(this, AlgorithmDescriptionActivity::class.java)
                intent.putExtra(AlgorithmDescriptionActivity.KEY_TYPE, Type.Shannon)
                startActivity(intent)
            }
        }
    }

    override fun inputNumberListener(errorType: InputNumberFragment.Companion.ErrorType,
                                     num: Int?) {
        when (errorType){
            InputNumberFragment.Companion.ErrorType.Input -> {
                // 数字が入力されていない
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_input_check))
                dialog.show(supportFragmentManager, null)
            }
            InputNumberFragment.Companion.ErrorType.Max -> {
                // 数が最大値よりも大きい
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_num_check).format(InputNumberFragment.MAX_NUM))
                dialog.show(supportFragmentManager, null)
            }
            else -> {
                // fragment変更
                val inputCharacterFragment = InputCharacterFragment.newInstance(num?: throw NullPointerException("num is null"))

                supportFragmentManager.beginTransaction()
                                      .setCustomAnimations(R.anim.slide_in_right,
                                                           R.anim.slide_out_left,
                                                           R.anim.slide_in_left,
                                                           R.anim.slide_out_right)
                                      .replace(R.id.container, inputCharacterFragment, inputCharacterFragment.tag)
                                      .addToBackStack(inputCharacterFragment.tag)
                                      .commit()
            }
        }
    }

    override fun inputCharacterListener(errorType: InputCharacterFragment.Companion.ErrorType,
                                        pairList: Array<Pair<String, String>>) {
        when (errorType) {
            InputCharacterFragment.Companion.ErrorType.InputAll -> {
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_input_check))
                dialog.show(supportFragmentManager, null)
            }

            InputCharacterFragment.Companion.ErrorType.CorrectProbability -> {
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_probability_check))
                dialog.show(supportFragmentManager, null)
            }

            InputCharacterFragment.Companion.ErrorType.Unique -> {
                val dialog = DialogHelper.createSimpleErrorDialog(resources.getString(R.string.error_unique_check))
                dialog.show(supportFragmentManager, null)
            }

            InputCharacterFragment.Companion.ErrorType.None -> {
                result = convertToShannonCode(pairList)
                val resultFragment = ResultFragment.newInstance(TopActivity.Companion.Type.Shannon,
                        result as Array<AbstractCode>,
                        quizType)

                supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_right,
                                R.anim.slide_out_left,
                                R.anim.slide_in_left,
                                R.anim.slide_out_right)
                        .replace(R.id.container, resultFragment)
                        .addToBackStack(resultFragment.tag)
                        .commit()
            }
        }
    }

    override fun resultListener(status: ResultFragment.Companion.Event, hintText: String?) {
       when(status) {
           ResultFragment.Companion.Event.Wrong -> {
               val dialog = DialogHelper.createSimpleDialog("Wrong Answer!", resources.getString(R.string.wrong_answer))
               dialog.show(supportFragmentManager, null)
           }

           ResultFragment.Companion.Event.Complete -> {
               // ダイアログを出す
               val dialog = DialogHelper.createSimpleDialog(resources.getString(R.string.congratulation),
                                                             resources.getString(R.string.all_correct))
               dialog.show(supportFragmentManager, null)
           }

           ResultFragment.Companion.Event.Hint -> {
               val dialog = DialogHelper.createSimpleDialog("Hint", hintText?: throw NullPointerException("hint text is null"))
               dialog.show(supportFragmentManager, null)
           }

           ResultFragment.Companion.Event.Encode -> {
               val intent = Intent(this, EncodeDecodeActivity::class.java)
               intent.putExtra(EncodeDecodeActivity.KEY_CODE, result?: throw NullPointerException("mCodeList is null"))
               intent.putExtra(EncodeDecodeActivity.KEY_STATUS, EncodeDecodeActivity.Companion.Status.Encode)
               intent.putExtra(EncodeDecodeActivity.KEY_TYPE, Type.Shannon)
               startActivity(intent)
           }

           ResultFragment.Companion.Event.Decode -> {
               val intent = Intent(this, EncodeDecodeActivity::class.java)
               intent.putExtra(EncodeDecodeActivity.KEY_CODE, result?: throw NullPointerException("mCodeList is null"))
               intent.putExtra(EncodeDecodeActivity.KEY_STATUS, EncodeDecodeActivity.Companion.Status.Decode)
               intent.putExtra(EncodeDecodeActivity.KEY_TYPE, Type.Shannon)
               startActivity(intent)
           }
       }
    }

    private fun convertToShannonCode(pairList: Array<Pair<String, String>>): Array<ShannonCode.Code> {
        val codeList = mutableListOf<ShannonCode.Code>()

        // 変換作業
        pairList.mapTo(codeList) {
            ShannonCode.Code(it.first[0],
                    it.second.toInt())
        }
        return ShannonCode.calc(codeList)
    }
}