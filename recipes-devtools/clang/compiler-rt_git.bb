# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

SUMMARY = "LLVM based C/C++ compiler Runtime"
DESCRIPTIOM = "Simple builtin library that provides an \
				implementation of the low-level target-specific \
				hooks required by code generation and other runtime \
				components"
HOMEPAGE = "http://compiler-rt.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

BPN = "compiler-rt"

inherit cmake cmake-native pkgconfig python3native


LIC_FILES_CHKSUM = "file://compiler-rt/LICENSE.TXT;md5=d846d1d65baf322d4c485d6ee54e877a"

LIBCPLUSPLUS = ""
COMPILER_RT = ""

TUNE_CCARGS:remove = "-no-integrated-as"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "ninja-native libgcc"
DEPENDS:append:class-target = " clang-cross-${TARGET_ARCH} virtual/${MLPREFIX}libc gcc-runtime"
DEPENDS:append:class-nativesdk = " clang-native clang-crosssdk-${SDK_SYS} nativesdk-gcc-runtime"
DEPENDS:append:class-native = " clang-native"

# Trick clang.bbclass into not creating circular dependencies
UNWINDLIB:class-nativesdk:toolchain-clang = "--unwindlib=libgcc"
COMPILER_RT:class-nativesdk:toolchain-clang = "-rtlib=libgcc --unwindlib=libgcc"
LIBCPLUSPLUS:class-nativesdk:toolchain-clang = "-stdlib=libstdc++"
UNWINDLIB:class-native:toolchain-clang = "--unwindlib=libgcc"
COMPILER_RT:class-native:toolchain-clang = "-rtlib=libgcc --unwindlib=libgcc"
LIBCPLUSPLUS:class-native:toolchain-clang = "-stdlib=libstdc++"
UNWINDLIB:class-target:toolchain-clang = "--unwindlib=libgcc"
COMPILER_RT:class-target:toolchain-clang = "-rtlib=libgcc --unwindlib=libgcc"
LIBCPLUSPLUS:class-target:toolchain-clang = "-stdlib=libstdc++"

PACKAGECONFIG ??= ""
PACKAGECONFIG[crt] = "-DCOMPILER_RT_BUILD_CRT:BOOL=ON,-DCOMPILER_RT_BUILD_CRT:BOOL=OFF"
PACKAGECONFIG[profile] ="-DCOMPILER_RT_BUILD_PROFILE=ON,-DCOMPILER_RT_BUILD_PROFILE=OFF"
# Context Profiling, might need to enable 'profile' too
PACKAGECONFIG[ctx-profile] ="-DCOMPILER_RT_BUILD_CTX_PROFILE=ON,-DCOMPILER_RT_BUILD_CTX_PROFILE=OFF"

HF = ""
HF:class-target = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"

OECMAKE_TARGET_COMPILE = "compiler-rt"
OECMAKE_TARGET_INSTALL = "install-compiler-rt install-compiler-rt-headers"
OECMAKE_SOURCEPATH = "${S}/llvm"
EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=RelWithDebInfo \
                  -DLLVM_ENABLE_PER_TARGET_RUNTIME_DIR=OFF \
                  -DCOMPILER_RT_STANDALONE_BUILD=ON \
                  -DCOMPILER_RT_INCLUDE_TESTS=OFF \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -DCOMPILER_RT_BUILD_SANITIZERS=OFF \
                  -DCOMPILER_RT_BUILD_MEMPROF=OFF \
                  -DCOMPILER_RT_BUILD_LIBFUZZER=OFF \
                  -DLLVM_ENABLE_RUNTIMES='compiler-rt' \
                  -DLLVM_LIBDIR_SUFFIX=${LLVM_LIBDIR_SUFFIX} \
                  -DLLVM_APPEND_VC_REV=OFF \
                  -S ${S}/runtimes \
"
EXTRA_OECMAKE:append:class-target = "\
               -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
               -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
               -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
               -DCMAKE_C_COMPILER_TARGET=${HOST_ARCH}${HOST_VENDOR}-${HOST_OS}${HF} \
               -DCOMPILER_RT_DEFAULT_TARGET_ONLY=ON \
               -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
"

EXTRA_OECMAKE:append:class-nativesdk = "\
               -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
               -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
               -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
               -DCMAKE_C_COMPILER_TARGET=${HOST_ARCH}${HOST_VENDOR}-${HOST_OS}${HF} \
               -DCOMPILER_RT_DEFAULT_TARGET_ONLY=ON \
"
EXTRA_OECMAKE:append:powerpc = " -DCOMPILER_RT_DEFAULT_TARGET_ARCH=powerpc "

do_install:append () {
    if [ -n "${LLVM_LIBDIR_SUFFIX}" ]; then
        mkdir -p ${D}${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib
        mv ${D}${libdir}/linux ${D}${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib
        rmdir --ignore-fail-on-non-empty ${D}${libdir}
    else
        mkdir -p ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib
        mv ${D}${libdir}/linux ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib
    fi
}

FILES_SOLIBSDEV = ""

FILES:${PN} += "${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/lib*${SOLIBSDEV} \
                ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/*.txt \
                ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/share/*.txt"
FILES:${PN}-staticdev += "${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.a"
FILES:${PN}-dev += "${datadir} ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/*.syms \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/include \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/clang_rt.crt*.o \
                    ${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}/lib/linux/libclang_rt.asan-preinit*.a"

INSANE_SKIP:${PN} = "dev-so libdir"
INSANE_SKIP:${PN}-dbg = "libdir"

#PROVIDES:append:class-target = "\
#        virtual/${MLPREFIX}compilerlibs \
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

TOOLCHAIN = "clang"
# Overrides defaults from clang.bbclass
TOOLCHAIN:class-nativesdk = "clang"
TOOLCHAIN:class-native = "clang"
SYSROOT_DIRS:append:class-target = " ${nonarch_libdir}"
