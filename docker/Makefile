
LATEXMK 		:= /usr/bin/latexmk
CHKTEX			:= /usr/bin/chktex
PDFLATEX 		:= /usr/bin/pdflatex
OUTPUT 			:= thesis
DIST			:= dist/
BUILD			:= build/
LOGFILE			:= build.log
SOURCES_APP 	:= appendixes/*tex
SOURCES_MAIN 	:= $(OUTPUT).tex chapters/*tex
SOURCES 		:= ${SOURCES_MAIN} ${SOURCES_APP}

COLOR_GREY 		:= \033[1m
COLOR_MAGENTA 	:= \033[35m
COLOR_CYAN 		:= \033[36m
COLOR_RESET 	:= \033[0m


define heading
	title="${1}"
	@echo -e "${COLOR_MAGENTA}:: ${title}${COLOR_RESET}"
endef

all: help

pre_build:
	@mkdir "$(DIST)"
	@mkdir --parents "$(BUILD)"{chapters,appendixes}
	@vlna $(SOURCES)

##@ Build

watch: $(SOURCES)  	## Live build.
	$(call heading,Building PDF (watch))
	@vlna $(SOURCES)
	@$(LATEXMK) -cd -pdf -bibtex -pvc -view=none $(OUTPUT)


thesis: $(SOURCES)  	## Build thesis as PDF document suitable for printing.
	$(call heading,Building PDF)
	@vlna $(SOURCES)
	@$(LATEXMK) -bibtex -pdf -pdflatex="pdflatex '\AtBeginDocument{\hypersetup{hidelinks}}\input{$(OUTPUT)}'"

pdf: thesis

ethesis: $(SOURCES)	## Build ebook version of thesis (with colored links).
	$(call heading,Building Ebook)
	@vlna $(SOURCES)
	@$(LATEXMK) -pdf -bibtex $(OUTPUT)

ebook: ethesis

eurocv: eurocv/eurocv.tex  ## Builds authors CV only.
	$(call heading,Building EuroCV)
	@cd eurocv && $(LATEXMK) -pdf -pvc eurocv.tex

dist: pre_build  ## Builds distribution ready documents.
	$(call heading,Building Distribution Ready Documents)

	# build full pdf with appendixes
	@$(LATEXMK) -bibtex -pdf -pdflatex="$(PDFLATEX) -output-directory=$(BUILD) '\def\hideappendixes{true}\include{$(OUTPUT)}'"
	@mv "$(BUILD)/$(OUTPUT).pdf" "$(DIST)/$(OUTPUT)-main.pdf"

	# # build full ebook with appendixes
	# @$(LATEXMK) -pdf -bibtex $(OUTPUT)
	# @mv thesis.pdf "$(DIST)/ethesis-full.pdf"


##@ Misc
clean:  ## Clean up.
	$(call heading,Cleanup)
	$(LATEXMK) -C -silent
	@rm -rf "${DIST}" "$(BUILD)" "$(LOGFILE)"
	@find . -regextype posix-extended -iregex ".*(log|aux|fls|fdb_latexmk|te~|bbl.*|ist|lol)" -exec rm -rf {} \;

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


.PHONY: help eurocv
help: ## Display this help.
	@awk 'BEGIN {FS = ":.*##"; printf "\n${COLOR_GREY}Usage:${COLOR_RESET}\n  make ${COLOR_CYAN}<target>${COLOR_RESET}\n"} \
	/^[a-zA-Z_0-9-]+:.*?##/ { printf "  ${COLOR_CYAN}%-15s${COLOR_RESET} %s\n", $$1, $$2 } \
	/^##@/{ printf "\n${COLOR_GREY}%s${COLOR_RESET}\n", substr($$0, 5) } \
	END { printf "\nCreated by (c)2024 mirek\n" } \
	' $(MAKEFILE_LIST)
