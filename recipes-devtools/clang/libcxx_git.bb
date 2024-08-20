# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake cmake-native python3native

PACKAGECONFIG ??= "compiler-rt exceptions ${@bb.utils.contains("TC_CXX_RUNTIME", "llvm", "unwind unwind-shared", "", d)}"
PACKAGECONFIG:append:armv5 = " no-atomics"
PACKAGECONFIG:remove:class-native = "compiler-rt"
PACKAGECONFIG[unwind] = "-DLIBCXXABI_USE_LLVM_UNWINDER=ON -DLIBCXXABI_ENABLE_STATIC_UNWINDER=ON,-DLIBCXXABI_USE_LLVM_UNWINDER=OFF,,"
PACKAGECONFIG[exceptions] = "-DLIBCXXABI_ENABLE_EXCEPTIONS=ON -DLIBCXX_ENABLE_EXCEPTIONS=ON,-DLIBCXXABI_ENABLE_EXCEPTIONS=OFF -DLIBCXX_ENABLE_EXCEPTIONS=OFF -DCMAKE_REQUIRED_FLAGS='-fno-exceptions',"
PACKAGECONFIG[no-atomics] = "-D_LIBCXXABI_HAS_ATOMIC_BUILTINS=OFF -DCMAKE_SHARED_LINKER_FLAGS='-latomic',,"
PACKAGECONFIG[compiler-rt] = "-DLIBCXX_USE_COMPILER_RT=ON -DLIBCXXABI_USE_COMPILER_RT=ON -DLIBUNWIND_USE_COMPILER_RT=ON,,compiler-rt"
PACKAGECONFIG[unwind-shared] = "-DLIBUNWIND_ENABLE_SHARED=ON,-DLIBUNWIND_ENABLE_SHARED=OFF,,"

DEPENDS += "ninja-native"
DEPENDS:append:class-target = " clang-cross-${TARGET_ARCH} virtual/${MLPREFIX}libc virtual/${TARGET_PREFIX}compilerlibs"
DEPENDS:append:class-nativesdk = " clang-crosssdk-${SDK_ARCH} nativesdk-compiler-rt"
DEPENDS:append:class-native = " clang-native"

LIBCPLUSPLUS = ""
COMPILER_RT ?= "-rtlib=compiler-rt"

# Trick clang.bbclass into not creating circular dependencies
UNWINDLIB:class-nativesdk = "--unwindlib=libgcc"
COMPILER_RT:class-nativesdk = "-rtlib=libgcc --unwindlib=libgcc"
LIBCPLUSPLUS:class-nativesdk = "-stdlib=libstdc++"

CC:append:toolchain-clang:class-native = " -unwindlib=libgcc -rtlib=libgcc"
CC:append:toolchain-clang:class-nativesdk = " -unwindlib=libgcc -rtlib=libgcc"

CXXFLAGS += "-stdlib=libstdc++"
LDFLAGS += "-unwindlib=libgcc -stdlib=libstdc++"
BUILD_CXXFLAGS += "-stdlib=libstdc++"
BUILD_LDFLAGS += "-unwindlib=libgcc -rtlib=libgcc -stdlib=libstdc++"
BUILD_CPPFLAGS:remove = "-stdlib=libc++"
BUILD_LDFLAGS:remove = "-stdlib=libc++ -lc++abi"

INHIBIT_DEFAULT_DEPS = "1"

LIC_FILES_CHKSUM = "file://libcxx/LICENSE.TXT;md5=55d89dd7eec8d3b4204b680e27da3953 \
                    file://libcxxabi/LICENSE.TXT;md5=7b9334635b542c56868400a46b272b1e \
                    file://libunwind/LICENSE.TXT;md5=f66970035d12f196030658b11725e1a1 \
"

OECMAKE_TARGET_COMPILE = "cxxabi cxx"
OECMAKE_TARGET_INSTALL = "install-cxx install-cxxabi ${@bb.utils.contains("TC_CXX_RUNTIME", "llvm", "install-unwind", "", d)}"

OECMAKE_SOURCEPATH = "${S}/llvm"
EXTRA_OECMAKE += "\
                  -DCMAKE_BUILD_TYPE=RelWithDebInfo \
                  -DLLVM_ENABLE_PER_TARGET_RUNTIME_DIR=OFF \
                  -DCMAKE_CROSSCOMPILING=ON \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
                  -DLLVM_ENABLE_RTTI=ON \
                  -DLIBUNWIND_ENABLE_CROSS_UNWINDING=ON \
                  -DLIBCXX_ENABLE_STATIC_ABI_LIBRARY=ON \
                  -DLIBCXXABI_INCLUDE_TESTS=OFF \
                  -DLIBCXXABI_ENABLE_SHARED=ON \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/libcxx/include \
                  -DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/lib${LLVM_LIBDIR_SUFFIX} \
                  -S ${S}/runtimes \
                  -DLLVM_ENABLE_RUNTIMES='libcxx;libcxxabi;libunwind' \
                  -DLLVM_RUNTIME_TARGETS=${HOST_SYS} \
                  -DLLVM_LIBDIR_SUFFIX=${LLVM_LIBDIR_SUFFIX} \
                  -DCMAKE_BUILD_WITH_INSTALL_RPATH=ON \
"

EXTRA_OECMAKE:append:class-target = " \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${AR} \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${NM} \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${RANLIB} \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=${HOST_SYS} \
                  -DCMAKE_POSITION_INDEPENDENT_CODE=ON \
"

EXTRA_OECMAKE:append:class-nativesdk = " \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${AR} \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${NM} \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${RANLIB} \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=${HOST_SYS} \
"

EXTRA_OECMAKE:append:libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "

CXXFLAGS:append:armv5 = " -mfpu=vfp2"

ALLOW_EMPTY:${PN} = "1"

PROVIDES:append:runtime-llvm = " libunwind"

do_install:append() {
    if ${@bb.utils.contains("TC_CXX_RUNTIME", "llvm", "true", "false", d)}
    then
        for f in libunwind.h __libunwind_config.h unwind.h unwind_itanium.h unwind_arm_ehabi.h
        do
            install -Dm 0644 ${S}/libunwind/include/$f ${D}${includedir}/$f
        done
        install -d ${D}${libdir}/pkgconfig
        sed -e 's,@LIBDIR@,${libdir},g;s,@VERSION@,${PV},g' ${S}/../libunwind.pc.in > ${D}${libdir}/pkgconfig/libunwind.pc
    fi
}

PACKAGES:append:runtime-llvm = " libunwind"
FILES:libunwind:runtime-llvm = "${libdir}/libunwind.so.*"

BBCLASSEXTEND = "native nativesdk"
TOOLCHAIN:forcevariable = "clang"
