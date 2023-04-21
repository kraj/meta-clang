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

inherit cmake cmake-native pkgconfig python3native

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

PACKAGECONFIG ??= "compiler-rt libcplusplus shared-libs lldb-wchar \
                   ${@bb.utils.filter('DISTRO_FEATURES', 'thin-lto full-lto', d)} \
                   rtti eh libedit \
                   "
PACKAGECONFIG_class-native = "rtti eh libedit"
PACKAGECONFIG_class-nativesdk = "rtti eh libedit ${@bb.utils.filter('DISTRO_FEATURES', 'thin-lto full-lto', d)}"

PACKAGECONFIG[compiler-rt] = "-DCLANG_DEFAULT_RTLIB=compiler-rt,,libcxx,compiler-rt"
PACKAGECONFIG[libcplusplus] = "-DCLANG_DEFAULT_CXX_STDLIB=libc++,,libcxx"
PACKAGECONFIG[unwindlib] = "-DCLANG_DEFAULT_UNWINDLIB=libunwind,-DCLANG_DEFAULT_UNWINDLIB=libgcc,libcxx"
PACKAGECONFIG[thin-lto] = "-DLLVM_ENABLE_LTO=Thin -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR},,binutils,"
PACKAGECONFIG[full-lto] = "-DLLVM_ENABLE_LTO=Full -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR},,binutils,"
PACKAGECONFIG[shared-libs] = "-DLLVM_BUILD_LLVM_DYLIB=ON -DLLVM_LINK_LLVM_DYLIB=ON,,,"
PACKAGECONFIG[terminfo] = "-DLLVM_ENABLE_TERMINFO=ON,-DLLVM_ENABLE_TERMINFO=OFF,ncurses,"
PACKAGECONFIG[pfm] = "-DLLVM_ENABLE_LIBPFM=ON,-DLLVM_ENABLE_LIBPFM=OFF,libpfm,"
PACKAGECONFIG[lldb-wchar] = "-DLLDB_EDITLINE_USE_WCHAR=1,-DLLDB_EDITLINE_USE_WCHAR=0,"
PACKAGECONFIG[bootstrap] = "-DCLANG_ENABLE_BOOTSTRAP=On -DCLANG_BOOTSTRAP_PASSTHROUGH='${PASSTHROUGH}' -DBOOTSTRAP_LLVM_ENABLE_LTO=Thin -DBOOTSTRAP_LLVM_ENABLE_LLD=ON,,,"
PACKAGECONFIG[eh] = "-DLLVM_ENABLE_EH=ON,-DLLVM_ENABLE_EH=OFF,,"
PACKAGECONFIG[rtti] = "-DLLVM_ENABLE_RTTI=ON,-DLLVM_ENABLE_RTTI=OFF,,"
PACKAGECONFIG[split-dwarf] = "-DLLVM_USE_SPLIT_DWARF=ON,-DLLVM_USE_SPLIT_DWARF=OFF,,"
PACKAGECONFIG[libedit] = "-DLLVM_ENABLE_LIBEDIT=ON -DLLDB_ENABLE_LIBEDIT=ON,-DLLVM_ENABLE_LIBEDIT=OFF -DLLDB_ENABLE_LIBEDIT=OFF,libedit libedit-native"

OECMAKE_SOURCEPATH = "${S}/llvm"

OECMAKE_TARGET_COMPILE = "${@bb.utils.contains('PACKAGECONFIG', 'bootstrap', 'stage2', 'all', d)}"
OECMAKE_TARGET_INSTALL = "${@bb.utils.contains('PACKAGECONFIG', 'bootstrap', 'stage2-install', 'install', d)}"
BINPATHPREFIX = "${@bb.utils.contains('PACKAGECONFIG', 'bootstrap', '/tools/clang/stage2-bins/NATIVE', '', d)}"

