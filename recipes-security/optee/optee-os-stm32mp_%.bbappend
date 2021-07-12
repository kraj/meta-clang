FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_toolchain-clang = " file://optee-fix-build-with-clang.patch"

EXTRA_OEMAKE_append_toolchain-clang = "COMPILER=clang"

DEPENDS_append_toolchain-clang = "compiler-rt"
