/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.yt.patterns

import java.util.regex.{Matcher, Pattern}

import io.Source._
import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer


class TorahData {


  val parshaNames = mutable.Map[Int,String]()
  var pesukim = ArrayBuffer[Pasuk]()

  val perakim =  new TwoLevelMap[Pasuk]()
  val aliyot =  new TwoLevelMap[Pasuk]()


  def setParshaName(parsha:Int, name:String) {
    parshaNames += (parsha -> name)
  }

  def getParshaName(parsha:Int):String = {
    return parshaNames(parsha);
  }

  def getChumashName(id:Int):String  = id match {
    case 1 => "בראשית"
    case 2 => "שמות"
    case 3 => "ויקרא"
    case 4 => "במדבר"
    case 5 => "דברים"
    case 0 => "תורה"

    case _ => "Unknown Chumash "+id // the default, catch-all
  }


  def addPasuk(p:Pasuk) {
//    println("Adding: "+p+" - words: "+p.wordCount+" letters: "+p.letterCount)
    pesukim.append(p)

    aliyot.addToMap(p.parsha, p.aliyah, p)
    perakim.addToMap(p.chumash, p.perek, p)

  }

  def iterateAll(): Iterator[Pasuk] = {

    return pesukim.iterator;
  }



  def calcPerAliyah[T](func: Iterator[Pasuk]=>T):Map[(Int, Int), T] = {
    return aliyot.iterateLevelTwo(func)
  }

  def calcPerParsha[T](func: Iterator[Pasuk]=>T):Map[(Int, Int), T] = {
    return aliyot.iterateLevelOne(func)
  }

  def calcPerPerek[T](func: Iterator[Pasuk]=>T):Map[(Int, Int), T] = {
    return perakim.iterateLevelTwo(func)
  }

  def calcPerChumash[T](func: Iterator[Pasuk]=>T):Map[(Int, Int), T] = {
    return perakim.iterateLevelOne(func)
  }

  def calcPerAll[T](func: Iterator[Pasuk]=>T):Map[(Int, Int), T] = {
    return perakim.iterateAll(func)
  }

  def calcPerSummary[T](func: Iterator[Pasuk]=>T):Map[(Int, Int), T] = {
    var all = calcPerAll(func)
    val chum = calcPerChumash(func)

    chum.foreach(e => all += e._1 -> e._2)

    return all
  }


  def loadDir(dir:String): Unit = {


    var curChumash = -1
    var curPasuk = 1
    var curPerek = 0
    var curAliyah = 1
    var curParsha = 0

    new java.io.File(dir).listFiles.foreach { f =>

      println("About to load " + f)
      val newChum = getChumashFromName(f.toString())
      if (newChum != curChumash) {
        println("New Chumash: " + newChum)
        curChumash = newChum
        curAliyah = 1
        curPerek = 0
        curPasuk = 0
      }



      fromFile(f).getLines.foreach { line =>
        var l = line.trim()
        if (!l.isEmpty) {
          if (!l.contains("@") && !l.startsWith("תורה")) {

            if (l.startsWith("{")) {
              curPasuk += 1
              l = l.substring(l.indexOf("}") + 1).trim();

              if (isAliyahChange(l)) {
                curAliyah += 1
              }

              var p = new Pasuk(curChumash, curPerek, curPasuk, curParsha, curAliyah, l)
              addPasuk(p)
//              println(p)
            }
            else if (l.contains("פרק-")) {
              curPerek += 1
              curPasuk = 0
            }
            else {
              println("New Parsha: "+l)
              curParsha += 1
              curAliyah = 1
              setParshaName(curParsha, l)

            }
          }
        }
      }

    }
    
    println("Pseukim Count: "+pesukim.length)
  }



  def getChumashFromName(name:String):Int = {
    var retVal = 0;
    val regStr = "torah_(\\d)"
    val p = Pattern.compile(regStr)
    val m:Matcher  = p.matcher(name.toString)
    if (m.find()) {
      retVal = m.group(1).toInt
    }
    return retVal;
  }




  def isAliyahChange(line:String):Boolean = {

    val l = line.substring(1) // get rid of wierd first character

    return (l.startsWith("שני") ||
        l.startsWith("שלישי") ||
        l.startsWith("רביעי") ||
        l.startsWith("חמישי") ||
        l.startsWith("שישי") ||
        l.startsWith("שביעי"))

  }


}
