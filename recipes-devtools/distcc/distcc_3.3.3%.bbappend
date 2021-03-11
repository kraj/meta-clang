FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append = "\
    file://distcc-3.3.3-clang-12.patch \
"
