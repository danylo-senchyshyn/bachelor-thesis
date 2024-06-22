
LATEXMK := /usr/bin/latexmk
CHKTEX	:= /usr/bin/chktex
OUTPUT 	:= thesis
SOURCES_MAIN := thesis.tex chapters/*tex
SOURCES_APP := appendixes/*tex
SOURCES := ${SOURCES_MAIN} ${SOURCES_APP}
VERSION := 2024.06

COLOR_GREY 		:= \033[1m
COLOR_MAGENTA 	:= \033[35m
COLOR_CYAN 		:= \033[36m
COLOR_RESET 	:= \033[0m


define heading
	@echo -e "${COLOR_MAGENTA}:: ${1}${COLOR_RESET}"
endef

all: help

##@ Build

watch: $(SOURCES)  	## Live build.
	$(call heading,Building PDF (watch))
	@vlna $(SOURCES)
	@$(LATEXMK) -cd -pdf -bibtex -pvc -view=none $(OUTPUT)

thesis: $(SOURCES)  	## Build thesis as PDF document.
	$(call heading,Building PDF)
	@vlna $(SOURCES)
	@$(LATEXMK) -pdf -bibtex $^

pdf: thesis

ethesis: $(SOURCES)	## Build ebook version of thesis (with colored links).
	$(call heading,Building Ebook)
	@vlna $(SOURCES)
	@$(LATEXMK) -pdf -bibtex $^

ebook: ethesis

eurocv: eurocv/eurocv.tex  ## Builds authors CV only.
	$(call heading,Building EuroCV)
	@cd eurocv && $(LATEXMK) -pdf -pvc eurocv.tex


##@ Misc
clean:  ## Clean up.
	$(call heading,Cleanup)
	$(LATEXMK) -C -silent
	@find . -regextype posix-extended -iregex ".*(log|aux|fls|fdb_latexmk|te~|bbl.*|ist|lol)" -exec rm -rf {} \;

version:  ## Shows version of this Makefile.
	@echo "$(VERSION)"

stats:  ## Shows thesis statistics.
	$(call heading,Statistics - Count Words)
	@texcount -total $(SOURCES_MAIN)

	$(call heading,Statistics - Count Letters)
	@texcount -total -letters $(SOURCES_MAIN)

spellcheck:  ## Runs spell checking of sources.
	$(call heading,Spell Checking)

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
