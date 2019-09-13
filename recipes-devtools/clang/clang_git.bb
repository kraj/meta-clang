# Copyright (C) 2014 Khem Raj <raj.khem@gmail.com>
# Released under the MIT license (see COPYING.MIT for the terms)

DESCRIPTION = "LLVM based C/C++ compiler"
HOMEPAGE = "http://clang.llvm.org/"
SECTION = "devel"

require clang.inc
require common-source.inc

INHIBIT_DEFAULT_DEPS = "1"

BUILD_CC_class-nativesdk = "clang"
BUILD_CXX_class-nativesdk = "clang++"
BUILD_AR_class-nativesdk = "llvm-ar"
BUILD_RANLIB_class-nativesdk = "llvm-ranlib"
BUILD_NM_class-nativesdk = "llvm-nm"
LDFLAGS_append_class-nativesdk = " -fuse-ld=gold"

inherit cmake cmake-native python3-dir python3native

OECMAKE_FIND_ROOT_PATH_MODE_PROGRAM = "BOTH"

def get_clang_experimental_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var, True)
    return ""

def get_clang_arch(bb, d, arch_var):
    import re
    a = d.getVar(arch_var, True)
    if   re.match('(i.86|athlon|x86.64)$', a):         return 'X86'
    elif re.match('arm$', a):                          return 'ARM'
    elif re.match('armeb$', a):                        return 'ARM'
    elif re.match('aarch64$', a):                      return 'AArch64'
    elif re.match('aarch64_be$', a):                   return 'AArch64'
    elif re.match('mips(isa|)(32|64|)(r6|)(el|)$', a): return 'Mips'
    elif re.match('riscv(32|64)(eb|)$', a):            return 'RISCV'
    elif re.match('p(pc|owerpc)(|64)', a):             return 'PowerPC'
    else:
        bb.note("'%s' is not a primary llvm architecture" % a)
    return ""

def get_clang_host_arch(bb, d):
    return get_clang_arch(bb, d, 'HOST_ARCH')

def get_clang_target_arch(bb, d):
    return get_clang_arch(bb, d, 'TARGET_ARCH')

def get_clang_experimental_target_arch(bb, d):
    return get_clang_experimental_arch(bb, d, 'TARGET_ARCH')

PACKAGECONFIG ??= "compiler-rt libcplusplus shared-libs ${@bb.utils.filter('DISTRO_FEATURES', 'thin-lto full-lto', d)}"
PACKAGECONFIG_class-native = ""
PACKAGECONFIG_class-nativesdk = "thin-lto"

PACKAGECONFIG[compiler-rt] = "-DCLANG_DEFAULT_RTLIB=compiler-rt,,libcxx,compiler-rt"
PACKAGECONFIG[libcplusplus] = "-DCLANG_DEFAULT_CXX_STDLIB=libc++,,libcxx"
PACKAGECONFIG[thin-lto] = "-DLLVM_ENABLE_LTO=Thin -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR},,binutils,"
PACKAGECONFIG[full-lto] = "-DLLVM_ENABLE_LTO=Full -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR},,binutils,"
PACKAGECONFIG[shared-libs] = "-DLLVM_BUILD_LLVM_DYLIB=ON -DLLVM_LINK_LLVM_DYLIB=ON,,,"
PACKAGECONFIG[terminfo] = "-DLLVM_ENABLE_TERMINFO=ON,-DLLVM_ENABLE_TERMINFO=OFF,ncurses,"
PACKAGECONFIG[pfm] = "-DLLVM_ENABLE_LIBPFM=ON,-DLLVM_ENABLE_LIBPFM=OFF,libpfm,"
#
# Default to build all OE-Core supported target arches (user overridable).
#
LLVM_TARGETS_TO_BUILD ?= "AArch64;ARM;BPF;Mips;PowerPC;RISCV;X86"
LLVM_TARGETS_TO_BUILD_append = ";${@get_clang_host_arch(bb, d)};${@get_clang_target_arch(bb, d)}"

LLVM_TARGETS_TO_BUILD_TARGET ?= ""
LLVM_TARGETS_TO_BUILD_TARGET_append ?= "${@get_clang_target_arch(bb, d)}"

