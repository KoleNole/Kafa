import android.annotation.SuppressLint
import android.graphics.Canvas
import android.view.SurfaceHolder
import com.gimnazija.kafa.Screen

class Loop(private val screen: Screen, private val surfaceHolder: SurfaceHolder) : Thread() {

    private var running: Boolean = false
    private var tps: Double = 0.0 // koliki nam je tps (ticks per second)
    private var fps: Double = 0.0 // koliki nam je fps (frames per second)
    private val updateCap: Double = 60.0 // koliko puta zelimo da update-amo i crtamo ekran u sekundi
    private val tpsPeriod = 1000 / updateCap // sekunda (1000ms) kroz updateCap

    /*
    *Da vam uprostim, ovo se desava u while petlji:
    *update()
    *draw()
    *sleep()
    * mi dajemo ogranicenje ovome, tako sto necemo update-tati ili crtati
    * vise od updateCap-a u jednoj sekundi
    * ako vam nesto nije jasno, ukucajte na internetu game loop
    * skoro sve su iste kao ova petlja
    */


    @SuppressLint("NewApi")
    override fun run() {
        super.run()

        var ticks = 0 // koliko tickova imamo na kraju petlje
        var frames = 0 // koliko frameova imamo na kraju petlje

        var startTime: Long // vrijeme od kad smo poceli while petlju
        var elapsedTime: Long // vrijeme koje je proslo od startTime

        /* vrijeme za koje necemo nista raditi
        ovo zelimo da dobijemo da bismo optimizovali aplikaciju */
        var sleepTime: Long

        var canvas: Canvas? = null // deklarisemo nas canvas

        // zapocinjemo petlju
        startTime = System.currentTimeMillis()
        while (running) {

            // pokusavamo da update-tamo i crtamo
            try {
                canvas = surfaceHolder.lockHardwareCanvas() // dobijamo nas canvas
                synchronized(surfaceHolder) {
                    screen.update() // update-amo
                    ticks++ // uspjesno smo update-ali
                    screen.draw(canvas) // crtamo
                }
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            } finally {
                // finally se uvijek izvrsi i ako smo dobili error
                if (canvas != null) {
                    try {
                        // ako canvas nije null, onda smo uspjesno crtali
                        surfaceHolder.unlockCanvasAndPost(canvas)
                        frames++ // uspjesno smo nacrtali
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }

                }
            }

            // ako imamo visak vremena(sleepTime) onda nista ne radimo
            elapsedTime = System.currentTimeMillis() - startTime
            sleepTime = (ticks * tpsPeriod - elapsedTime).toLong()
            if (sleepTime > 0) {
                try {
                    sleep(sleepTime)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }

            }

            /* ako je sleepTime negativan, to znaci da smo previse vremena crtali
            * preskacemo frame-ove da bismo imali trazeni tps
            */
            while (sleepTime < 0 && ticks < updateCap - 1) {
                screen.update()
                ticks++
                elapsedTime = System.currentTimeMillis() - startTime
                sleepTime = (ticks * tpsPeriod - elapsedTime).toLong()
            }

            // ako je proslo vise od jedne sekunde, izracunavamo tps i fps
            elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime >= 1000) {
                tps = (ticks / (elapsedTime / 1000)).toDouble()
                fps = (frames / (elapsedTime / 1000)).toDouble()
                ticks = 0 // resetujemo za sledecu sekundu
                frames = 0 // resetujemo za sledecu sekundu

                startTime = System.currentTimeMillis() // vracamo se na pocetak petlje
            }
        }
    }

    // funkcija za zapocinjanje petlje
    fun startLoop() {
        this.running = true
        start()
    }

    // setter za running
    fun setRunning(running: Boolean) {
        this.running = running
    }

    // getter za tps
    fun getTPS(): Int {
        return this.tps.toInt()
    }

    // getter za fps
    fun getFPS(): Int {
        return this.fps.toInt()
    }

}
