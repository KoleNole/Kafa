package com.gimnazija.kafa.objects

import android.graphics.Canvas

// svaka klasa koja implementuje ovaj interface ce sadrzati sledece funkcije
interface MapObject {

    fun draw(canvas: Canvas) // funkcija za crtanje objekta na ekran
    fun update() // funkcija gde se update-a objekat
}