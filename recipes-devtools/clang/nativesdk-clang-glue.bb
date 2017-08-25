# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "SDK Cross compiler wrappers for LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/NCSA;md5=1b5fdec70ee13ad8a91667f16c1959d7"
SECTION = "devel"

require clang.inc
inherit nativesdk
DEPENDS += "nativesdk-clang"

S = "${WORKDIR}"

do_install() {
    install -d ${D}${prefix_nativesdk}
    cd ${D}${prefix_nativesdk}
    ln -s ..${libdir} .
    ln -s ..${includedir} .
}

sysroot_stage_all () {
	sysroot_stage_dir ${D} ${SYSROOT_DESTDIR}
}

FILES_${PN} += "${prefix_nativesdk}"
