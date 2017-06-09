package odm

import java.text.SimpleDateFormat
import java.util.Date

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}


/**
 * Created by docker on 5/17/17.
 */
class GlobalContext(appName: String) extends Serializable {

  def createSparkConfig(appName:String ) : SparkContext =
  {
    //Set up system properties from json config file

    System.setProperty("SPARK_YARN_MODE", "true")

    //Set up system properties from Global json config

    val config = new SparkConf()
    config.setAppName(appName)
    .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
    .set("spark.driver.maxResultSize","0")

    config
//     val sc = new SparkContext(config)
//     sc
  }

  def getFolderName ()={
     new SimpleDateFormat("yyyyMMddHHmm").format(new Date())
  }

  val SC =this.createSC(appName)
  val NumPartitions : Int = 100
  val result_path : String = "maprfs:///datalake/uhclake/prd/developer/idemidov/results/"
  val result_folder = getFolderName()
  val sqlContext = new org.apache.spark.sql.SQLContext(SC)
      sqlContext.sql("SET spark.sql.autoBroadcastJoinThreshold = -1")

Logger.getLogger("org").setLevel(Level.OFF)
Logger.getLogger("akka").setLevel(Level.OFF)

val left_df =sqlContext.sql("select * from parquet.`maprfs:////datalake/uhclake/prd/developer/idemidov/testdata/L_cnsm_srch_full_pqt_f`" )
val right_df = sqlContext.sql("select * from parquet.`maprfs:////datalake/uhclake/prd/developer/idemidov/testdata/L_cnsm_srch_inc_prq`")
val left_prefix = "df1"
val right_prefix = "df2"

//  // All equal rows from both left_df and right_df
//  val intersect_df = left_df.intersect(right_df)
//
//  // All rows from left_df which is don't exist in right_df
//
//  val left_minus_df     = left_df.except(left_df.intersect(right_df))
//
//  //All rows from right_df which is don't exist in left_df
//  val right_minus_df     = right_df.except(right_df.intersect(left_df))
//
//  // all rows which is different from both right_df and left_df
//  val all_non_matching_rows_df  = right_df.unionAll(left_df).except(right_df.intersect(left_df))





 // val hiveContext = new org.apache.spark.sql.hive.HiveContext(SC)

  //    val df1 =sqlContext.sql("select concat_ws('|',partn_nbr,cnsm_id,src_cd,lgcy_src_id) as hash_keyf, * " +
  //      "from parquet.`maprfs:////datalake/uhclake/prd/developer/sbandar8/CDB-ARA/L_cnsm_srch_full_pqt`" )
  //    val df2 = sqlContext.sql("select concat_ws('|',partn_nbr,cnsm_id,src_cd,lgcy_src_id) as hash_keyi," +
  //      "* from parquet.`maprfs:////datalake/uhclake/prd/developer/sbandar8/CDB-ARA/L_cnsm_srch_full_INC_pqt`")

}
