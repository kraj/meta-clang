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
          "

# llvm patches
SRC_URI += "\
           file://0001-llvm-Remove-CMAKE_CROSSCOMPILING-so-it-can-cross-com.patch \
           file://0002-llvm-Do-not-assume-linux-glibc.patch \
           file://0003-llvm-TargetLibraryInfo-Undefine-libc-functions-if-th.patch \
           file://0004-llvm-allow-env-override-of-exe-path.patch \
           file://0005-llvm-ARM-Use-correct-calling-convention-for-libm.patch \
          "

# Clang patches
SRC_URI += "\
           file://0001-clang-driver-Use-lib-for-ldso-on-OE.patch;patchdir=tools/clang \
           file://0002-clang-Driver-tools.cpp-Add-lssp-and-lssp_nonshared-o.patch;patchdir=tools/clang \
           file://0003-clang-musl-ppc-does-not-support-128-bit-long-double.patch;patchdir=tools/clang \
           file://0004-clang-Prepend-trailing-to-sysroot.patch;patchdir=tools/clang \
           file://0005-clang-Look-inside-the-target-sysroot-for-compiler-ru.patch;patchdir=tools/clang \
          "

SRCREV_FORMAT = "llvm_clang"

INHIBIT_DEFAULT_DEPS = "1"

S = "${WORKDIR}/git"

inherit cmake

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

def get_clang_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var, True)
    if   re.match('(i.86|athlon|x86.64)$', a):         return 'X86'
    elif re.match('arm$', a):                          return 'ARM'
    elif re.match('armeb$', a):                        return 'ARM'
    elif re.match('aarch64$', a):                      return 'AArch64'
    elif re.match('aarch64_be$', a):                   return 'AArch64'
    elif re.match('mips(isa|)(32|64|)(r6|)(el|)$', a): return 'Mips'
    elif re.match('p(pc|owerpc)(|64)', a):             return 'PowerPC'
    else:
        bb.error("cannot map '%s' to a supported llvm architecture" % a)
    return ""

def get_clang_host_arch(bb, d):
    return get_clang_arch(bb, d, 'HOST_ARCH')

def get_clang_target_arch(bb, d):
    return get_clang_arch(bb, d, 'TARGET_ARCH')

PACKAGECONFIG ??= "compiler-rt libcplusplus"
PACKAGECONFIG_class-native = ""

PACKAGECONFIG[compiler-rt] = "-DCLANG_DEFAULT_RTLIB=compiler-rt,,compiler-rt"
PACKAGECONFIG[libcplusplus] = "-DCLANG_DEFAULT_CXX_STDLIB=libc++,,libcxx"
#
# Default to build all OE-Core supported target arches (user overridable).
#
LLVM_TARGETS_TO_BUILD ?= "AArch64;ARM;Mips;PowerPC;X86"
LLVM_TARGETS_TO_BUILD_append = ";${@get_clang_host_arch(bb, d)};${@get_clang_target_arch(bb, d)}"

EXTRA_OECMAKE="-DLLVM_ENABLE_RTTI=True \
               -DLLVM_ENABLE_FFI=False \
               -DCMAKE_SYSTEM_NAME=Linux \
               -DCMAKE_BUILD_TYPE=Release \
               -DLLVM_BUILD_EXTERNAL_COMPILER_RT=True \
"

EXTRA_OECMAKE_append_class-native = "\
               -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
"
EXTRA_OECMAKE_append_class-nativesdk = "\
               -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"
EXTRA_OECMAKE_append_class-target = "\
               -DBUILD_SHARED_LIBS=OFF \
               -DLLVM_BUILD_LLVM_DYLIB=ON \
               -DLLVM_ENABLE_PIC=ON \
               -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
               -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
               -DLLVM_TARGETS_TO_BUILD=${@get_clang_target_arch(bb, d)} \
               -DLLVM_TARGET_ARCH=${@get_clang_target_arch(bb, d)} \
               -DLLVM_DEFAULT_TARGET_TRIPLE=${TARGET_SYS} \
"
EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"

DEPENDS = "zlib libffi libxml2"
DEPENDS_remove_class-nativesdk = "nativesdk-binutils nativesdk-compiler-rt nativesdk-libcxx nativesdk-llvm-unwind"
DEPENDS_append_class-nativesdk = " clang-native virtual/${TARGET_PREFIX}binutils-crosssdk virtual/${TARGET_PREFIX}gcc-crosssdk virtual/${TARGET_PREFIX}g++-crosssdk"
DEPENDS_append_class-target = " clang-cross-${TARGET_ARCH} ${@bb.utils.contains('TOOLCHAIN', 'gcc', 'virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++', '', d)}"

RRECOMMENDS_${PN} = "binutils"

do_install_append_class-native () {
	install -Dm 0755 ${B}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
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

PACKAGES =+ "${PN}-libllvm"

BBCLASSEXTEND = "native nativesdk"

FILES_${PN} += "\
  ${libdir}/BugpointPasses.so \
  ${libdir}/LLVMHello.so \
  ${datadir}/scan-* \
"

FILES_${PN}-libllvm += "\
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}.so \
"

FILES_${PN}-dev += "\
  ${datadir}/llvm/cmake \
  ${libdir}/cmake \
"

INSANE_SKIP_${PN} += "already-stripped"
INSANE_SKIP_${PN}-dev += "dev-elf"
