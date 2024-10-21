# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler Runtime"
HOMEPAGE = "http://compiler-rt.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake cmake-native pkgconfig python3native


LIC_FILES_CHKSUM = "file://compiler-rt/LICENSE.TXT;md5=d846d1d65baf322d4c485d6ee54e877a"

LIBCPLUSPLUS = ""
COMPILER_RT = ""

TUNE_CCARGS:remove = "-no-integrated-as"

INHIBIT_DEFAULT_DEPS = "1"

DEPENDS += "ninja-native libgcc"
DEPENDS:append:class-target = " clang-cross-${TARGET_ARCH} virtual/${MLPREFIX}libc gcc-runtime"
DEPENDS:append:class-nativesdk = " clang-native clang-crosssdk-${SDK_ARCH} nativesdk-gcc-runtime"
DEPENDS:append:class-native = " clang-native"

# Trick clang.bbclass into not creating circular dependencies
UNWINDLIB:class-nativesdk = "--unwindlib=libgcc"
COMPILER_RT:class-nativesdk:toolchain-clang:runtime-llvm = "-rtlib=libgcc --unwindlib=libgcc"
LIBCPLUSPLUS:class-nativesdk:toolchain-clang:runtime-llvm = "-stdlib=libstdc++"

CXXFLAGS += "-stdlib=libstdc++"
LDFLAGS += "-unwindlib=libgcc -rtlib=libgcc -stdlib=libstdc++"
BUILD_CXXFLAGS += "-stdlib=libstdc++"
BUILD_LDFLAGS += "-unwindlib=libgcc -rtlib=libgcc -stdlib=libstdc++"
BUILD_CPPFLAGS:remove = "-stdlib=libc++"
BUILD_LDFLAGS:remove = "-stdlib=libc++ -lc++abi"

BUILD_CC:toolchain-clang  = "${CCACHE}clang"
BUILD_CXX:toolchain-clang = "${CCACHE}clang++"
BUILD_CPP:toolchain-clang = "${CCACHE}clang -E"
BUILD_CCLD:toolchain-clang = "${CCACHE}clang"
BUILD_RANLIB:toolchain-clang = "llvm-ranlib"
BUILD_AR:toolchain-clang = "llvm-ar"
BUILD_NM:toolchain-clang = "llvm-nm"

PACKAGECONFIG ??= ""
PACKAGECONFIG[crt] = "-DCOMPILER_RT_BUILD_CRT:BOOL=ON,-DCOMPILER_RT_BUILD_CRT:BOOL=OFF"
PACKAGECONFIG[profile] ="-DCOMPILER_RT_BUILD_PROFILE=ON,-DCOMPILER_RT_BUILD_PROFILE=OFF"

HF = ""
HF:class-target = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"

OECMAKE_TARGET_COMPILE = "compiler-rt"
OECMAKE_TARGET_INSTALL = "install-compiler-rt install-compiler-rt-headers"
OECMAKE_SOURCEPATH = "${S}/llvm"
EXTRA_OECMAKE += "-DCMAKE_BUILD_TYPE=RelWithDebInfo \
                  -DLLVM_ENABLE_PER_TARGET_RUNTIME_DIR=OFF \
                  -DCOMPILER_RT_STANDALONE_BUILD=OFF \
                  -DCOMPILER_RT_DEFAULT_TARGET_ONLY=ON \
                  -DCOMPILER_RT_INCLUDE_TESTS=OFF \
                  -DCMAKE_C_COMPILER_TARGET=${HOST_ARCH}${HOST_VENDOR}-${HOST_OS}${HF} \
                  -DCOMPILER_RT_BUILD_XRAY=OFF \
                  -DCOMPILER_RT_BUILD_SANITIZERS=OFF \
                  -DCOMPILER_RT_BUILD_MEMPROF=OFF \
                  -DCOMPILER_RT_BUILD_LIBFUZZER=OFF \
                  -DLLVM_ENABLE_PROJECTS='compiler-rt' \
                  -DLLVM_LIBDIR_SUFFIX=${LLVM_LIBDIR_SUFFIX} \
"
EXTRA_OECMAKE:append:class-target = "\
               -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
               -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
               -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
               -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
"

EXTRA_OECMAKE:append:class-nativesdk = "\
               -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
               -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
               -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"
EXTRA_OECMAKE:append:powerpc = " -DCOMPILER_RT_DEFAULT_TARGET_ARCH=powerpc "

do_install:append () {
    if [ -n "${LLVM_LIBDIR_SUFFIX}" ]; then
        mkdir -p ${D}${nonarch_libdir}/clang
        mv ${D}${libdir}/clang/${MAJOR_VER} ${D}${nonarch_libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}
        rmdir --ignore-fail-on-non-empty ${D}${libdir}/clang ${D}${libdir}
    else
        mv ${D}${libdir}/clang/${MAJOR_VER} ${D}${libdir}/clang/${MAJOR_VER}.${MINOR_VER}.${PATCH_VER}
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
