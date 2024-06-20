FROM fedora:40

LABEL version="2024.06"
ENV TUKE_THESIS_VERSION="2024.06"
ENV TZ="Europe/Bratislava"

ARG USERNAME="student"
ARG GROUPNAME="student"
ARG UID=1000
ARG GID=1000

RUN dnf install -y \
        aspell-sk \
        make \
        texlive-pdfpages \
        texlive-metafont \
        texlive-collection-fontsrecommended \
        texlive-totalcount \
        texlive-biblatex-iso690 \
        texlive-glossaries \
        texlive-babel-english \
        texlive-babel-slovak \
        texlive-hyphen-slovak \
        latexmk \
        texlive-vlna \
        texlive-texcount \
        texlive-chktex \
    && dnf clean all \
    && groupadd --system "${GROUPNAME}" --gid "${GID}" \
    && useradd --no-log-init --system --gid "${GROUPNAME}" --uid "${UID}" "${USERNAME}" \
    && echo "ALL ALL=(ALL) NOPASSWD: /usr/bin/dnf" >> /etc/sudoers

COPY Makefile /app/
COPY entrypoint.bash /

WORKDIR /thesis

ENTRYPOINT [ "/entrypoint.bash" ]

USER "${USERNAME}"

CMD [ "make", "--makefile=/app/Makefile", "help" ]

