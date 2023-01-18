import org.apache.hadoop.shaded.org.eclipse.jetty.websocket.common.frames.DataFrame
import org.apache.spark.SparkContext
import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.DataFrame
import com.mongodb.spark._
import org.apache.spark.rdd.RDD
import org.bson.Document
object MeTest {
  case class CustomerObj(id: String, isoCustNameLangCd: String, custNameLangGrpCd: String)
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().master("local").appName("Hello World")
    .config("spark.mongodb.input.database", "local")
      .config("spark.mongodb.output.database", "local")
      .config("spark.mongodb.input.uri", "mongodb://localhost:27017/local.customer-test")
      .config("spark.mongodb.output.uri", "mongodb://localhost:27017/local.customer-test")
      .config("spark.mongodb.read.connection.uri", "mongodb://localhost/local.customer-test")
      .config("spark.mongodb.write.connection.uri", "mongodb://localhost/local.customer-test")
      .getOrCreate()

    val rdd = loadDataSetFromBinaryRecords(spark)


    var i=0

    val customerRdd = rdd.map(row =>
       {
         i += 1

         val custId = row.substring(0, 30)
         val isoCustNameLangCd = row.substring(31, 31+2)
         val custNameLangGrpCd = row.substring(33, 33+1)
         val custObject = new CustomerObj(custId, isoCustNameLangCd, custNameLangGrpCd)
         //println("Line: " + custObject + " " + i)
         custObject
      }
    )

    saveToMongoDB(spark, customerRdd)

    spark.stop()
  }

  def loadDataSetFromBinaryRecords(sparkSession: SparkSession): org.apache.spark.rdd.RDD[String] = {
    val ds = sparkSession.sparkContext.binaryRecords("/Users/kaweepapkongkittisan/IdeaProjects/scala-test/data/BI_RM_P143_RMXTRTCS_L10_221031.dat", 412)
      .map(bytes => {
        val s = new String(bytes, "IBM-Thai")
        println(s)
        s
      })

    ds
  }

  def saveToMongoDB(sparkSession: SparkSession, rdd: RDD[CustomerObj]): Unit = {
    val df = sparkSession.createDataFrame(rdd)

    df.write.format("mongodb").mode("overwrite").save()

  }





}
