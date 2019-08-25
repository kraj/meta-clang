# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "libc++ is a new implementation of the C++ standard library, targeting C++11"
HOMEPAGE = "http://libcxx.llvm.org/"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pythonnative

DEPENDS += "ninja-native"
BASEDEPENDS_remove_toolchain-clang = "libcxx"
DEPENDS_append_toolchain-clang = " virtual/${TARGET_PREFIX}compilerlibs"
TARGET_CXXFLAGS_remove_toolchain-clang = "--stdlib=libc++"
TUNE_CCARGS_remove_toolchain-clang = "--rtlib=compiler-rt --unwindlib=libunwind --stdlib=libc++"

LDFLAGS_append_toolchain-gcc = " -lgcc"

PACKAGECONFIG ??= "unwind"
PACKAGECONFIG_toolchain-gcc = ""
PACKAGECONFIG_powerpc = ""
PACKAGECONFIG_riscv64 = ""
PACKAGECONFIG_riscv32 = ""
PACKAGECONFIG[unwind] = "-DLIBCXXABI_USE_LLVM_UNWINDER=ON -DLIBUNWIND_ENABLE_SHARED=ON -DLIBCXXABI_ENABLE_STATIC_UNWINDER=ON -DLIBCXXABI_LIBUNWIND_INCLUDES=${S}/projects/libunwind/include, -DLIBCXXABI_USE_LLVM_UNWINDER=OFF,"

PROVIDES += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind', '', d)}"
LIBUNWIND = "${@bb.utils.contains('PACKAGECONFIG', 'unwind', ';libunwind', '', d)}"

LIC_FILES_CHKSUM = "file://libcxx/LICENSE.TXT;md5=55d89dd7eec8d3b4204b680e27da3953 \
                    file://libcxxabi/LICENSE.TXT;md5=7b9334635b542c56868400a46b272b1e \
                    file://libunwind/LICENSE.TXT;md5=f66970035d12f196030658b11725e1a1 \
"
THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"
EXTRA_OECMAKE_append_armv5 = " -D_LIBCXXABI_HAS_ATOMIC_BUILTINS=OFF"

EXTRA_OECMAKE += "\
                  -DLIBCXX_CXX_ABI=libcxxabi \
                  -DLIBCXX_USE_COMPILER_RT=YES \
                  -DLIBCXXABI_USE_COMPILER_RT=YES \
                  -DCXX_SUPPORTS_CXX11=ON \
                  -DLIBCXX_INSTALL_EXPERIMENTAL_LIBRARY=ON \
                  -DLIBCXXABI_LIBCXX_INCLUDES=${S}/libcxx/include \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/lib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${AR} \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${NM} \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${RANLIB} \
                  -DLLVM_ENABLE_PROJECTS='libcxx;libcxxabi${LIBUNWIND}' \
                  -G Ninja \
                  ${S}/llvm \
"

EXTRA_OECMAKE_append_class-native = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"
EXTRA_OECMAKE_append_class-nativesdk = " -DLIBCXX_ENABLE_ABI_LINKER_SCRIPT=OFF"
EXTRA_OECMAKE_append_libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "
EXTRA_OECMAKE_append_riscv64 = " -DLIBCXXABI_ENABLE_EXCEPTIONS=ON \
                                 -DLIBCXX_ENABLE_EXCEPTIONS=ON \
                                 -DLIBOMP_LIBFLAGS='-latomic' \
                                 -DCMAKE_SHARED_LINKER_FLAGS='-lgcc_s -latomic' \
                                 "
EXTRA_OECMAKE_append_riscv32 = " -DLIBCXXABI_ENABLE_EXCEPTIONS=ON \
                                 -DLIBCXX_ENABLE_EXCEPTIONS=ON \
                                 -DLIBOMP_LIBFLAGS='-latomic' \
                                 -DCMAKE_SHARED_LINKER_FLAGS='-lgcc_s -latomic' \
                                 "
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
    fi
    DESTDIR=${D} ninja ${PARALLEL_MAKE} install-cxx install-cxxabi
}

ALLOW_EMPTY_${PN} = "1"

RPROVIDES_${PN} += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind', '', d)}"
RPROVIDES_${PN}-dbg += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind-dbg', '', d)}"
RPROVIDES_${PN}-dev += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind-dev', '', d)}"
RPROVIDES_${PN}-doc += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind-doc', '', d)}"
RPROVIDES_${PN}-staticdev += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind-staticdev', '', d)}"
RPROVIDES_${PN}-locale += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind-locale', '', d)}"
RPROVIDES_${PN}-src += "${@bb.utils.contains('PACKAGECONFIG', 'unwind', 'libunwind-src', '', d)}"

BBCLASSEXTEND = "native nativesdk"
