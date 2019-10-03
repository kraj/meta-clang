# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pkgconfig pythonnative


LIC_FILES_CHKSUM = "file://compiler-rt/LICENSE.TXT;md5=d846d1d65baf322d4c485d6ee54e877a"

LIBCPLUSPLUS = ""
COMPILER_RT = ""
TUNE_CCARGS_remove = "-no-integrated-as"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "ninja-native clang-cross-${TARGET_ARCH} virtual/${MLPREFIX}libc virtual/${TARGET_PREFIX}compilerlibs"
DEPENDS_append_class-nativesdk = " clang-native"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"
EXTRA_OECMAKE += "-DCOMPILER_RT_STANDALONE_BUILD=OFF \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${HOST_ARCH}${HF}${HOST_VENDOR}-${HOST_OS} \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -DLLVM_ENABLE_PROJECTS='compiler-rt' \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -G Ninja ${S}/llvm \
"

EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"

EXTRA_OECMAKE_append_libc-musl = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF "
CXXFLAGS_append_libc-musl = " -D_LIBCPP_HAS_MUSL_LIBC=ON "
EXTRA_OECMAKE_append_mipsarch = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF "
EXTRA_OECMAKE_append_powerpc = " -DCOMPILER_RT_DEFAULT_TARGET_ARCH=powerpc "

do_compile() {
	ninja ${PARALLEL_MAKE} compiler-rt
}

do_install() {
	DESTDIR=${D} ninja ${PARALLEL_MAKE} install-compiler-rt
}


do_install_append () {
	if [ -d ${D}${exec_prefix}/lib/linux ]; then
		for f in `find ${D}${exec_prefix}/lib/linux -maxdepth 1 -type f`
		do
			install -D -m 0644 $f ${D}${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/`basename $f`
			rm $f
		done
		rmdir ${D}${exec_prefix}/lib/linux
	fi
	for f in `find ${D}${exec_prefix} -maxdepth 1 -name '*.txt' -type f`
	do
		install -D -m 0644  $f ${D}${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/`basename $f`
		rm $f
	done
        rm -rf ${D}${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/clang_rt.crt*.o
}

sysroot_stage_all_append_class-target() {
        sysroot_stage_dir ${D}${exec_prefix}/lib ${SYSROOT_DESTDIR}${exec_prefix}/lib
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/lib*${SOLIBSDEV} \
                ${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/*.txt \
                ${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/share/*.txt"
FILES_${PN}-staticdev += "${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.a"
FILES_${PN}-dev += "${datadir} ${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.syms \
                    ${exec_prefix}/lib/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/clang_rt.crt*.o \
                   "
INSANE_SKIP_${PN} = "dev-so libdir"

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

TOOLCHAIN_forcevariable = "clang"
