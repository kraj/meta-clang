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
                    file://projects/compiler-rt/LICENSE.TXT;md5=27b14ab4ce08d04c3a9a5f0ed7997362; \
                   "
SRC_URI = "git://github.com/llvm-mirror/llvm.git;branch=${BRANCH};name=llvm \
           git://github.com/llvm-mirror/clang.git;branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
           git://github.com/llvm-mirror/compiler-rt.git;branch=${BRANCH};destsuffix=git/projects/compiler-rt;name=compiler-rt \
          "

SRCREV_llvm = "c39f5dd0e2d689a10d1e7de3da07f1975c0aa8f4"
SRCREV_clang = "35793f330181dae066b999c56ef117763c1df13c"
SRCREV_compiler-rt = "9b0ca95e42c4e9c4ee4d1d0f0c07d44c85350157"

SRCREV_FORMAT = "llvm_clang_compiler-rt"

S = "${WORKDIR}/git"

inherit perlnative pythonnative cmake

EXTRA_OECMAKE="-DLLVM_ENABLE_RTTI:BOOL=True \
               -DLLVM_ENABLE_FFI:BOOL=False \
               -DCMAKE_SYSTEM_NAME=Linux \
               -DCMAKE_BUILD_TYPE:STRING=Release \
               -DLLVM_TARGETS_TO_BUILD:STRING='AArch64;ARM;Mips;PowerPC;X86' \
	      "

EXTRA_OECMAKE_append_class-target = "\
               -DCMAKE_CROSSCOMPILING=True \
"
EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"


DEPENDS = "zlib libffi libxml2-native binutils"

PROVIDES_append_class-target = "\
        virtual/${TARGET_PREFIX}compilerlibs \
        gcc-runtime \
        libgcc \
        libgcc-initial \
        libg2c \
        libg2c-dev \
        libssp \
        libssp-dev \
        libssp-staticdev \
        libgfortran \
        libgfortran-dev \
        libgfortran-staticdev \
        libmudflap \
        libmudflap-dev \
        libgomp \
        libgomp-dev \
        libgomp-staticdev \
        libitm \
        libitm-dev \
        libitm-staticdev \
        libgcov-dev \
        \
        libgcc-dev \
        libgcc-initial-dev \
        libstdc++ \
        libstdc++-dev \
        libstdc++-staticdev \
        libatomic \
        libatomic-dev \
        libatomic-staticdev \
        libasan \
        libasan-dev \
        libasan-staticdev \
        libubsan \
        libubsan-dev \
        libubsan-staticdev \
        liblsan \
        liblsan-dev \
        liblsan-staticdev \
        libtsan \
        libtsan-dev \
        libtsan-staticdev \
        libssp \
        libssp-dev \
        libssp-staticdev \
        libgfortran \
        libgfortran-dev \
        libgfortran-staticdev \
        libmudflap \
        libmudflap-dev \
        libmudflap-staticdev \
        libgomp \
        libgomp-dev \
        libgomp-staticdev \
        libitm \
        libitm-dev \
        libitm-staticdev \
"

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

BBCLASSEXTEND = "native"
