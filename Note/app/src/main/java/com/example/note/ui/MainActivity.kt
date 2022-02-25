package com.example.note.ui

import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.*
import com.example.note.R
import com.example.note.adapter.INoteRVAdapter
import com.example.note.adapter.NoteRVAdapter
import com.example.note.data.Note
import com.example.note.databinding.ActivityMainBinding
import com.example.note.utils.MyService
import com.google.android.material.snackbar.Snackbar
import kotlin.concurrent.thread
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : AppCompatActivity(), INoteRVAdapter, androidx.appcompat.widget.SearchView.OnQueryTextListener ,SensorEventListener{
    private var binding: ActivityMainBinding? = null
    lateinit var viewModel: NoteViewModel
    lateinit var adapter: NoteRVAdapter
    lateinit var sensorManager:SensorManager
    lateinit var mAcc:Sensor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        val intent=Intent(this, MyService::class.java)
        startService(intent)//开启前台服务
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        mAcc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if(mAcc == null)
            Toast.makeText(this,"no mAcc ava",Toast.LENGTH_SHORT).show()
        binding?.apply {
            fabAddNote.setOnClickListener {
                fabAddNote.isVisible = false
                circleAnim.isVisible = true
                    root.isVisible = false
                    val addNoteIntent = Intent(this@MainActivity, AddNote::class.java)
                    startActivity(addNoteIntent)
            }
        }
        binding?.recyclerView?.layoutManager = StaggeredGridLayoutManager(
            2,
            StaggeredGridLayoutManager.VERTICAL
        )
        adapter = NoteRVAdapter(this, this)
        binding?.recyclerView?.adapter = adapter
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        viewModel.allNotes.observe(this, Observer { list ->
            list?.let {
                adapter.updateList(it)
            }
        })
        //实现swipe以删除
        val itemTouchHelper = object: ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                //此项将为空，因为仅仅上下移动，我们无需对Note进行任何操作
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                //获取每个子项的position
                val position = viewHolder.adapterPosition
                //现在通过使用position in RV从database中将notes取出
                val note = adapter.getNote(position)
                //通过viewModel函数来删除note
                viewModel.deleteNote(note)
                //制作一个snackbar来显示 note 已经被删除并且给用户提供撤销的操作
                Snackbar.make(binding?.root!!, "笔记已删除", Snackbar.LENGTH_LONG).apply {
                    setAction("撤销") {
                        //将刚删除的笔记重新保存回数据库
                        viewModel.insertNote(note)
                    }
                    show()
                }

            }
        }

        //制作itemTouchHelper并附加到recyclerView
        ItemTouchHelper(itemTouchHelper).apply {
            attachToRecyclerView(binding?.recyclerView)
        }

    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(this,mAcc,500000)
        binding?.root?.isVisible = true
        binding?.fabAddNote?.isVisible=true
    }


    //当项目有变化就刷新Note Adapter
    private fun refreshNotes(adapter: NoteRVAdapter){
        thread{
            runOnUiThread {
                adapter.notifyDataSetChanged()
            }

        }
    }

    override fun onPause() {

        super.onPause()
        sensorManager?.unregisterListener(this)
    }
    //    override fun onDeleteClicked(note: Note) {
//        每当单击note上的delete时，我们调用viewModel中的delete函数
//        viewModel.deleteNote(note)
//
//    }

    override fun onCardClicked(note: Note) {
        //每当单击一张note时，我们都会导航到传递note的openNote活动
        val openNoteIntent = Intent(this, OpenNote::class.java)
        openNoteIntent.putExtra("SelectedNote", note)
        startActivity(openNoteIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
//        binding?.fabAddNote?.visibility= View.INVISIBLE
        val search = menu?.findItem(R.id.menu_search)
        val searchView = search?.actionView as androidx.appcompat.widget.SearchView
        searchView.isSubmitButtonEnabled = true
        searchView.setOnQueryTextListener(this)
        return true
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if (query != null) {
            searchDatabase(query)
        }
        return true
    }

    //使用输入的查询创建一个搜索数据库的函数，并用结果更新recyclerview
    private fun searchDatabase(searchQuery: String) {
        val searchQuery = "%$searchQuery%" //添加%来格式化sql查询语句 以便查询

        viewModel.searchNote(searchQuery).observe(this) {
            it.let {
                adapter.updateList(it)
            }
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        var values = event?.values
        values?.let {
            var x= values[0] // x轴方向的重力加速度，向右为正
            var y = values[1] // y轴方向的重力加速度，向前为正
//            var z = values[2] // z轴方向的重力加速度，向上为正
            if(abs(x) >= 8 || abs(y) >= 8 )
            {
                updateColor()
            }

        }
    }
    private fun updateColor(){
        val listOfNote = viewModel.allNotes.value
        val numOfNotes = listOfNote?.size
        numOfNotes?.let {
            val colorsArray = this.resources.getIntArray(R.array.cardColors) as IntArray
            for(note in listOfNote)
            {
                var temp = (Random.nextInt() % 32) * (Random.nextInt() % 32)
                temp = abs(temp) % 32
                note.color = colorsArray[temp]
            }
            refreshNotes(adapter)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        Log.d("MainActivity","onAccuracyChanged is called")
    }


}
