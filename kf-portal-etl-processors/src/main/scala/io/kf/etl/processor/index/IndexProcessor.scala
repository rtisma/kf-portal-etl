package io.kf.etl.processor.index

import io.kf.etl.processor.common.Processor
import io.kf.etl.processor.index.context.IndexContext
import io.kf.etl.processor.repo.Repository
import io.kf.model.Doc
import org.apache.spark.sql.Dataset

class IndexProcessor(context: IndexContext,
                     source: Repository[Doc] => Dataset[Doc],
                     transform: Dataset[Doc] => Dataset[Doc],
                     sink: Dataset[Doc] => Unit) extends Processor[Repository[Doc], Unit]{

  def process(input: Repository[Doc]):Unit = {
    source.andThen(transform).andThen(sink)(input)

  }

}