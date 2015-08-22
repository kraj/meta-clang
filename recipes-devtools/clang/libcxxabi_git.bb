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

BRANCH ?= "release_37"

DEPENDS += "compiler-rt"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=9041c9f38eb0f718f408e28ce138bb9a; \
                   "
SRC_URI = "\
	   git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/libcxx.git;branch=${BRANCH};name=libcxx;destsuffix=git/projects/libcxx \
           git://github.com/llvm-mirror/libcxxabi.git;branch=${BRANCH};name=libcxxabi;destsuffix=git/projects/libcxxabi \
          "

SRCREV_libcxxabi = "8f53d45e9ac7d3c078ab3fe8f91c40a4cc6b579a"
SRCREV_libcxx = "de80a7d886d75188de210eb4d6f0768fdc4d4d0b"
SRCREV_llvm = "937d48bea8f01b02d5f3db05a9e58dfb976e9d3b"

SRCREV_FORMAT = "llvm_libcxx_libcxxabi"

S = "${WORKDIR}/git/projects/libcxxabi"

THUMB_TUNE_CCARGS = " -ffreestanding -nostdlib -nostdinc++ -nobuiltininc"

EXTRA_OECMAKE += "-DLIBCXXABI_LIBCXX_PATH=${S}/../libcxx -DLLVM_PATH=${S}/../../ -DLIBCXXABI_LIBCXX_INCLUDES=${S}/../libcxx/include"

BBCLASSEXTEND = "native nativesdk"
