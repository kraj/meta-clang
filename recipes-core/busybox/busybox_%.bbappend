FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"
SRC_URI_append_toolchain-clang = " file://0001-writing-to-a-const-variable-is-undefined-behavior-C9.patch"