PASSTHROUGH = "\
CLANG_DEFAULT_RTLIB;CLANG_DEFAULT_CXX_STDLIB;LLVM_BUILD_LLVM_DYLIB;LLVM_LINK_LLVM_DYLIB;\
LLVM_ENABLE_ASSERTIONS;LLVM_ENABLE_EXPENSIVE_CHECKS;LLVM_ENABLE_PIC;\
LLVM_BINDINGS_LIST;LLVM_ENABLE_FFI;FFI_INCLUDE_DIR;LLVM_OPTIMIZED_TABLEGEN;\
LLVM_ENABLE_RTTI;LLVM_ENABLE_EH;LLVM_BUILD_EXTERNAL_COMPILER_RT;CMAKE_SYSTEM_NAME;\
CMAKE_BUILD_TYPE;BUILD_SHARED_LIBS;LLVM_ENABLE_PROJECTS;LLVM_BINUTILS_INCDIR;\
LLVM_TARGETS_TO_BUILD;LLVM_EXPERIMENTAL_TARGETS_TO_BUILD;PYTHON_EXECUTABLE;\
PYTHON_LIBRARY;PYTHON_INCLUDE_DIR;LLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN;LLDB_EDITLINE_USE_WCHAR;\
LLVM_ENABLE_LIBEDIT;LLDB_ENABLE_LIBEDIT; \
CMAKE_C_FLAGS_RELEASE;CMAKE_CXX_FLAGS_RELEASE;CMAKE_ASM_FLAGS_RELEASE;\
"
#
# Default to build all OE-Core supported target arches (user overridable).
#
LLVM_TARGETS_TO_BUILD ?= "AMDGPU;AArch64;ARM;BPF;Mips;PowerPC;RISCV;X86"
LLVM_TARGETS_TO_BUILD_append = ";${@get_clang_host_arch(bb, d)};${@get_clang_target_arch(bb, d)}"

LLVM_TARGETS_TO_BUILD_TARGET ?= "${LLVM_TARGETS_TO_BUILD}"
LLVM_TARGETS_TO_BUILD_TARGET_append ?= ";${@get_clang_target_arch(bb, d)}"

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
                  -DLLVM_BUILD_EXTERNAL_COMPILER_RT=ON \
                  -DCMAKE_SYSTEM_NAME=Linux \
                  -DCMAKE_BUILD_TYPE=Release \
                  -DBUILD_SHARED_LIBS=OFF \
                  -DLLVM_ENABLE_PROJECTS='clang;clang-tools-extra;lld;lldb' \
                  -DLLVM_BINUTILS_INCDIR=${STAGING_INCDIR} \
                  -DLLVM_TEMPORARILY_ALLOW_OLD_TOOLCHAIN=ON \
"

EXTRA_OECMAKE_append_class-native = "\
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
                  -DPYTHON_EXECUTABLE='${PYTHON}' \
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
                  -DLLVM_LIBDIR_SUFFIX=${@d.getVar('baselib').replace('lib', '')} \
                  -DPYTHON_EXECUTABLE='${PYTHON}' \
"

DEPENDS = "binutils zlib libffi libxml2 libxml2-native ninja-native swig-native"
DEPENDS_append_class-nativesdk = " clang-crosssdk-${SDK_ARCH} virtual/${TARGET_PREFIX}binutils-crosssdk nativesdk-python3"
DEPENDS_append_class-target = " clang-cross-${TARGET_ARCH} python3"

COMPATIBLE_HOST_riscv64 = "null"
COMPATIBLE_HOST_riscv32 = "null"

RRECOMMENDS_${PN} = "binutils"
RRECOMMENDS_${PN}_append_class-target = " libcxx-dev"

do_install_append() {
    rm -rf ${D}${libdir}/python*/site-packages/six.py
}

do_install_append_class-target () {
    # Allow bin path to change based on YOCTO_ALTERNATE_EXE_PATH
    sed -i 's;${_IMPORT_PREFIX}/bin;${_IMPORT_PREFIX_BIN};g' ${D}${libdir}/cmake/llvm/LLVMExports-release.cmake

    # Insert function to populate Import Variables
    sed -i "4i\
if(DEFINED ENV{YOCTO_ALTERNATE_EXE_PATH})\n\
  execute_process(COMMAND \"llvm-config\" \"--bindir\" OUTPUT_VARIABLE _IMPORT_PREFIX_BIN OUTPUT_STRIP_TRAILING_WHITESPACE)\n\
else()\n\
  set(_IMPORT_PREFIX_BIN \"\${_IMPORT_PREFIX}/bin\")\n\
endif()\n" ${D}${libdir}/cmake/llvm/LLVMExports-release.cmake

    if [ -n "${LLVM_LIBDIR_SUFFIX}" ]; then
        mkdir -p ${D}${nonarch_libdir}
        mv ${D}${libdir}/clang ${D}${nonarch_libdir}/clang
        lnr ${D}${nonarch_libdir}/clang ${D}${libdir}/clang
        rmdir --ignore-fail-on-non-empty ${D}${libdir}
    fi
}

