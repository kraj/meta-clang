# Copyright (C) 2017 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://openmp.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc
require common-source.inc

DEPENDS += "ninja-native"

RPROVIDES_${PN} += "libgomp"
RPROVIDES_${PN}-dev += "libgomp-dev"

TOOLCHAIN = "clang"

LIC_FILES_CHKSUM = "file://openmp/LICENSE.txt;md5=b1dcbf2c86cbf9bdc0b7cea88a543010"

inherit cmake pkgconfig perlnative

EXTRA_OECMAKE = "-G Ninja ${S}/openmp"

THUMB_TUNE_CCARGS = ""

do_compile() {
	ninja ${PARALLEL_MAKE}
}

do_install() {
	DESTDIR=${D} ninja ${PARALLEL_MAKE} install
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/lib*${SOLIBSDEV}"
INSANE_SKIP_${PN} = "dev-so"
