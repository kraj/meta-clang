# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
SECTION = "devel"

require clang.inc

BRANCH ?= "master"
LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=4c0bc17c954e99fd547528d938832bfa; \
                    file://tools/clang/LICENSE.TXT;md5=82ed8fe1976ca709bbd81f4f10a48ccd; \
                   "
SRC_URI = "git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/clang.git;branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
          "

SRCREV_llvm = "2c64a1129f14d6322631e1c6d610b92c4c4871d0"
SRCREV_clang = "070ffd29fb0a5a558e8f9bd464f784ff24ef1a54"

SRCREV_FORMAT = "llvm_clang"

S = "${WORKDIR}/git"

inherit perlnative pythonnative cmake

EXTRA_OECMAKE="-DLLVM_ENABLE_RTTI:BOOL=True \
               -DLLVM_ENABLE_FFI:BOOL=False \
               -DCMAKE_SYSTEM_NAME=Linux \
               -DCMAKE_BUILD_TYPE:STRING=Release \
	       -DLLVM_BUILD_EXTERNAL_COMPILER_RT:BOOL=True \
               -DLLVM_TARGETS_TO_BUILD:STRING='AArch64;ARM;Mips;PowerPC;X86' \
	      "

EXTRA_OECMAKE_append_class-target = "\
               -DCMAKE_CROSSCOMPILING=True \
"
EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"


DEPENDS = "zlib libffi libxml2-native binutils"

do_configure_prepend() {
        # Remove RPATHs
        sed -i 's:$(RPATH) -Wl,$(\(ToolDir\|LibDir\|ExmplDir\))::g' ${S}/Makefile.rules
        # Drop "svn" suffix from version string
        sed -i 's/${PV}svn/${PV}/g' ${S}/configure

        # Fix paths in llvm-config
        sed -i "s|sys::path::parent_path(CurrentPath))\.str()|sys::path::parent_path(sys::path::parent_path(CurrentPath))).str()|g" ${S}/tools/llvm-config/llvm-config.cpp
        sed -ri "s#/(bin|include|lib)(/?\")#/\1/${LLVM_DIR}\2#g" ${S}/tools/llvm-config/llvm-config.cpp
}

do_compile_prepend() {
        oe_runmake LLVMNativeTableGen
        oe_runmake CLANGNativeTableGen
}

do_install_append_class-native () {
        for f in `find ${D}${bindir} -executable -type f -not -type l`; do
            test -n "`file $f|grep -i ELF`" && ${STRIP} $f
        done
}

BBCLASSEXTEND = "native nativesdk"
