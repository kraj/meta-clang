# Copyright (C) 2017 Kai Ruhnau <kai.ruhnau@target-sg.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Next generation, high-performance debugger"
HOMEPAGE = "http://lldb.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "devel"

DEPENDS += "clang-native zlib libxml2 ninja-native"

FILESPATH =. "${FILE_DIRNAME}/clang:"
require clang.inc
require common.inc

inherit cmake pkgconfig
PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=${LLVMMD5SUM}; \
                    file://tools/clang/LICENSE.TXT;md5=${CLANGMD5SUM}; \
                    file://tools/lldb/LICENSE.TXT;md5=${LLDBMD5SUM}; \
                   "

SRC_URI = "\
    ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
    ${LLVM_GIT}/clang.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
    ${LLVM_GIT}/lldb.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};destsuffix=git/tools/lldb;name=lldb \
    ${LLVMPATCHES} \
    ${CLANGPATCHES} \
   "

# lldb patches
SRC_URI += "\
    file://0001-Include-limits.h-for-PATH_MAX-definition.patch;patchdir=tools/lldb \
    file://0001-lldb-Add-lxml2-to-linker-cmdline-of-xml-is-found.patch;patchdir=tools/lldb \
   "
SRCREV_FORMAT = "llvm_clang_lldb"

S = "${WORKDIR}/git"

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
    -DCMAKE_BUILD_TYPE=Release \
    -G Ninja \
    ${S} \
    "

EXTRA_OEMAKE = "VERBOSE=1"

do_compile() {
       NINJA_STATUS="[%p] " ninja ${PARALLEL_MAKE} lldb
}

do_install() {
       NINJA_STATUS="[%p] " DESTDIR=${D} ninja ${PARALLEL_MAKE} tools/lldb/install
}

INSANE_SKIP_${PN}-dbg = "libdir"
INSANE_SKIP_${PN} = "libdir"
