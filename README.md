# Data pipeline experiments

This repository shows you the potential stack of technologies and their cooperation to conduct experiments on data pipelines.

The experiment is a change in methodology and obtaining final results based on the impact of these changes.

## Installation

 - Open the project in IntelliJ IDEA or something similar.
 - Setup SDK for Java and Scala.
 - Import jobs directory as SBT module.
 - Go to ./service/zeppelin and execute:
   ```bash
    docker build . -t zeppelin
   ```
 - Go to ./service/airflow and execute:
   ```bash
    docker build . -t airflow
   ```
 - Go to ./service and execute:
   ```bash
    docker-compose up
   ```