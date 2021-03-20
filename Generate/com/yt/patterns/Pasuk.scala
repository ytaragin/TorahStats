package com.yt.patterns

import scala.collection.mutable.ArrayBuffer

/**
 * Created by yonatan on 8/3/15.
 */
class Pasuk(private val _chumash:Int, private val _perek:Int, private val _pasuk:Int, private val _parsha:Int, private val _aliyah:Int, private val _actual:String) {

  def chumash = _chumash;
  def perek = _perek
  def pasuk = _pasuk
  def parsha = _parsha
  def aliyah = _aliyah
  def actual = _actual

  def wordCount = _words.length
  def letterCount = _letterCount

  private var _words:Array[String] = null;
  private var _trops:Array[Trops.Value] = null;

  private var _letterCount = 0

  init();

  def trops():Array[Trops.Value] = { return _trops }

  def init(): Unit = {

    val ts = ArrayBuffer[Trops.Value]()

    _words = _actual.split("[ -|]").filter(!_.isEmpty())
    _words.foreach { w =>
      w.toCharArray
        .filter(c => Trops.isTrop(c.toInt))
        .map(c => Trops(c.toInt))
        .distinct
        .foreach ( ts.append(_))
    }
    _trops = ts.toArray

    _words.foreach { w =>
      w.toCharArray
        .filter(c => c.isLetter)
        .foreach (c => { _letterCount = _letterCount + 1; }) //println(""+_letterCount+" " +c);  })
    }




  }





  override def toString:String = {
     return "Chumash:"+_chumash+" Parsha:" + _parsha + " Aliya:" + _aliyah + " Perek:" + _perek + " Pasuk:" + _pasuk + " >>" + _actual+"<<"

  }




}
