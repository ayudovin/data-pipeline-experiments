package com.data.pipeline

import org.apache.spark.sql.{DataFrame, SaveMode}

object Split {
  def main(args: Array[String]): Unit = {
    val branch = args(0)
    val split = new Split(branch)
    split.write()
  }
}

class Split(branch: String) extends PipelinePoint[(DataFrame, DataFrame)] {
  override def write(): Unit = {
    val (trainSet, validationSet) = this.transform()

    trainSet.write
      .mode(SaveMode.Overwrite)
      .parquet(s"s3a://experiments/$branch/train_set.parquet")
    validationSet.write
      .mode(SaveMode.Overwrite)
      .parquet(s"s3a://experiments/$branch/validation_set.parquet")
  }

  override def transform(): (DataFrame, DataFrame) = {
    val ranksUnits = spark.read.parquet(s"s3a://experiments/$branch/rank_units.parquet")

    val splits = ranksUnits.randomSplit(Array(0.7, 0.3))
    val trainSet = splits(0)
    val validationSet = splits(1)

    (trainSet, validationSet)
  }
}
