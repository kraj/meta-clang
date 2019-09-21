# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pkgconfig pythonnative


LIC_FILES_CHKSUM = "file://compiler-rt/LICENSE.TXT;md5=92bfbe70fc44c6e5efc6403a31180ed7; \
"

BASEDEPENDS_remove_toolchain-clang_class-target = "compiler-rt"
BASEDEPENDS_remove_toolchain-clang_class-target = "libcxx"
TARGET_CXXFLAGS_remove_toolchain-clang = " -stdlib=libc++ "
TUNE_CCARGS_remove = "-no-integrated-as --rtlib=compiler-rt"
DEPENDS += "ninja-native"
DEPENDS_append_class-nativesdk = " clang-native"

THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"
EXTRA_OECMAKE += "-DCOMPILER_RT_STANDALONE_BUILD=ON \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${HOST_ARCH}${HF}${HOST_VENDOR}-${HOST_OS} \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -G Ninja ${S}/compiler-rt \
"

EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"

EXTRA_OECMAKE_append_libc-musl = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF -DCOMPILER_RT_BUILD_XRAY=OFF "
EXTRA_OECMAKE_append_mipsarch = "-DCOMPILER_RT_BUILD_SANITIZERS=OFF -DCOMPILER_RT_BUILD_XRAY=OFF "

do_compile() {
	ninja ${PARALLEL_MAKE}
}

do_install() {
	DESTDIR=${D} ninja ${PARALLEL_MAKE} install
}


do_install_append () {
	if [ -d ${D}${libdir}/linux ]; then
		for f in `find ${D}${libdir}/linux -maxdepth 1 -type f`
		do
			install -D -m 0644 $f ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/`basename $f`
			rm $f
		done
		rmdir ${D}${libdir}/linux
	fi
	for f in `find ${D}${exec_prefix} -maxdepth 1 -name '*.txt' -type f`
	do
		install -D -m 0644  $f ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/`basename $f`
		rm $f
	done
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/lib*${SOLIBSDEV} \
                ${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/*.txt"
FILES_${PN}-staticdev += "${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.a"
FILES_${PN}-dev += "${datadir} ${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.syms"
INSANE_SKIP_${PN} = "dev-so"

#PROVIDES_append_class-target = "\
#        virtual/${TARGET_PREFIX}compilerlibs \
#        libgcc \
#        libgcc-initial \
#        libgcc-dev \
#        libgcc-initial-dev \
#        "
#

RDEPENDS_${PN}-dev += "${PN}-staticdev"

BBCLASSEXTEND = "native nativesdk"

ALLOW_EMPTY_${PN} = "1"
ALLOW_EMPTY_${PN}-dev = "1"

TOOLCHAIN = "clang"
