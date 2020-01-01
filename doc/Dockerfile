FROM thxbb12/pandoc:2.6

RUN apt-get update && apt-get install -y make
RUN mkdir /tmp/pandoc-crossref && \
    cd /tmp/pandoc-crossref && \
    wget https://github.com/lierdakil/pandoc-crossref/releases/download/v0.3.0.1/linux-ghc80-pandoc20.tar.gz && \
    tar xzvf linux-ghc80-pandoc20.tar.gz && \
    cp pandoc-crossref /bin && \
    cd /docs

