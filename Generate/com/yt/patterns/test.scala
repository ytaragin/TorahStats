package com.yt.patterns

/**
  * Created by yonatan on 22/08/2016.
  */
object test {


  def main(args: Array[String]): Unit = {
    println("Pasuk")



    def t = "וַיֹּ֣אמֶר יְהוָ֔ה וַיֹּ֣אמֶר יְהוָ֔ה זַעֲקַ֛ת סְדֹ֥ם וַעֲמֹרָ֖ה כִּי-רָ֑בָּה וְחַ֨טָּאתָ֔ם כִּ֥י כָבְדָ֖ה מְאֹֽד:"
def t1 = " וְאֶת-הֲדוֹרָ֥ם וְאֶת-אוּזָ֖ל וְאֶת-דִּ וְאֶת-הֲדוֹרָ֥ם וְאֶת-אוּזָ֖ל וְאֶת-דִּ"

    var p = new Pasuk(1, 1, 1, 1, 1, t1)

    var ng = new NGram(6)
    ng.addData(p)
    //ng.addData(p)
  ng.dumpStats()
    val res = NGram.calcChiSquare(ng,ng,0)
    println("Self Chi Sq = "+res)


    print (p)
    println(" - words: "+p.wordCount+" letters: "+p.letterCount)



    def f = Trops(1428)
    println(f)
    println(f.id)

    def c = (f.id -(1425-65)).toChar
    println(c)





  }

}
