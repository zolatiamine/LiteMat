package Tbox

import java.io.{File, FileOutputStream, PrintStream}

import TBox.Models.IdContainer

import scala.collection.mutable
import scala.collection.mutable.HashMap
import scala.io.Source

object Dictionary {

  private var d: Dictionary = null

  def getInstance(filename: String): Dictionary = {
    if (d == null)
      d = new Dictionary(filename)
    return d
  }

}

class Dictionary(var filename: String) {

  private val ConceptsUI = HashMap.empty[String, IdContainer]
  private val PropertiesUI = HashMap.empty[String, IdContainer]
  private val ConceptsIU = HashMap.empty[Long, String]
  private val propertiesIU = HashMap.empty[Long, String]
  private var SubjectUI: Stream[(String, Int)] = Stream.empty[(String, Int)]

  def DisplayC() = {
    val idSet = ConceptsUI.keySet
    for (concept <- idSet) {
      println(concept + "    =>    " + ConceptsUI.get(concept).get.toString)
    }
  }

  def DisplayP(): Unit = {
    val idSet = PropertiesUI.keySet
    for (property <- idSet) {
      println(property + "    =>     " + PropertiesUI.get(property).get.toString)
    }
  }

  def normalizeConcepts(): Unit = {
    val Keys = ConceptsUI.keySet
    val MaxIdBits = MaxId(ConceptsUI, Keys)
    for (concept <- Keys) {
      val tmpId: IdContainer = ConceptsUI.get(concept).get
      var IdLength: Int = getItemEncodingLength(tmpId)
      var resId: Long = tmpId.getId() << (MaxIdBits - IdLength)
      tmpId.setId(resId)
      ConceptsIU.put(resId, concept)
    }
  }

  def normalizeProperties(): Unit = {
    val Keys = PropertiesUI.keySet
    var MaxIdBits: Int = MaxId(PropertiesUI, Keys)

    for (property <- Keys) {
      val tmpId: IdContainer = PropertiesUI.get(property).get
      var IdLength: Int = getItemEncodingLength(tmpId)
      val resId: Long = tmpId.getId() << (MaxIdBits - IdLength)
      tmpId.setId(resId)
      propertiesIU.put(resId, property)

    }

  }

  def MaxId(ConceptsUI: mutable.HashMap[String, IdContainer], Keys: collection.Set[String]): Int = {
    var max = 0L
    for (item <- Keys) {
      if (max < ConceptsUI.get(item).get.getId()) {
        max = ConceptsUI.get(item).get.getId()
      }
    }
    return Math.ceil(Math.log(max) / Math.log(2)).toInt
  }

  def getItemEncodingLength(Id: IdContainer): Int = {
    if (Id.getEncodingStart == 0)
      return Id.getLengthId
    else
      return Math.ceil(Math.log(Id.getId()) / Math.log(2)).toInt
  }

  def normalizeSubject(): Unit = {
    val linesLumb = Source.fromFile(FilePath.OwlFiLELIMB).getLines

    SubjectUI = linesLumb.map(x => x.split(" ")).map(t => t(0)).toStream.distinct.zipWithIndex
  }

  def addConceptURL2IdItem(concept: String, id: Long, encodingStart: Int, localLength: Int): Unit = {
    ConceptsUI.put(concept, new IdContainer(id, encodingStart, localLength))
  }

  def addPropertiesURL2IdItem(property: String, id: Long, encodingStart: Int, localLength: Int): Unit = {
    PropertiesUI.put(property, new IdContainer(id, encodingStart, localLength))
  }

  def ExportConcToFile(filePath: String): Unit = {
    val fileC: java.io.File = new File(filePath)
    try {
      val FOC: FileOutputStream = new FileOutputStream(fileC)
      val PSC: PrintStream = new PrintStream(FOC)
      val keys = ConceptsUI.keySet
      for (key <- keys) {
        PSC.println(key + "  " + ConceptsUI.get(key).get.getId())
      }
      PSC.close()
    } catch {
      case e: Exception => println("errrrrrrrrror Concept")
    }
  }

  def ExportPropToFile(filePath: String): Unit = {
    val fileP: java.io.File = new File(filePath)
    try {
      val FOP: FileOutputStream = new FileOutputStream(fileP)
      val PSP: PrintStream = new PrintStream(FOP)
      val keys = PropertiesUI.keySet
      for (key <- keys) {
        PSP.println(key + "  " + PropertiesUI.get(key).get.getId())
      }
      PSP.close()
    } catch {
      case e => println("errrrrrrrrror Properties")
    }
  }

  def ExportSubToFile(filePath: String): Unit = {
    val fileP: java.io.File = new File(filePath)
    try {
      val FOP: FileOutputStream = new FileOutputStream(fileP)
      val PSP: PrintStream = new PrintStream(FOP)
      val mapL = SubjectUI.toIterator

      while (mapL.hasNext) {
        val lumb = mapL.next()
        PSP.println(lumb._1 + " " + lumb._2)
      }
      PSP.close()
    } catch {
      case e => println("errrrrrrrrror Properties")
    }
  }

  def addProperty(property: String, id: Long, encodingStart: Int, IdLength: Int): Unit ={
    PropertiesUI.put(property,new IdContainer(id,encodingStart,IdLength))
  }
}

