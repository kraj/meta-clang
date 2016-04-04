# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxxabi.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TARGET_ARCH}"

require clang.inc

inherit cmake
PV .= "+git${SRCPV}"

DEPENDS += "compiler-rt"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=9041c9f38eb0f718f408e28ce138bb9a; \
                   "
SRC_URI = "\
           ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
           ${LLVM_GIT}/libcxx.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           ${LLVM_GIT}/libcxxabi.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
           file://0001-Do-not-use-LIBCXXABI_ARM_EHABI.patch \
"

SRCREV_FORMAT = "llvm_libcxx_libcxxabi"

S = "${WORKDIR}/git/projects/libcxxabi"

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-ffreestanding -nostdlib"
TUNE_CCARGS += "-nostdlib"
EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/../libcxx \
                  -DLLVM_PATH=${S}/../../ \
                  -DLLVM_ENABLE_LIBCXX=True \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/../libcxx/include \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
                  -DLIBCXXABI_ENABLE_SHARED=False \
"
CXXFLAGS_append_libc-musl = " -D_LIBCPP_HAS_MUSL_LIBC "

BBCLASSEXTEND = "native nativesdk"
