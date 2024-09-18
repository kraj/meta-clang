FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

# tests need compiler on target and they need the compiler which was
# used to build python
RDEPENDS:${PN}-ptest:append:toolchain-clang = " clang"
