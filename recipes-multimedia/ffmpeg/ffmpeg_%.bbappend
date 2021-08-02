FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang:mips64 = " file://clang_mips64.patch"
