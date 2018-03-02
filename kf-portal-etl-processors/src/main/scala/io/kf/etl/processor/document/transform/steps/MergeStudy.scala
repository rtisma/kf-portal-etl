package io.kf.etl.processor.document.transform.steps

import io.kf.etl.model.{Family, Participant, Study}
import org.apache.spark.sql.Dataset

class MergeStudy(override val ctx:StepContext) extends StepExecutable[Dataset[Participant], Dataset[Participant]] {
  override def process(participants: Dataset[Participant]): Dataset[Participant] = {
    import ctx.parentContext.sparkSession.implicits._
    val all = ctx.dbTables
    all.participant.joinWith(all.study, all.participant.col("studyId") === all.study.col("kfId"), "left").map(tuple => {
      Participant(
        kfId = tuple._1.kfId,
        uuid = tuple._1.uuid,
        createdAt = tuple._1.createdAt,
        modifiedAt = tuple._1.modifiedAt,
        family = tuple._1.familyId match {
          case Some(id) => Some(Family(familyId = id))
          case None => None
        },
        studies = Seq(
          Study(
            kfId = tuple._2.kfId,
            uuid = tuple._2.uuid,
            createdAt = tuple._2.createdAt,
            modifiedAt = tuple._2.modifiedAt,
            dataAccessAuthority = tuple._2.dataAccessAuthority,
            externalId = tuple._2.externalId,
            version = tuple._2.version,
            name = tuple._2.name,
            attribution = tuple._2.attribution
          )
        )
      )
    })
  }
}