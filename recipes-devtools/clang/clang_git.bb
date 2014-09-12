# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
SECTION = "devel"

require clang.inc

LLVM_RELEASE = "3.5"
LLVM_DIR = "llvm${LLVM_RELEASE}"

DEPENDS = "zlib libffi libxml2-native llvm-common"

BRANCH ?= "master"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=47e311aa9caedd1b3abf098bd7814d1d; \
                    file://tools/clang/LICENSE.TXT;md5=3954ab76dfb9ce9024cdce4c24268267; \
                    file://projects/compiler-rt/LICENSE.TXT;md5=1ee2b380c3e34d2dd756b922ab4f8b6c; \
                   "
SRC_URI = "git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/clang.git;branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
           git://github.com/llvm-mirror/compiler-rt.git;branch=${BRANCH};destsuffix=git/projects/compiler-rt;name=compiler-rt \
          "

SRCREV_llvm = "${AUTOREV}"
SRCREV_clang = "${AUTOREV}"
SRCREV_compiler-rt = "${AUTOREV}"


S = "${WORKDIR}/git"

BBCLASSEXTEND = "native nativesdk"
