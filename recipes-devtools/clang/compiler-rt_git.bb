# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc

TOOLCHAIN = "clang"

PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=bf24bca27049b52e9738451aa55771d4; \
"
SRC_URI =  "\
    ${LLVM_GIT}/compiler-rt.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=compiler-rt \
    file://0001-support-a-new-embedded-linux-target.patch \
    file://0002-Simplify-cross-compilation.-Don-t-use-native-compile.patch \
    file://0003-Disable-tsan-on-OE-glibc.patch \
    file://0004-cmake-mips-Do-not-specify-target-with-OE.patch \
    file://0005-Pass-march-armv8.1-a-instead-of-mcrc.patch \
"

SRCREV_FORMAT = "compiler-rt"

BASEDEPENDS_remove_toolchain-clang_class-target = "compiler-rt"
BASEDEPENDS_remove_toolchain-clang_class-target = "libcxx"
TARGET_CXXFLAGS_remove_toolchain-clang = " -stdlib=libc++ "

DEPENDS += "ninja-native"

S = "${WORKDIR}/git"

inherit cmake pkgconfig pythonnative

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"
EXTRA_OECMAKE += "-DCOMPILER_RT_STANDALONE_BUILD=ON \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${HOST_ARCH}${HF}${HOST_VENDOR}-${HOST_OS} \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -G Ninja \
"

EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"

EXTRA_OECMAKE_append_libc-musl = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF -DCOMPILER_RT_BUILD_XRAY=OFF "

do_compile() {
	NINJA_STATUS="[%p] " ninja ${PARALLEL_MAKE}
}

do_install() {
	NINJA_STATUS="[%p] " DESTDIR=${D} ninja ${PARALLEL_MAKE} install
}


do_install_append () {
	install -d ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux
	if [ -d ${D}${libdir}/linux ]; then
		for f in `find ${D}${libdir}/linux -maxdepth 1 -type f`
		do
			mv $f ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux
		done
		rmdir ${D}${libdir}/linux
	fi
	for f in `find ${D}${exec_prefix} -maxdepth 1 -name '*.txt' -type f`
	do
		mv $f ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}
	done
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/lib*${SOLIBSDEV} \
                ${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/*.txt"
FILES_${PN}-staticdev += "${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.a"
FILES_${PN}-dev += "${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.syms"
INSANE_SKIP_${PN} = "dev-so"

#PROVIDES_append_class-target = "\
#        virtual/${TARGET_PREFIX}compilerlibs \
#        libgcc \
#        libgcc-initial \
#        libgcc-dev \
#        libgcc-initial-dev \
#        "
#

BBCLASSEXTEND = "native nativesdk"

ALLOW_EMPTY_${PN} = "1"
