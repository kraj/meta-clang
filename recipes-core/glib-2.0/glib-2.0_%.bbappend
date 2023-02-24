FILESEXTRAPATHS:prepend := "${THISDIR}/${BPN}:"

SRC_URI:append:toolchain-clang = " file://0001-Ignore-clang-warning-for-function-signature-match.patch"

CFLAGS:append:libc-musl = " -Wno-format-nonliteral"
