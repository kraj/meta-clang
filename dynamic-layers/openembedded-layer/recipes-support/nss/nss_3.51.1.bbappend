FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

SRC_URI:append:toolchain-clang = "\
file://0001-Bug-1738028-avoid-a-clang-13-unused-variable-warning.patch \
file://0001-Bug-1661378-pkix-Do-not-use-NULL-where-0-is-needed.patch \
"
