# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pythonnative

DEPENDS += "ninja-native llvm-libunwind"

DEPENDS_remove_powerpc = "llvm-libunwind"
DEPENDS_remove_riscv32 = "llvm-libunwind"
DEPENDS_remove_riscv64 = "llvm-libunwind"

BASEDEPENDS_remove_toolchain-clang = "libcxx"
DEPENDS_append_toolchain-clang = " virtual/${TARGET_PREFIX}compilerlibs"

TARGET_CXXFLAGS_remove_toolchain-clang = "--stdlib=libc++"
TUNE_CCARGS_remove_toolchain-clang = "--stdlib=libc++"

LIC_FILES_CHKSUM = "file://libcxx/LICENSE.TXT;md5=55d89dd7eec8d3b4204b680e27da3953 \
                    file://libcxxabi/LICENSE.TXT;md5=7b9334635b542c56868400a46b272b1e \
"
THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

EXTRA_OECMAKE += "\
                  -DCMAKE_CROSSCOMPILING=ON \
                  -DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXX_USE_COMPILER_RT=ON \
                  -DLIBCXXABI_USE_COMPILER_RT=ON \
                  -DLIBCXX_INSTALL_EXPERIMENTAL_LIBRARY=ON \
                  -DLIBCXX_ENABLE_STATIC_ABI_LIBRARY=ON \
                  -DLIBCXX_STATICALLY_LINK_ABI_IN_SHARED_LIBRARY=OFF \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/libcxx/include \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/${baselib} \
                  -DCMAKE_SHARED_LINKER_FLAGS='${SHAREDFLAGS}' \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${AR} \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${NM} \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${RANLIB} \
                  -DLLVM_ENABLE_PROJECTS='libcxx;libcxxabi' \
                  -DLLVM_LIBDIR_SUFFIX=${@d.getVar('baselib').replace('lib', '')} \
                  -G Ninja \
                  ${S}/llvm \
"
SHAREDFLAGS ?= "-lunwind"
SHAREDFLAGS_riscv32 = "-lgcc_s -latomic"
SHAREDFLAGS_riscv64 = "-lgcc_s -latomic"
SHAREDFLAGS_powerpc = "-lgcc_s -latomic"
SHAREDFLAGS_append_arm = " -latomic"
SHAREDFLAGS_append_armeb = " -latomic"

EXTRA_OECMAKE_append_class-native = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"
EXTRA_OECMAKE_append_class-nativesdk = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"
EXTRA_OECMAKE_append_libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "
EXTRA_OECMAKE_append_riscv64 = " -DLIBCXXABI_ENABLE_EXCEPTIONS=ON \
                                 -DLIBCXX_ENABLE_EXCEPTIONS=ON \
                                 -DLIBOMP_LIBFLAGS='-latomic' \
                                 -DLIBCXX_HAS_GCC_S_LIB=ON \
                                 "

EXTRA_OECMAKE_append_armv5 = " -D_LIBCXXABI_HAS_ATOMIC_BUILTINS=OFF"

EXTRA_OECMAKE_append_arm = " -DCMAKE_REQUIRED_FLAGS='-fno-exceptions'"

EXTRA_OECMAKE_append_riscv32 = " -DLIBCXXABI_ENABLE_EXCEPTIONS=ON \
                                 -DLIBCXX_ENABLE_EXCEPTIONS=ON \
                                 -DLIBOMP_LIBFLAGS='-latomic' \
                                 -DLIBCXX_HAS_GCC_S_LIB=ON \
                                 "
EXTRA_OECMAKE_append_powerpc = " -DLIBCXXABI_ENABLE_EXCEPTIONS=ON \
                                 -DLIBCXX_ENABLE_EXCEPTIONS=ON \
                                 -DLIBOMP_LIBFLAGS='-latomic' \
                                 -DLIBCXX_HAS_GCC_S_LIB=ON \
                                 "
do_compile() {
    ninja -v ${PARALLEL_MAKE} cxxabi
    ninja -v ${PARALLEL_MAKE} cxx
}

do_install() {
    DESTDIR=${D} ninja ${PARALLEL_MAKE} install-cxx install-cxxabi
}

ALLOW_EMPTY_${PN} = "1"

BBCLASSEXTEND = "native nativesdk"
TOOLCHAIN = "clang"

