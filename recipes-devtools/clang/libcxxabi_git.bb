# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
LICENSE = "MIT & UIUC"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TARGET_ARCH}"

require clang.inc

inherit cmake

DEPENDS += "compiler-rt"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=9041c9f38eb0f718f408e28ce138bb9a; \
                   "
SRC_URI = "\
	   git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/libcxx.git;branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           git://github.com/llvm-mirror/libcxxabi.git;branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
          "

SRCREV_FORMAT = "llvm_libcxx_libcxxabi"

S = "${WORKDIR}/git/projects/libcxxabi"

THUMB_TUNE_CCARGS = ""
TUNE_CCARGS += "-ffreestanding -nostdlib"

EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/../libcxx -DLLVM_PATH=${S}/../../ -DLIBCXXABI_LIBCXX_INCLUDES=${S}/../libcxx/include -DLLVM_ENABLE_LIBCXX=True -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True -DLIBCXXABI_ENABLE_SHARED=False"

BBCLASSEXTEND = "native nativesdk"
