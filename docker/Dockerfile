FROM fedora:40

ARG VERSION="2024.06"
ARG USERNAME="student"
ARG GROUPNAME="student"
ARG UID=1000
ARG GID=1000

LABEL maintainer="mirek <miroslav.binas@tuke.sk>"
LABEL version="2024.06"
LABEL homepage="https://git.kpi.fei.tuke.sk/tuke/thesis.in.latex"
ENV TUKE_THESIS_VERSION="${VERSION}"
ENV TZ="Europe/Bratislava"
ENV APP_PATH="/app"
ENV MAKEFILE_PATH="${APP_PATH}/Makefile"
ENV LC_ALL="sk_SK.UTF-8"
ENV TEXMFHOME=/tmp
ENV TEXMFVAR=/tmp

# latex installation instructions in fedora: https://docs.fedoraproject.org/en-US/neurofedora/latex/
RUN dnf install -y \
        glibc-langpack-sk \
        make \
        "tex(pdfpages.sty)" \
        "tex(totalcount.sty)" \
        "tex(luavlna.sty)" \
        "tex(inconsolata.sty)" \
        texlive-metafont \
        texlive-collection-fontsrecommended \
        texlive-biblatex-iso690 \
        texlive-glossaries \
        texlive-babel-english \
        texlive-babel-slovak \
        texlive-hyphen-slovak \
        latexmk \
        texlive-texcount \
        texlive-chktex \
    && dnf clean all

RUN groupadd --system "${GROUPNAME}" --gid "${GID}" \
    && useradd --no-log-init --system --gid "${GROUPNAME}" --uid "${UID}" "${USERNAME}" \
    && echo "ALL ALL=(ALL) NOPASSWD: /usr/bin/dnf" >> /etc/sudoers \
    && echo 'LANG="en_US.UTF-8"' > /etc/locale.conf

COPY Makefile /app/
COPY colors.mk /app/
COPY latexmkrc /app/
COPY entrypoint.bash /

WORKDIR /thesis

ENTRYPOINT [ "/entrypoint.bash" ]

USER "${USERNAME}"

CMD [ "make", "help" ]
