# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libunwind implements a stack unwinder, needed to perform C++ exception handling."
HOMEPAGE = "http://llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc
require common.inc

inherit cmake
PV .= "+git${SRCPV}"

DEPENDS += "libcxx ninja-native"
BASEDEPENDS_remove_toolchain-clang_class-target = "llvm-libunwind"
BASEDEPENDS_remove_toolchain-clang_class-target = "compiler-rt"
PROVIDES += "libunwind"

LIC_FILES_CHKSUM = "file://projects/libcxx/LICENSE.TXT;md5=7b3a0e1b99822669d630011defe9bfd9; \
"
SRC_URI = "\
    ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
    ${LLVM_GIT}/libcxx.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
    ${LLVM_GIT}/libcxxabi.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=cxxabi;destsuffix=git/projects/libcxxabi \
    ${LLVM_GIT}/libunwind.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libunwind;destsuffix=git/projects/libunwind \
    ${LLVMPATCHES} \
    ${LIBCXXPATCHES} \
    ${LIBCXXABIPATCHES} \
"

SRCREV_FORMAT = "llvm_libcxx_cxxabi_libunwind"

S = "${WORKDIR}/git"

COMPATIBLE_HOST_mips = "null"
COMPATIBLE_HOST_mipsel = "null"
COMPATIBLE_HOST_mips64 = "null"
COMPATIBLE_HOST_mips64el = "null"

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/projects/libcxxabi \
                  -DLLVM_PATH=${S} \
                  -DLLVM_ENABLE_LIBCXX=True \
                  -DLLVM_ENABLE_LIBCXXABI=True \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
                  -DLIBUNWIND_ENABLE_SHARED=ON \
                  -DUNIX=True \
                  -G Ninja \
                  ${S}/projects/libunwind \
"
do_configure_prepend () {
	(cd ${S}/projects/libunwind/include && ln -sf ../../libcxxabi/include/__cxxabi_config.h)
}

do_compile() {
	NINJA_STATUS="[%p] " ninja ${PARALLEL_MAKE}
}

do_install() {
	NINJA_STATUS="[%p] " DESTDIR=${D} ninja ${PARALLEL_MAKE} install
}

ALLOW_EMPTY_${PN} = "1"

RPROVIDES_${PN} = "libunwind"

BBCLASSEXTEND = "native nativesdk"
