(TeX-add-style-hook
 "story-board"
 (lambda ()
   (TeX-add-to-alist 'LaTeX-provided-class-options
                     '(("article" "11pt")))
   (TeX-add-to-alist 'LaTeX-provided-package-options
                     '(("inputenc" "utf8") ("fontenc" "T1") ("ulem" "normalem") ("geometry" "top=0.5in")))
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperref")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperimage")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "hyperbaseurl")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "nolinkurl")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "url")
   (add-to-list 'LaTeX-verbatim-macros-with-braces-local "path")
   (add-to-list 'LaTeX-verbatim-macros-with-delims-local "path")
   (TeX-run-style-hooks
    "latex2e"
    "article"
    "art11"
    "inputenc"
    "fontenc"
    "graphicx"
    "grffile"
    "longtable"
    "wrapfig"
    "rotating"
    "ulem"
    "amsmath"
    "textcomp"
    "amssymb"
    "capt-of"
    "hyperref"
    "geometry")
   (LaTeX-add-labels
    "sec:orgdc59b1e"
    "sec:org59e6adf"
    "sec:orgb047251"
    "sec:org0f2aca8"
    "sec:org567c974"
    "sec:org280f01d"
    "sec:org2d589b4"
    "sec:org2fd4db9"
    "sec:org1858654"
    "sec:orgc6e9514"
    "sec:orgcfee835"
    "sec:org97fc8c0"
    "sec:org0aaa352"
    "sec:orgd23693b"
    "sec:org0c93c2f"
    "sec:org4e920bb"
    "sec:org4a43c8d"
    "sec:orgd419dc2"
    "sec:org2bbb81c"
    "sec:org0480c74"
    "sec:orgfbb77bd"))
 :latex)

