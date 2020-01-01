OPTIONS = --filter=pandoc-numbering
OPTIONS += --filter=pandoc-crossref
OPTIONS += --toc

PDFOPTIONS = --highlight-style my_highlight.theme
PDFOPTIONS += --pdf-engine pdflatex
PDFOPTIONS += --number-sections
PDFOPTIONS += --template=./default.latex

report.pdf: report.md *.theme img/*
	pandoc -s $(OPTIONS) $(PDFOPTIONS) -o $@ $<

docker:
	docker build -t pandoc-augmented .
	docker run -it --rm --user $$(id -u):$$(id -g) -v "$$PWD":/docs pandoc-augmented bash

clean:
	rm -f *.pdf *.html
