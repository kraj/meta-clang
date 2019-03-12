# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pythonnative

DEPENDS += "ninja-native"
BASEDEPENDS_remove_toolchain-clang = "libcxx"
DEPENDS_append_toolchain-clang = " virtual/${TARGET_PREFIX}compilerlibs"
TARGET_CXXFLAGS_remove_toolchain-clang = "--stdlib=libc++"
TUNE_CCARGS_remove_toolchain-clang = "--rtlib=compiler-rt --stdlib=libc++"

PACKAGECONFIG ??= "unwind"
PACKAGECONFIG_powerpc = ""
PACKAGECONFIG_mipsarch = ""
PACKAGECONFIG_riscv64 = ""
PACKAGECONFIG[unwind] = "-DLIBCXXABI_USE_LLVM_UNWINDER=ON -DLIBUNWIND_ENABLE_SHARED=OFF -DLIBCXXABI_ENABLE_STATIC_UNWINDER=ON -DLIBCXXABI_LIBUNWIND_INCLUDES=${S}/projects/libunwind/include, -DLIBCXXABI_USE_LLVM_UNWINDER=OFF,"

#PROVIDES += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind', '', d)}"
LIBUNWIND = "${@bb.utils.contains('PACKAGECONFIG', 'unwind', ';libunwind', '', d)}"

LIC_FILES_CHKSUM = "file://libcxx/LICENSE.TXT;md5=3de3deb8323d5cf3360104190e804a75 \
                    file://libcxxabi/LICENSE.TXT;md5=9dad5a191d1fc03b31525706040c4ed1 \
                    file://libunwind/LICENSE.TXT;md5=af2bc0b29ead3c8f201c5486f3c9d59b \
"
THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"
EXTRA_OECMAKE_append_armv5 = " -D_LIBCXXABI_HAS_ATOMIC_BUILTINS=OFF"

EXTRA_OECMAKE += "\
                  -DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXX_USE_COMPILER_RT=YES \
                  -DLIBCXXABI_USE_COMPILER_RT=YES \
                  -DCXX_SUPPORTS_CXX11=ON \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/libcxx/include \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/lib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DLLVM_ENABLE_PROJECTS='libcxx;libcxxabi${LIBUNWIND}' \
                  -G Ninja \
                  ${S}/llvm \
"

EXTRA_OECMAKE_append_class-native = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"
EXTRA_OECMAKE_append_class-nativesdk = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"
EXTRA_OECMAKE_append_libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "

do_compile() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'true', 'false', d)}; then
        ninja -v ${PARALLEL_MAKE} unwind
    fi
    ninja -v ${PARALLEL_MAKE} cxxabi
    ninja -v ${PARALLEL_MAKE} cxx
}

do_install() {
    if ${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'true', 'false', d)}; then
        DESTDIR=${D} ninja ${PARALLEL_MAKE} install-unwind
        rm -rf ${D}${libdir}/libunwind.so
    fi
    DESTDIR=${D} ninja ${PARALLEL_MAKE} install-cxx install-cxxabi
}

ALLOW_EMPTY_${PN} = "1"

#PROVIDES = "virtual/${TARGET_PREFIX}compilerlibs"

RDEPENDS_${PN}-dev += "${PN}-staticdev"

BBCLASSEXTEND = "native nativesdk"
TOOLCHAIN_forcevariable = "clang"
