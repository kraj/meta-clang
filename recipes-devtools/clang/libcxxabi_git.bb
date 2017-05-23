# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxxabi.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc
require common.inc

TOOLCHAIN = "clang"

inherit cmake
PV .= "+git${SRCPV}"

DEPENDS += "compiler-rt"
BASEDEPENDS_remove_toolchain-clang_class-target = "libcxx"
BASEDEPENDS_remove_toolchain-clang_class-target = "llvm-libunwind"

LIC_FILES_CHKSUM = "file://projects/libcxxabi/LICENSE.TXT;md5=8ae94dd6195890583eee15a988b6ea79; \
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
EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/projects/libcxx \
                  -DLLVM_PATH=${S} \
                  -DLLVM_ENABLE_LIBCXX=OFF \
                  -DLIBCXXABI_ENABLE_EXCEPTIONS=ON \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/projects/libcxx/include \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
                  -DCXX_SUPPORTS_CXX11=ON \
                  -DLIBCXXABI_ENABLE_SHARED=ON \
                  ${S}/projects/libcxxabi \
"
CXXFLAGS_append_libc-musl = " -D_LIBCPP_HAS_MUSL_LIBC "

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native nativesdk"
