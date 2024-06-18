
LATEXMK := /usr/bin/latexmk
OUTPUT := thesis
SOURCES := thesis.tex chapters/*tex
VERSION := 2024.06

COLOR_GREY := \033[1m
COLOR_MAGENTA := \033[35m
COLOR_CYAN := \033[36m
COLOR_RESET := \033[0m

all: pdf

##@ Development

watch: $(SOURCES)  ## Live build.
	@echo "Building PDF"
	@vlna $(SOURCES)
	@$(LATEXMK) -cd -pdf -bibtex -pvc -view=none $(OUTPUT)

pdf: $(SOURCES)  ## Build thesis as PDF document.
	@echo "Building PDF"
	@vlna $(SOURCES)
	@$(LATEXMK) -pdf -bibtex $^

eurocv: eurocv/eurocv.tex  ## Builds authors CV only.
	@echo "Building EuroCV"
	@cd eurocv && $(LATEXMK) -pdf -pvc eurocv.tex


##@ Misc
clean:  ## Clean up.
	@echo "Cleanup"
	$(LATEXMK) -C -silent
	@find . -regextype posix-extended -iregex ".*(log|aux|fls|fdb_latexmk|te~|bbl.*|ist|lol)" -exec rm -rf {} \;

version:  ## Shows version of this Makefile.
	@echo "$(VERSION)"

stats:  ## Shows thesis statistics.
	@echo "Statistics"
	@texcount -total $(SOURCES)


deploy: pdf

.PHONY: help eurocv
help: ## Display this help.
	@awk 'BEGIN {FS = ":.*##"; printf "\n${COLOR_GREY}Usage:${COLOR_RESET}\n  make ${COLOR_CYAN}<target>${COLOR_RESET}\n"} \
	/^[a-zA-Z_0-9-]+:.*?##/ { printf "  ${COLOR_CYAN}%-15s${COLOR_RESET} %s\n", $$1, $$2 } \
	/^##@/{ printf "\n${COLOR_GREY}%s${COLOR_RESET}\n", substr($$0, 5) } \
	END { printf "\nCreated by (c)2024 mirek\n" } \
	' $(MAKEFILE_LIST)
