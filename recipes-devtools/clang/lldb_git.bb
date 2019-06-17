# Copyright (C) 2017 Kai Ruhnau <kai.ruhnau@target-sg.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Next generation, high-performance debugger"
HOMEPAGE = "http://lldb.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "devel"

DEPENDS += "clang-native zlib libxml2 ninja-native"

require clang.inc
require common-source.inc

inherit cmake pkgconfig

LIC_FILES_CHKSUM = "file://llvm/LICENSE.TXT;md5=${LLVMMD5SUM}; \
                    file://clang/LICENSE.TXT;md5=${CLANGMD5SUM}; \
                    file://lldb/LICENSE.TXT;md5=${LLDBMD5SUM}; \
"

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE="\
    -DCMAKE_CROSSCOMPILING=1 \
    -DLLVM_ENABLE_CXX11=ON \
    -DLLVM_BUILD_LLVM_DYLIB=ON \
    -DBUILD_SHARED_LIBS=OFF \
    -DLLVM_BUILD_LLVM_DYLIB=ON \
    -DLLVM_ENABLE_PIC=ON \
    -DLLDB_DISABLE_LIBEDIT=1 \
    -DLLDB_DISABLE_CURSES=1 \
    -DLLDB_DISABLE_PYTHON=1 \
    -DLLVM_ENABLE_TERMINFO=0 \
    -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
    -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
    -DLLVM_HOST_TRIPLE=${TARGET_SYS} \
    -DLLDB_TEST_USE_CUSTOM_C_COMPILER=ON \
    -DLLDB_TEST_USE_CUSTOM_CXX_COMPILER=ON \
    -DLLDB_TEST_C_COMPILER='${CC}' \
    -DLLDB_TEST_CXX_COMPILER='${CXX}' \
    -DCMAKE_BUILD_TYPE=Release \
    -DLLVM_ENABLE_PROJECTS='clang;lldb' \
    -G Ninja ${S}/llvm \
"

EXTRA_OEMAKE = "VERBOSE=1"

CXXFLAGS_append_toolchain-gcc = " -Wno-error=format-security"

do_compile() {
       ninja ${PARALLEL_MAKE} lldb
}

do_install() {
       DESTDIR=${D} ninja ${PARALLEL_MAKE} tools/lldb/install
}

INSANE_SKIP_${PN}-dbg = "libdir"
INSANE_SKIP_${PN} = "libdir"
