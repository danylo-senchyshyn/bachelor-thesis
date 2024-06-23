
OUTPUT 			:= thesis
DIST			:= dist/
BUILD			:= build/
LOGFILE			:= build.log
SOURCES_APP 	:= appendixes/*tex
SOURCES_MAIN 	:= $(OUTPUT).tex chapters/*tex
SOURCES 		:= ${SOURCES_MAIN} ${SOURCES_APP}
LATEXMK 		:= /usr/bin/latexmk -r ${APP_PATH}/latexmkrc
CHKTEX			:= /usr/bin/chktex
PDFLATEX 		:= /usr/bin/pdflatex -output-directory="${BUILD}" -halt-on-error
VLNA			:= /usr/bin/vlna -s -l

COLOR_GREY 		:= \033[1m
COLOR_MAGENTA 	:= \033[35m
COLOR_CYAN 		:= \033[36m
COLOR_RESET 	:= \033[0m

# Get the directory of the Makefile
MKFILE_PATH := $(abspath $(lastword $(MAKEFILE_LIST)))
MKFILE_DIR := $(dir $(MKFILE_PATH))

# Include the colors.mk file from the same directory
include $(MKFILE_DIR)colors.mk


define heading
	@echo -e "${COLOR_MAGENTA}:: $1${COLOR_RESET}"
endef

define cleanup
	$(LATEXMK) -C -silent
	@rm -rf "$(BUILD)" "$(LOGFILE)"
endef

all: help

pre_build:
	@mkdir --parents "$(DIST)" "$(BUILD)"{chapters,appendixes}
	@$(VLNA) $(SOURCES)


##@ Build

watch: pre_build  	## Live build.
	@$(call heading,Building PDF (watch))
	@$(LATEXMK) -pvc -view=none $(OUTPUT)


thesis: clean pre_build  	## Build thesis as PDF document suitable for printing.
	@$(call heading,Building Full PDF)
	@$(LATEXMK) -pdflatex="$(PDFLATEX)" $(OUTPUT)
	@mv "$(BUILD)/$(OUTPUT).pdf" "$(DIST)/$(OUTPUT)-full.pdf"

thesis_main: clean pre_build
	@$(call heading,Building PDF with Main Part Only)
	@$(LATEXMK) -pdflatex="$(PDFLATEX) '\def\hideappendixes{true}\include{$(OUTPUT)}'"
	@mv "$(BUILD)/$(OUTPUT).pdf" "$(DIST)/$(OUTPUT)-main.pdf"


pdf: thesis

ethesis: clean pre_build	## Build ebook version of thesis (with colored links).
	@$(call heading,Building Full Ebook)
	@$(LATEXMK) -pdflatex="$(PDFLATEX)"  $(OUTPUT)
	@mv "$(BUILD)/$(OUTPUT).pdf" "$(DIST)/e$(OUTPUT)-full.pdf"

ethesis_main: clean pre_build
	@$(call heading,Building Ebook with Main Part Only)
	@$(LATEXMK) -pdflatex="$(PDFLATEX)" $(OUTPUT)
	@mv "$(BUILD)/$(OUTPUT).pdf" "$(DIST)/e$(OUTPUT)-main.pdf"

ebook: ethesis

eurocv: eurocv/eurocv.tex  ## Builds authors CV only.
	$(call heading,Building EuroCV)
	@cd eurocv && $(LATEXMK) -pdf -pvc eurocv.tex

dist: thesis ethesis thesis_main ethesis_main ## Builds distribution ready documents.
	$(call heading,Building Distribution Ready Documents)


##@ Misc
clean:  ## Clean up.
	@$(call heading,Cleanup)
	@$(LATEXMK) -C -silent
	@rm -rf "$(DIST)" "$(BUILD)" "$(LOGFILE)"
	@find . -regextype posix-extended -iregex ".*(log|aux|fls|fdb_latexmk|te~|bbl.*|ist|lol|glo|acn)" -exec rm -rf {} \;


version:  ## Shows version of this Makefile.
	@echo "${TUKE_THESIS_VERSION}"
	@pdflatex --version | head --lines 1
	@latexmk -version
	@texcount --version | head --lines 1
	@chktex --version | head --lines 1


stats:  ## Shows thesis statistics.
	$(call heading,Statistics - Count Words)
	@texcount -total $(SOURCES_MAIN)

	$(call heading,Statistics - Count Letters)
	@texcount -total -letters $(SOURCES_MAIN)

# spellcheck:  ## Runs spell checking of sources.
# 	$(call heading,Spell Checking)

lint:  ## Runs linter for sources.
	$(call heading,Linter)
	$(CHKTEX) $(SOURCES)


help: ## Display this help.
	@awk 'BEGIN {FS = ":.*##"; printf "\n${COLOR_GREY}Usage:${COLOR_RESET}\n  make ${COLOR_CYAN}<target>${COLOR_RESET}\n"} \
	/^[a-zA-Z_0-9-]+:.*?##/ { printf "  ${COLOR_CYAN}%-15s${COLOR_RESET} %s\n", $$1, $$2 } \
	/^##@/{ printf "\n${COLOR_GREY}%s${COLOR_RESET}\n", substr($$0, 5) } \
	END { printf "\nCreated by (c)2024 mirek\n" } \
	' $(MAKEFILE_LIST)


.PHONY: help eurocv thesis lint clean version
