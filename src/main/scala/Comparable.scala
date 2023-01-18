import scala.reflect.runtime.universe._
import scala.reflect.{ClassTag, runtime}
object Comparable {
  case class CustomerObj(id: String, isoCustNameLangCd: String, custNameLangGrpCd: String)
  def main(args: Array[String]): Unit = {

    val c = new CustomerObj("0001", "XX", "xx")
    val d = new CustomerObj("0001", "XX", "xx")



    compareTwoObjects[CustomerObj](c, d)
    val mirror = runtimeMirror(c.getClass.getClassLoader)
    c.getClass().getMethods().foreach(f =>{
      println(f)

    })


    val customerType = typeOf[CustomerObj]

    val customerGetters = customerType.decls.collect {
      case m: MethodSymbol if m.isGetter => m
    }.map(ms =>{
      mirror.reflect(c).reflectMethod(ms).apply()
    })

    println(customerGetters) // prints List("id", "isoCustNameLangCd", "custNameLangGrpCd")
  }

  def compareTwoObjects[T: TypeTag]( c1: Any,  c2: Any): Boolean = {

    val t = typeOf[T]

    val mirror = runtimeMirror(c1.getClass.getClassLoader)

    val c1Values = t.decls.collect {
      case m: MethodSymbol if m.isGetter => m
    }.map(ms => {
      //val ct: ClassTag[String] = t.asInstanceOf[ClassTag[String]]
      mirror.reflect(c1).reflectMethod(ms).apply()

    })
    val c2Values = t.decls.collect {
      case m: MethodSymbol if m.isGetter => m
    }.map(ms => {
      mirror.reflect(c2).reflectMethod(ms).apply()

    })

    println(c1Values, c2Values)
    true
  }
}
