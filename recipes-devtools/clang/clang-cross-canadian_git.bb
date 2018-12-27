# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Clang/LLVM based C/C++ compiler (cross-canadian for ${TARGET_ARCH} target)"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/NCSA;md5=1b5fdec70ee13ad8a91667f16c1959d7"
SECTION = "devel"

PN = "clang-cross-canadian-${TRANSLATED_TARGET_ARCH}"

require clang.inc
require common-source.inc
inherit cross-canadian

DEPENDS += "nativesdk-clang binutils-cross-canadian-${TRANSLATED_TARGET_ARCH} virtual/${HOST_PREFIX}binutils-crosssdk virtual/nativesdk-libc"
# We have to point gcc at a sysroot but we don't need to rebuild if this changes
# e.g. we switch between different machines with different tunes.
EXTRA_OECONF_PATHS[vardepsexclude] = "TUNE_PKGARCH"
TARGET_ARCH[vardepsexclude] = "TUNE_ARCH"

do_install() {
        install -d  ${D}${bindir}
        ln -sf ../clang ${D}${bindir}/${TARGET_PREFIX}clang
        ln -sf ../clang++ ${D}${bindir}/${TARGET_PREFIX}clang++
        ln -sf ../llvm-profdata ${D}${bindir}/${TARGET_PREFIX}llvm-profdata
        cross_canadian_bindirlinks
}

SSTATE_SCAN_FILES += "*-clang *-clang++ *-llvm-profdata"