LLVM_EXPERIMENTAL_TARGETS_TO_BUILD ?= ""
LLVM_EXPERIMENTAL_TARGETS_TO_BUILD_append = ";${@get_clang_experimental_target_arch(bb, d)}"

HF = "${@ bb.utils.contains('TUNE_CCARGS_MFLOAT', 'hard', 'hf', '', d)}"
HF[vardepvalue] = "${HF}"

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
                  -DLLVM_ENABLE_PROJECTS='clang;lld;lldb' \
                  -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR} \
                  -G Ninja ${S}/llvm \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
"

EXTRA_OECMAKE_append_class-native = "\
                  -DCLANG_ENABLE_BOOTSTRAP=On \
                  -DCLANG_BOOTSTRAP_PASSTHROUGH='${PASSTHROUGH}' \
                  -DBOOTSTRAP_LLVM_ENABLE_LTO=Thin \
                  -DBOOTSTRAP_LLVM_ENABLE_LLD=ON \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
                  -DLLVM_EXPERIMENTAL_TARGETS_TO_BUILD='${LLVM_EXPERIMENTAL_TARGETS_TO_BUILD}' \
                  -DPYTHON_EXECUTABLE='${PYTHON}' \
"
EXTRA_OECMAKE_append_class-nativesdk = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DCROSS_TOOLCHAIN_FLAGS_NATIVE='-DCMAKE_TOOLCHAIN_FILE=${WORKDIR}/toolchain-native.cmake' \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD}' \
                  -DLLVM_EXPERIMENTAL_TARGETS_TO_BUILD='${LLVM_EXPERIMENTAL_TARGETS_TO_BUILD}' \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
                  -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
                  -DLLDB_TABLEGEN=${STAGING_BINDIR_NATIVE}/lldb-tblgen \
                  -DPYTHON_LIBRARY=${STAGING_LIBDIR}/lib${PYTHON_DIR}${PYTHON_ABI}.so \
                  -DPYTHON_INCLUDE_DIR=${STAGING_INCDIR}/${PYTHON_DIR}${PYTHON_ABI} \
"
EXTRA_OECMAKE_append_class-target = "\
                  -DCMAKE_CROSSCOMPILING:BOOL=ON \
                  -DLLVM_TABLEGEN=${STAGING_BINDIR_NATIVE}/llvm-tblgen \
                  -DCLANG_TABLEGEN=${STAGING_BINDIR_NATIVE}/clang-tblgen \
                  -DLLDB_TABLEGEN=${STAGING_BINDIR_NATIVE}/lldb-tblgen \
                  -DLLVM_TARGETS_TO_BUILD='${LLVM_TARGETS_TO_BUILD_TARGET}' \
                  -DCMAKE_RANLIB=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib \
                  -DCMAKE_AR=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar \
                  -DCMAKE_NM=${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm \
                  -DLLVM_TARGET_ARCH=${@get_clang_target_arch(bb, d)} \
                  -DLLVM_DEFAULT_TARGET_TRIPLE=${TARGET_SYS}${HF} \
                  -DPYTHON_LIBRARY=${STAGING_LIBDIR}/lib${PYTHON_DIR}${PYTHON_ABI}.so \
                  -DPYTHON_INCLUDE_DIR=${STAGING_INCDIR}/${PYTHON_DIR}${PYTHON_ABI} \
"

DEPENDS = "binutils zlib libffi libxml2 libedit ninja-native swig-native"
DEPENDS_append_class-nativesdk = " clang-crosssdk-${SDK_ARCH} virtual/${TARGET_PREFIX}binutils-crosssdk nativesdk-python3"
DEPENDS_append_class-target = " clang-cross-${TARGET_ARCH} python3"

