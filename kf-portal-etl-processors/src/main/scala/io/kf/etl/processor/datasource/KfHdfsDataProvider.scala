package io.kf.etl.processor.datasource

import io.kf.etl.common.datasource.KfDataProviderParametersMissingException
import io.kf.etl.common.transform.ProtoBuf2StructType
import io.kf.model.Doc
import io.kf.play.In
import org.apache.spark.sql.SQLContext
import org.apache.spark.sql.sources.{BaseRelation, DataSourceRegister, RelationProvider}
import io.kf.etl.common.Constants._

class KfHdfsDataProvider extends RelationProvider with DataSourceRegister{

  private val must_have_options = Set("")

  override def createRelation(sqlContext: SQLContext, parameters: Map[String, String]): BaseRelation = {
    val missingKeys =
      must_have_options.flatMap(key => {
        if(parameters.get(key).isDefined)
          Set("")
        else
          Set(key)
      })
    if(missingKeys.isEmpty) {
      throw KfDataProviderParametersMissingException(missingKeys)
    }


    parameters
      .get(DATASOURCE_OPTION_PROCESSOR_NAME)
      .map(_ match {
        case PROCESSOR_DOCUMENT => KfHdfsParquetData(sqlContext, ProtoBuf2StructType.parseDescriptor(Doc.scalaDescriptor), parameters.get(DATASOURCE_OPTION_PATH).get)
        case PROCESSOR_INDEX => KfHdfsParquetData(sqlContext, ProtoBuf2StructType.parseDescriptor(Doc.scalaDescriptor), parameters.get(DATASOURCE_OPTION_PATH).get)
      })
      .getOrElse(KfHdfsParquetData(sqlContext, ProtoBuf2StructType.parseDescriptor(In.scalaDescriptor), parameters.get(DATASOURCE_OPTION_PATH).get))

  }

  override def shortName(): String = HDFS_DATASOURCE_SHORT_NAME


}