do_install_append_class-native () {
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/lldb-tblgen ${D}${bindir}/lldb-tblgen
    for f in `find ${D}${bindir} -executable -type f -not -type l`; do
        test -n "`file $f|grep -i ELF`" && ${STRIP} $f
        echo "stripped $f"
    done
    ln -sf clang-tblgen ${D}${bindir}/clang-tblgen${PV}
    ln -sf llvm-tblgen ${D}${bindir}/llvm-tblgen${PV}
    ln -sf llvm-config ${D}${bindir}/llvm-config${PV}
}

do_install_append_class-nativesdk () {
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/clang-tblgen ${D}${bindir}/clang-tblgen
    install -Dm 0755 ${B}${BINPATHPREFIX}/bin/lldb-tblgen ${D}${bindir}/lldb-tblgen
    for f in `find ${D}${bindir} -executable -type f -not -type l`; do
        test -n "`file $f|grep -i ELF`" && ${STRIP} $f
    done
    ln -sf clang-tblgen ${D}${bindir}/clang-tblgen${PV}
    ln -sf llvm-tblgen ${D}${bindir}/llvm-tblgen${PV}
    ln -sf llvm-config ${D}${bindir}/llvm-config${PV}
    rm -rf ${D}${datadir}/llvm/cmake
    rm -rf ${D}${datadir}/llvm
}

PACKAGES =+ "${PN}-libllvm ${PN}-lldb-python libclang lldb lldb-server liblldb"

PROVIDES += "llvm llvm${PV}"
PROVIDES_append_class-native = " llvm-native"

BBCLASSEXTEND = "native nativesdk"

RDEPENDS:${PN} += "\
  perl-module-digest-md5 \
  perl-module-file-basename \
  perl-module-file-copy \
  perl-module-file-find \
  perl-module-file-path \
  perl-module-findbin \
  perl-module-hash-util \
  perl-module-sys-hostname \
  perl-module-term-ansicolor \
"

RDEPENDS_lldb += "${PN}-lldb-python"

FILES_${PN}-lldb-python = "${libdir}/python*/site-packages/lldb/*"

FILES_${PN} += "\
  ${libdir}/BugpointPasses.so \
  ${libdir}/LLVMHello.so \
  ${libdir}/LLVMgold.so \
  ${libdir}/*Plugin.so \
  ${libdir}/${BPN} \
  ${nonarch_libdir}/${BPN}/*/include/ \
  ${datadir}/scan-* \
  ${datadir}/opt-viewer/ \
"

FILES_lldb = "\
  ${bindir}/lldb \
"

FILES_lldb-server = "\
  ${bindir}/lldb-server \
"

FILES_liblldb = "\
  ${libdir}/liblldbIntelFeatures.so* \
  ${libdir}/liblldb.so* \
"

FILES_${PN}-libllvm =+ "\
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}.so \
  ${libdir}/libLLVM-${MAJOR_VER}git.so \
  ${libdir}/libLLVM-${MAJOR_VER}.${MINOR_VER}git.so \
"

FILES_libclang = "\
  ${libdir}/libclang.so.${MAJOR_VER} \
"

FILES_${PN}-dev += "\
  ${datadir}/llvm/cmake \
  ${libdir}/cmake \
  ${nonarch_libdir}/${BPN}/*.la \
"

FILES_${PN}-staticdev += "${nonarch_libdir}/${BPN}/*.a"

FILES_${PN}-staticdev_remove = "${libdir}/${BPN}/*.a"
FILES_${PN}-dev_remove = "${libdir}/${BPN}/*.la"
FILES_${PN}_remove = "${libdir}/${BPN}/*"


INSANE_SKIP_${PN} += "already-stripped"
#INSANE_SKIP_${PN}-dev += "dev-elf"
INSANE_SKIP_${PN}-lldb-python += "dev-so dev-deps"
INSANE_SKIP_liblldb = "dev-so"

#Avoid SSTATE_SCAN_COMMAND running sed over llvm-config.
SSTATE_SCAN_FILES_remove = "*-config"

TOOLCHAIN = "clang"
TOOLCHAIN_class-native = "gcc"
TOOLCHAIN_class-nativesdk = "clang"

SYSROOT_DIRS_append_class-target = " ${nonarch_libdir}"
