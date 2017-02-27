# Copyright (C) 2017 Kai Ruhnau <kai.ruhnau@target-sg.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "Next generation, high-performance debugger"
HOMEPAGE = "http://lldb.llvm.org/"
LICENSE = "MIT | NCSA"
SECTION = "devel"

DEPENDS += "clang-native zlib libxml2"

require clang.inc

inherit cmake
PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=${LLVMMD5SUM}; \
                    file://tools/clang/LICENSE.TXT;md5=${CLANGMD5SUM}; \
                    file://tools/lldb/LICENSE.TXT;md5=${LLDBMD5SUM}; \
                   "

SRC_URI = "\
           ${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
           ${LLVM_GIT}/clang.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
           ${LLVM_GIT}/lldb.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};destsuffix=git/tools/lldb;name=lldb \
          "
SRCREV_FORMAT = "llvm_clang_lldb"

S = "${WORKDIR}/git"

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

EXTRA_OECMAKE="\
    -DLLVM_BUILD_LLVM_DYLIB=ON \
    -DLLDB_DISABLE_LIBEDIT=1 \
    -DLLDB_DISABLE_CURSES=1 \
    -DLLDB_DISABLE_PYTHON=1 \
    -DLLVM_ENABLE_TERMINFO=0 \
    -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
    -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
    "

do_compile() {
	cd ${B}/tools/lldb
	base_do_compile VERBOSE=1
}

do_install() {
	cd ${B}/tools/lldb
	oe_runmake 'DESTDIR=${D}' install
}
