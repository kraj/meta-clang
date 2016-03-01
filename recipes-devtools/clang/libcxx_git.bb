# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
LICENSE = "MIT & NCSA"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TARGET_ARCH}"

require clang.inc

inherit cmake
PV .= "+git${SRCPV}"

DEPENDS += "libcxxabi"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=149d2e8e8d99e3a2d702997b5f919fd9; \
                   "
SRC_URI = "\
           ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
           ${LLVM_GIT}/libcxx.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           ${LLVM_GIT}/libcxxabi.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
          "

SRCREV_FORMAT = "llvm_libcxx_libcxxabi"

S = "${WORKDIR}/git/projects/libcxx"

THUMB_TUNE_CCARGS = ""
TUNE_CCARGS += "-ffreestanding -nostdlib"

EXTRA_OECMAKE += "-DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXXABI_LIBCXX_PATH=${S}/../libcxx \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/../libcxxabi/include \
                  -DLLVM_PATH=${S}/../../ \
                  -DLIBCXX_ENABLE_SHARED=False \
                 "

BBCLASSEXTEND = "native nativesdk"
