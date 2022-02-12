FROM alpine:3.14
RUN rm -rf /usr/local/article-repo
COPY ./articles-repo /usr/local/article-repo