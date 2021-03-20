package com.yt.patterns

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer

/**
 * Created by yonatan on 8/5/15.
 */
class TwoLevelMap[A] extends Iterable[A]{
  type OneLevel = mutable.Map[Int, ArrayBuffer[A]]
  def OneLevel() = mutable.Map[Int, ArrayBuffer[A]]()

  type TwoLevel= mutable.Map[Int, OneLevel]
  def TwoLevel() = mutable.Map[Int, OneLevel]()

  val data = TwoLevel()



  def addToMap(level1:Int, level2:Int, p:A): Unit = {
    if (! data.contains(level1)) {
      data += (level1 -> OneLevel())
    }
    val topLevel = data(level1)

    if (!topLevel.contains(level2)) {
      topLevel += (level2 -> new ArrayBuffer[A]())
    }
    val secLevel = topLevel(level2)

    secLevel.append(p)



  }


/*
  def iterateAll(func: Iterator[A]=>Array[Double]): Map[(Int,Int), Array[Double]] = {

    var retMap = mutable.Map[(Int, Int), Array[Double]]()


    var calcData = func(iterator)
    retMap += ((0,0) -> calcData)

    return retMap.toMap

  }
    def iterateLevelOne(func: Iterator[A]=>Array[Double]): Map[(Int,Int), Array[Double]] = {

    var retMap = mutable.Map[(Int, Int), Array[Double]]()

    data.keys.toList.foreach { l1Id =>
      var calcData = func(l1Iterator(l1Id))
      retMap += ((l1Id, 0) -> calcData)
    }

    return retMap.toMap

  }


  def iterateLevelTwo(func: Iterator[A]=>Array[Double]): Map[Tuple2[Int,Int], Array[Double]] = {

    var retMap = mutable.Map[(Int,Int), Array[Double]]()

    data.foreach {(l1entry) =>
      l1entry._2.foreach { (l2entry) =>
          var calcData =func(l2entry._2.iterator)
          retMap += ((l1entry._1, l2entry._1) -> calcData)
      }
    }
    return retMap.toMap

  }


  */
  def iterateAll[T](func: Iterator[A]=>T): Map[(Int,Int), T] = {

    var retMap = mutable.Map[(Int, Int), T]()


    var calcData = func(iterator)
    retMap += ((0,0) -> calcData)

    return retMap.toMap

  }


  def iterateLevelOne[T](func: Iterator[A]=>T): Map[(Int,Int), T] = {

    var retMap = mutable.Map[(Int, Int), T]()

    data.keys.toList.foreach { l1Id =>
      var calcData = func(l1Iterator(l1Id))
      retMap += ((l1Id, 0) -> calcData)
    }

    return retMap.toMap

  }


  def iterateLevelTwo[T](func: Iterator[A]=>T): Map[Tuple2[Int,Int], T] = {

    var retMap = mutable.Map[(Int,Int), T]()

    data.foreach {(l1entry) =>
      l1entry._2.foreach { (l2entry) =>
          var calcData =func(l2entry._2.iterator)
          retMap += ((l1entry._1, l2entry._1) -> calcData)
      }
    }
    return retMap.toMap
    
  }

  def l1Iterator(l1Id:Int):Iterator[A] = {
    var it = ArrayBuffer[A]().iterator
    val l2Map = data(l1Id)
    l2Map.keys.toList.sortBy(x=>x).foreach { l2Id =>

      it = it ++ l2Map(l2Id).iterator
    }

    return it
  }

  override def iterator: Iterator[A] = {
    var it = ArrayBuffer[A]().iterator

    data.keys.toList.sortBy(x=>x).foreach{ l1Id =>
      it = it ++ l1Iterator(l1Id)
    }

    return it
  }
}
