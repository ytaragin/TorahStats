package com.yt.patterns


class NGram(private val _degree:Int) {

  private var _data: scala.collection.mutable.Map[List[Trops.Value], Int] = scala.collection.mutable.Map()
  private var _pCount = 0;


  def addData(pasuk: Pasuk): Unit = {
    _pCount += 1
    val d = pasuk.trops().toList

    if (d.length <= 1) return

    // work around for bug in scala library
    val max_deg = Math.min(_degree, d.size)
    for (n <- 2 to max_deg) {
      d.sliding(n).foreach { ng =>
 //        if ((ng.length == 2) && (ng(0)==Trops.MERCHA) &&( ng(1)==Trops.TIPHA)) { println ("Pe: "+pasuk.perek+" Pa: "+pasuk.pasuk)}
        _data(ng) = _data.getOrElse(ng, 0) + 1
      }
    }
  }

  def getRecord(rec : List[Trops.Value]):(Int,Float) = {
    val count = _data.getOrElse(rec, 0)
    return (count, count.toFloat/_pCount)
  }

  def ngIterator(dim:Int) : Iterator[(List[Trops.Value], Int, Float)] = {
    if (dim > 0) {
      _data.filterKeys(_.length == dim).iterator.map(x => (x._1, x._2, x._2.toFloat / _pCount)).toIterator
    } else {
      _data.iterator.map(x => (x._1, x._2, x._2.toFloat / _pCount)).toIterator
    }
  }

  def ngIterator() : Iterator[(List[Trops.Value], Int, Float)] = {
    return ngIterator(0)
  }


  override def toString: String = {
    return _data.keys.toList.sortBy(_.length).map(k => k.mkString(",") + "->" + _data(k)).mkString("\n")

  }

  def dumpStats(): Unit = {
    println("Total NGrams: "+_data.size)
    println("Total Instances: "+_data.values.foldLeft(0)((a,b) => a + b))

    for (n <- 2 to _degree) {
      println("NGrams["+n+"]:")
      var iter = ngIterator(n)
      println("\tNum: "+ iter.size)
      iter = ngIterator(n)
      println("\tInstances: "+iter.map(t=>t._2).sum);
      println("\tTop Three:" )
      iter = ngIterator(n)
      iter.toSeq.sortWith(_._2 > _._2).take(3).foreach(p=> println("\t\t"+p._1.mkString("|")+"="+p._2 +"("+p._3+")"))
      println()




    }
  }
}

object NGram {
  def ngramGenerator(iter: (Iterator[Pasuk])): NGram = {
    val ng = new NGram(8)
    iter.foreach(p => ng.addData(p))

    return ng

  }

  def chiSquareGenerator(expected:NGram, dim:Int)( iter: (Iterator[Pasuk])): Double = {
    val ng = ngramGenerator(iter)


    return calcChiSquare(expected,ng, dim)

  }

  def gTestGenerator(expected:NGram, dim:Int)( iter: (Iterator[Pasuk])): Double = {
    val ng = ngramGenerator(iter)


    return calcGTest(expected,ng, dim)

  }



  def calcGTest(expected:NGram, observed:NGram, dim:Int): Double = {
    var score = 0.0.toDouble

    expected.ngIterator(dim).foreach { exp =>
      val obs = observed.getRecord(exp._1)
      if (obs._2 > 0) {
        score += obs._2 * Math.log(obs._2 / exp._3)
      }
    }

    return 2*score
  }


  def calcChiSquare(expected:NGram, observed:NGram, dim:Int): Double = {
    var score = 0.0

    expected.ngIterator(dim).foreach { exp =>
      val obs = observed.getRecord(exp._1)
      val delta = obs._2 - exp._3
      val nodeScore = delta * delta / exp._3
      score += nodeScore
    }

    return score
  }

  def calcChiSquare(expected:NGram, observed:NGram): Double = {
    return calcChiSquare(expected, observed, 0)
  }

}



