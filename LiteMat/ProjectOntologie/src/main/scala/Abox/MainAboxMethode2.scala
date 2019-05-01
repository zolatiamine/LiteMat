package Abox

import Tbox.FilePath
import org.apache.spark.{SparkConf, SparkContext}

object MainAboxMethode2 {


  def main(args: Array[String]): Unit = {

    //Encodage Abox Optmiser

    System.setProperty("hadoop.home.dir", "C://ProjetBigData//ProjectOntologie//winutils")

    val conf = new SparkConf()
    conf.setMaster("local")
    conf.setAppName("Abox")

    val sc = new SparkContext(conf)

    val triples = sc.textFile(FilePath.OwlFiLELIMB).map(x => x.split(" ")).map(t => (t(0), t(1), t(2)))

    val conc = sc.textFile(FilePath.SaveFileC).map(x => x.split("  "))
    val concUI = conc.map(x => ("<" + x(0) + ">", x(1)))

    val prop = sc.textFile(FilePath.SaveFileP).map(x => x.split("  "))
    val propUI = prop.map(x => ("<" + x(0) + ">", x(1)))

    val subj = sc.textFile(FilePath.SaveFileS).map(x => x.split(" "))
    val subUI = subj.map(x => (x(0), x(1)))

    val aboxDS = triples.map(x => (x._2, (x._1, x._3)))

    val aboxProp = aboxDS.join(propUI).map { case (p, ((s, o), idp)) => (s, (idp, o))}

    val aboxSub = aboxProp.join(subUI).map { case (s, ((p, o), ids)) => (o, (ids, p))}

    val aboxConc = aboxSub.join(subUI).union(aboxSub.join(concUI)).map { case (o, ((s, p), ido)) => (s, p, ido)}


    aboxConc.map(x => x._1 + " " + x._2 + " " + x._3).saveAsTextFile(FilePath.lubmEncod)


  }

}
