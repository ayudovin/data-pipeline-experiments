package com.data.pipeline

import org.apache.spark.sql.{DataFrame, SaveMode}
import org.apache.spark.sql.functions.{not, round, max, avg}

object UnitsPreparation {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val salePreparation = new UnitsPreparation("/data", branch)
    salePreparation.write()
  }
}

class UnitsPreparation(salesPath: String, branch: String) extends PipelinePoint[DataFrame] {

  import spark.implicits._

  override def write(): Unit = {
    val result = this.transform()

    result.write
      .mode(SaveMode.Overwrite)
      .parquet(s"s3a://experiments/$branch/units_preparation.parquet")
  }

  override def transform(): DataFrame = {
    val salesPremium = spark.read.parquet(s"$salesPath/sales_premium.parquet")
    val salesBasic = spark.read.parquet(s"$salesPath/sales_basic.parquet")

    filtering(salesPremium.unionByName(salesBasic))
  }

  protected def filtering(df: DataFrame): DataFrame = {
    df.filter($"units".isNotNull and not($"units".isNaN))
      .filter($"uid".isNotNull and $"uid".isNotNull)
  }

  protected def aggUnits(df: DataFrame): DataFrame = {
    df.select($"uid", $"units")
      .groupBy($"uid").agg(avg("units") as "units")
  }
}
