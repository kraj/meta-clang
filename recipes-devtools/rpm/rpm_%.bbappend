FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_append_toolchain-clang = " openmp"
