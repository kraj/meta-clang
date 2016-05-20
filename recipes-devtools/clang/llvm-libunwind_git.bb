# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libunwind implements a stack unwinder, needed to perform C++ exception handling."
HOMEPAGE = "http://llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TARGET_ARCH}"

require clang.inc

inherit cmake
PV .= "+git${SRCPV}"

DEPENDS += "libcxx"
PROVIDES += "libunwind"

LIC_FILES_CHKSUM = "file://../libcxx/LICENSE.TXT;md5=149d2e8e8d99e3a2d702997b5f919fd9; \
                   "
SRC_URI = "\
           ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
           ${LLVM_GIT}/libcxx.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           ${LLVM_GIT}/libcxxabi.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
           ${LLVM_GIT}/libunwind.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=libunwind;destsuffix=git/projects/libunwind \
          "

SRCREV_FORMAT = "llvm_libcxx_libcxxabi_libunwind"

S = "${WORKDIR}/git/projects/libunwind"

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/../libcxxabi \
                  -DLLVM_PATH=${S}/../../ \
                  -DLLVM_ENABLE_LIBCXX=True \
                  -DLLVM_ENABLE_LIBCXXABI=True \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
                  -DLIBUNWIND_ENABLE_SHARED=ON \
                  -DUNIX=True \
                 "
do_configure_prepend () {
	(cd ${S}/include && ln -sf ../../libcxxabi/include/__cxxabi_config.h)
}

ALLOW_EMPTY_${PN} = "1"

RPROVIDES_${PN} = "libunwind"

BBCLASSEXTEND = "native nativesdk"
