FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

RDEPENDS_packagegroup-core-buildessential_append_toolchain-clang = " clang "
