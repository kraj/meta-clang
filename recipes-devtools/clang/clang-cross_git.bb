# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Cross compiler wrappers for LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/NCSA;md5=1b5fdec70ee13ad8a91667f16c1959d7"
SECTION = "devel"

PN = "clang-cross-${TARGET_ARCH}"

require clang.inc
require common-source.inc
inherit cross
DEPENDS += "clang-native binutils-cross-${TARGET_ARCH}"

do_install() {
        install -d ${D}${bindir}
        ln -sf ../clang ${D}${bindir}/${TARGET_PREFIX}clang
        ln -sf ../clang++ ${D}${bindir}/${TARGET_PREFIX}clang++
        ln -sf ../clang-tidy ${D}${bindir}/${TARGET_PREFIX}clang-tidy
        ln -sf ../llvm-profdata ${D}${bindir}/${TARGET_PREFIX}llvm-profdata
	for tool in llvm-ar llvm-ranlib llvm-nm
	do
		ln -sf ../$tool ${D}${bindir}/${TARGET_PREFIX}$tool
	done
}

SYSROOT_PREPROCESS_FUNCS += "clangcross_sysroot_preprocess"

clangcross_sysroot_preprocess () {
        sysroot_stage_dir ${D}${bindir} ${SYSROOT_DESTDIR}${bindir}
}
SSTATE_SCAN_FILES += "*-clang *-clang++ *-llvm-profdata *-llvm-ar \
                      *-llvm-ranlib *-llvm-nm"
PACKAGES = ""
