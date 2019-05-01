package Abox

import java.io.{File, FileOutputStream, PrintStream}

import Tbox.FilePath

import scala.io.Source

object MainAboxMethode1 {


  def main(args: Array[String]): Unit = {
    println("test avant")

    val fileP: java.io.File = new File(FilePath.SaveFileLumbFinal)

    val linesLumb = Source.fromFile(FilePath.OwlFiLELIMB).getLines
    val mapLumb = linesLumb.map(x => x.split(" ")).map(t => (t(0), t(1), t(2)))

    try {
      val FOP: FileOutputStream = new FileOutputStream(fileP)
      val PSP: PrintStream = new PrintStream(FOP)

      while (mapLumb.hasNext) {
        val lum = mapLumb.next()
        var sujet1 = -1
        var pred1 = -1
        var objec1 = -1
        var sujet2 = -1
        var pred2 = -1
        var objec2 = -1
        var sujet = -1
        var pred = -1
        var objec = -1

        val linesSubject = Source.fromFile(FilePath.SaveFileS).getLines
        val mapsubject = linesSubject.map(x => x.split(" ")).map(t => (t(0), t(1)))
        val su = mapsubject.toIterator
        for (aa <- su) {
          if (lum._1 == aa._1) sujet = aa._2.toInt
          else if (lum._2 == aa._1) pred = aa._2.toInt
          else if (lum._3 == aa._1) objec = aa._2.toInt
        }

        val linesPrope = Source.fromFile(FilePath.SaveFileP).getLines
        val mapPrope = linesPrope.map(x => x.split(" ")).map(t => ("<"+ t(0) + ">", t(2)))
        val p = mapPrope.toIterator
        for (aa <- p) {
          if (lum._1.equals(aa._1)) sujet1 = aa._2.toInt
          else if (lum._2.equals(aa._1)) pred1 = aa._2.toInt
          else if (lum._3.equals(aa._1)) objec1 = aa._2.toInt
        }

        val linesConcept = Source.fromFile(FilePath.SaveFileC).getLines
        val mapConcept = linesConcept.map(x => x.split(" ")).map(t => ("<"+ t(0) + ">",t(2)))
        val c = mapConcept.toIterator
        for (aa <- c) {
          if (lum._1.equals(aa._1)) sujet2 = aa._2.toInt
          else if (lum._2.equals(aa._1)) pred2 = aa._2.toInt
          else if (lum._3.equals(aa._1)) objec2 = aa._2.toInt
        }
        var sujeFinal = ""
        if (sujet != -1) {
          sujeFinal = sujet.toString
        } else if (sujet1 != -1) {
          sujeFinal = sujet1.toString
        } else if (sujet2 != -1) {
          sujeFinal = sujet2.toString
        } else {
          sujeFinal = lum._1
        }
        var predFinal = ""
        if (pred != -1) {
          predFinal = pred.toString
        } else if (pred1 != -1) {
          predFinal = pred1.toString
        } else if (pred2 != -1) {
          predFinal = pred2.toString
        } else {
          predFinal = lum._2
        }

        var objectFinal = ""
        if (objec != -1) {
          objectFinal = objec.toString
        } else if (objec1 != -1) {
          objectFinal = objec1.toString
        } else if (objec2 != -1) {
          objectFinal = objec2.toString
        } else {
          objectFinal = lum._3
        }
        PSP.println(sujeFinal + " " + predFinal + " " + objectFinal)
      }
      PSP.close()
    } catch {
      case e => println("errrrrrrrrror Properties")
    }
    println("test après")
  }

}

