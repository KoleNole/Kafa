package com.gimnazija.kafa.objects

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Rect

class Map(private var bm: Bitmap) : MapObject {

    private var posX: Float = 0f // X koordinata
    private var posY: Float = 0f // Y kooridnata
    private var scale: Float = 0.5f// promenjiva koju cemo da koristimo za zoomiranje na mapu


    // funkcija iz MapObject interface
    override fun draw(canvas: Canvas) {
        //TODO: nacrtati na ekran samo dio mape, jer je suvise velika za bilo koji ekran
        var width = canvas.width
        var height = canvas.height
        var src = Rect(0, 0, width, height) // dio slike koji zelimo da uzmemo
        var dst = Rect(0, 0, width, height) // dio ekrana koji zelimo ispuniti slikom
        canvas.drawBitmap(bm, src, dst, null)
    }

    // funkcija iz MapObject interface
    override fun update() {
        //TODO: update-tati mapu, mozda tako sto promjenimo dio mape koji zelimo da nacrtamo na ekran
    }

    // setter za X koordinatu
    fun setPosX(posX: Float) {
        this.posX = posX - (bm.width / 2)
    }

    // setter za Y koordinatu
    fun setPosY(posY: Float) {
        this.posY = posY - (bm.height / 2)
    }
}
