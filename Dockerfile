FROM ubuntu:xenial

RUN \
  # configure the "reactivity" user
  groupadd reactivity && \
  useradd reactivity -s /bin/bash -m -g reactivity -G sudo && \
  echo 'reactivity:reactivity' |chpasswd && \
  mkdir /home/reactivity/reactivity && \
  
  # install open-jdk 8
  apt-get update && \
  apt-get install -y openjdk-8-jdk && \

  # install utilities
  apt-get install -y \
     wget \
     curl \
     nano \
     git \
     zip \
     bzip2 \
     fontconfig \
     python \
     g++ \
	 build-essential && \
	 
  # cleanup
  apt-get clean && \
  rm -rf \
    /var/lib/apt/lists/* \
    /tmp/* \
    /var/tmp/*

# Install CloudFoundry
ADD https://cli.run.pivotal.io/stable?release=linux64-binary&source=github /tmp/cf-cli.tgz

RUN \
  # install CloudFoundry
  tar xvf /tmp/cf-cli.tgz && \
  mv /cf /usr/bin/cf

RUN \
  # fix user permissions
  chown -R reactivity:reactivity \
    /home/reactivity && \

  # cleanup
  rm -rf \
    /var/lib/apt/lists/* \
    /tmp/* \
    /var/tmp/*

# expose the working directory
USER reactivity
WORKDIR "/home/reactivity/reactivity"
VOLUME ["/home/reactivity/reactivity"]