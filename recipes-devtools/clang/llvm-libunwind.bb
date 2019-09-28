# Copyright (C) 2015 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "C++ runtime stack unwinder from LLVM"
HOMEPAGE = "https://github.com/llvm-mirror/libunwind"
SECTION = "base"

require clang.inc
require common-source.inc

inherit cmake pythonnative

PROVIDES += "libunwind"

DEPENDS += "ninja-native"

LIBCPLUSPLUS = ""
UNWINDLIB = ""

COMPATIBLE_HOST_powerpc = "null"
COMPATIBLE_HOST_riscv32 = "null"
COMPATIBLE_HOST_riscv64 = "null"

LIC_FILES_CHKSUM = "file://libunwind/LICENSE.TXT;md5=f66970035d12f196030658b11725e1a1 \
"
THUMB_TUNE_CCARGS = ""
#TUNE_CCARGS += "-nostdlib"

EXTRA_OECMAKE += "\
                  -DCMAKE_CROSSCOMPILING=ON \
                  -DLIBCXXABI_USE_LLVM_UNWINDER=ON \
                  -DLIBUNWIND_ENABLE_SHARED=ON \
                  -DLIBCXXABI_ENABLE_STATIC_UNWINDER=ON \
                  -DLIBCXXABI_STATICALLY_LINK_UNWINDER_IN_SHARED_LIBRARY=OFF \
                  -DLIBCXXABI_LIBUNWIND_INCLUDES=${S}/projects/libunwind/include \
                  -DLIBUNWIND_ENABLE_THREADS=OFF \
                  -DLIBUNWIND_ENABLE_CROSS_UNWINDING=ON \
                  -DLLVM_ENABLE_LIBCXX=ON \
                  -DLIBCXX_CXX_ABI_INCLUDE_PATHS=${S}/libcxxabi/include \
                  -DLIBCXX_CXX_ABI_LIBRARY_PATH=${B}/${baselib} \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${AR} \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${NM} \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${RANLIB} \
                  -DLLVM_ENABLE_PROJECTS='libunwind' \
                  -DLLVM_LIBDIR_SUFFIX=${@d.getVar('baselib').replace('lib', '')} \
                  -G Ninja \
                  ${S}/llvm \
"

EXTRA_OECMAKE_append_libc-musl = " -DLIBCXX_HAS_MUSL_LIBC=ON "
EXTRA_OECMAKE_append_riscv64 = " -DCMAKE_SHARED_LINKER_FLAGS='-lgcc_s -latomic' \
                               "

EXTRA_OECMAKE_append_arm = " -DCMAKE_REQUIRED_FLAGS='-fno-exceptions'"

EXTRA_OECMAKE_append_riscv32 = " -DCMAKE_SHARED_LINKER_FLAGS='-lgcc_s -latomic' \
                               "
EXTRA_OECMAKE_append_powerpc = " -DCMAKE_SHARED_LINKER_FLAGS='-lgcc_s -latomic' \
                               "
do_compile() {
    ninja -v ${PARALLEL_MAKE} unwind
}

do_install() {
    DESTDIR=${D} ninja ${PARALLEL_MAKE} install-unwind
    install -d ${D}${includedir}
    install -m 644 ${S}/libunwind/include/*.h ${D}${includedir}
}

ALLOW_EMPTY_${PN} = "1"

RPROVIDES_${PN} += "libunwind"
RPROVIDES_${PN}-dbg += "libunwind-dbg"
RPROVIDES_${PN}-dev += "libunwind-dev"
RPROVIDES_${PN}-doc += "libunwind-doc"
RPROVIDES_${PN}-staticdev += "libunwind-staticdev"
RPROVIDES_${PN}-locale += "libunwind-locale"
RPROVIDES_${PN}-src += "libunwind-src"

PACKAGES_DYNAMIC += "^libunwind-locale-.*"

BBCLASSEXTEND = "native nativesdk"
TOOLCHAIN = "clang"

