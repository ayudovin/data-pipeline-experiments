package com.data.pipeline

import org.apache.spark.ml.PipelineModel
import org.apache.spark.sql.{DataFrame, SaveMode}

object ApplySoldUnits {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val soldUnits = new ApplySoldUnits(branch)
    soldUnits.write()
  }
}

/**
 * Estimate sold units for ranks.
 *
 * @param branch from lakefs
 */
class ApplySoldUnits(branch: String) extends PipelinePoint[DataFrame] {
  import spark.implicits._

  override def write(): Unit = {
    val results = this.transform()
    results.write.mode(SaveMode.Overwrite).parquet(s"s3a://experiments/$branch/sold_units.parquet")
  }

  override def transform(): DataFrame = {
    val ranks = spark.read.parquet(s"s3a://experiments/$branch/rank_preparation.parquet")
    val unitsExists = spark.read.parquet(s"s3a://experiments/$branch/units_preparation.parquet")
    val estimationMethod = PipelineModel.load(s"s3a://experiments/$branch/estimation.model")
    val rankUnitAccord = spark.read.parquet(s"s3a://experiments/$branch/rank_units.parquet")

    val estimated = estimationMethod.transform(ranks.join(unitsExists, Seq("uid"), "left_anti"))
      .select($"uid", $"rank", $"prediction" as "units")

    estimated.unionByName(rankUnitAccord)
  }
}
