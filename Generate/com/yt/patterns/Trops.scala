package com.yt.patterns

/**
 * Created by yonatan on 8/3/15.
 *
 * from https://en.wikipedia.org/wiki/Unicode_and_HTML_for_the_Hebrew_alphabet
 * and http://www.i18nguy.com/unicode/hebrew.html
 *
 */
object Trops extends Enumeration {
  val ETNAHTA = Value(1425)
  val SEGOL = Value(1426)
  val SHALSHELET = Value(1427)
  val ZAKEF_KATAN = Value(1428)
  val ZAKEF_GADOL = Value(1429)
  val TIPHA = Value(1430)
  val REVIA = Value(1431)
  val ZARKA = Value(1432)
  val PASHTA = Value(1433)
  val YETIV = Value(1434)
  val TEVIR = Value(1435)
  val GERESH = Value(1436)
  val GERESH_MUKDAM = Value(1437)
  val GERSHAYIM = Value(1438)
  val KARNEI_PARA = Value(1439)
  val TELISHA_GEDOLA = Value(1440)
  val PAZER = Value(1441)
  val ETNACH_HAFUCH = Value(1442)
  val MUNACH = Value(1443)
  val MAHAPACH = Value(1444)
  val MERCHA = Value(1445)
  val MERCHA_KFULA = Value(1446)
  val DARGA = Value(1447)
  val KADMA = Value(1448)
  val TELISHA_KTANA = Value(1449)
  val YERACH_BEN_YOMO = Value(1450)
  val OLE = Value(1451)
  val ILUY = Value(1452)
  val DEHI = Value(1453)
  val ZINOR = Value(1454)


  val scores = Map(

    MERCHA_KFULA -> 20,
    YERACH_BEN_YOMO -> 20,
    KARNEI_PARA -> 20,
    SHALSHELET -> 14,
    ETNACH_HAFUCH -> 12,
    PAZER -> 8,
    ZARKA -> 8,
    ZINOR -> 8,
    SEGOL -> 8,
    TELISHA_KTANA -> 6,
    TELISHA_GEDOLA -> 6,
    GERESH -> 5,
    GERSHAYIM -> 5,
    ZAKEF_GADOL -> 4,
    DARGA -> 4,
    TEVIR -> 4,
    REVIA -> 4,
    ZAKEF_KATAN -> 2,
    TIPHA -> 2,
    PASHTA -> 2,
    YETIV -> 2,
    GERESH_MUKDAM -> 2,
    MUNACH -> 2,
    MAHAPACH -> 2,
    MERCHA -> 2,
    KADMA -> 2,
    ETNAHTA -> 1,


    OLE -> 12,
    ILUY -> 12,
    DEHI -> 12

  )


  def isTrop(code:Int):Boolean = {
    return ((code>=1425) && (code<this.maxId))
  }



  def getScore(v:Trops.Value):Int = {
    return scores(v)


  }


}
