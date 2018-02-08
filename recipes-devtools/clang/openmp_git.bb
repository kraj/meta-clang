# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://openmp.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc

DEPENDS += "ninja-native"

RPROVIDES_${PN} += "libgomp"
RPROVIDES_${PN}-dev += "libgomp-dev"

TOOLCHAIN = "clang"

PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.txt;md5=5dcbca021bcb2fbc22186bc7a8a159e6; \
"
SRC_URI =  "\
    ${LLVM_GIT}/openmp.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=openmp \
"

SRCREV_FORMAT = "openmp"

BASEDEPENDS_remove_toolchain-clang_class-target = "compiler-rt"
BASEDEPENDS_remove_toolchain-clang_class-target = "libcxx"

S = "${WORKDIR}/git"

inherit cmake pkgconfig perlnative

EXTRA_OECMAKE = "-G Ninja"

THUMB_TUNE_CCARGS = ""

do_compile() {
	NINJA_STATUS="[%p] " ninja ${PARALLEL_MAKE}
}

do_install() {
	NINJA_STATUS="[%p] " DESTDIR=${D} ninja ${PARALLEL_MAKE} install
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/lib*${SOLIBSDEV}"
INSANE_SKIP_${PN} = "dev-so"
