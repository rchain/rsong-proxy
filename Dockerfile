FROM openjdk:11

ENV PORT 9000
ENV APP_DIR /var/app
RUN mkdir -p $APP_DIR
WORKDIR $APP_DIR
COPY . $APP_DIR
EXPOSE $PORT

RUN apt-get update
# make sure that locales package is available
RUN apt-get install --reinstall -y locales
# uncomment chosen locale to enable it's generation
RUN sed -i 's/# pl_PL.UTF-8 UTF-8/pl_PL.UTF-8 UTF-8/' /etc/locale.gen
# generate chosen locale
RUN locale-gen pl_PL.UTF-8
# set system-wide locale settings
ENV LANG pl_PL.UTF-8
ENV LANGUAGE pl_PL
ENV LC_ALL pl_PL.UTF-8
# verify modified configuration
RUN dpkg-reconfigure --frontend noninteractive locales

# Env variables
ENV SCALA_VERSION 2.12.7
ENV SBT_VERSION 1.2.8

# Install Scala
## Piping curl directly in tar
RUN \
curl -fsL https://downloads.typesafe.com/scala/$SCALA_VERSION/scala-$SCALA_VERSION.tgz | tar xfz - -C /root/ && \
     echo >> /root/.bashrc && \
     echo "export PATH=~/scala-$SCALA_VERSION/bin:$PATH" >> /root/.bashrc

# Install sbt
RUN  curl -L -o sbt-$SBT_VERSION.deb https://dl.bintray.com/sbt/debian/sbt-$SBT_VERSION.deb && \
     dpkg -i sbt-$SBT_VERSION.deb && \
     rm sbt-$SBT_VERSION.deb && \
     apt-get update && \
     apt-get install sbt && \
     sbt sbtVersion && \
     sbt sbtVersion clean compile universal:stage 
RUN ls -la $APP_DIR/target/universal/stage/bin && \
    ls -la $APP_DIR/target/universal/stage/lib 
CMD ["target/universal/stage/bin/rsong-proxy"]
