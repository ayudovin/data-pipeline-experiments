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
   
## Demo video
[![Watch the video](https://cdn.pixabay.com/photo/2017/03/13/04/25/play-button-2138735_960_720.png)](https://drive.google.com/file/d/1y21XKugY5hNHwboYpzTOkgG8WMpkxfwb/view?usp=sharing)