BOOTSTRAPSTAGE ?= ""
BOOTSTRAPSTAGE_class-native = "stage2"
INSTALLTARGET ?= "install"
INSTALLTARGET_class-native = "stage2-install"
PASSTHROUGH ?= ""
PASSTHROUGH_class-native = "\
CLANG_DEFAULT_RTLIB;CLANG_DEFAULT_CXX_STDLIB;LLVM_BUILD_LLVM_DYLIB;LLVM_LINK_LLVM_DYLIB;\
LLVM_ENABLE_ASSERTIONS;LLVM_ENABLE_EXPENSIVE_CHECKS;LLVM_ENABLE_PIC;\
LLVM_BINDINGS_LIST;LLVM_ENABLE_FFI;FFI_INCLUDE_DIR;LLVM_OPTIMIZED_TABLEGEN;\
LLVM_ENABLE_RTTI;LLVM_ENABLE_EH;LLVM_BUILD_EXTERNAL_COMPILER_RT;CMAKE_SYSTEM_NAME;\
CMAKE_BUILD_TYPE;BUILD_SHARED_LIBS;LLVM_ENABLE_PROJECTS;LLVM_BINUTILS_INCDIR;\
LLVM_TARGETS_TO_BUILD;LLVM_EXPERIMENTAL_TARGETS_TO_BUILD;PYTHON_EXECUTABLE;\
PYTHON_LIBRARY;PYTHON_INCLUDE_DIR;LLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN;\
"

COMPATIBLE_HOST_riscv64 = "null"
COMPATIBLE_HOST_riscv32 = "null"

RRECOMMENDS_${PN} = "binutils"
RRECOMMENDS_${PN}_append_class-target = " libcxx-dev"

do_compile() {
	ninja ${PARALLEL_MAKE} ${BOOTSTRAPSTAGE}
}

do_install() {
        DESTDIR=${D} ninja ${PARALLEL_MAKE} ${INSTALLTARGET}
        rm -rf ${D}${libdir}/python*/site-packages/six.py
}

do_install_append_class-native () {
	install -Dm 0755 ${B}/tools/clang/stage2-bins/NATIVE/bin/clang-tblgen ${D}${bindir}/clang-tblgen
	install -Dm 0755 ${B}/tools/clang/stage2-bins/NATIVE/bin/lldb-tblgen ${D}${bindir}/lldb-tblgen
	for f in `find ${D}${bindir} -executable -type f -not -type l`; do
		test -n "`file $f|grep -i ELF`" && ${STRIP} $f
		echo "stripped $f"
	done
}

do_install_append_class-nativesdk () {
	install -Dm 0755 ${B}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
	install -Dm 0755 ${B}/bin/lldb-tblgen ${D}${bindir}/lldb-tblgen
	for f in `find ${D}${bindir} -executable -type f -not -type l`; do
		test -n "`file $f|grep -i ELF`" && ${STRIP} $f
	done
	rm -rf ${D}${datadir}/llvm/cmake
	rm -rf ${D}${datadir}/llvm
}

PACKAGE_DEBUG_SPLIT_STYLE_class-nativesdk = "debug-without-src"

PACKAGES =+ "${PN}-libllvm ${PN}-lldb-python libclang"

BBCLASSEXTEND = "native nativesdk"

FILES_${PN}-lldb-python = "${libdir}/python*/site-packages/lldb/*"

FILES_${PN} += "\
  ${libdir}/BugpointPasses.so \
  ${libdir}/LLVMHello.so \
  ${libdir}/LLVMgold.so \
  ${libdir}/*Plugin.so \
  ${datadir}/scan-* \
  ${datadir}/opt-viewer/ \
"

FILES_${PN}-libllvm += "\
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}svn.so \
"

FILES_libclang = "\
  ${libdir}/libclang.so.${MAJOR_VER} \
"

FILES_${PN}-dev += "\
  ${datadir}/llvm/cmake \
  ${libdir}/cmake \
"

INSANE_SKIP_${PN} += "already-stripped"
INSANE_SKIP_${PN}-dev += "dev-elf"
INSANE_SKIP_${PN}-lldb-python += "dev-so dev-deps"

#Avoid SSTATE_SCAN_COMMAND running sed over llvm-config.
SSTATE_SCAN_FILES_remove = "*-config"

TOOLCHAIN = "clang"
TOOLCHAIN_class-native = "gcc"
TOOLCHAIN_class-nativesdk = "clang"
