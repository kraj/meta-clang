# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
LICENSE = "NCSA"
SECTION = "devel"

require clang.inc

PV .= "+git${SRCPV}"

LIC_FILES_CHKSUM = "file://LICENSE.TXT;md5=${LLVMMD5SUM}; \
                    file://tools/clang/LICENSE.TXT;md5=${CLANGMD5SUM}; \
                   "
SRC_URI = "${LLVM_GIT}/llvm.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};name=llvm \
           ${LLVM_GIT}/clang.git;protocol=${LLVM_GIT_PROTOCOL};branch=${BRANCH};destsuffix=git/tools/clang;name=clang \
           file://0001-llvm-Remove-CMAKE_CROSSCOMPILING-so-it-can-cross-com.patch \
           file://0002-llvm-Do-not-assume-linux-glibc.patch \
           file://0003-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
           \
           file://0001-clang-driver-Use-lib-for-ldso-on-OE.patch;patchdir=tools/clang \
           file://0002-clang-Driver-tools.cpp-Add-lssp-and-lssp_nonshared-o.patch;patchdir=tools/clang \
           file://0003-clang-musl-ppc-does-not-support-128-bit-long-double.patch;patchdir=tools/clang \
           file://0004-clang-Do-not-search-clang-install-dir-relative-.-lib.patch;patchdir=tools/clang \
          "

SRCREV_FORMAT = "llvm_clang"

INHIBIT_DEFAULT_DEPS = "1"

S = "${WORKDIR}/git"

inherit cmake

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

def get_clang_target_arch(bb, d):
    target_arch = d.getVar('TRANSLATED_TARGET_ARCH', True)
    clang_arches = {
        "i586"     : "X86",
        "x86-64"   : "X86",
        "powerpc"  : "PowerPC",
        "mips"     : "Mips",
        "arm"      : "ARM",
        "arm64"    : "AArch64",
        "aarch64"  : "AArch64",
    }

    if target_arch in clang_arches:
        return clang_arches[target_arch]
    return ""

#TUNE_CCARGS_remove = "-mthumb-interwork"
#TUNE_CCARGS_remove = "-march=armv7-a"
#TUNE_CCARGS_remove = "-marm"
#TUNE_CCARGS_append_class-target = " -D__extern_always_inline=inline -I${PKG_CONFIG_SYSROOT_DIR}${includedir}/libxml2 "
#LDFLAGS_append_class-target = " -L${PKG_CONFIG_SYSROOT_DIR}${libdir}/libxml2 "

EXTRA_OECMAKE="-DLLVM_ENABLE_RTTI=True \
               -DLLVM_ENABLE_FFI=False \
               -DCMAKE_SYSTEM_NAME=Linux \
               -DCMAKE_BUILD_TYPE=Release \
	       -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
	      "

EXTRA_OECMAKE_append_class-native = "\
               -DLLVM_TARGETS_TO_BUILD='AArch64;ARM;Mips;PowerPC;X86' \
"
EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TARGETS_TO_BUILD='AArch64;ARM;Mips;PowerPC;X86' \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"
EXTRA_OECMAKE_append_class-target = "\
               -DLLVM_ENABLE_PIC=False \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
               -DLLVM_TARGETS_TO_BUILD=${@get_clang_target_arch(bb, d)} \
               -DLLVM_TARGET_ARCH=${@get_clang_target_arch(bb, d)} \
               -DLLVM_DEFAULT_TARGET_TRIPLE=${TARGET_ARCH} \
"
#               -DCMAKE_CXX_FLAGS='-target armv7a -ccc-gcc-name ${HOST_PREFIX}g++ ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -v -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0 -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0/arm-rdk-linux-gnueabi' \
#               -DCMAKE_C_FLAGS='-target armv7a -ccc-gcc-name ${HOST_PREFIX}gcc ${HOST_CC_ARCH}${TOOLCHAIN_OPTIONS} -v -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0 -I ${PKG_CONFIG_SYSROOT_DIR}${includedir}/c++/5.1.0/arm-rdk-linux-gnueabi' \
#
#
EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"

DEPENDS = "zlib libffi libxml2 binutils"
DEPENDS_remove_class-nativesdk = "nativesdk-binutils"
DEPENDS_append_class-nativesdk = " clang-native virtual/${TARGET_PREFIX}binutils-crosssdk virtual/${TARGET_PREFIX}gcc-crosssdk virtual/${TARGET_PREFIX}g++-crosssdk"
DEPENDS_append_class-target = " clang-cross-${TARGET_ARCH} ${@bb.utils.contains('TOOLCHAIN', 'gcc', 'virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++', '', d)}"

do_configure_prepend() {
	# Remove RPATHs
#	sed -i 's:$(RPATH) -Wl,$(\(ToolDir\|LibDir\|ExmplDir\))::g' ${S}/Makefile.rules
	# Drop "svn" suffix from version string
#	sed -i 's/${PV}svn/${PV}/g' ${S}/configure

	# Fix paths in llvm-config
	sed -i "s|sys::path::parent_path(CurrentPath))\.str()|sys::path::parent_path(sys::path::parent_path(CurrentPath))).str()|g" ${S}/tools/llvm-config/llvm-config.cpp
	sed -ri "s#/(bin|include|lib)(/?\")#/\1/${LLVM_DIR}\2#g" ${S}/tools/llvm-config/llvm-config.cpp
}

do_compile_prepend_class-native () {
	oe_runmake LLVM-tablegen-host
	oe_runmake CLANG-tablegen-host
}

do_install_append_class-native () {
	install -Dm 0755 ${B}/NATIVE/bin/clang-tblgen ${D}${bindir}/clang-tblgen
	for f in `find ${D}${bindir} -executable -type f -not -type l`; do
		test -n "`file $f|grep -i ELF`" && ${STRIP} $f
		echo "stripped $f"
	done
}

do_install_append_class-nativesdk () {
	install -Dm 0755 ${B}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
	for f in `find ${D}${bindir} -executable -type f -not -type l`; do
		test -n "`file $f|grep -i ELF`" && ${STRIP} $f
	done
	rm -rf ${D}${datadir}/llvm/cmake
	rm -rf ${D}${datadir}/llvm
}

PACKAGE_DEBUG_SPLIT_STYLE_class-nativesdk = "debug-without-src"

BBCLASSEXTEND = "native nativesdk"

FILES_${PN} += "\
  ${libdir}/BugpointPasses.so \
  ${libdir}/LLVMHello.so \
  ${datadir}/scan-* \
"

FILES_${PN}-dev += "\
  ${datadir}/llvm/cmake \
  ${libdir}/cmake \
"

INSANE_SKIP_${PN} += "already-stripped"
INSANE_SKIP_${PN}-dev += "dev-elf"
