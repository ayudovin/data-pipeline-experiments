#FROM java:8
#
#WORKDIR .
#
#COPY  target/scala-2.12/jobs-assembly-1.0.jar /
#
##CMD java -jar jobs-assembly-1.0.jar
#
##docker run --rm jobs java -jar jobs-assembly-1.0.jar

FROM openjdk:8
ARG SPARK_VERSION=3.1.2

RUN apt-get install -y curl wget \
      && wget http://archive.apache.org/dist/spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
      && tar -xvzf spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
      && mv spark-${SPARK_VERSION}-bin-hadoop3.2 /spark \
      && rm spark-${SPARK_VERSION}-bin-hadoop3.2.tgz \
      && cd /
RUN wget https://repo1.maven.org/maven2/org/apache/hadoop/hadoop-aws/3.2.0/hadoop-aws-3.2.0.jar
RUN mv hadoop-aws-3.2.0.jar /spark/jars
RUN wget https://repo1.maven.org/maven2/com/amazonaws/aws-java-sdk-bundle/1.11.375/aws-java-sdk-bundle-1.11.375.jar
RUN mv aws-java-sdk-bundle-1.11.375.jar /spark/jars

ENV SPARK_HOME /spark
ENV HADOOP_HOME /spark
ENV HADOOP_COMMON_LIB_NATIVE_DIR $HADOOP_HOME/lib/native
ENV HADOOP_OPTS "-Djava.library.path=$HADOOP_HOME/lib"

COPY  target/scala-2.12/jobs-assembly-2.0.jar /

#docker run --network=lakefs --link lakefs:s3.docker.lakefs.io --rm -p 4041:4040  -v /Users/yudovin/workspace/profitero/data-pipeline-experiments/data:/data jobs /spark/bin/spark-submit --class "com.data.pipeline.BsrPreparation"  /jobs-assembly-2.0.jar