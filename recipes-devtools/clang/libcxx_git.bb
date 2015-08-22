# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
LICENSE = "MIT & UIUC"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TRANSLATED_TARGET_ARCH}"

require clang.inc

inherit cmake

DEPENDS += "libcxxabi"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=149d2e8e8d99e3a2d702997b5f919fd9; \
                   "
SRC_URI = "\
	   git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/libcxx.git;branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           git://github.com/llvm-mirror/libcxxabi.git;branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
          "

SRCREV_FORMAT = "llvm_libcxx_libcxxabi"

S = "${WORKDIR}/git/projects/libcxx"

THUMB_TUNE_CCARGS = " -ffreestanding -nostdlib -nostdinc++ -nobuiltininc"

EXTRA_OECMAKE += "-DLIBCXX_CXX_ABI=libcxxabi -DLIBCXXABI_LIBCXX_PATH=${S}/../libcxx -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/../libcxxabi/include -DLLVM_PATH=${S}/../../"

BBCLASSEXTEND = "native nativesdk"
