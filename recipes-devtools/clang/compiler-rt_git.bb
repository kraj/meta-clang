# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"
INHIBIT_DEFAULT_DEPS = "1"

require clang.inc

# libgcc gcc-runtime needed during configuring compiler-rt
DEPENDS += "clang-cross-${TARGET_ARCH} virtual/${TARGET_PREFIX}libc-for-gcc libgcc gcc-runtime"

PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=bf24bca27049b52e9738451aa55771d4; \
"
SRC_URI =  "${LLVM_GIT}/compiler-rt.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=compiler-rt \
            file://0001-support-a-new-embedded-linux-target.patch \
            file://0002-Simplify-cross-compilation.-Don-t-use-native-compile.patch \
            file://0003-Disable-tsan-on-OE-glibc.patch \
            file://0004-cmake-mips-Do-not-specify-target-with-OE.patch \
"

SRCREV_FORMAT = "compiler-rt"

S = "${WORKDIR}/git"

inherit cmake pkgconfig pythonnative

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"
EXTRA_OECMAKE += "-DCOMPILER_RT_STANDALONE_BUILD=ON \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${HOST_ARCH}${HF}${HOST_VENDOR}-${HOST_OS} \
"

EXTRA_OECMAKE_append_libc-musl = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF -DCOMPILER_RT_BUILD_XRAY=OFF "

do_install_append () {
	install -d ${D}${libdir}/clang/${BASEPV}/lib/linux
	if [ -d ${D}${libdir}/linux ]; then
		for f in `find ${D}${libdir}/linux -maxdepth 1 -type f`
		do
			mv $f ${D}${libdir}/clang/${BASEPV}/lib/linux
		done
		rmdir ${D}${libdir}/linux
	fi
	for f in `find ${D}${exec_prefix} -maxdepth 1 -name '*.txt' -type f`
	do
		mv $f ${D}${libdir}/clang/${BASEPV}
	done
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/clang/${BASEPV}/lib/linux/lib*${SOLIBSDEV} ${libdir}/clang/${BASEPV}/*.txt"
FILES_${PN}-staticdev += "${libdir}/clang/${BASEPV}/lib/linux/*.a"
INSANE_SKIP_${PN} = "dev-so"

#PROVIDES_append_class-target = "\
#        virtual/${TARGET_PREFIX}compilerlibs \
#        libgcc \
#        libgcc-initial \
#        libgcc-dev \
#        libgcc-initial-dev \
#        "
#

FILES_${PN}-dev += "${libdir}/clang/${BASEPV}/lib/linux/*.syms"

BBCLASSEXTEND = "native nativesdk"

ALLOW_EMPTY_${PN} = "1"
