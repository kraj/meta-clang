SUMMARY = "Include What You Use (IWYU) - Clang based checker for C/C++ header includes"
DESCRIPTION = "For every symbol (type, function, variable, or macro) that you \
               use in foo.cc (or foo.cpp), either foo.cc or foo.h should \
               include a .h file that exports the declaration of that symbol."
HOMEPAGE = "https://include-what-you-use.org"
BUGTRACKER = "https://github.com/include-what-you-use/include-what-you-use/issues"
LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=59d01ad98720f3c50d6a8a0ef3108c88 \
                    file://iwyu-check-license-header.py;md5=7bdb749831163fbe9232b3cb7186116f"

DEPENDS = "clang"

SRC_URI = "git://github.com/include-what-you-use/include-what-you-use.git;protocol=https;branch=clang_18"
SRCREV = "377eaef70cdda47368939f4d9beabfabe3f628f0"

S = "${WORKDIR}/git"

inherit cmake python3native

FILES:${PN} += "${datadir}/${BPN}"

BBCLASSEXTEND = "nativesdk"
