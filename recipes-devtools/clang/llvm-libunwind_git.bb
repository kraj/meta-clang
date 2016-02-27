# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libunwind implements a stack unwinder, needed to perform C++ exception handling."
HOMEPAGE = "http://llvm.org/"
LICENSE = "MIT & UIUC"
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
	   git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/libcxx.git;branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           git://github.com/llvm-mirror/libcxxabi.git;branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
           git://github.com/llvm-mirror/libunwind.git;branch=${BRANCH};name=libunwind;destsuffix=git/projects/libunwind \
           file://0001-aarch64-Use-x29-and-x30-for-fp-and-lr-respectively.patch \
          "

SRCREV_FORMAT = "llvm_libcxx_libcxxabi_libunwind"

S = "${WORKDIR}/git/projects/libunwind"

THUMB_TUNE_CCARGS = ""
TUNE_CCARGS += "-ffreestanding -nostdlib"

EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/../libcxxabi \
                  -DLLVM_PATH=${S}/../../ \
                  -DLLVM_ENABLE_LIBCXX=True \
                  -DLLVM_ENABLE_LIBCXXABI=True \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
                  -DLIBCXXABI_ENABLE_SHARED=False \
                  -DUNIX=True \
                 "
do_configure_prepend () {
	(cd ${S}/include && ln -sf ../../libcxxabi/include/__cxxabi_config.h)
}

RPROVIDES_${PN} = "libunwind"

BBCLASSEXTEND = "native nativesdk"
