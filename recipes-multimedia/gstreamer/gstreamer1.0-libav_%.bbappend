FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang:mips64 = " file://0001-Disable-fpu-using-code-when-using-clang-mips64-combo.patch"

