# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake python3native

PACKAGECONFIG ??= "compiler-rt exceptions ${@bb.utils.contains("RUNTIME", "llvm", "unwind", "", d)}"
PACKAGECONFIG_riscv32 = "exceptions"
PACKAGECONFIG_riscv64 = "exceptions"
PACKAGECONFIG_append_armv5 = " no-atomics"

PACKAGECONFIG[unwind] = "-DLIBCXXABI_USE_LLVM_UNWINDER=ON -DLIBCXXABI_ENABLE_STATIC_UNWINDER=ON -DLIBCXXABI_STATICALLY_LINK_UNWINDER_IN_SHARED_LIBRARY=ON -DLIBCXXABI_STATICALLY_LINK_UNWINDER_IN_STATIC_LIBRARY=ON,-DLIBCXXABI_USE_LLVM_UNWINDER=OFF,,"
PACKAGECONFIG[exceptions] = "-DLIBCXXABI_ENABLE_EXCEPTIONS=ON -DDLIBCXX_ENABLE_EXCEPTIONS=ON,-DLIBCXXABI_ENABLE_EXCEPTIONS=OFF -DLIBCXX_ENABLE_EXCEPTIONS=OFF -DCMAKE_REQUIRED_FLAGS='-fno-exceptions',"
PACKAGECONFIG[no-atomics] = "-D_LIBCXXABI_HAS_ATOMIC_BUILTINS=OFF -DCMAKE_SHARED_LINKER_FLAGS='-latomic',,"
PACKAGECONFIG[compiler-rt] = ",,compiler-rt"

DEPENDS += "ninja-native"
DEPENDS_append_class-target = " clang-cross-${TARGET_ARCH} virtual/${MLPREFIX}libc virtual/${TARGET_PREFIX}compilerlibs"

LIBCPLUSPLUS = ""
COMPILER_RT ?= "${@bb.utils.contains("PACKAGECONFIG", "compiler-rt", "-rtlib=compiler-rt", "", d)} ${UNWINDLIB}"
UNWINDLIB ?= "${@bb.utils.contains("RUNTIME", "gnu", "--unwindlib=libgcc", "", d)}"

INHIBIT_DEFAULT_DEPS = "1"

LIC_FILES_CHKSUM = "file://libcxx/LICENSE.TXT;md5=55d89dd7eec8d3b4204b680e27da3953 \
                    file://libcxxabi/LICENSE.TXT;md5=7b9334635b542c56868400a46b272b1e \
                    file://libunwind/LICENSE.TXT;md5=f66970035d12f196030658b11725e1a1 \
"

LLVM_LIBDIR_SUFFIX_powerpc64 = "64"

OECMAKE_TARGET_COMPILE = "cxxabi cxx"
OECMAKE_TARGET_INSTALL = "install-cxx install-cxxabi"
OECMAKE_SOURCEPATH = "${S}/llvm"
EXTRA_OECMAKE += "\
                  -DCMAKE_CROSSCOMPILING=ON \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
                  -DLLVM_ENABLE_RTTI=ON \
                  -DLIBUNWIND_ENABLE_SHARED=OFF \
                  -DLIBUNWIND_ENABLE_THREADS=OFF \
                  -DLIBUNWIND_WEAK_PTHREAD_LIB=ON \
                  -DLIBUNWIND_ENABLE_CROSS_UNWINDING=ON \
                  -DLIBCXXABI_INCLUDE_TESTS=OFF \
                  -DLIBCXXABI_ENABLE_SHARED=ON \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/libcxx/include \
                  -DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/lib${LLVM_LIBDIR_SUFFIX} \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${AR} \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${NM} \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${RANLIB} \
                  -DLLVM_ENABLE_PROJECTS='libcxx;libcxxabi;libunwind' \
                  -DLLVM_LIBDIR_SUFFIX=${LLVM_LIBDIR_SUFFIX} \
"

EXTRA_OECMAKE_append_class-native = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"

EXTRA_OECMAKE_append_class-nativesdk = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"

EXTRA_OECMAKE_append_libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "

CXXFLAGS_append_armv5 = " -mfpu=vfp2"

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native nativesdk"
TOOLCHAIN = "clang"

