package com.data.pipeline

import org.apache.spark.sql.SparkSession

/**
 * Main trait for initilizing each pipeline point.
 *
 * @tparam T
 */
trait PipelinePoint[T] {
  val spark: SparkSession =  SparkSession.builder()
    .appName("experiments")
    .config("spark.hadoop.fs.s3a.access.key", "AKIAIOSFODNN7EXAMPLE")
    .config("spark.hadoop.fs.s3a.secret.key", "wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY")
    .config("spark.hadoop.fs.s3a.endpoint", "http://s3.docker.lakefs.io:8000")
    .config("spark.hadoop.fs.s3a.path.style.access", "true")
    .config("spark.sql.shuffle.partitions", "10")
    .getOrCreate()

  def write(): Unit
  def transform(): T
}
