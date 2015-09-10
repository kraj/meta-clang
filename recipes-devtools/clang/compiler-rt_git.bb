# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
LICENSE = "MIT & UIUC"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TARGET_ARCH}"

require clang.inc

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=27b14ab4ce08d04c3a9a5f0ed7997362; \
                   "
SRC_URI = "git://github.com/llvm-mirror/compiler-rt.git;branch=${BRANCH};name=compiler-rt \
           file://0001-support-a-new-embedded-linux-target.patch \
          "

SRCREV_FORMAT = "compiler-rt"

S = "${WORKDIR}/git"

THUMB_TUNE_CCARGS = ""
TUNE_CCARGS += "-ffreestanding -nostdlib"

EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"

do_configure () {
	:
}

do_compile () {
	cd ${B}
	oe_runmake VERBOSE=1 clang_linux
}

#PROVIDES_append_class-target = "\
#        virtual/${TARGET_PREFIX}compilerlibs \
#        libgcc \
#        libgcc-initial \
#        libgcc-dev \
#        libgcc-initial-dev \
#        "
#
BBCLASSEXTEND = "native nativesdk"
