package com.yt.patterns

import java.io.File
import java.util.regex.{Matcher, Pattern}

import scala.collection.mutable.ArrayBuffer

/**
 * Hello world!
 *
 */
object App 
{

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try { op(p) } finally { p.close() }
  }


  def generateNGram(td:TorahData): NGram = {

    //val ngram = new NGram(8)

    def ret = td.calcPerAll(NGram.ngramGenerator )
//    ret.foreach(m => m._2.dumpStats())

    def globalNG = ret(0,0)
//    globalNG.dumpStats()


    val res = NGram.calcChiSquare(globalNG,globalNG,0)
    println("*****Self Chi Sq = "+res)


    return globalNG

  }

  def runSetOfCalculators(outFile:String, groupLabels:(String,String),
                          calculators:Array[(Array[String], Iterator[Pasuk]=> Array[Double])],
                          iterFunc: (Iterator[Pasuk]=>Array[Double]) => Map[(Int, Int), Array[Double]],
                           labelFunc:Int=>String): Unit = {
    var labels = new ArrayBuffer[String]()
    var allResults: Map[(Int, Int), Array[Double]] = null

    calculators.foreach { calc =>
      labels.appendAll(calc._1)
      var res = iterFunc(calc._2)
      if (allResults == null) {
        allResults = res
      }
      else {
        allResults = (allResults.toSeq ++ res.toSeq) //combine both maps
          .groupBy { case (id, vals) => id } // sort by keys
          .mapValues(v => v.flatMap(v => v._2).toArray) // flatten new values to single array
      }
    }

    var counter = 1

    printToFile(new File(outFile)) { pw =>
      pw.print("Order,"+groupLabels._1+","+groupLabels._2+",")
      labels.foreach(l => pw.print(l + ","))
      pw.println()
      allResults.keys.toList.sortBy(x => x).foreach { key =>
        pw.print(counter+",")
        counter += 1
        pw.print(labelFunc(key._1)  + ","+ key._2 + ",")
        allResults(key).foreach { v =>
          if (v.floor == v) {
            pw.print(f"$v%.0f,")
          }
          else {
            pw.print(f"$v%.2f,")

          }
        }
        pw.println()
      }

    }

  }

    def main(args: Array[String]):Unit= {
      println("Starting Load....")


      val td = new TorahData();
      //td.loadDir("../data")
      td.loadDir("data");
      generateNGram(td)

      println("Running Calculators...")
      val calculators = Calculators.calculators(td)


      runSetOfCalculators("AliyahBreakdown.csv", ("Parsha", "Aliyah"), calculators, td.calcPerAliyah _, td.getParshaName _)
      runSetOfCalculators("ParshaBreakdown.csv", ("Parsha", "N/A"), calculators, td.calcPerParsha _, td.getParshaName _)
      runSetOfCalculators("PerekBreakdown.csv", ("Chumash", "Perek"), calculators, td.calcPerPerek _, td.getChumashName _)
//      runSetOfCalculators("ChumashBreakdown.csv", calculators, td.calcPerChumash _, td.getChumashName _)
      runSetOfCalculators("TorahBreakdown.csv", ("Chumash", "N/A"), calculators, td.calcPerSummary _, td.getChumashName _)


    }


}
