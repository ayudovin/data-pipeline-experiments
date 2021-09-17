package com.data.pipeline

import org.apache.spark.sql.{DataFrame, SaveMode}

object RankUnitAccord {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val rankUnits = new RankUnitAccord(branch)
    rankUnits.write()
  }
}

class RankUnitAccord(branch: String) extends PipelinePoint[DataFrame] {
  override def write(): Unit = {
    val result = this.transform()

    result.write
      .mode(SaveMode.Overwrite)
      .parquet(s"s3a://experiments/$branch/rank_units.parquet")
  }

  override def transform(): DataFrame = {
    val rank = spark.read.parquet(s"s3a://experiments/$branch/rank_preparation.parquet")
    val units = spark.read.parquet(s"s3a://experiments/$branch/units_preparation.parquet")

    rank.join(units, Seq("uid"))
      .select("uid", "units", "rank")
      .distinct()
  }
}
