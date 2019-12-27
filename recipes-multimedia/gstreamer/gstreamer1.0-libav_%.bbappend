FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

SRC_URI_append_toolchain-clang_mips64 = " file://0001-Disable-fpu-using-code-when-using-clang-mips64-combo.patch"

