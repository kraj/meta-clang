# Copyright (C) 2021 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pkgconfig python3native


LIC_FILES_CHKSUM = "file://compiler-rt/LICENSE.TXT;md5=d846d1d65baf322d4c485d6ee54e877a"

TUNE_CCARGS:remove = "-no-integrated-as"

DEPENDS += "ninja-native virtual/crypt"
DEPENDS:append:class-native = " clang-native libxcrypt-native"
DEPENDS:append:class-nativesdk = " clang-native nativesdk-libxcrypt"

PACKAGECONFIG ??= ""
PACKAGECONFIG[crt] = "-DCOMPILER_RT_BUILD_CRT:BOOL=ON,-DCOMPILER_RT_BUILD_CRT:BOOL=OFF"
PACKAGECONFIG[static-libcxx] = "-DSANITIZER_USE_STATIC_CXX_ABI=ON -DSANITIZER_USE_STATIC_LLVM_UNWINDER=ON,,"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"

OECMAKE_TARGET_COMPILE = "compiler-rt"
OECMAKE_TARGET_INSTALL = "install-compiler-rt install-compiler-rt-headers"
OECMAKE_SOURCEPATH = "${S}/llvm"
EXTRA_OECMAKE += "-DCOMPILER_RT_STANDALONE_BUILD=OFF \
                  -DCOMPILER_RT_DEFAULT_TARGET_TRIPLE=${HOST_ARCH}${HF}${HOST_VENDOR}-${HOST_OS} \
                  -DCOMPILER_RT_BUILD_BUILTINS=OFF \
                  -DSANITIZER_CXX_ABI_LIBNAME=${@bb.utils.contains("RUNTIME", "llvm", "libc++", "libstdc++", d)} \
                  -DCOMPILER_RT_BUILD_XRAY=ON \
                  -DCOMPILER_RT_BUILD_SANITIZERS=ON \
                  -DCOMPILER_RT_BUILD_LIBFUZZER=ON \
                  -DCOMPILER_RT_BUILD_PROFILE=ON \
                  -DCOMPILER_RT_BUILD_MEMPROF=ON \
                  -DLLVM_ENABLE_PROJECTS='compiler-rt' \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DLLVM_LIBDIR_SUFFIX=${LLVM_LIBDIR_SUFFIX} \
"

EXTRA_OECMAKE:append:class-nativesdk = "\
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"

EXTRA_OECMAKE:append:libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "
EXTRA_OECMAKE:append:powerpc = " -DCOMPILER_RT_DEFAULT_TARGET_ARCH=powerpc "

do_install:append () {
    if [ -n "${LLVM_LIBDIR_SUFFIX}" ]; then
        mkdir -p ${D}${nonarch_libdir}
        mv ${D}${libdir}/clang ${D}${nonarch_libdir}/clang
        rmdir --ignore-fail-on-non-empty ${D}${libdir}
    fi
    # Already shipped with compile-rt Orc support
    rm -rf ${D}${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/libclang_rt.orc-x86_64.a
}

FILES_SOLIBSDEV = ""
FILES:${PN} += "${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/lib*${SOLIBSDEV} \
                ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/*.txt \
                ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/share/*.txt"
FILES:${PN}-staticdev += "${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.a"
FILES:${PN}-dev += "${datadir} ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.syms \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/include \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/clang_rt.crt*.o \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/libclang_rt.asan-preinit*.a \
                   "
INSANE_SKIP:${PN} = "dev-so libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

#PROVIDES:append:class-target = "\
#        virtual/${TARGET_PREFIX}compilerlibs \
#        libgcc \
#        libgcc-initial \
#        libgcc-dev \
#        libgcc-initial-dev \
#        "
#

RDEPENDS:${PN}-dev += "${PN}-staticdev"

BBCLASSEXTEND = "native nativesdk"

ALLOW_EMPTY:${PN} = "1"
ALLOW_EMPTY:${PN}-dev = "1"

TOOLCHAIN:forcevariable = "clang"
SYSROOT_DIRS:append:class-target = " ${nonarch_libdir}"

# riscv and x86_64 Sanitizers work on musl too
COMPATIBLE_HOST:libc-musl:x86-64 = "(.*)"
COMPATIBLE_HOST:libc-musl:riscv64 = "(.*)"
COMPATIBLE_HOST:libc-musl:riscv32 = "(.*)"
COMPATIBLE_HOST:libc-musl = "null"
