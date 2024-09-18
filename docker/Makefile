
OUTPUT 			:= thesis
DIST			:= dist
BUILD			:= build
LOGFILE			:= build.log
SOURCES_APP 	:= appendixes/*tex
SOURCES_MAIN 	:= $(OUTPUT).tex chapters/*tex
SOURCES 		:= ${SOURCES_MAIN} ${SOURCES_APP}
LATEXMK 		:= /usr/bin/latexmk -lualatex -out2dir=${DIST}
CHKTEX			:= /usr/bin/chktex
LUALATEX 		:= /usr/bin/lualatex -output-directory=${BUILD} -halt-on-error -output-format=pdf

# Include external modules
MKFILE_DIR := $(dir $(abspath $(lastword $(MAKEFILE_LIST))))
include $(MKFILE_DIR)colors.mk

# prints heading with color
define heading
	@echo -e "$(B_PURPLE):: $1$(RESET)"
endef

# clean only build results, not the dist/ folder
define cleanup
	$(LATEXMK) -C -silent
	@rm -rf "$(BUILD)" "$(LOGFILE)"
endef

all: help

pre_build:
	@mkdir --parents "$(DIST)" "$(BUILD)/"{chapters,appendixes}


##@ Build

# init:	## Init empty Thesis
# 	@$(call heading,Init Thesis)

watch: pre_build  	## Live build.
	@$(call heading,Building PDF (watch))
	@$(LATEXMK) -pvc -view=none $(OUTPUT) -jobname="$(OUTPUT)-watch"


thesis: clean pre_build  	## Build thesis as PDF document suitable for printing.
	@$(call heading,Building Full PDF)

	@$(LUALATEX) '\def\printable{true}\input{$(OUTPUT)}' -jobname="$(OUTPUT)"
	@makeglossaries -d "$(BUILD)" thesis
	@biber --output-directory="$(BUILD)" "$(OUTPUT)"
	@$(LUALATEX) '\def\printable{true}\input{$(OUTPUT)}' -jobname="$(OUTPUT)"
	@$(LUALATEX) '\def\printable{true}\input{$(OUTPUT)}' -jobname="$(OUTPUT)"

	@cp "$(BUILD)/$(OUTPUT).pdf" "$(DIST)/$(OUTPUT).pdf"

pdf: thesis


ethesis: clean pre_build	## Build ebook version of thesis (with colored links).
	@$(call heading,Building Full Ebook)
	@$(LATEXMK) -lualatex="$(LUALATEX)" $(OUTPUT) -jobname="e$(OUTPUT)"

ebook: ethesis


eurocv: eurocv/eurocv.tex  ## Builds authors CV only.
	@$(call heading,Building EuroCV)
	@cd eurocv && $(LATEXMK) -pdf -pvc eurocv.tex


dist: thesis ethesis	## Builds distribution ready documents.
	@$(call heading,Building Distribution Ready Documents)


##@ Misc
clean:  ## Clean up.
	@$(call heading,Cleanup)
	@$(LATEXMK) -C -silent
	@rm -rf "$(DIST)" "$(BUILD)" "$(LOGFILE)"
	@find . -regextype posix-extended -iregex ".*(log|aux|bcf|blg|fls|fdb_latexmk|te~|bbl.*|ist|lol|lof|lot|out|glo|acn|run.xml|toc)" -exec rm -rf {} \;


version:  ## Shows version of this Makefile.
	@echo "TUKE Thesis version: ${TUKE_THESIS_VERSION}"
	@lualatex --version | head --lines 1
	@latexmk -version
	@texcount --version | head --lines 1
	@chktex --version | head --lines 1


stats:  ## Shows thesis statistics.
	@$(call heading,Statistics - Count Words)
	@texcount -total $(SOURCES_MAIN)

	@$(call heading,Statistics - Count Letters)
	@texcount -total -letters $(SOURCES_MAIN)


lint:  ## Runs linter for sources.
	@$(call heading,Linter)
	@$(CHKTEX) $(SOURCES)


shell:  ## Spawns shell in the contianer
	@$(call heading,Shell)
	@bash


fonts:   ## List all fonts for LuaLaTeX
	@$(call heading,List Fonts)
	@luaotfload-tool --list=*


help: ## Display this help.
	@awk 'BEGIN {FS = ":.*##"; printf "\n${B_HI_WHITE}Usage:${RESET}\n  make ${B_CYAN}<target>${RESET}\n"} \
	/^[a-zA-Z_0-9-]+:.*?##/ { printf "  ${B_CYAN}%-15s${RESET} %s\n", $$1, $$2 } \
	/^##@/{ printf "\n${B_HI_WHITE}%s${RESET}\n", substr($$0, 5) } \
	END { printf "\nCreated by (c)2024 mirek\n" } \
	' $(MAKEFILE_LIST)


.PHONY: help eurocv thesis lint clean version
