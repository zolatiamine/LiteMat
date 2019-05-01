package Tbox

import org.apache.jena.ontology.{DatatypeProperty, OntClass, OntModel, OntModelSpec, OntProperty}
import org.apache.jena.rdf.model.ModelFactory
import org.apache.jena.util.iterator.ExtendedIterator
import org.apache.jena.vocabulary.OWL2

import scala.collection.mutable.ArrayBuffer


class TBoxServices(var tbox: String, val name: String) {

  private val d: Dictionary = Dictionary.getInstance(name)
  private var m: OntModel = null

  def loadModel(): Unit = {
    m = ModelFactory.createOntologyModel(OntModelSpec.OWL_DL_MEM_RULE_INF)
    try {
      m.read(tbox)
    }
    catch {
      case e: org.apache.jena.riot.RiotNotFoundException => println("File not found")
    }
  }

  def isConcept(SubC: OntClass, C: OntClass): Boolean = {
    return ((!SubC.isAnon) && !(SubC == OWL2.Nothing) && !(SubC == C))
  }

  def countSubC(C: OntClass): Int = {
    var count = 0
    val listSC = C.listSubClasses(true)
    while (listSC.hasNext) {
      val SubC = listSC.next()
      if (isConcept(SubC, C)) count = count + 1
    }
    return count
  }

  def countSubP(P: OntProperty): Int = {
    var count = 0
    val listP = P.listSubProperties(true)
    while (listP.hasNext) {
      val subP = listP.next()
      if (isProperty(subP, P)) count = count + 1
    }
    return count
  }

  def isProperty(subP: OntProperty, P: OntProperty): Boolean = {
    return ((!subP.isAnon) && !(subP == OWL2.Nothing) && !(subP == P))
  }

  def isSuperProperty(P: OntProperty): Boolean = {
    val listSP = P.listSuperProperties()
    return listSP.hasNext
  }

  def isOWLProperty(P: OntProperty): Boolean = {
    return P.getURI().subSequence(0, 13).equals("http://www.w3")
  }

  def getSubProperties(P: OntProperty): ArrayBuffer[OntProperty] = {
    val resP: ArrayBuffer[OntProperty] = new ArrayBuffer[OntProperty]()
    if (P.equals(OWL2.topDataProperty)) {
      val tmpSubP: ExtendedIterator[DatatypeProperty] = m.listDatatypeProperties()
      while (tmpSubP.hasNext) {
        val tmpP: OntProperty = tmpSubP.next()
        if (!isSuperProperty(tmpP))
          resP += tmpP
      }
    }
    else if (P.equals(OWL2.topObjectProperty)) {
      val tmpSubP: ExtendedIterator[org.apache.jena.ontology.ObjectProperty] = m.listObjectProperties()
      while (tmpSubP.hasNext()) {
        val tmpP: OntProperty = tmpSubP.next()
        if (!isSuperProperty(tmpP) && !isOWLProperty(tmpP)) {
          resP += tmpP
        }
      }
    }
    val tmpSubP = P.listSubProperties(true)
    if (tmpSubP != null) {
      while (tmpSubP.hasNext) {
        val tmpP: OntProperty = tmpSubP.next()
        if (!isOWLProperty(tmpP)) {
          if ((P == null && !isSuperProperty(tmpP)) || (P != null)) {
            resP += tmpP
          }
        }
      }
    }

    return resP

  }

  def ConcepAction(Id: Long, encodingStart: Int, idLength: Int, UriConcept: String): Unit = {
    val C: OntClass = m.createClass(UriConcept)
    d.addConceptURL2IdItem(C.getURI, Id, encodingStart, idLength)
    val count = countSubC(C)
    if (count > 0) {
      val countB: Int = Math.ceil(Math.log(count + 1) / Math.log(2)).toInt
      val subC = C.listSubClasses(true)
      var tmpId = Id << countB
      while (subC.hasNext) {
        val tmpC = subC.next()
        if (isConcept(tmpC, C)) {
          val Id2 = {
            tmpId += 1;
            tmpId
          }
          ConcepAction(Id2, encodingStart + idLength, countB, tmpC.getURI)
        }
      }
    }
  }

  def PropAction(Id: Long, encoodingStart: Int, idLength: Int, UriProp: String): Unit = {
    val P: OntProperty = m.createOntProperty(UriProp)
    d.addPropertiesURL2IdItem(P.getURI, Id, encoodingStart, idLength)
    val subp: ArrayBuffer[OntProperty] = getSubProperties(P)
    if (subp.size > 0) {
      val countB: Int = Math.ceil(Math.log(subp.size + 1) / Math.log(2)).toInt
      var tmpId: Long = (Id << countB)
      for (tmpP <- subp) {
        if ((UriProp == null && !isSuperProperty(tmpP)) || (UriProp != null)) {
          val tmp = {
            tmpId += 1;
            tmpId
          }
          PropAction(tmp, encoodingStart + idLength, countB, tmpP.toString)
        }
      }
    }
  }

  def getDictionary(): Dictionary = d
}
