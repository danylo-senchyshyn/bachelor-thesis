#!/usr/bin/env bash

set -o errexit

readonly COLOR_MAGENTA="\033[35m"
readonly COLOR_RESET="\033[0m"
readonly packages_file="packages"

# install packages if there are some in packages file
if [[ -f "${packages_file}" ]]; then
    content=$(sed '/#/d' "${packages_file}" | tr --delete "[:space:][:blank:]")

    if [[ -n "${content}" ]]; then
      echo "${COLOR_MAGENTA}Installing additional packages based on requirements.txt file:${COLOR_RESET}"
      cat "${packages_file}"
      echo
      sudo dnf install -y $(sed '/#/d' "${packages_file}")
    fi
fi

# running make?
if [[ "${1}" == "make" ]]; then
  shift
  make --makefile="${MAKEFILE_PATH}" "${@}"
  exit 0
fi

# exec
exec "$@"

