# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc
require common.inc

inherit cmake pythonnative
PV .= "+git${SRCPV}"

DEPENDS += "ninja-native"
BASEDEPENDS_remove_toolchain-clang_class-target = "libcxx"
BASEDEPENDS_remove_toolchain-clang_class-target = "llvm-libunwind"
#BASEDEPENDS_remove_toolchain-clang_class-target = "compiler-rt"


LIC_FILES_CHKSUM = "file://projects/libcxx/LICENSE.TXT;md5=7b3a0e1b99822669d630011defe9bfd9; \
"
SRC_URI = "\
    ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
    ${LLVM_GIT}/libcxx.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
    ${LLVM_GIT}/libcxxabi.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=cxxabi;destsuffix=git/projects/libcxxabi \
    ${LLVMPATCHES} \
    ${LIBCXXPATCHES} \
    ${LIBCXXABIPATCHES} \
"

SRCREV_FORMAT = "llvm_libcxx_cxxabi"

S = "${WORKDIR}/git"

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

EXTRA_OECMAKE += "\
                  -DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXX_ENABLE_SHARED=ON \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=ON \
                  -DCXX_SUPPORTS_CXX11=ON \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/projects/libcxx/include \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/projects/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/lib \
                  -G Ninja \
                  ${S} \
"

EXTRA_OECMAKE_append_libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "

do_compile() {
	NINJA_STATUS="[%p] " ninja -v ${PARALLEL_MAKE} cxxabi cxx
}

do_install() {
	NINJA_STATUS="[%p] " DESTDIR=${D} ninja ${PARALLEL_MAKE} install-cxxabi install-cxx
}

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native nativesdk"
TOOLCHAIN = "clang"
