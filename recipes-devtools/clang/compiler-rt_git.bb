# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
LICENSE = "MIT & NCSA"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "clang-cross-${TARGET_ARCH}"

require clang.inc

PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=27b14ab4ce08d04c3a9a5f0ed7997362; \
                   "
SRC_URI = "${LLVM_GIT}/compiler-rt.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=compiler-rt \
           file://0001-support-a-new-embedded-linux-target.patch \
           file://0001-Simplify-cross-compilation.-Don-t-use-native-compile.patch \
           file://0001-dont-include-stdlib.h-and-limits.h-together.patch \
          "

SRCREV_FORMAT = "compiler-rt"

S = "${WORKDIR}/git"

inherit cmake pkgconfig pythonnative

THUMB_TUNE_CCARGS = ""

EXTRA_OECMAKE += "-DLLVM_BUILD_EXTERNAL_COMPILER_RT=ON \
                  -DCOMPILER_RT_BUILD_SANITIZERS=OFF \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${MULTIMACH_HOST_SYS} \
"

do_install_append () {
	install -d ${D}${base_libdir}
	mv ${D}${libdir}/linux/*.a ${D}${base_libdir}
	rmdir ${D}${libdir}/linux
	rmdir ${D}${libdir}
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
