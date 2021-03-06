package io.kf.etl.processors.index

import io.kf.etl.processors.common.processor.Processor
import io.kf.etl.processors.index.context.IndexContext
import io.kf.etl.processors.index.posthandler.IndexProcessorPostHandler
import io.kf.etl.processors.index.posthandler.impl.ReplaceIndexInAlias
import io.kf.etl.processors.repo.Repository
import org.apache.spark.sql.Dataset

class IndexProcessor(context: IndexContext,
                     source: ((String, Repository)) => (String, Dataset[String]),
                     transform: ((String, Dataset[String])) => (String, Dataset[String]),
                     sink: ((String, Dataset[String])) => Unit) extends Processor[(String, Repository), Unit]{

  def process(input: (String, Repository)):Unit = {

    source.andThen(transform).andThen(sink)(input)

//    context.config.aliasActionEnabled match {
//      case true => {
//        val handler = Class.forName(context.config.aliasHandlerClass).getConstructor(classOf[IndexContext], classOf[String]).newInstance(context, input._1).asInstanceOf[IndexProcessorPostHandler]
//        handler.post()
//      }
//      case false =>
//    }
  }

}
