package com.yt.patterns

import scala.collection.mutable.ArrayBuffer

/**
 * Created by yonatan on 8/6/15.
 */
object Calculators {


  def doubleToArray (func: (Iterator[Pasuk])=>Double)(it:Iterator[Pasuk]) : Array[Double] = {
     return Array(func(it))
  }

  def oneValFunc(onePFunc: (Pasuk, Double)=>Double)( it: Iterator[Pasuk]):Array[Double] = {
    val ret = new Array[Double](1)
    ret(0) = 0
    it.foreach{p => ret(0) = onePFunc(p, ret(0)) }
    return ret
  }

  def pasukCount(p:Pasuk, curVal:Double):Double = {
    return curVal+1
  }

  def wordCount(p:Pasuk, curVal:Double):Double = {
    return p.wordCount+curVal
  }
  def letterCount(p:Pasuk, curVal:Double):Double = {
    return p.letterCount+curVal
  }

  def tropCount(p:Pasuk, curVal:Double):Double = {

    var count = 0.0
    p.trops().foreach {t =>
      count += 1
    }

    return curVal+count

  }


  def tropScore(p:Pasuk, curVal:Double):Double = {

    var score = 0.0
    var lastTrop:Trops.Value = null
    var consecutiveBonus = 0

    p.trops().foreach {t =>
      score += Trops.getScore(t)
      if (t == lastTrop) {
        consecutiveBonus += 1
        score += consecutiveBonus
      }
      else {
        lastTrop = t
        consecutiveBonus = 0
      }
    }

    return curVal+score

  }


  def freqFunc( onePFunc: (Pasuk, Double)=>Double)( it: Iterator[Pasuk]):Array[Double] = {
    var count = 0.0
    var curVal = 0.0

    it.foreach{p =>
      curVal = onePFunc(p, curVal)
      count += 1
    }

    return Array(curVal, curVal/count)

  }



  def patternMatcher(patt:Array[Trops.Value])(p:Pasuk, curVal:Double):Double = {

    var count = curVal

    val trops = p.trops

    var curSpot = 0
    var foundSpot = 0
    while ((curSpot <= trops.length) && (foundSpot >=0)) {
      foundSpot = trops.indexOfSlice(patt, curSpot)
      if (foundSpot >=0 ) {
        count += 1
        curSpot = foundSpot + patt.length
      }
    }




    return count
  }

  def patternCount(patt:Array[Trops.Value]) : Iterator[Pasuk]=>Array[Double] = {
    return freqFunc(patternMatcher(patt) _ ) _
  }


  def calculators(td:TorahData):Array[(Array[String], Iterator[Pasuk]=> Array[Double])] = {

    var singles = ArrayBuffer[(Array[String], Iterator[Pasuk]=> Array[Double])]();

    singles.append((Array("Pesukim"), oneValFunc(pasukCount) _))
    singles.append((Array("Words", "WordsPerPasuk"), freqFunc(wordCount) _))
    singles.append((Array("Letters", "LettersPerPasuk"), freqFunc(letterCount) _))
    singles.append((Array("TotalTrops", "TropPerPasuk"), freqFunc(tropCount) _))
    singles.append((Array("TotalScore", "AvgScore"), freqFunc(tropScore) _))


    val ret = td.calcPerAll(NGram.ngramGenerator )
    //    ret.foreach(m => m._2.dumpStats())

    val globalNG = ret(0,0)
    singles.append((Array("ChiSquare"), doubleToArray(NGram.chiSquareGenerator(globalNG, 0) _) _))
 //   singles.append((Array("GTest"), doubleToArray(NGram.gTestGenerator(globalNG, 0) _) _))
    (2 to 8).foreach { dim =>
      singles.append((Array("ChiSquare["+dim+"]"), doubleToArray(NGram.chiSquareGenerator(globalNG, dim) _) _))
//      singles.append((Array("GTest["+dim+"]"), doubleToArray(NGram.gTestGenerator(globalNG, dim) _) _))
    }


    Trops.values.foreach { t =>
      singles.append((Array(t.toString, t.toString +" Freq" ), patternCount(Array(t))))
    }




    val calcs = Array(
      (Array("MerchaTipcha", "MT Freq"), patternCount(Array(Trops.MERCHA, Trops.TIPHA))),
      (Array("KadmaAzla", "KA Freq"), patternCount(Array(Trops.KADMA, Trops.GERESH))),
      (Array("KadmaAzlaRevii", "KAR Freq"), patternCount(Array(Trops.KADMA, Trops.GERESH, Trops.REVIA))),
      (Array("KadmaAzlaMunachRevii", "KAMR Freq"), patternCount(Array(Trops.KADMA, Trops.GERESH, Trops.MUNACH, Trops.REVIA))),
      (Array("DargaTvir", "DT Freq"), patternCount(Array(Trops.DARGA, Trops.TEVIR))),
      (Array("DargaMunchRevii", "DMR Freq"), patternCount(Array(Trops.DARGA, Trops.MUNACH, Trops.REVIA))),
      (Array("MunachMunach", "MM Freq"), patternCount(Array(Trops.MUNACH, Trops.MUNACH))),
      (Array("KadmaMahpachPashta", "KMP Freq"), patternCount(Array(Trops.KADMA, Trops.MAHAPACH, Trops.PASHTA)))
    )

    singles.appendAll(calcs)
    return singles.toArray

  }



}
