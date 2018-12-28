# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
SECTION = "devel"

require clang.inc
require common-source.inc

INHIBIT_DEFAULT_DEPS = "1"

inherit cmake cmake-native

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
    elif re.match('riscv(32|64)$', a):                 return 'RISCV'
    else:
        bb.error("cannot map '%s' to a supported llvm architecture" % a)
    return ""

def get_clang_host_arch(bb, d):
    return get_clang_arch(bb, d, 'HOST_ARCH')

def get_clang_target_arch(bb, d):
    return get_clang_arch(bb, d, 'TARGET_ARCH')

PACKAGECONFIG ??= "compiler-rt libcplusplus shared-libs"
PACKAGECONFIG_class-native = ""
PACKAGECONFIG_class-nativesdk = "compiler-rt libcplusplus"

PACKAGECONFIG[compiler-rt] = "-DCLANG_DEFAULT_RTLIB=compiler-rt,,compiler-rt"
PACKAGECONFIG[libcplusplus] = "-DCLANG_DEFAULT_CXX_STDLIB=libc++,,libcxx"
PACKAGECONFIG[shared-libs] = "-DLLVM_BUILD_LLVM_DYLIB=ON -DLLVM_LINK_LLVM_DYLIB=ON,,,"

#
# Default to build all OE-Core supported target arches (user overridable).
#
LLVM_TARGETS_TO_BUILD ?= "AArch64;ARM;BPF;Mips;PowerPC;X86"
LLVM_TARGETS_TO_BUILD_append = ";${@get_clang_host_arch(bb, d)};${@get_clang_target_arch(bb, d)}"

LLVM_TARGETS_TO_BUILD_TARGET ?= ""
LLVM_TARGETS_TO_BUILD_TARGET_append ?= ";${@get_clang_target_arch(bb, d)}"
EXTRA_OECMAKE += "-DLLVM_ENABLE_ASSERTIONS=OFF \
                  -DLLVM_ENABLE_EXPENSIVE_CHECKS=OFF \
                  -DLLVM_ENABLE_PIC=ON \
                  -DLLVM_BINDINGS_LIST='' \
                  -DLLVM_ENABLE_FFI=ON \
                  -DFFI_INCLUDE_DIR=$(pkg-config --variable=includedir libffi) \
                  -DLLVM_OPTIMIZED_TABLEGEN=ON \
                  -DLLVM_ENABLE_RTTI=ON \
                  -DLLVM_ENABLE_EH=ON \
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=ON \
                  -DCMAKE_SYSTEM_NAME=Linux \
                  -DCMAKE_BUILD_TYPE=Release \
                  -DBUILD_SHARED_LIBS=OFF \
                  -DLLVM_ENABLE_PROJECTS='clang;lld' \
                  -G Ninja ${S}/llvm \
"

EXTRA_OECMAKE_append_class-native = "\
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
"
EXTRA_OECMAKE_append_class-nativesdk = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DCROSS_TOOLCHAIN_FLAGS_NATIVE='-DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain-native.cmake' \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
                  -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
"
EXTRA_OECMAKE_append_class-target = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
                  -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD_TARGET}' \
                  -DLLVM_TARGET_ARCH=${@get_clang_target_arch(bb, d)} \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=${TARGET_SYS} \
"

DEPENDS = "zlib libffi libxml2 ninja-native"
DEPENDS_append_class-nativesdk = " clang-native virtual/${TARGET_PREFIX}binutils-crosssdk virtual/${TARGET_PREFIX}gcc-crosssdk virtual/${TARGET_PREFIX}g++-crosssdk"
DEPENDS_append_class-target = " clang-cross-${TARGET_ARCH} ${@bb.utils.contains('TOOLCHAIN', 'gcc', 'virtual/${TARGET_PREFIX}gcc virtual/${TARGET_PREFIX}g++', '', d)}"

RRECOMMENDS_${PN} = "binutils"

do_compile() {
	ninja ${PARALLEL_MAKE}
}

do_install() {
        DESTDIR=${D} ninja ${PARALLEL_MAKE} install
}

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
  ${libdir}/TestPlugin.so \
  ${datadir}/scan-* \
  ${datadir}/opt-viewer/ \
"

FILES_${PN}-libllvm += "\
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}svn.so \
"

FILES_${PN}-dev += "\
  ${datadir}/llvm/cmake \
  ${libdir}/cmake \
"

INSANE_SKIP_${PN} += "already-stripped"
INSANE_SKIP_${PN}-dev += "dev-elf"

#Avoid SSTATE_SCAN_COMMAND running sed over llvm-config.
SSTATE_SCAN_FILES_remove = "*-config"
