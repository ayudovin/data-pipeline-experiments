package com.data.pipeline

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions.{avg, max, not, round}

object RankPreparation {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val bsrPreparation = new RankPreparation("/data/rank.parquet", branch)
    bsrPreparation.write()
  }
}

class RankPreparation(bsrPath: String, branch: String) extends PipelinePoint[DataFrame] {

  import spark.implicits._

  override def write(): Unit = {
    val result = this.transform()

    result.write
      .mode(SaveMode.Overwrite)
      .parquet(s"s3a://experiments/$branch/rank_preparation.parquet")
  }

  override def transform(): DataFrame = {
    spark.read.parquet(bsrPath)
      .transform(filtering)
      .transform(rankAggregation)
  }

  protected def filtering(df: DataFrame): DataFrame = {
    df.filter($"rank".isNotNull and not($"rank".isNaN))
      .filter($"uid".isNotNull and not($"rank".isNaN))
      .filter($"code" === "Kindle Store")
  }

  protected def rankAggregation(df: DataFrame): DataFrame = {
    df.select($"uid", $"rank")
      .groupBy($"uid").agg(round(max("rank")) as "rank")
  }
}
