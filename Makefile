
LATEXMK = /usr/bin/latexmk
OUTPUT = thesis
SOURCES = thesis.tex chapters/*tex

COLOR_CYAN = \033[36m
COLOR_RESET = \033[0m
COLOR_GREY = \033[1m

all: pdf

##@ Development

watch: $(SOURCES)
	@echo "Building PDF"
	@vlna $(SOURCES)
	@$(LATEXMK) -pdf -bibtex -pvc $(OUTPUT)

pdf: $(SOURCES)  ## Build thesis as PDF document
	@echo "Building PDF"
	@vlna $(SOURCES)
	@$(LATEXMK) -pdf -bibtex $^

##@ Misc
clean:  ## Clean
	@echo "Cleanup"
	$(LATEXMK) -C
	@rm -rf chapters/*aux

deploy: pdf

.PHONY: help
help: ## Display this help.
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make ${COLOR_CYAN}<target>${COLOR_RESET}\n"} /^[a-zA-Z_0-9-]+:.*?##/ { printf "  ${COLOR_CYAN}%-15s${COLOR_RESET} %s\n", $$1, $$2 } /^##@/ { printf "\n${COLOR_GREY}%s${COLOR_RESET}\n", substr($$0, 5) } ' $(MAKEFILE_LIST)
