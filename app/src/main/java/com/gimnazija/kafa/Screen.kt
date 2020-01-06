package com.gimnazija.kafa

import Loop
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Paint
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.core.content.ContextCompat
import com.gimnazija.kafa.objects.Map

class Screen(context: Context) : SurfaceView(context), SurfaceHolder.Callback {

    /*
    * nemojte gledati kod za Loop jer se vise nece menjati
    * ali ako stvarno zelite vidjeti kod, slobodno.
    * ovo se inace naziva game loop, pa ako zelite vise da
    * saznate, pretrazite na internetu
    */
    private var loop: Loop // nasa petlja
    private var map: Map
    private var paint: Paint

    init {

        /* ovo nam sadrzi ekran i canvas pomocu kojeg crtamo na ekran
        * ne trebate ovo da razumijete, jer ni ja ne razumijem, ali po
        * imenu mozemo zakljuciti da sadrzi povrsinu ekrana*/
        val surfaceHolder = holder

        surfaceHolder.addCallback(this)

        // deklarisemo objekat tipa Loop
        loop = Loop(this, surfaceHolder)

        isFocusable = true

        // OBAVEZNO!!! za sad koristimo untitled.png jer je map.png prevelika
        // deklarisemo objekat tipa Map
        map = Map(BitmapFactory.decodeResource(resources, R.drawable.map))
        // deklarisemo objekat tipa Paint
        paint = Paint()
    }

    // kada nam se napravi ekran(povrsina), zapocecemo petlju
    override fun surfaceCreated(holder: SurfaceHolder) {
        loop.startLoop()
    }

    // funkcija koja nam nije potrebna
    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
    }

    // kada izadjemo iz aplikacije(povrsina nam se unisti), pokusacemo da prekinemo petlju
    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry = true
        while (retry) {
            try {
                loop.setRunning(false) // zaustavljamo petlju
                loop.join() //
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            retry = false
        }
    }

    // funkcija koja se poziva iz petlje da bismo crtali na ekran
    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        map.draw(canvas)
        drawInfo(canvas)
    }

    // funkcija koja se pozove kada pritisnemo na ekran
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_MOVE -> {
                map.setPosX(event.x)
                map.setPosY(event.y)
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    // funkcija koja se poziva iz petlje da bismo update-tovali sve objekte u programu
    fun update() {
        // za sad jedini objekat nam je mapa
        map.update()
    }

    // dodatna funkcija da ne bismo dobili error kad izadjemo iz aplikacije
    fun stopView() {
        loop.setRunning(false)
    }

    // funkcija koja se poziva is draw(Canvas canvas) da bismo dobili informacije o aplikaciji na ekranu
    private fun drawInfo(canvas: Canvas) {
        // Canvas: sta crtamo
        // Paint: kako crtamo
        var color = ContextCompat.getColor(context, R.color.red)
        paint.color = color
        paint.textSize = 25f
        canvas.drawText("TPS: " + loop.getTPS(), 0f, 25f, paint)
        canvas.drawText("FPS: " + loop.getFPS(), 0f, 50f, paint)
        canvas.drawText("Hardware Accelerated: " + canvas.isHardwareAccelerated, 0f, 75f, paint)
        canvas.drawText("Screen Width: $width | Screen Height: $height", 0f, 100f, paint)
    }
}