# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
SECTION = "devel"

require clang.inc

BRANCH ?= "release_37"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=4c0bc17c954e99fd547528d938832bfa; \
                    file://tools/clang/LICENSE.TXT;md5=82ed8fe1976ca709bbd81f4f10a48ccd; \
                   "
SRC_URI = "git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/clang.git;branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
	   file://0001-Remove-CMAKE_CROSSCOMPILING-so-it-can-cross-compile.patch \
          "

SRCREV_llvm = "937d48bea8f01b02d5f3db05a9e58dfb976e9d3b"
SRCREV_clang = "0c0116f5826045008e6627967abe0e9320700e2b"

SRCREV_FORMAT = "llvm_clang"

INHIBIT_DEFAULT_DEPS = "1"

S = "${WORKDIR}/git"

inherit perlnative pythonnative cmake

def get_clang_target_arch(bb, d):
    target_arch = d.getVar('TRANSLATED_TARGET_ARCH', True)
    clang_arches = {
        "i586"     : "X86",
        "x86-64"   : "X86",
        "powerpc"  : "PowerPC",
        "mips"     : "Mips",
        "arm"      : "ARM",
        "arm64"    : "AArch64",
        "aarch64"  : "AArch64",
    }

    if target_arch in clang_arches:
        return clang_arches[target_arch]
    return ""

#TUNE_CCARGS_remove = "-mthumb-interwork"
#TUNE_CCARGS_remove = "-march=armv7-a"
#TUNE_CCARGS_remove = "-marm"
TUNE_CCARGS_append_class-target = " -D__extern_always_inline=inline -L${PKG_CONFIG_SYSROOT_DIR}${libdir}/libxml2 -I${PKG_CONFIG_SYSROOT_DIR}${includedir}/libxml2 "

EXTRA_OECMAKE="-DLLVM_ENABLE_RTTI:BOOL=True \
               -DLLVM_ENABLE_FFI:BOOL=False \
               -DCMAKE_SYSTEM_NAME=Linux \
               -DCMAKE_BUILD_TYPE:STRING=Release \
	       -DLLVM_BUILD_EXTERNAL_COMPILER_RT:BOOL=True \
	      "

EXTRA_OECMAKE_append_class-native = "\
               -DLLVM_TARGETS_TO_BUILD:STRING='AArch64;ARM;Mips;PowerPC;X86' \
"
EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TARGETS_TO_BUILD:STRING='AArch64;ARM;Mips;PowerPC;X86' \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"
EXTRA_OECMAKE_append_class-target = "\
               -DLLVM_ENABLE_PIC=False \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
               -DLLVM_TARGETS_TO_BUILD:STRING='${@get_clang_target_arch(bb, d)}' \
               -DLLVM_TARGET_ARCH:STRING='${@get_clang_target_arch(bb, d)}' \
"
#               -DCMAKE_CXX_FLAGS='-target armv7a -ccc-gcc-name ${HOST_PREFIX}g++ ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -v -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0 -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0/arm-rdk-linux-gnueabi' \
#               -DCMAKE_C_FLAGS='-target armv7a -ccc-gcc-name ${HOST_PREFIX}gcc ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -v -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0 -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0/arm-rdk-linux-gnueabi' \
#
#
EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"

DEPENDS = "zlib libffi libxml2 binutils"
DEPENDS_remove_class-nativesdk = "nativesdk-binutils"
DEPENDS_append_class-nativesdk = " clang-native "

do_configure_prepend() {
        # Remove RPATHs
        sed -i 's:$(RPATH) -Wl,$(\(ToolDir\|LibDir\|ExmplDir\))::g' ${S}/Makefile.rules
        # Drop "svn" suffix from version string
        sed -i 's/${PV}svn/${PV}/g' ${S}/configure

        # Fix paths in llvm-config
        sed -i "s|sys::path::parent_path(CurrentPath))\.str()|sys::path::parent_path(sys::path::parent_path(CurrentPath))).str()|g" ${S}/tools/llvm-config/llvm-config.cpp
        sed -ri "s#/(bin|include|lib)(/?\")#/\1/${LLVM_DIR}\2#g" ${S}/tools/llvm-config/llvm-config.cpp
}

do_compile_prepend_class-native () {
	oe_runmake LLVM-tablegen-host
	oe_runmake CLANG-tablegen-host
}

do_install_append_class-native () {
	install -Dm 0755 ${B}/NATIVE/bin/clang-tblgen ${D}${bindir}/clang-tblgen
        for f in `find ${D}${bindir} -executable -type f -not -type l`; do
            test -n "`file $f|grep -i ELF`" && ${STRIP} $f
        done
}

do_install_append_class-nativesdk () {
	install -Dm 0755 ${B}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
        for f in `find ${D}${bindir} -executable -type f -not -type l`; do
            test -n "`file $f|grep -i ELF`" && ${STRIP} $f
        done
	rm -rf ${D}${datadir}/llvm/cmake
}
PACKAGE_DEBUG_SPLIT_STYLE_class-nativesdk = "debug-without-src"

BBCLASSEXTEND = "native nativesdk"
