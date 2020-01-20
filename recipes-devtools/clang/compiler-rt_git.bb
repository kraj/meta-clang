# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pkgconfig python3native


LIC_FILES_CHKSUM = "file://compiler-rt/LICENSE.TXT;md5=d846d1d65baf322d4c485d6ee54e877a"

LIBCPLUSPLUS = ""
COMPILER_RT = ""
TUNE_CCARGS_remove = "-no-integrated-as"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "ninja-native clang-cross-${TARGET_ARCH} virtual/${MLPREFIX}libc virtual/${TARGET_PREFIX}compilerlibs virtual/crypt"
DEPENDS_append_class-nativesdk = " clang-native"

PACKAGECONFIG ??= ""
PACKAGECONFIG[crt] = "-DCOMPILER_RT_BUILD_CRT:BOOL=ON,-DCOMPILER_RT_BUILD_CRT:BOOL=OFF"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"

OECMAKE_TARGET_COMPILE = "compiler-rt"
OECMAKE_TARGET_INSTALL = "install-compiler-rt install-compiler-rt-headers"
OECMAKE_SOURCEPATH = "${S}/llvm"
EXTRA_OECMAKE += "-DCOMPILER_RT_STANDALONE_BUILD=OFF \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${HOST_ARCH}${HF}${HOST_VENDOR}-${HOST_OS} \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -DLLVM_ENABLE_PROJECTS='compiler-rt' \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DLLVM_LIBDIR_SUFFIX=${LLVM_LIBDIR_SUFFIX} \
"

EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"

EXTRA_OECMAKE_append_libc-musl = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF "
CXXFLAGS_append_libc-musl = " -D_LIBCPP_HAS_MUSL_LIBC=ON "
EXTRA_OECMAKE_append_mipsarch = " -DCOMPILER_RT_BUILD_SANITIZERS=OFF "
EXTRA_OECMAKE_append_powerpc = " -DCOMPILER_RT_DEFAULT_TARGET_ARCH=powerpc "

do_install_append () {
    if [ -n "${LLVM_LIBDIR_SUFFIX}" ]; then
        mkdir -p ${D}${nonarch_libdir}
        mv ${D}${libdir}/clang ${D}${nonarch_libdir}/clang
        rmdir --ignore-fail-on-non-empty ${D}${libdir}
    fi
}

FILES_SOLIBSDEV = ""
FILES_${PN} += "${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/lib*${SOLIBSDEV} \
                ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/*.txt \
                ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/share/*.txt"
FILES_${PN}-staticdev += "${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.a"
FILES_${PN}-dev += "${datadir} ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.syms \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/include \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/clang_rt.crt*.o \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/libclang_rt.asan-preinit*.a \
                   "
INSANE_SKIP_${PN} = "dev-so libdir"
INSANE_SKIP_${PN}-dbg = "libdir"

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
SYSROOT_DIRS_append_class-target = " ${nonarch_libdir}"
