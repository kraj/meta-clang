FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

RDEPENDS:packagegroup-core-buildessential:append:toolchain-clang = " clang "
